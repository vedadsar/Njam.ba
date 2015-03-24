package controllers;

import play.mvc.Result;
import views.html.*;

import java.util.Iterator;
import java.util.List;

import Utilites.AdminFilter;
import Utilites.Session;
import models.*;
import models.orders.Cart;
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
	
	public static Result showCart(){
		email = session("email");
		return ok(cart.render(email));
	}
	
	public static Result addMealToBasket(int id){
		Meal meal = Meal.find(id);
//		User user = User.find(id);
//		Cart cart = new Cart(user.id);
//		cart.addMeal(meal);
//		cart.save();
		flash("SucessAdded", "Meal...");
		return redirect("/");
	}
	
	public static Result viewMeal(int id){
		Meal meal = Meal.find(id);
		email = session("email");
		return ok(mealView.render(email, meal));
	}
	
}
