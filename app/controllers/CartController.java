package controllers;

import play.mvc.Result;
import views.html.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Utilites.AdminFilter;
import Utilites.Session;
import models.*;
import models.orders.Cart;
import models.orders.CartItem;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Security;
import Utilites.*;

public class CartController extends Controller {
	
	static String email;
	static double total;
	static Finder<Integer, Meal> findM = new Finder<Integer, Meal>(Integer.class, Meal.class);
	static Finder<Integer, CartItem> findI = new Finder<Integer, CartItem>(Integer.class, CartItem.class);
	static List<CartItem> cartItems = new ArrayList<CartItem>(0);
	static Finder<Integer, Cart> findC = new Finder<Integer, Cart>(Integer.class, Cart.class);

	
	public static Result showCart(){
		email = session("email");
		User u = Session.getCurrentUser(ctx());
		total = 0;
		Cart newCart = Cart.findByUserId(u.id);
		List<CartItem> cartItems = newCart.cartItems;
		for (CartItem cartItem : cartItems) {
			total = total + cartItem.totalPrice;
		}

		if(Session.getCurrentUser(ctx()) == null){
			flash("loginP", "Please login");
			return redirect("/login");
		}

		return ok(cart.render(email, Cart.findByUserId(u.id).cartItems, total));		
	}
	
	
	public static Result addMealToBasket(int id, int cartID){
		Meal meal = Meal.find(id);
		Cart cart = findC.byId(cartID);
		Logger.debug(String.valueOf(cartID));
		if(cart == null){
			cart = new Cart(cartID, Session.getCurrentUser(ctx()));
			cart.addMeal(meal);
			CartItem cartItem = new CartItem(cart, 1, meal.price, meal);
			cartItems.add(cartItem);
			cart.paid = false;
			cart.save();
			cartItem.save();
		} else {
			System.out.println("Else u kontroleru");
			cart.addMealToCart(meal);
			cart.update();
			}
		
		flash("SucessAdded", "Meal...");
		return redirect("/cart");
	}
	
	public static Result removeFromBasket(int id) {
		Meal meal = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
		Cart cart = Cart.findByUserId(u.id);
		
		for (Iterator<CartItem> it = cartItems.iterator(); 
				it.hasNext();) {
			CartItem cartItem = CartItem.find.byId(cart.cartItems.indexOf(1));
			if (cartItem.meal.find(meal.id).equals(Meal.find(id))) {
				if (cartItem.quantity > 1) {
					cartItem.decreaseQuantity();
				} else {
					it.remove();
				}
			}
		}		
		
		flash("successR", "Meal successfully removed");
		return redirect("/cart");
	}
	
	
	public static Result viewMeal(int id){
		Meal meal = Meal.find(id);
		
		email = session("email");
		return ok(mealView.render(email, meal));
	}
			
}