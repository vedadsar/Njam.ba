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
import play.Logger;
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
	
	public static Result showPurchase(){
		return ok(creditStatus.render(""));
	}
	
	public static Result purchaseProcessing(){
				
		try{
			String accessToken = new OAuthTokenCredential("Ae8Tq4Qee9EIZk7dCZGDPku5AOH0RvG4STCohE2lErEIk1pW2bxxexAP4EY53PtZXL7EaKe22AFv4cul", 
					"EAFYsYCnq9ZaV686lU9HEA_oVh7OtnyvSler0OTet3ki60IYZyHGprga_lKHxEQuY0f1G2PGyoL1CrJP").getAccessToken();	
		
			Map<String, String> sdkConfig = new HashMap<String, String>();
			sdkConfig.put("mode", "sandbox");
			
			APIContext apiContext = new APIContext(accessToken);
			apiContext.setConfigurationMap(sdkConfig);
			
			Amount amount = new Amount();
			amount.setTotal("6.00");
			amount.setCurrency("USD");
			
			Transaction transaction = new Transaction();
			transaction.setDescription("Davor Handsome");
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
			redirectUrls.setCancelUrl("http://localhost:9000/creditfail");
			redirectUrls.setReturnUrl("http://localhost:9000/creditsuccess");
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
		String accessToken = new OAuthTokenCredential("Ae8Tq4Qee9EIZk7dCZGDPku5AOH0RvG4STCohE2lErEIk1pW2bxxexAP4EY53PtZXL7EaKe22AFv4cul", 
				"EAFYsYCnq9ZaV686lU9HEA_oVh7OtnyvSler0OTet3ki60IYZyHGprga_lKHxEQuY0f1G2PGyoL1CrJP").getAccessToken();	
	
		Map<String, String> sdkConfig = new HashMap<String, String>();
		sdkConfig.put("mode", "sandbox");
		
		APIContext apiContext = new APIContext(accessToken);
		apiContext.setConfigurationMap(sdkConfig);
		
		Payment payment = Payment.get(accessToken, paymentID);
		
		PaymentExecution paymentExecution = new PaymentExecution();
		paymentExecution.setPayerId(payerID);
		
		Payment newPayment = payment.execute(apiContext, paymentExecution);
		
		} catch(PayPalRESTException e){
			Logger.warn(e.getMessage());
		}		
		return ok(creditStatus.render("Proslo"));
	}
	
	public static Result creditFail(){
		return ok(creditStatus.render("Nije proslo"));
	}
}
