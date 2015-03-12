package controllers;

import java.util.List;
import Utilites.AdminFilter;
import Utilites.Session;
import models.*;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Security;
import views.html.*;
import Utilites.*;

/**
 * Controller class for restaurant. Restaurant is able to create/modify/delete
 * meal.
 * 
 * @author vedad & nedim
 *
 */
public class RestaurantController extends Controller {

	static Form<Meal> inputForm = new Form<Meal>(Meal.class);
	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);



	/**
	 * Method to create meal.
	 * Use Meal.create method from models.
	 * @return
	 */
	@Security.Authenticated(RestaurantFilter.class)
	public static Result createMeal() {
		User u= Session.getCurrentUser(ctx());
		if(!u.role.equalsIgnoreCase("RESTAURANT")){
			return ok(wrong.render("Cannot create meal if you're not reastaurant! "));
		}
		
		String mealName = inputForm.bindFromRequest().field("name").value();
		String mealPrice = inputForm.bindFromRequest().field("price").value();
		
		mealPrice = mealPrice.replace(',', '.');
		Double price = Double.parseDouble(mealPrice);
		
		if (Meal.create(mealName, price) == true) {
//			return ok(restaurant.render("Successfully created meal", Session.getCurrentUser(ctx()).email));
			String userEmail= Session.getCurrentUser(ctx()).email;
			 session("email", userEmail);
			 flash("successMeal", "Succesfully created meal!");
			 return redirect("/restaurantOwner/" + userEmail);
		}
		return TODO;
	}

	/**
	 * Method to delete meal by
	 * using meal ID which is unique for each meal.
	 * @return
	 */
	@Security.Authenticated(RestaurantFilter.class)
	public static Result deleteMeal() {
		int mealID = inputForm.bindFromRequest().get().id;
		Meal.delete(mealID);
		return redirect("/restaurantOwner");
	}
	
	/**
	 * Method that returns list of restaurants.
	 * @return list of all meals from Restaurant
	 */
	public static Result list(){
		List<Meal> meals = Meal.all();
		return TODO;
	}
	

	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Result details(int id){
		return TODO;
	}	
	
	@Security.Authenticated(RestaurantFilter.class)
	public static Result restaurant(String email){			
		List <Restaurant> restaurants = findR.all();		
		User u = User.find(email);
		List <Meal> meals = Meal.allById(u);
		
		return ok(restaurantOwner.render(email, meals, restaurants));
	}
}