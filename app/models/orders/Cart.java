package models.orders;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import Utilites.Session;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Meal;
import models.User;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.twirl.api.Content;

@Entity
public class Cart extends Model {
	
	@Id
	public int id;
	@OneToMany
	public List<CartItem> cartItems;
	@OneToOne
	public User user;
	@Required
	public boolean paid;
	@Required
	public double total;
	
	
	static Finder<Integer, Cart> findC = new Finder<Integer, Cart>(Integer.class, Cart.class);
	static Finder<Integer, Meal> findM = new Finder<Integer, Meal>(Integer.class, Meal.class);
	static Finder<Integer, CartItem> findI = new Finder<Integer, CartItem>(Integer.class, CartItem.class);

	
	public Cart(int id, User user){
		this.id = id;
		this.user = user;
		this.paid = false;
		this.total = 0;
	}
	
	public void addMeal(Meal meal) {
		for (CartItem cartItem : cartItems) {
			if(this.id == cartItem.meal.id){
				cartItem.increaseQuantity();
				cartItem.update();
			}
		}
	}
	
	public void addMealToCart(Meal meal) {

		for (CartItem cartItem : cartItems) {
			if (cartItem.meal.id == meal.id) {
				System.out.println("U ifu je u Cart");
				cartItem.increaseQuantity();
				cartItem.update();
				return;
			}
		}
		System.out.println("U elsu je");
		CartItem newItem = new CartItem(this, 1, meal.price, meal);
		newItem.save();
		cartItems.add(newItem);
	}
	
	public static Cart find(int id) {
		return findC.byId(id);
	} 
	
	public static Cart findByUserId(int userId){
		return findC.where().eq("user_id", userId).findUnique();	 
	}
}
	