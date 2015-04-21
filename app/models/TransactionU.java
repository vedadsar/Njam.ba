package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.orders.Cart;
import models.orders.CartItem;

import com.fasterxml.jackson.databind.JsonNode;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import play.Logger;
import play.db.ebean.Model;
import play.libs.Json;

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
	
	@OneToMany(mappedBy="transaction",cascade=CascadeType.ALL)
	public List<CartItem> items = new ArrayList<CartItem>();
	
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
		
		Cart cart = Cart.find(cartToPayId);
		this.items.addAll(cart.cartItems);
		
		List<MetaItem> metaitems = new ArrayList<MetaItem>();
		
		for(int i=0; i<this.items.size(); i++) {
			
			String name = this.items.get(i).getMealName(); 
			double price = this.items.get(i).meal.price; 
			int quantity = this.items.get(i).quantity;
			double totalPrice = this.items.get(i).totalPrice;
			MetaItem meta = new MetaItem(name, price, quantity, totalPrice);
			metaitems.add(meta);
		}
		JsonNode jsonNode = Json.toJson(metaitems);
		Logger.debug("JSON I TO: " + jsonNode.toString());
		
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
