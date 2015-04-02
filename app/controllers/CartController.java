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

	
	public static Result showCart(){
		email = session("email");
		User u = Session.getCurrentUser(ctx());
		total = 0;

		Cart newCart = Cart.findLastCart(u.id);
		List<CartItem> cartItems;
		try {
			cartItems = newCart.cartItems;
			for (CartItem cartItem : cartItems) {
				total = total + cartItem.totalPrice;
				minOrder = cartItem.meal.restaurant.minOrder;
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		if(Session.getCurrentUser(ctx()) == null){
			flash("loginP", "Please login");
			return redirect("/login");
		}
		
		if  ( Cart.timeGap(u.id)==false || newCart.paid==true){
			flash("Warning", "Please add Meal to your cart.");
			return redirect("/");
		}
		

		return ok(cart.render(email, Cart.findLastCart(u.id).cartItems, total, minOrder));		
	}
	
	
	public static Result addMealToBasket(int id){
		Meal meal = Meal.find(id);
		
		/*
		Cart cart = findC.byId(cartID);
		Logger.debug(String.valueOf(cartID));
		*/
		User user = Session.getCurrentUser(ctx());
//		Cart cart = Cart.findByUserId(user.id);
		Cart cart = Cart.findLastCart(user.id);
//		Cart cart = null ;

		if(Session.getCurrentUser(ctx())==null){			
			flash("Warning", "If you want to order food please Login.");
		} else{
			cart = Cart.findLastCart(user.id);
		}
		
		
		System.out.println("U cart-u je" + cart);
		
		
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
		
		/*
		Cart newCart = null;
		if (cart.paid==false || Cart.timeGap(user.id) == false){
			newCart = new Cart(user);
			newCart.addMeal(meal);
			CartItem newCartItem = new CartItem(cart, 1, meal.price, meal);
			cartItems.add(newCartItem);
			newCart.total += newCartItem.totalPrice;
			newCart.paid=false;
			newCart.save();
			newCartItem.save();
		} else {
			
			newCart.addMealToCart(meal);
			newCart.update();
		}
		*/
	
		
		
		flash("SucessAdded", "Successfully added Meal.");
		return redirect("/cart");
	}
	
	public static Result removeFromBasket(int id) {
		Meal meal = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
//		Cart cart = Cart.findByUserId(u.id);
		Cart cart = Cart.findLastCart(u.id);
	
		
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