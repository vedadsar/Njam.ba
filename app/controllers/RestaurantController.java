package controllers;

import java.util.List;
import Utilites.Session;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

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
			return ok(wrong.render("Cannot create meal if you're not reastaurant! "));
		}
		
		String mealName = inputForm.bindFromRequest().field("name").value();
		String mealPrice = inputForm.bindFromRequest().field("price").value();
		
		mealPrice = mealPrice.replace(',', '.');
		Double price = Double.parseDouble(mealPrice);
		
		if (Meal.create(mealName, price) == true) {
			return redirect("/restaurant");
		}
		return TODO;
	}

	/**
	 * Method to delete meal by
	 * using meal ID which is unique for each meal.
	 * @return
	 */
	public static Result deleteMeal() {
		int mealID = inputForm.bindFromRequest().get().id;
		Meal.delete(mealID);
		return redirect("/restaurant");
	}
	
	public static Result list(){
		List<Meal> meals = Meal.all();
		return TODO;
	}
	
	public static Result details(int id){
		return TODO;
	}	
}