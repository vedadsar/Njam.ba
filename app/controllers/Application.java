package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import models.Meal;
import models.Restaurant;
import models.User;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import views.html.*;
import Utilites.Session;
import play.data.DynamicForm;
import play.db.ebean.Model.Finder;
import Utilites.MailHelper;



public class Application extends Controller {

	static String email = null;
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);
	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);

	/**
	 * This method just redirects to index page.
	 * 
	 * @return
	 */
	public static Result index() {

		List <Meal> meals = findM.all();
		List <Restaurant> restaurants = findR.all();
		email = session("email");
		return ok(index.render(" ", email, meals, restaurants));
	}
	
	public static Result toUser(){
		List <Restaurant> restaurants = findR.all();
		String email = session().get("email");
		if(email == null)
			return redirect("/login");

		User u = User.find(email);
		if(u.role.equals(User.RESTAURANT)){
			return ok(restaurant.render("", email));
		}
		if(u.role.equals(User.ADMIN)){
			return ok(admin.render(" ", restaurants));
		}
		return ok(user.render(email));
	}

	/**
	 * This method just redirects to registration page.
	 * 
	 * @return
	 */
	public static Result toRegistration() {
		List <Meal> meals = findM.all();
		List <Restaurant> restaurants = findR.all();
		
		if((session().get("email") != null) )
			return ok(wrong.render("Cannot acces to registration page while you're logged in"));

		if(email == null){
			return ok(registration.render(""));
		} else {
			return ok(index.render(" ", email, meals, restaurants));
		}
	}

	/**
	 * This method just redirects to login page.
	 * 
	 * @return
	 */
	public static Result toLogin() {				
		if(session().get("email") != null){			
			return redirect("/user");
		} else {
			return ok(login.render(""));
		}
	}

	/**
	 * This method registers new user. If user doesn't exist, method will
	 * register it and redirect to index page, so user can login. If user
	 * already exists, method will redirect to register page, with error
	 * message.
	 * 
	 * @return
	 * @throws MalformedURLException 
	 */
	public static Result registration() throws MalformedURLException {
		String message = "Thanks for your registration";
		DynamicForm form = Form.form().bindFromRequest();
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
		
		if(hashedPassword.length() < 6){
			return ok(registration.render("Password length is not valid"));
		}
		User usr = new User(email,hashedPassword);
		usr.confirmationString = UUID.randomUUID().toString();
		if(usr.checkIfExists(email) == true){
            flash("inDatabase", Messages.get("User already exists - please confirm user"));
			return redirect("/restaurant");
		}
		usr.save();
		
		if (usr.checkA(email, hashedPassword) == true) {
	        String urlString = "http://localhost:9000" + "/" + "confirm/" + usr.confirmationString;
	        URL url = new URL(urlString); 
	    	MailHelper.send(email, url.toString());
	    	if(usr.validated == true){
				return TODO;
	    	}
            flash("validate", Messages.get("Please check your email"));
			return redirect("/registration");
		} else {
			return redirect("/registration");

		}
	}
	

	public static Result registerRestaurant() {
		List <Restaurant> restaurants = findR.all();
		DynamicForm form = Form.form().bindFromRequest();
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
		String nameOfRestaurant = form.data().get("name");		

		boolean isSuccess = User.createRestaurant(nameOfRestaurant, email, hashedPassword);
		if (isSuccess == true) {			
			return ok(admin.render("You successfuly created restaurant with email: " +email, restaurants));
		} else {
			return ok(admin.render("Restaurant with that email is already registred", restaurants));

		}
	}
	

	/**
	 * This method logs in user. If user exists, method will redirect to user
	 * page. If user doesn't exist, method will redirect to login page, with
	 * error message
	 * 
	 * @return
	 */
	public static Result login() {
		List <Meal> meals = findM.all();
		List <Restaurant> restaurants = findR.all();
		
		if(Session.getCurrentUser(ctx()) != null){
			if(Session.getCurrentRole(ctx()).equals(User.RESTAURANT))
				return ok(index.render(" ", email, meals, restaurants));
			if(Session.getCurrentRole(ctx()).equals(User.USER))
				return ok(index.render(" ", email, meals, restaurants));
			if(Session.getCurrentRole(ctx()).equals(User.ADMIN))
				return ok(admin.render("", restaurants));
		}
		
		
		DynamicForm form = Form.form().bindFromRequest();
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
		boolean isSuccess = User.authenticate(email, hashedPassword);
		//If we successful created we redirect user
		//depending on its role !
		if (isSuccess == true) {
			session("email", email);
			String role = User.checkRole(email);
			if(role.equalsIgnoreCase(User.ADMIN))
				return ok(admin.render("", restaurants));
			else if (role.equalsIgnoreCase(User.RESTAURANT))
				return ok(index.render(" ", email, meals, restaurants));
			else			
				return ok(index.render(" ", email, meals, restaurants));
		} else {
            flash("failed", Messages.get("Incorrect username/pass or user is not verified"));
			return redirect("/login");
		}
	}
	
	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.index());
	}

}