package controllers;
 
import play.mvc.Result;
 
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
 
import models.Faq;
import models.Location;
import models.Meal;
import models.Restaurant;
import models.TransactionU;
import models.User;
import models.orders.Cart;
import models.orders.CartItem;
import play.Logger;
import play.Play;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import views.html.*;
import Utilites.AdminFilter;
import Utilites.Session;
import play.data.DynamicForm;
import play.db.ebean.Model.Finder;
import Utilites.*;
 
import com.paypal.api.payments.*;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
 
public class PaypalController extends Controller {
	
	private static String hostUrl = Play.application().configuration().getString("hostUrl");
	private static String paypalToken1 = Play.application().configuration().getString("paypalToken1");
	private static String paypalToken2 = Play.application().configuration().getString("paypalToken2");
 
	static int userToPayId;
	static int cartToPayId;
	static int restaurantId;
	static Restaurant restaurantToPay;
	static double priceToPay;
	static String contextToPay;
	static String paymentExecutionToPay;
	static String paymentToPay;
	static String paymentIdToPay;
	static String tokenToPay;
	static final String CLIENT_ID = Play.application().configuration().getString("cliendID");
	static final String CLIENT_SECRET = Play.application().configuration().getString("cliendSecret");
	static String transactionID;
	
	
	public static Result showPurchase(){
		return ok(views.html.user.creditStatus.render(""));
	}
	
