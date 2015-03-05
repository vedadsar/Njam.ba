package controllers;

import java.util.List;

import Utilites.Session;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

/**
 * Controller class for restaurant. Restaurant is able to create/modify/delete
 * meal.
 * 
 * @author vedad & nedim
 *
 */
public class RestaurantController extends Controller {

	static Form<Meal> inputForm = new Form<Meal>(Meal.class);

	/**
	 * Method to create meal.
	 * Use Meal.create method from models.
	 * @return
	 */
	public static Result createMeal() {
		User u= Session.getCurrentUser(ctx());
		if(!u.role.equalsIgnoreCase("RESTAURANT")){
			return null; //Redirect to index, user is not restaurant !
		}
		
		String mealName = inputForm.bindFromRequest().get().name;
		double mealPrice = inputForm.bindFromRequest().get().price;
		if (Meal.create(mealName, mealPrice) == true) {
			return TODO;
		}
		return null;
	}

	/**
	 * Method to delete meal by
	 * using meal ID which is unique for each meal.
	 * @return
	 */
	public static Result deleteMeal() {
		int mealID = inputForm.bindFromRequest().get().id;
		Meal.delete(mealID);
		return TODO;
	}
	
	public static Result list(){
		List<Meal> meals = Meal.all();
		return TODO;
	}
	
	public static Result details(int id){
		return TODO;
	}	
}