package controllers;

import play.mvc.Result;
import views.html.*;
import views.html.restaurant.mealView;
import views.html.widgets.cart;

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
	static double minOrder;
	static Finder<Integer, Meal> findM = new Finder<Integer, Meal>(Integer.class, Meal.class);
	static Finder<Integer, CartItem> findI = new Finder<Integer, CartItem>(Integer.class, CartItem.class);
	static List<CartItem> cartItems = new ArrayList<CartItem>(0);
	static Finder<Integer, Cart> findC = new Finder<Integer, Cart>(Integer.class, Cart.class);

	
	public static Result showCart(){
		
		if(Session.getCurrentUser(ctx()) == null){
			flash("loginP", "Please login");
			return redirect("/login");
		}
		
		email = session("email");
		User u = Session.getCurrentUser(ctx());
		total = 0;
		
		List<Cart> carts = u.carts;
		
//		if(carts.isEmpty()) {
//			return TODO;
//		}
		
		List<CartItem> cartItems;
		
		for (int i = 0; i < carts.size(); i++) {
			
			Cart newCart = carts.get(i);
			
			try {
				cartItems = newCart.cartItems;
				for (CartItem cartItem : cartItems) {
					newCart.total = newCart.total + cartItem.totalPrice;
					newCart.minOrder = cartItem.meal.restaurant.minOrder;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

//			if (Cart.timeGap(u.id) == false || newCart.paid == true) {
//				return redirect("/");
//			}

		}

//		return ok(cart.render(email, Cart.findLastCart(u.id).cartItems, total, minOrder));		
		

	//	if  ( Cart.timeGap(u.id)==false || newCart.paid==true){
	//		flash("Warning", "Please add Meal to your cart.");
	//		return redirect("/");
	//	}

		return ok(cart.render(email, carts));

		
	}
	
	
	
	
	public static Result addMealToBasket(int id) {

		try {

			if (Session.getCurrentUser(ctx()) == null) {
				flash("Warning", "If you want to order food please Login.");
			}

			Meal meal = Meal.find(id);
			User user = Session.getCurrentUser(ctx());
			String mealOwnerRestaurant = meal.restaurant.name;

			Cart cart = null;

			if (user.carts.isEmpty()) {
				cart = new Cart(user, mealOwnerRestaurant);
				user.carts.add(cart);
				cart.addMealToCart(meal);
				return redirect("/cart");
			} else {

				for (int i = 0; i < user.carts.size(); i++) {
					if (user.carts.get(i).restaurantName
							.equals(mealOwnerRestaurant)) {
						cart = user.carts.get(i);
						break;
					}
				}
				if (cart != null) {
					if (cart == null || cart.paid == true
							|| Cart.timeGap(user.id, cart.id) == false) {
						cart.addMealToCart(meal);
					} else {
						cart.addMealToCart(meal);
						cart.update();
					}
				} else {
					cart = new Cart(user, mealOwnerRestaurant);
					user.carts.add(cart);
					if (cart == null || cart.paid == true
							|| Cart.timeGap(user.id, cart.id) == false) {
						cart.addMealToCart(meal);
					} else {
						cart.addMealToCart(meal);
						cart.update();
					}

				}
			}

			flash("SucessAdded", "Successfully added Meal.");
			return redirect("/cart");

		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			flash("Error", e.getMessage());
			return redirect("/");
		}

	}

	
//	public static Result bindQuantity(int mealId) {
//
//		Meal meal = Meal.find(mealId);
//
//		if (meal == null) {
//			flash("Warning", "MA GREŠKA");
//			return redirect("/");
//		} else {
//			User user = Session.getCurrentUser(ctx());
//			Cart cart = Cart.findLastCart(user.id);
//			if(cart == null) {
//				flash("Warning", "CART IS NULL");
//				return redirect("/");
//			}
//			if (Session.getCurrentUser(ctx()) == null) {
//				flash("Warning", "If you want to order food please Login.");
//			} else {
//				cart = Cart.findLastCart(user.id);
//			}
//
//			cart.addMealToCartButton(meal);
//			cart.update();
//			return redirect("/cart");
//		}
//	}
	
	public static Result bindQuantity(int mealId, int cartId) {

		Meal meal = Meal.find(mealId);

		if (meal == null) {
			flash("Warning", "MA GREŠKA");
			return redirect("/");
		} else {
			User user = Session.getCurrentUser(ctx());
			Cart cart = Cart.findCartInCarts(user.id, cartId);
			if(cart == null) {
				flash("Warning", "CART IS NULL");
				return redirect("/");
			}
			if (Session.getCurrentUser(ctx()) == null) {
				flash("Warning", "If you want to order food please Login.");
			} else {
				cart = Cart.findCartInCarts(user.id, cartId);
			}

			cart.addMealToCartButton(meal);
			cart.update();
			return redirect("/cart");
		}
	}
	
	
	public static Result removeFromCart(int id, int cartId) {
		Meal m = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
		Cart cart = Cart.findCartInCarts(u.id, cartId);
		if(cart == null) {
			flash("Warning", "CART IS NULL");
			return redirect("/");
		}
		List<CartItem> newCartItems = cart.cartItems;
		Iterator<CartItem> iter = newCartItems.iterator();
		int quantity = 0;
		while(iter.hasNext()){
			CartItem ci = iter.next();
			if( ci.meal.equals(m)){
				quantity = ci.quantity;
			}
		}
		if( quantity > 0){
			cart.removeMeal(m, u.id, cartId);
//			cart.update();	
			return redirect("/cart");
		}
		
		return redirect("/cart");
	}
		

	
	
//	public static Result viewMeal(int id) {
//		Meal meal = Meal.find(id);
//		List<Image> imgs = meal.image;
//		if (meal == null) {
//			flash("Warning", "MA GREŠKA");
//			return redirect("/");
//		} else {
//			email = session("email");
//			return ok(mealView.render(email, meal));
//		}
//	}
	
	public static Result viewMeal(int id){
		Meal meal = Meal.find(id);
		List<Image> imgs = meal.image;
		Restaurant restaurant = Restaurant.findByMeal(meal);
		email = session("email");

		return ok(views.html.restaurant.mealView.render(email, meal,imgs,restaurant));

	}
			
}