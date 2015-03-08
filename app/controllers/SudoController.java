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
	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);

	public static Result createRestaurant(){
		
		List <Restaurant> restaurants = findR.all();
		String email = inputForm.bindFromRequest().get().email;
		String password = inputForm.bindFromRequest().get().hashedPassword;			

		User.createRestaurant(email, password);		
		User user = User.find(email);
		Restaurant.create("No Name", user);
		return ok(admin.render("You have just created restaurant with email: " +email, restaurants));
	}
	
}