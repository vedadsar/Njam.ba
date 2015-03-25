package models.orders;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import models.Meal;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

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
	
	public CartItem(Cart cart, int quantity, double totalPrice, Meal meal){
		this.cart = cart;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.meal = meal;
		meal.addMealToCart(this);
	}
	
	public void increaseQuantity(){
		quantity += 1;
		calculatePrice();
	}
	
	public void decreaseQuantity(){
		quantity -= 1;
		calculatePrice();
	}

	public void calculatePrice(){
		totalPrice = meal.price * quantity;
	}
}
