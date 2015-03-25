package models.orders;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import models.Meal;
import models.User;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Cart extends Model {
	
	@Id
	public int id;
	@OneToMany
	public List<CartItem> cartItems = new ArrayList<CartItem>(0);
	
	
	static Finder<Integer, Cart> find = new Finder<Integer, Cart>(Integer.class, Cart.class);

	
	public Cart(int id){
		this.id = id;
	}
	
	public void addMeal(Meal meal) {
		boolean itemExists = false;
		for (CartItem cartItem : cartItems) {
			if (cartItem.meal.find(id).equals(Meal.find(id))) {
				cartItem.increaseQuantity();
				itemExists = true;
			}
		}
		if (!itemExists) {
			CartItem cartItem = new CartItem(this, 1, 15, meal);
			cartItems.add(cartItem);
		}
	}
//
//	public void removeMeal(Meal meal) {
//		for (Iterator<CartItem> it = cartItems.iterator(); it.hasNext();) {
//			CartItem carttItem = (CartItem) it.next();
//			if (cartItem.meal.find(id).equals(Meal.find(id))) {
//				if (cartItem.quantity > 1) {
//					cartItem.decreaseQuantity();
//				} else {
//					it.remove();
//				}
//			}
//		}
//	}
	
	public int getItemNumber(){
		return cartItems.size();
	}
	
	public static Cart findById(int id){
		return find.byId(id);
	}
		
}
