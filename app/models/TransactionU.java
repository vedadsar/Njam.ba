package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.orders.Cart;

import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import play.Logger;
import play.db.ebean.Model;

@Entity
public class TransactionU extends Model {

	@Id
	public int id;

	@ManyToOne
	public Restaurant restaurant;
	
	public APIContext contextToPay;

	public Payment paymentToPay;

	public PaymentExecution paymentExecutionToPay;

	public int userToPayId;

	public int cartToPayId;
	
	public String email;
	
	public double price;
	
	public Boolean approved = false;
	
	public Boolean refused = false;
	
	private static Finder<Long, TransactionU> find = new Finder<Long, TransactionU>(Long.class,
			TransactionU.class);
	
	
	public TransactionU(APIContext contextToPay, Payment paymentToPay,
			PaymentExecution paymentExecutionToPay, int userToPayId,
			int cartToPayId, Restaurant restaurantToPay) {
		email = User.find(userToPayId).email;
		price = Cart.find(cartToPayId).total;
		this.restaurant = restaurantToPay;
		this.contextToPay = contextToPay;
		this.paymentToPay = paymentToPay;
		this.paymentExecutionToPay = paymentExecutionToPay;
		this.userToPayId = userToPayId;
		this.cartToPayId = cartToPayId;
	}

	
	public static TransactionU createTransaction(APIContext contextToPay,
			Payment paymentToPay, PaymentExecution paymentExecutionToPay,
			int userToPayId, int cartToPayId, Restaurant restaurantToPay) {

		TransactionU transaction = new TransactionU(contextToPay, paymentToPay,
				paymentExecutionToPay, userToPayId, cartToPayId, restaurantToPay);
		
		
		transaction.save();

		return transaction;
	}
	
	
	public static TransactionU find(long id) {
		return find.byId(id);
	}
	

}
