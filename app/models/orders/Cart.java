package models.orders;

import java.util.ArrayList;
import java.util.Date;
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
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import play.twirl.api.Content;

@Entity
public class Cart extends Model {
	
	@Id
	public int id;
	@OneToMany(mappedBy="cart",cascade=CascadeType.ALL)
	public List<CartItem> cartItems;
	@OneToOne
	public User user;
	@Required
	public boolean paid;
	@Required
	public boolean ordered;
	@Required
	public boolean timedOut;
	@Required
	public double total;
	@Required
	public double minOrder;
	
	public Date date;
	
	public String orderNote;
	
	public String getOrderNote() {
		return orderNote;
	}

	public void setOrderNote(String orderNote) {
		this.orderNote = orderNote;
	}

	@Required
	public String restaurantName;
	
	
	static Finder<Integer, Cart> findC = new Finder<Integer, Cart>(Integer.class, Cart.class);
	static Finder<Integer, Meal> findM = new Finder<Integer, Meal>(Integer.class, Meal.class);
	static Finder<Integer, CartItem> findI = new Finder<Integer, CartItem>(Integer.class, CartItem.class);
	static Form<CartItem> inputForm = new Form<CartItem>(CartItem.class);

	
	public Cart(int id, User user){
		this.id = id;
		this.user = user;
		this.paid = false;
		this.ordered = false;
		this.timedOut = false;
		this.total = 0;
		this.date = new Date();
		this.orderNote = "Regular";
	}
	
	public Cart( User user){
		this.user = user;
		this.paid = false;
		this.ordered = false;
		this.timedOut = false;
		this.total = 0;
		this.date =new Date();
		this.orderNote = "Regular";
	}
	
	public Cart( User user, String restaurantName) {
		this.user = user;
		this.paid = false;
		this.ordered = false;
		this.timedOut = false;
		this.total = 0;
		this.date =new Date();
		this.restaurantName = restaurantName;
		this.orderNote = "Regular";
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
				cartItem.increaseQuantity();
				cartItem.update();
				this.total += cartItem.meal.price;
				Logger.debug("TOTAL TRENUTNO JE: " + cartItem.cart.total);
				this.update();
				return;
			}
		}
		CartItem cartItem = new CartItem(this, 1, meal.price, meal);
		this.cartItems.add(cartItem);
		Logger.debug("TOTAL TRENUTNO JE: " + cartItem.cart.total);
		this.total += cartItem.totalPrice;
		this.paid = false;
		this.save();
		cartItem.save();
	}
	
	
	public void addMealToCartButton(Meal meal) {

		for (CartItem cartItem : cartItems) {
			if (cartItem.meal.id == meal.id) {
				System.out.println("U ifu je u Cart");
				String itemQuantity = inputForm.bindFromRequest().field("quantity").value();
				int quantity = Integer.parseInt(itemQuantity);
				cartItem.quantity = quantity;
				cartItem.totalPrice = cartItem.meal.price*quantity;
				cartItem.cart.total = cartItem.totalPrice;
				cartItem.update();
				cartItem.cart.update();
				return;
			}
		}
		System.out.println("U elsu je");
		CartItem newItem = new CartItem(this, 1, meal.price, meal);
		newItem.cart.total += newItem.totalPrice;
		newItem.save();
		cartItems.add(newItem);
	}
	
	
	public static Cart find(int id) {
		return findC.byId(id);
	} 
	
	public static Cart findByUserId(int userId){
		return findC.where().eq("user_id", userId).findList().get(findC.findRowCount() - 1);
	}

	
	public static Cart findLastCart(int userId){
		List<Cart> carts = findC.where().eq("user_id", userId).findList();
		if(carts.size()==0)
			return null;
		Cart lastCart=carts.get(carts.size()-1);
		if (lastCart==null)
			return null;
		return lastCart;
	}
	
	public static Cart findCartInCarts(int userId, int cartId) {
		User user = User.find(userId);
		List<Cart> carts = user.carts;
		Cart cart = null;
		for (int i = 0; i < carts.size(); i++) {
			if (carts.get(i).id == cartId) {
				cart = carts.get(i);
				break;
			}
		}
		return cart;
	}
	
	public static boolean timeGap(int userId, int cartId) {
		Cart lastCart = Cart.findCartInCarts(userId, cartId);
		if (lastCart == null)
			return false;
		Date currentDate = new Date();
		long currentDateSec = currentDate.getTime();
		long time = 0;
		try {
			time = currentDateSec - lastCart.date.getTime();
			Logger.debug("TIME: " + time);

			if (time == 0 || time > 300000) {
				lastCart.timedOut = true;
				lastCart.update();
				return true;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		return false;
	}
	
public void removeMeal(Meal m, int userId, int cartId) {
		User user = User.find(userId);
		Cart cart = Cart.findCartInCarts(user.id, cartId);
		List<CartItem> cartItems = cart.cartItems;
		Iterator<CartItem> it = cartItems.iterator();
		while (it.hasNext()) {
			CartItem basketItem = (CartItem) it.next();
			if (basketItem.meal.id == m.id) {
				if (basketItem.quantity > 1) {
					basketItem.decreaseQuantity();
					System.out.println("Smanjuje se quantity");
					basketItem.cart.total = basketItem.totalPrice;
					basketItem.update();
				} else {
					System.out.println("Usao je u else da brise basketItem");
					basketItem.delete();
					basketItem.cart.total = 0;
				}

			}
		}
	}
	
	public void removeMealAll(Meal m){
		Iterator<CartItem> it = cartItems.iterator();  
		while(it.hasNext()){
			CartItem basketItem = (CartItem) it.next();
			if(basketItem.meal.id == m.id){
				basketItem.delete();		
				basketItem.save();
			}
		}
	}
	
	
}
	