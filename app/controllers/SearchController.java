package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

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
	
	public static Result autocomplete(String q){
		List<Meal> meals = Meal.find.where().like("name", "%" + q + "%").findList();
				
		for(Meal m : meals){
			return TODO;
		}
		return TODO;
	}
}
