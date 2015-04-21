package controllers;

import play.libs.Json;
import play.mvc.Result;
import views.html.*;
import views.html.restaurant.mealView;
import views.html.widgets.cart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

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
	static Finder<Integer, Meal> findM = new Finder<Integer, Meal>(
			Integer.class, Meal.class);
	static Finder<Integer, CartItem> findI = new Finder<Integer, CartItem>(
			Integer.class, CartItem.class);
	static List<CartItem> cartItems = new ArrayList<CartItem>(0);
	static Finder<Integer, Cart> findC = new Finder<Integer, Cart>(
			Integer.class, Cart.class);

	public static Result showCart() {

		if (Session.getCurrentUser(ctx()) == null) {
			flash("loginP", "Please login");
			return redirect("/login");
		}

		email = session("email");
		User u = Session.getCurrentUser(ctx());
		total = 0;

		List<Cart> carts = u.carts;
		Cart newCart = null;

		// if(carts.isEmpty()) {
		// return TODO;
		// }

		List<CartItem> cartItems = null;

		for (int i = 0; i < carts.size(); i++) {

			newCart = carts.get(i);

			try {
				cartItems = newCart.cartItems;
				for (CartItem cartItem : cartItems) {
					newCart.total = newCart.total + cartItem.totalPrice;
					newCart.minOrder = cartItem.meal.restaurant.minOrder;
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

			// if (Cart.timeGap(u.id) == false || newCart.paid == true) {
			// return redirect("/");
			// }

		}

		// return ok(cart.render(email, Cart.findLastCart(u.id).cartItems,
		// total, minOrder));

		// if ( Cart.timeGap(u.id)==false || newCart.paid==true){
		// flash("Warning", "Please add Meal to your cart.");
		// return redirect("/");
		// }

		return ok(views.html.widgets.cart.render(email, carts));

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
							.equals(mealOwnerRestaurant)
							&& user.carts.get(i).paid == false) {
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

	public static Result bindQuantity(int mealId, int cartId) {

		Meal meal = Meal.find(mealId);

		if (meal == null) {
			flash("Warning", "MA GREŠKA");
			return redirect("/");
		} else {
			User user = Session.getCurrentUser(ctx());
			Cart cart = Cart.findCartInCarts(user.id, cartId);
			if (cart == null) {
				flash("Warning", "CART IS NULL");
				return redirect("/");
			}
			if (Session.getCurrentUser(ctx()) == null) {
				flash("Warning", "If you want to order food please Login.");
			} else {
				cart = Cart.findCartInCarts(user.id, cartId);
			}

			cart.addMealToCart(meal);
			cart.update();
			return redirect("/cart");
		}
	}

	/**
	 * 
	 * @param id
	 * @param cartId
	 * @return
	 */
	public static Result removeFromCart(int id, int cartId) {
		Meal m = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
		Cart cart = Cart.findCartInCarts(u.id, cartId);
		if (cart == null) {
			flash("Warning", "CART IS NULL");
			return redirect("/");
		}
		List<CartItem> newCartItems = cart.cartItems;
		Iterator<CartItem> iter = newCartItems.iterator();
		int quantity = 0;
		while (iter.hasNext()) {
			CartItem ci = iter.next();
			if (ci.meal.equals(m)) {
				quantity = ci.quantity;
			}
		}
		if (quantity > 0) {
			cart.removeMeal(m, u.id, cartId);
			return redirect("/cart");
		}

		return redirect("/cart");
	}

	public static Result removeAllFromCart(int id, int cartId) {
		Meal m = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
		Cart cart = Cart.findCartInCarts(u.id, cartId);

		cart.removeMealAll(m);

		return redirect("/cart");

	}

	// public static Result viewMeal(int id) {
	// Meal meal = Meal.find(id);
	// List<Image> imgs = meal.image;
	// if (meal == null) {
	// flash("Warning", "MA GREŠKA");
	// return redirect("/");
	// } else {
	// email = session("email");
	// return ok(mealView.render(email, meal));
	// }
	// }

	public static Result viewMeal(int id) {
		Meal meal = Meal.find(id);
		List<Image> imgs = meal.image;
		Restaurant restaurant = Restaurant.findByMeal(meal);
		email = session("email");
		List<Restaurant> restaurants = Restaurant.all();
		List<Comment> comments = Comment.find.findList();

		return ok(views.html.restaurant.mealView.render(email, meal, imgs,
				restaurant, restaurants, comments));

	}
	
	public static Result giveMeDetails(String id) {
		Logger.debug("ID KOJI JE STIGAO KAO STRING JE: " + id);
		int x = Integer.parseInt(id);
		Logger.debug("PRETVORIO SAM STRING U INTEGER I SAD GLASI: " + x);
		TransactionU transaction = TransactionU.find(x);
		Cart cart = Cart.find(transaction.cartToPayId);
		List<CartItem> cartitems = transaction.items;
		
		if(cartitems.isEmpty()) Logger.debug("LISTA JE PRAZNA! LISTA JE PRAZNA! LISTA JE PRAZNA! LISTA JE PRAZNA!");
		
		for(int i=0; i<cartitems.size(); i++) {
				Logger.debug("U ITEMS LISTI:" + cartitems.get(i).getMealName());
		}
		
		List<MetaItem> metaitems = new ArrayList<MetaItem>();
		
		for(int i=0; i<cartitems.size(); i++) {
			
			String name = cartitems.get(i).getMealName(); 
			Logger.debug("NAME JE: " + name);
			double price = cartitems.get(i).meal.price; 
			int quantity = cartitems.get(i).quantity;
			double totalPrice = cartitems.get(i).totalPrice;
			MetaItem meta = new MetaItem(name, price, quantity, totalPrice);
			metaitems.add(meta);
		}
		
		
		JsonNode jsonNode = Json.toJson(metaitems);
		Logger.debug(jsonNode.toString());
		return ok(jsonNode);
	}

}