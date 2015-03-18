package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import play.db.ebean.Model.Finder;
import Utilites.*;

import java.util.List;

public class SudoController extends Controller{

	static Form<User> inputForm = new Form<User>(User.class);
	static Form<Restaurant> inputR= new Form<Restaurant>(Restaurant.class);

	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);

	@Security.Authenticated(AdminFilter.class)
	public static Result createRestaurant(){	
		String email = inputForm.bindFromRequest().get().email;
		String password = inputForm.bindFromRequest().get().hashedPassword;			
		String nameOfRestaurant = inputR.bindFromRequest().get().name;
		
		User.createRestaurant(nameOfRestaurant, email, password);	
		flash("successRestaurant", "Successfully added Restaurant");
		return redirect("/admin/create");

	}
	@Security.Authenticated(AdminFilter.class)
	public static Result deleteRestaurant(int id){
		Restaurant r = Restaurant.find(id);
		User u = r.user;
		List<Meal> allMeals = Meal.allById(u);
		
		for(Meal m: allMeals){
			m.restaurant = null;
			Meal.delete(m);
		}
		r.user = null;
		u.restaurant = null;
		r.save();		
		u.save();
		
		Restaurant.delete(id);
		User.deleteUser(u);
		flash("successDeleteRestaurant", "Restaurant successfully deleted");
		
		return redirect("/admin/" +Session.getCurrentUser(ctx()).email);		
	}
	
	
	@Security.Authenticated(AdminFilter.class)
	public static Result administrator(String email) {

		List<Meal> meals = findM.all(); 
		List<Restaurant> restaurants = findR.all();

		User u = User.find(email);

		return ok(admin.render(email,meals, restaurants));
	}

	
	
	public static Result approveRestaurant(int id){
		
		Restaurant restaurant = Restaurant.find(id);
		
		User userRestaurant = restaurant.user;
				
		userRestaurant.validated = true;
		userRestaurant.update();
		
		flash("successApprovedRestaurant", "Restaurant successfully approved!");	
		return redirect("/admin/" + id);
		
	}
}