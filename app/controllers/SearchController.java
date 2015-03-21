package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import play.Logger;
import models.Faq;
import models.Location;
import models.Meal;
import models.Restaurant;
import models.User;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import views.html.*;
import Utilites.AdminFilter;
import Utilites.Session;
import play.data.DynamicForm;
import play.db.ebean.Model.Finder;
import Utilites.*;

public class SearchController extends Controller {

	public static DynamicForm form = DynamicForm.form().bindFromRequest();

	public static Result autocomplete(String q) {
		List<Meal> meals = Meal.find.where().like("name", "%" + q + "%")
				.findList();

		for (Meal m : meals) {
			return TODO;
		}
		return TODO;
	}

	/**
	 * This method accepts the string sequence to be checked
	 * and typeGen variable which tells us what generic is about to be used 
	 * 
	 * 
	 * @param q String sequence
	 * @param typeGen  Generic to be searched
	 * @return searchResults as Lists
	 */
	public static Result searchByGeneric(String q, String typeGen) {
		List<Restaurant> RestaurantEmpty = null;
		List<Meal> mealsEmpty = null;
		String currentEmail = null;

		// Check if the current user is logged in;
		// if the user is not logged in then we 
		// assign value null to it
		if (Session.getCurrentUser(ctx()) != null) {
			currentEmail = Session.getCurrentUser(ctx()).email;

		}

		// check to see what type value we are searching
		// if typegen is not sellected default action is to search all meals
		
		if (typeGen == null) {
			return ok(searchResult.render(" ", currentEmail, Restaurant.all(),
					RestaurantEmpty, searchAllMeals(q)));
		}
        
		
		if (typeGen.equals("Meal")) {
			return ok(searchResult.render(" ", currentEmail, Restaurant.all(),
					RestaurantEmpty, searchAllMeals(q)));

		}
		if (typeGen.equals("Restaurant")) {
			return ok(searchResult.render(" ", currentEmail, Restaurant.all(),
					searchAllRestaurants(q), mealsEmpty));

		}
		return TODO;
	}

	/** Methods which search the string 
	 * seqemce 
	 * 
	 * @param q String sequence
	 * @return
	 */
	 
	 
	
	public static List<Meal> searchAllMeals(String q) {
		List<Meal> meals = Meal.find.where().ilike("name", "%" + q + "%")
				.findList();

		return meals;
	}

	public static List searchAllRestaurants(String q) {
		List<Restaurant> restaurants = Restaurant.find.where()
				.ilike("name", "%" + q + "%").findList();
		return restaurants;
	}

}
