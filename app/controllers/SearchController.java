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

	
	
	public static Result searchByGeneric(String q,String typeGen) {
		List <Restaurant> RestaurantEmpty=null;
		List<Meal> mealsEmpty = null;
		String currentEmail=null;
	//	String typeGen= form.data().get("typeGen");
		Logger.debug(typeGen);
		
	
	//Check if the current user is logged in;
	 if (Session.getCurrentUser(ctx())!=null)
     {currentEmail =Session.getCurrentUser(ctx()).email ;
     
     }
	 
	 //check to se  what tipe value  we  are searching
	    if (typeGen==null){
	    	return  ok(searchResult.render(" ",currentEmail,Restaurant.all(),RestaurantEmpty,searchAllMeals(q)));
	    }
	 
		if (typeGen.equals("Meal")) {
			return ok(searchResult.render(" ",currentEmail,Restaurant.all(),RestaurantEmpty, searchAllMeals(q)
					));
			
			
		}  if (typeGen.equals("Restaurant")) {
			return ok(searchResult.render(" ",currentEmail,Restaurant.all(),searchAllRestaurants(q),mealsEmpty));
		
		}
		return TODO;
	}

	
	public static List<Meal> searchAllMeals(String q) {
		List<Meal> meals                   = Meal.find.where().ilike("name", "%" + q+ "%")
				.findList();
			
		return meals;
	}
	

	public static List searchAllRestaurants(String q) {
		List<Restaurant> restaurants = Restaurant.find.where().ilike("name", "%" + q + "%").findList();
		return restaurants;
	}

}