	public static Result purchaseProcessing(int cartId){
				
		try{
			String accessToken = new OAuthTokenCredential(paypalToken1, paypalToken2).getAccessToken();	
		
			Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			User u = Session.getCurrentUser(ctx());
			Cart cart = Cart.findCartInCarts(u.id, cartId);
			
			String restName = cart.restaurantName;
			restaurantToPay = Restaurant.findByName(restName);
			
			double total = cart.total;
			String price = String.format("%1.2f",total);
			
			userToPayId = u.id;
			cartToPayId = cartId;
			
			Amount amount = new Amount();
			amount.setTotal(price);
			amount.setCurrency("USD");
						
			String description = String.format("Description of order: %s\n"
					
					+ "Total Price: %s\n"
					+ "Cart ID: %s\n", "Njam.ba", cart.total, cart.id);
			
			Transaction transaction = new Transaction();
			transaction.setDescription(description);
			transaction.setAmount(amount);
			
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);
			
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			
			
			Payment payment = new Payment();
			payment.setIntent("sale");
			payment.setPayer(payer);
			payment.setTransactions(transactions);
			RedirectUrls redirectUrls = new RedirectUrls();
			redirectUrls.setCancelUrl(hostUrl + "creditfail");
			redirectUrls.setReturnUrl(hostUrl + "creditsuccess/" + cartId);
			payment.setRedirectUrls(redirectUrls);
			
			Payment createdPayment = payment.create(apiContext);
			Iterator <Links> itr = createdPayment.getLinks().iterator();
			while(itr.hasNext()){
				Links link = itr.next();
				if(link.getRel().equals("approval_url"))
					return redirect(link.getHref());
			}
			return TODO; 
		} catch(PayPalRESTException e){
			Logger.warn(e.getMessage());
		}
		return TODO;
	}
	
	public static Result creditSuccess(int cartId){
		
		DynamicForm paypalReturn = Form.form().bindFromRequest();
		
		String paymentID = paypalReturn.get("paymentId");
		String payerID = paypalReturn.get("PayerID");
		String token = paypalReturn.get("token");
		
		try{
		String accessToken = new OAuthTokenCredential(paypalToken1, paypalToken2).getAccessToken();	
	
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		
		APIContext apiContext = new APIContext(accessToken);
		apiContext.setConfigurationMap(sdkConfig);
		
		contextToPay = accessToken;
		paymentToPay = paymentID;
		
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerID);
		
		paymentExecutionToPay = paymentExecution.getPayerId();
		
		TransactionU newTrans = TransactionU.createTransaction(contextToPay, paymentToPay, paymentExecutionToPay, userToPayId, cartToPayId, restaurantToPay, token, 15);
		addTransactionToPendingList(newTrans);
		} catch(PayPalRESTException e){
			Logger.warn(e.getMessage());
		}
		flash("PaidOK","Thank You for ordering> Wait until restaurant confirm your order.");
		return redirect("/user/"+Session.getCurrentUser(ctx()).email);
	}
	
	private static void addTransactionToPendingList(TransactionU newTrans) {
		Cart cart = Cart.find(newTrans.cartToPayId);
		String restaurantName = cart.restaurantName;
		Restaurant restaurant = Restaurant.findByName(restaurantName);
		restaurant.toBeApproved.add(newTrans);
		cart.ordered = true;
		cart.update();
		
		for(int i=0; i<restaurant.toBeApproved.size(); i++) {
			Logger.debug("U LISTI:" + restaurant.toBeApproved.get(i).id );
		}
	}
	
	public static Result executePaymentById(int paymentId) throws PayPalRESTException {
		TransactionU transaction = TransactionU.find(paymentId);
		
		DynamicForm paypalReturn = Form.form().bindFromRequest();
		String devTime = paypalReturn.get("deliveryTime");
		int deliveryTime = Integer.parseInt(devTime);
		
		Cart newCart = Cart.findCartInCarts(transaction.userToPayId, transaction.cartToPayId);
		try {
			
			Payment payment = Payment.get(contextToPay, transaction.paymentToPay);
			Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			
			APIContext apiContext = new APIContext(contextToPay);
			apiContext.setConfigurationMap(sdkConfig);
			
			PaymentExecution paymentExecution = new PaymentExecution();
			paymentExecution.setPayerId(transaction.paymentExecutionToPay);
			
			Payment response  = payment.execute(apiContext, paymentExecution);
			
			transactionID = response.getTransactions().get(0).getRelatedResources().get(0).getSale().getId();
			
			transaction.approved = true;
			transaction.deliveryTime = deliveryTime;
			transaction.update();
			newCart.paid=true;
			newCart.update();
			Restaurant restaurant = transaction.restaurant;
			restaurant.approvedOrders ++;
			restaurant.update();
			
			flash("SuccessApprovedOrder", "Order successfully approved!");
			MailHelper.tellUserThatOrderIsApproved(transaction.email, transaction.price, transaction.restaurant.name, transaction.items);
			return redirect("/restaurantOwner/" + Session.getCurrentUser(ctx()).email);
		} catch (PayPalRESTException e) {
			e.printStackTrace();
		} catch (NullPointerException e1){
			Logger.error("Payment did not execute " + e1.getMessage());
		}
		return TODO;
	}
	
	public static Result deleteOrder(int paymentId) {
		
		DynamicForm form = Form.form().bindFromRequest();
		String message = form.data().get("message");
		
		TransactionU transaction = TransactionU.find(paymentId);
		transaction.refused = true;
		transaction.update();
		
		Cart newCart = Cart.findCartInCarts(transaction.userToPayId, transaction.cartToPayId);
		newCart.paid = true;
		newCart.update();
		
		Restaurant restaurant = transaction.restaurant;
		restaurant.refusedOrders ++;
		restaurant.update();
		
		flash("RefusedOrder", "Order successfully refused!");
		MailHelper.tellUserThatOrderIsRefused(transaction.email, transaction.price, transaction.restaurant.name, message, transaction.items);
		return redirect("/restaurantOwner/" + Session.getCurrentUser(ctx()).email);
	}
	
 
	public static Result creditFail(){
		flash("FailedPayPal","Payment did not pass throw.");
		return redirect("/user/"+Session.getCurrentUser(ctx()).email);
	}
	
	
	public static Result refundProcessing(int cartID){
				
		try {
			TransactionU transaction = TransactionU.findByCart(cartID);
 
			Map<String, String> sdkConfig = new HashMap<String, String>();
			List<Map<Sale, Refund>> listOfRefunds = new ArrayList<Map<Sale, Refund>>();
			sdkConfig.put("mode", "sandbox");
			APIContext apiContext = new APIContext(transaction.contextToPay);
			apiContext.setConfigurationMap(sdkConfig);
 
			priceToPay = transaction.price;
 
			String totalPrice = String.format("%1.2f", priceToPay);
 
			Map<Sale, Refund> refundMap = new HashMap<Sale, Refund>();
			Sale sale = new Sale();
			sale.setId(transactionID);
			Refund refund = new Refund();
			Amount amount = new Amount();
			amount.setCurrency("USD");
			amount.setTotal(totalPrice);
			refund.setAmount(amount);
			refundMap.put(sale, refund);
 
			listOfRefunds.add(refundMap);
 
			for (int i = 0; i < listOfRefunds.size(); i++) {
				for (Map.Entry<Sale, Refund> e : listOfRefunds.get(i)
						.entrySet()) {
					Sale sale2 = e.getKey();
					Refund refund2 = e.getValue();
 
					sale2.refund(apiContext, refund2);
				}
			}
			flash("Success",
					"Buyer's money from this cart is successfully refunded!");
			return redirect("/");
			
		} catch (PayPalRESTException e) {
			flash("Failed" , "Error occured during refunding paypal. Please contact admin!");
			Logger.error("Error at refunding paypal: " + e.getMessage());
			return redirect("/");
		}
	}
}