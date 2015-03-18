package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import models.Location;
import models.Meal;
import models.Restaurant;
import models.User;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import views.html.*;
import Utilites.AdminFilter;
import Utilites.Session;
import play.data.DynamicForm;
import play.db.ebean.Model.Finder;
import Utilites.*;



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
	
	@Security.Authenticated(UserFilter.class)
	public static Result toUser(String email){
		List <Restaurant> restaurants = findR.all();
		List <Meal> meals = findM.all();
		String emailE = session().get("email");
		if(emailE == null)
			return redirect("/login");

		User u = User.find(email);
		if(u.role.equals(User.RESTAURANT)){
			return redirect("restaurantOwner/" + email);
		}
		if(u.role.equals(User.ADMIN)){
			return ok(admin.render(email, meals, restaurants));
		}
		return ok(user.render(email, restaurants));
	}
	
	@Security.Authenticated(UserFilter.class)
	public static Result user(String email){
		
		List <Meal> meals = findM.all();
		List <Restaurant> restaurants = findR.all();
		
		User u = User.find(email);
		
		return ok(user.render(email, restaurants));
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
			return ok(restaurantOwner.render(email, meals, restaurants));
		}
	}
	

	/**
	 * This method just redirects to login page.
	 * 
	 * @return
	 */
	public static Result toLogin() {				
		if(session().get("email") != null){			
			return redirect("/");
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
		DynamicForm form = Form.form().bindFromRequest();
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
		
		User usr = new User(email, hashedPassword);		
		Location loc = new Location("", "", "");
		usr.location = loc;
		loc.save();
		usr.confirmationString = UUID.randomUUID().toString();
		if (usr.checkIfExists(email) == true) {
			flash("inDatabase",
					Messages.get("User already exists - please confirm user or login"));
			return redirect("/login");
		}
		loc.user = usr;
		usr.save();
		loc.update();
		

		if (usr.checkA(email, hashedPassword) == true) {
			// First we need to create url and send confirmation mail to user
			// (with url inside).
			String urlString = "http://localhost:9000" + "/" + "confirm/"
					+ usr.confirmationString;
			URL url = new URL(urlString);
			MailHelper.send(email, url.toString());
			if (usr.validated == true) {
				return redirect("/");
			}
			flash("validate", Messages.get("Please check your email"));
			return redirect("/registration");
		} else {
			return redirect("/registration");
		}
	}
	
	public static Result toRegistrationRestaurant(){		
		return ok(registrationRestaurant.render(email));
	}
	
	
	public static Result registrationRestaurant(){
		DynamicForm form = Form.form().bindFromRequest();	
			
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
				
		User restaurantUser = new User (email, hashedPassword, "RESTAURANT");
		
		String name = form.data().get("name");
		
		
		String street = form.data().get("street");
		String number = form.data().get("number");
		String city = form.data().get("city");
		User.createRestaurant(name, email, hashedPassword, street, number, city);

		Location restaurantLocatin = new Location(street, number, city);
		
		
		
		
		flash("successSendRequest", "You have succesfully send request for restaurant registration! Wait until admin contacts you!");
		
		return redirect("/");
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
				return ok(restaurantOwner.render(email, meals, restaurants));

			if(Session.getCurrentRole(ctx()).equals(User.USER))
				return ok(index.render(" ", email, meals, restaurants));
			if(Session.getCurrentRole(ctx()).equals(User.ADMIN))
				return ok(admin.render(email, meals, restaurants));
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
				return redirect("/admin/" + email);
			else if (role.equalsIgnoreCase(User.RESTAURANT))
				return redirect("/restaurantOwner/" + email);
			else			
				return redirect("/user/" + email);
		} else {
            flash("failed", Messages.get("Incorrect username/pass or user is not verified"));
			return redirect("/login");
		}
	}
	
	/**
	 * Method taht goes to Public restaurant view
	 * @return
	 */
	public static Result toRestaurant(String name){
		
		Restaurant restaurant = Restaurant.findByName(name);
		User restaurantUser = restaurant.user;
		List<Meal> restaurantMeals = Meal.allById(restaurantUser);
		return ok(restaurantProfile.render(email, name, restaurantMeals));
	}
	
	
	/**
	 * 
	 * This method is used to change password on user profile page.
	 * @return redirect on profile page.
	 */
	public static Result editUser(String email) {		
		DynamicForm form = Form.form().bindFromRequest();
		User currentUser = Session.getCurrentUser(ctx());
				
		String hashedPassword = form.data().get("hashedPassword");
		String city = form.data().get("city");
		String street = form.data().get("street");
		String number = form.data().get("number");

		currentUser.hashedPassword = Hash.hashPassword(hashedPassword);
		currentUser.location.city = city;
		currentUser.location.street = street;
		currentUser.location.number = number;
		currentUser.location.update();
		currentUser.update();
		
		flash("successUpdate", "You have successfully updated contact information");
		return redirect("/user/" + email);
	}
	
	/**
	 * 
	 * @return
	 */
	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.index());
	}

}