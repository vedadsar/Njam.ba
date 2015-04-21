package models.orders;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import models.Meal;
import models.Restaurant;
import models.TransactionU;
import models.User;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class CartItem extends Model {
	
	@Id
	public int id;
	@OneToOne
	public Cart cart;
	@OneToOne
	public TransactionU transaction;
	@Required
	public int quantity;
	@Required
	public double totalPrice;
	@ManyToOne
	public Meal meal;
	
	public String getMealName(){
		return meal.name;
	}
	
	public static Finder<Integer, CartItem> find = new Finder<Integer, CartItem>(
			Integer.class, CartItem.class);

	
	public CartItem(Cart cart, int quantity, double totalPrice, Meal meal){
		this.cart = cart;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.meal = meal;
		meal.addMealToCart(this);
	}
	
	public void increaseQuantity(){
		this.quantity += 1;
		calculatePrice();
	}
	
	public void decreaseQuantity(){
		quantity -= 1;
		calculatePrice();
	}

	public void calculatePrice(){
		totalPrice = meal.price * quantity;
	}
	
	public static List<CartItem> all() {
		return find.all();
	}
	
	public static List<TransactionU> allById(User u) {		
		int id =u.id;
		Restaurant restaurant = u.restaurant;
		List<TransactionU> tobeapproved = restaurant.toBeApproved;
		return tobeapproved;
	}
}