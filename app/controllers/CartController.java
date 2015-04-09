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
	static double minOrder;
	static Finder<Integer, Meal> findM = new Finder<Integer, Meal>(Integer.class, Meal.class);
	static Finder<Integer, CartItem> findI = new Finder<Integer, CartItem>(Integer.class, CartItem.class);
	static List<CartItem> cartItems = new ArrayList<CartItem>(0);
	static Finder<Integer, Cart> findC = new Finder<Integer, Cart>(Integer.class, Cart.class);

	/**
	 * This method shows user shopping cart.
	 * If user is not logged in our page it will warn it with flash messages.
	 * @return redirection to shopping cart of user.
	 */
	public static Result showCart(){
		email = session("email");
		User u = Session.getCurrentUser(ctx());
		total = 0;
		
		Cart newCart = Cart.findLastCart(u.id);
		if( newCart == null){
			flash("Warning", "Please add  new Meal to your cart.");
			return redirect("/");
		}
		List<CartItem> cartItems;
		try {
			cartItems = newCart.cartItems;
			for (CartItem cartItem : cartItems) {
				total = total + cartItem.totalPrice;
				minOrder = cartItem.meal.restaurant.minOrder;
			}
		} catch (NullPointerException e) {
			flash("Warning", "Now You can add meals to your cart.");
			Logger.error("Empty cartItem "+ e.getMessage() );
		}
		

		if(Session.getCurrentUser(ctx()) == null){
			flash("loginP", "Please login");
			return redirect("/login");
		}
		
		if  ( Cart.timeGap(u.id)==false || newCart.paid==true){
			flash("Warning", "Please add Meal to your cart.");
			return redirect("/");
		}
		
		return ok(views.html.widgets.cart.render(email, Cart.findLastCart(u.id).cartItems, total, minOrder));		
	}
	
	/**
	 * Method that adds Meals to users shopping cart (basket).
	 * @param id - Meal id that user wants to put in cart.
	 * @return redirection to current shopping cart of user.
	 */
	public static Result addMealToBasket(int id){
		
		try {
			Meal meal = Meal.find(id);
			User user = Session.getCurrentUser(ctx());		
			Cart cart = Cart.findLastCart(user.id);

			if(Session.getCurrentUser(ctx())==null ){			
				flash("Warning", "If you want to order food please Login.");
				redirect("/");
			} else{
				cart = Cart.findLastCart(user.id);
			}		
			
			System.out.println("U cart-u je" + cart);
			
			/*If there is users shopping cart is empty 
			 * or last cart that user had is paid
			 * and the time gap from last logging is more that 1 min, t
			 * than we will create new cart.
			 */
			
			if(cart == null || cart.paid==true || Cart.timeGap(user.id) == false)  {
				System.out.println("time gap : " + Cart.timeGap(user.id));
				cart = new Cart(user);
				cart.addMeal(meal);
				CartItem cartItem = new CartItem(cart, 1, meal.price, meal);
				cartItems.add(cartItem);
				cart.total += cartItem.totalPrice;
				cart.paid = false;
				cart.save();
				cartItem.save();
				System.out.println("U If je" + cart);
			} else {
				System.out.println("Else u kontroleru");
				cart.addMealToCart(meal);
				cart.update();
				}
		} catch (NullPointerException e) {
			Logger.error("No User in session: " + e.getMessage());
			flash("Warning", "If you want to order food please Login.");
			redirect("/");
		}	
		
		flash("SucessAdded", "Successfully added Meal.");
		return redirect("/cart");
	}
	
	/**
	 * Binds the quantity of meals that user want to order in shopping cart.
	 * @param mealId - id of meal which quantity user is updating.
	 * @return - cart  with new updated quantity of specific meals.
	 */
	public static Result bindQuantity(int mealId){
		Meal meal = Meal.find(mealId);
		
		User user = Session.getCurrentUser(ctx());
		Cart cart = Cart.findLastCart(user.id);

		if(Session.getCurrentUser(ctx())==null){			
			flash("Warning", "If you want to order food please Login.");
		} else{
			cart = Cart.findLastCart(user.id);
		}
		
		cart.addMealToCartButton(meal);
		cart.update();
		return redirect("/cart");
	}
	
	/**
	 * Method that removes meals form carts.
	 * @param id - id of meal that user wants to remove.
	 * @return- cart with new updated quantity of specific meals.
	 */
	public static Result removeFromCart(int id) {
		Meal m = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
		Cart cart = Cart.findByUserId(u.id);
		
		cart.removeMeal(m);
		
		cart.update();
		
		return redirect("/cart");
	}
		

	
	/**
	 * Method that show user view of one meal.
	 * @param id - id of meal that user wants to see in more details.
	 * @return  - meal view.
	 */
	public static Result viewMeal(int id){
		Meal meal = Meal.find(id);
		
		email = session("email");
		return ok(views.html.restaurant.mealView.render(email, meal));
	}
			
}