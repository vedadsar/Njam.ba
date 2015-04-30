package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
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

	public static String sequenceBuilt;
	public static String locationBuilt;
	public static String restaurantBuilt;

	public static int max;
	public static boolean ascending;
	
	
	public static DynamicForm form = DynamicForm.form().bindFromRequest();

	
	public static Finder<Integer, Meal> find = new Finder<Integer, Meal>(
			Integer.class, Meal.class);
	
	

	
	public static Result redirect (){
		List<Meal> mealsEmpty = new ArrayList(0);
		return ok(searchAdvanced.render(" ", " ", Restaurant.all(),
				mealsEmpty,"","",""));
	}

	public static Result autocomplete(String q) {
		List<Meal> meals = Meal.find.where().like("name", "%" + q + "%").findList();

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
	public static Result searchByGeneric(String q) {
		List<Restaurant> RestaurantEmpty = null;
		List<Meal> mealsEmpty = null;
		String currentEmail = null;
       String typeGen="Meal";
		
		// Check if the current user is logged in;
		// if the user is not logged in then we 
		// assign value null to it
        
		if (Session.getCurrentUser(ctx()) != null) {
			currentEmail = Session.getCurrentUser(ctx()).email;

		}

		// check to see what type value we are searching
		// if typegen is not sellected default action is to search all meals
		
		if (typeGen == null) {
			return ok(views.html.index.render(" ", currentEmail,searchAllMeals(q), Restaurant.all()));
		}
        
		
		if (typeGen.equals("Meal")) {
			return ok(views.html.index.render(" ", currentEmail,searchAllMeals(q), Restaurant.all()
					));

		}
		
		return TODO;
	}
	
	
	
	public static Result searchByCity(String city) {
		List<Restaurant> RestaurantEmpty = null;
		List<Meal> mealsEmpty = null;
		String currentEmail = null;
       String typeGen="Meal";
		
		// Check if the current user is logged in;
		// if the user is not logged in then we 
		// assign value null to it
        
		if (Session.getCurrentUser(ctx()) != null) {
			currentEmail = Session.getCurrentUser(ctx()).email;

		}

		// check to see what type value we are searching
		// if typegen is not sellected default action is to search all meals
		
		if (typeGen == null) {
			return ok(views.html.index.render(" ", currentEmail,searchAllMeals(city), Restaurant.all()));
		}
        
		
		if (typeGen.equals("Meal")) {
			return ok(views.html.index.render(" ", currentEmail,searchAllMealsByCity(city), Restaurant.all()
					));

		}
		
		return TODO;
	}
	
	
	/**
	 * This method renders userSearch HTML page.
	 * @return
	 */
	public static Result ajaxSearch() {
		return ok(views.html.user.userSearch.render(""));
	}
	
	/**
	 * This method takes text from search bar and finds users who contain 
	 * that string in their email, and packs them all in ArrayList and sends them
	 * back to Ajax function as JSON.
	 * @return Result as JSON
	 */
	public static Result ajaxList() {
		DynamicForm form = Form.form().bindFromRequest();
		String name = form.data().get("name");

		if (name.equals("") == false) {
			List<User> users = searchAllUsers(name);
			if (users.isEmpty()) {
				List<String> empty = new ArrayList<String>();
				empty.add("No results found!");
				JsonNode failNode = Json.toJson(empty);
				return ok(failNode);
			}

			List<String> emails = new ArrayList<String>();
			for (int i = 0; i < users.size(); i++) {
				String value = users.get(i).email;
				emails.add(value);
			}
			JsonNode jsonNode = Json.toJson(emails);
			return ok(jsonNode);
			
		} else {
			List<String> empty = new ArrayList<String>();
			empty.add("No results found!");
			JsonNode failNode = Json.toJson(empty);
			return ok(failNode);
		}
	}

	
	/**Method for building search parameters from  advancedSearch Form
	 * and binds the values to static class variables.
	 */
	public static void buildSearchfromForm(){
		 sequenceBuilt= form.bindFromRequest().field("sequence").value();
		 locationBuilt=form.bindFromRequest().field("location").value();
	     restaurantBuilt=form.bindFromRequest().field("restaurant").value();
	     Logger.debug(locationBuilt);
	}
	
	
	/**
	 * Method to build search parameters based on String input
	 */
	public static void buildSearchFromString(String inSequence,String inLocation,String inrestaurant,String inMin,String inMax){
		sequenceBuilt= inSequence;
		locationBuilt=inLocation;
		restaurantBuilt=inrestaurant;
	
	}
	
		
/**
 * Result of advanced search also the default route method
 * for listing meals from parameters.
 * @return
 */
	public static Result advancedSearch(){
		buildSearchfromForm();
		List<Meal> m = mealAdvancedSearch();
			Logger.debug(String.valueOf(m.size()));

		
	return ok(searchAdvanced.render(" ", " ", Restaurant.all(),
			 m,sequenceBuilt,locationBuilt,restaurantBuilt));
	
	}
	
	
	/**Scalable method  for searching through the
	 * meal database
	 * 
	 * @return List<Meal> that satisfies the search parameter
	 */
	public static List <Meal> mealAdvancedSearch(){
	List <Meal> m = find.where().ilike("name","%"+sequenceBuilt+"%").ilike("restaurant.user.locations.city", "%"+locationBuilt+"%").ilike("restaurant.name","%"+restaurantBuilt+"%").findList(); 
	return m;	
	}
	
	
	
	/** Methods which search the string 
	 * sequence  for meals
	 * 
	 * @param q String sequence
	 * @return
	 */
	 	
	public static List<Meal> searchAllMeals(String q) {
		List<Meal> meals = Meal.find.where().ilike("name", "%" + q + "%").findList();
				
		return meals;
	}

	public static List<Meal> searchAllMealsByCity(String q) {
		List<Meal> meals = Meal.find.where().ilike("restaurant.user.locations.city", "%"+q+"%").findList();
				
		return meals;
	}
	

	public static List searchAllRestaurants(String q) {
		List<Restaurant> restaurants = Restaurant.find.where()
				.ilike("name", "%" + q + "%").findList();
		return restaurants;
	}

	public static List<User> searchAllUsers(String q) {
		List<User> users = User.find.where()
				.ilike("email", "%" + q + "%").findList();
		return users;
	}
	
}
