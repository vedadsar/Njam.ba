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
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

public class PaypalController extends Controller {
	
	private static String hostUrl = Play.application().configuration().getString("hostUrl");
	private static String paypalToken1 = Play.application().configuration().getString("paypalToken1");
	private static String paypalToken2 = Play.application().configuration().getString("paypalToken2");

	
	public static Result showPurchase(){
		return ok(views.html.user.creditStatus.render(""));
	}
	
	public static Result purchaseProcessing(){
				
		try{
			String accessToken = new OAuthTokenCredential(paypalToken1, paypalToken2).getAccessToken();	
		
			Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			User u = Session.getCurrentUser(ctx());
			Cart cart = Cart.findLastCart(u.id);
			
			double total = cart.total;
			double cartID = cart.id;
			String price = String.format("%1.2f",total);
			Amount amount = new Amount();
			amount.setTotal(price);
			amount.setCurrency("USD");
			
			String description = String.format("Description: %s\n"
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
			redirectUrls.setReturnUrl(hostUrl + "creditsuccess");
			payment.setRedirectUrls(redirectUrls);
			
			Payment createdPayment = payment.create(apiContext);
			
			Iterator <Links> itr = createdPayment.getLinks().iterator();
			while(itr.hasNext()){
				Links link = itr.next();
				if(link.getRel().equals("approval_url"))
					return redirect(link.getHref());
			}
			
//			Logger.debug(createdPayment.toJSON());
			
			return TODO; // Nesto nije proslo kako treba
			

		} catch(PayPalRESTException e){
			Logger.warn(e.getMessage());
		}		
		return TODO;
	}
	
	public static Result creditSuccess(){
		
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
		
		Payment payment = Payment.get(accessToken, paymentID);
		
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerID);
		
		Payment newPayment = payment.execute(apiContext, paymentExecution);
		User u = Session.getCurrentUser(ctx());
		Cart newCart = Cart.findLastCart(u.id);
		newCart.paid=true;
		newCart.update();
		} catch(PayPalRESTException e){
			Logger.warn(e.getMessage());
		}
		flash("PaidOK","Thank You for ordering> Wait until restaurant confirm your order.");
		return redirect("/user/"+Session.getCurrentUser(ctx()).email);
	}
	
	public static Result creditFail(){
		flash("FailedPayPal","Payment did not pass throw.");
		return redirect("/user/"+Session.getCurrentUser(ctx()).email);
	}
}
