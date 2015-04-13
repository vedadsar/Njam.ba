package models.orders;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import models.Meal;
import models.User;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class CartItem extends Model {
	
	@Id
	public int id;
	@ManyToOne
	public Cart cart;
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
	
	public static List<CartItem> allById(User u) {		
		int id =u.id;
		List<CartItem> tobeapproved = u.toBeApproved;
		return tobeapproved;
	}
}