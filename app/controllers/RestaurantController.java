package controllers;

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
		String MealName = inputForm.bindFromRequest().get().name;
		double MealPrice = Double.parseDouble(inputForm.bindFromRequest().get().price);
		if (Meal.create(MealName, MealPrice) == true) {
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
		String MealID = inputForm.bindFromRequest().get().id;
		if (Meal.delete(MealID) == true) {
			return TODO;
		}
		return TODO;
	}
	
	public static Result list(){
		/*
		List<Meal> meals = Meal.all();
		*/
		return TODO;
	}
	
	public static Result details(){
		return TODO;
	}


}
