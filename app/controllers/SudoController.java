package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.db.ebean.Model.Finder;

import java.util.List;

public class SudoController extends Controller{

	static Form<User> inputForm = new Form<User>(User.class);
	static Form<Restaurant> inputR= new Form<Restaurant>(Restaurant.class);

	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);

	public static Result createRestaurant(){
		
		List <Restaurant> restaurants = findR.all();
		List<Meal> meals = findM.all();
		String email = inputForm.bindFromRequest().get().email;
		String password = inputForm.bindFromRequest().get().hashedPassword;			
		String nameOfRestaurant = inputR.bindFromRequest().get().name;

		User.createRestaurant(nameOfRestaurant, email, password);		
//		User user = User.find(email);
//		Restaurant.create("No Name", user);
		//moguce je da je ovaj dio nekoristan
		flash("successRestaurant", "Successfully added Restaurant");
		return redirect("/admin/create");
//		return ok(admin.render(meals, restaurants));
//		flash("successRestaurant", "Successfully added Restaurant");
//		return redirect("/admin");
	}
	
	public static Result administrator(String email) {

		List<Meal> meals = findM.all();
		List<Restaurant> restaurants = findR.all();

		User u = User.find(email);

		return ok(admin.render(email,meals, restaurants));
	}

}