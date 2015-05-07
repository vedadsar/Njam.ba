package controllers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import controllers.api.UserApi;
import models.Faq;
import models.Location;
import models.Meal;
import models.Pin;
import models.Restaurant;
import models.TransactionU;
import models.User;
import play.Logger;
import play.Play;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import views.html.*;
import views.html.restaurant.*;
import Utilites.AdminFilter;
import Utilites.RestaurantFilter;
import Utilites.Session;
import play.data.DynamicForm;
import play.db.ebean.Model.Finder;
import Utilites.*;

/**
 * @author amrapoprzanovic
 *
 */
public class Application extends Controller {

	static String email = null;
	private static String hostUrl = Play.application().configuration().getString("hostUrl");
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);
	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	static Finder<Integer, Faq> findF =  new Finder<Integer,Faq>(Integer.class, Faq.class);
	static Finder<Integer, TransactionU> findT =  new Finder<Integer,TransactionU>(Integer.class, TransactionU.class);


	/**
	 * This method just redirects to index page.
	 * 
	 * @return to index view.
	 */
	public static Result index() {

		List <Meal> meals = findM.all();
		List <Restaurant> restaurants = findR.all();
		
		email = session("email");
		return ok(index.render(" ", email, meals, restaurants));
	}
	
	/**
	 * Method that redirect to User page.
	 * @param email - User email.
	 * @return user view depending on the role has (user, administrator, restaurant).
	 */
	@Security.Authenticated(UserFilter.class)
	public static Result toUser(String email){
		List <Restaurant> restaurants = findR.all();
		List <Meal> meals = findM.all();
		List <TransactionU> transactions = findT.all();
		String username = User.find(email).username;

		String emailE = session().get("email");
		if(emailE == null)
			return redirect("/login");

		User u = User.find(email);
		if(u.role.equals(User.RESTAURANT)){
			return redirect("restaurantOwner/" + email);
		}
		if(u.role.equals(User.ADMIN)){
			List<String> logs = SudoController.lastLogs();
			List <Faq> faqs = findF.all();
			
			return ok(views.html.admin.admin.render(email, meals, restaurants, logs, faqs));
		}
		return ok(views.html.user.user.render(email, username, restaurants,transactions, User.find(Session.getCurrentUser(ctx()).id)));
	}
	
	
	// NEPOTREBNA METODA
	
	/*
	@Security.Authenticated(UserFilter.class)
	public static Result user(String email){	
		List <Restaurant> restaurants = findR.all();	
		List <TransactionU> transactions = findT.all();

		String username = User.find(email).username;

		return ok(views.html.user.user.render(email, username, restaurants,transactions, User.find(Session.getCurrentUser(ctx()).id)));
	}
	*/

	/**
	 * This method just redirects to registration page.
	 * 
	 * @return redirection to registration of user.
	 */
	public static Result toRegistration() {
		
		List <Meal> meals = findM.all();
		List <Restaurant> restaurants = findR.all();
		
		User u = Session.getCurrentUser(ctx());
		
		if((session().get("email") != null) )
			return ok(views.html.admin.wrong.render("Cannot acces to registration page while you're logged in"));

		
		if(email == null){
			return ok(registration.render(""));
		} else { 
			Restaurant restaurant = u.restaurant;
			List<TransactionU> tobeapproved = restaurant.toBeApproved;
			 
			return ok(views.html.restaurant.restaurantOwner.render(email, meals, restaurant ,restaurants, tobeapproved));
		}
		
	}
	
	/**
	 * This method just redirects to login page.
	 * 
	 * @return redirection to Login view.
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
	 * @return after successful registration the user can login.
	 * @throws MalformedURLException 
	 */
	public static Result registration() throws MalformedURLException {
		DynamicForm form = Form.form().bindFromRequest();
				
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
		String confirmPassword = form.data().get("confirmPassword");
		
		if(!hashedPassword.equals(confirmPassword)){
			flash("MatchPass", "Passwords don't match");
			return redirect("/registration");
		}
		
		User usr = new User(email, hashedPassword);		
		Location loc = new Location("", "", "");
		usr.locations.add(loc);
		loc.save();
		usr.confirmationString = UUID.randomUUID().toString();
		if (User.checkIfExists(email) == true) {
			flash("inDatabase",
					Messages.get("User already exists - please confirm user or login"));
			return redirect("/login");
		}
		loc.user = usr;
		usr.save();
		loc.update();
		

		if (User.checkA(email, hashedPassword) == true) {
			// First we need to create url and send confirmation mail to user
			// (with url inside).
			String urlString = hostUrl + "confirm/"
					+ usr.confirmationString;
			URL url = new URL(urlString);
			try{
			MailHelper.send(email, url.toString());
			} catch(Exception e){
				flash("ErrorMail", "Pleas try again");
				Logger.error("Sending meail error: " + e.getMessage());
				return redirect("/");
			}
			if (usr.validated == true) {
				return redirect("/");
			}
			flash("validate", Messages.get("Please check your email"));
			Logger.info("User with email " +email +" registred. Verification email has been sent!");
			return redirect("/registration");
		} else {
			return redirect("/registration");
		}
	}
	
	/**
	 * Sends you to view for registration of restaurant.
	 * @return view for restaurant registration.
	 */
	public static Result toRegistrationRestaurant(){		
		return ok(registrationRestaurant.render(email));
	}
	
	/**
	 * Send you to view where you can repeat verification of email.
	 * @return repeat verification view.
	 */
	public static Result toRepeatVerification(){		
		return ok(repeatVerification.render(""));
	}
	
	/**
	 * Method which repeats verification of users email.
	 * @return to Login view or registration view is user is not in database.
	 */
	public static Result repeatVerification(){	
		DynamicForm form = Form.form().bindFromRequest();
		String email = form.data().get("email");
		
		if(email == null){
			flash("warnning", "Please enter your Email, so we can verificate it again!");
			return redirect("/repeatVerification");
		}
		
		User user = User.find(email);
		if (user== null){
			flash("warnning", "Your registration was not successfull, please try again!");
			return redirect("/registration");
		} 
		
		String urlString = hostUrl + "confirm/"
				+ user.confirmationString;
		URL url;
		try {
			url = new URL(urlString);
			
			if (user.validated == true) {
				return redirect("/login");
			}
			flash("validate", Messages.get("Please check your email"));
			
		} catch (MalformedURLException e) {
			Logger.error("Verification mail not sent: " + e.getMessage());
			
		}
		
		flash("validate", Messages.get("Please check your email"));

		return redirect("/login");
		
	}

	/**
	 * Method that register restaurant as user, by taking
	 * all parameters that restaurant needs for registration:
	 * email, password, name, business hours, location(city, street, number).
	 * @return redirection to index page after restaurant user finished registration.
	 */
	public static Result registrationRestaurant(){
		DynamicForm form = Form.form().bindFromRequest();	
			
		String email = form.data().get("email");
		String hashedPassword = form.data().get("hashedPassword");
		String confirmPassword = form.data().get("confirmPassword");

		String name = form.data().get("name");
		String workingTime = form.data().get("workingTime");
		String street = form.data().get("street");
		String number = form.data().get("number");
		String city = form.data().get("city");
		
		if(!hashedPassword.equals(confirmPassword)){
			flash("MatchPass", "Passwords don't match");
			return redirect("/registrationRestaurant");
		}

		
		try{
			
			User.createRestaurant(name, email, hashedPassword, workingTime, city, street, number);
			MailHelper.sendRestaurntWatiToAprrove(email);
			Logger.info("Restaurant " +name +" with email " +email +" registred. Visit admin panel to approve.");
			flash("successSendRequest", "You have succesfully send request for restaurant registration! Wait until admin contacts you!");
		}catch(Exception e){
			Logger.error("Restaurant " +name +" with email" +email +" failed to register.");
		}			

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
		List<String> logs = SudoController.lastLogs();
		List <Faq> faqs = findF.all();
		
		User u = Session.getCurrentUser(ctx());

		if(Session.getCurrentUser(ctx()) != null){
			Restaurant restaurant = u.restaurant;
			List<TransactionU> tobeapproved = restaurant.toBeApproved;
			if(Session.getCurrentRole(ctx()).equals(User.RESTAURANT))
				return ok(views.html.restaurant.restaurantOwner.render(email, meals, restaurant ,restaurants, tobeapproved));

			if(Session.getCurrentRole(ctx()).equals(User.USER))
				return ok(index.render(" ", email, meals, restaurants));
			if(Session.getCurrentRole(ctx()).equals(User.ADMIN))
				return ok(views.html.admin.admin.render(email, meals, restaurants, logs, faqs));
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
	 * Method that send us to Public restaurant view.
	 * @return view of restaurant designed for user.
	 */
	public static Result toRestaurant(String name){
		
		Restaurant restaurant = Restaurant.findByName(name);
		double minOrder = restaurant.minOrder;
		User restaurantUser = restaurant.user;
		List<Meal> restaurantMeals = Meal.allById(restaurantUser);
		restaurant.statistic.visited();
		return ok(views.html.restaurant.restaurantProfile.render(email, name, minOrder, restaurantMeals));
	}
	
	
	/**
	 * 
	 * This method is used to change password on user profile page.
	 * @return redirect on profile page.
	 */
	public static Result editUser(String email) {		
		Form form = Form.form().bindFromRequest();
		User currentUser = Session.getCurrentUser(ctx());
				
		String newHashedPassword= form.data().get("hashedPassword").toString();
//		String confirmPassword = form.data().get("confirmPassword").toString();
//		
//		if(!newHashedPassword.equals(confirmPassword)){
//			flash("MatchPass", "Passwords don't match");
//			return redirect("/user/" + email);
//		}
		String username = form.data().get("username").toString();
		currentUser.username = username;

		String city = form.data().get("city").toString();
		String street = form.data().get("street").toString();
		String number = form.data().get("number").toString();
				
		if (!newHashedPassword.equals("")) {
			currentUser.hashedPassword = Hash.hashPassword(newHashedPassword);
		}
		Location location = new Location(city, street, number);
//		currentUser.location.city = city;
//		currentUser.location.street = street;
//		currentUser.location.number = number;
//		currentUser.location.update();
		currentUser.locations.add(location);
		currentUser.update();
		
		Logger.info("User with email " +currentUser.email +" just edited his info!");
		flash("successUpdate", "You have successfully updated contact information");
		return redirect("/user/" + email);
	}
	
	/**
	 * Show all FAQ (Frequently asked questions).
	 * @return view of all FAQ that had been asked.
	 */
	public static Result showFaq() {		
		List <Faq> faqs = findF.all();
		email = session("email");
		return ok(views.html.widgets.faqPage.render(email, faqs));
	}
		
	/**
	 * Logs out user.
	 * @return redirection to index page .
	 */
	public static Result logout() {
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.index());
	}
	
	/**
	 * Shows view for uploading images for meal.
	 * @param id of meal which images you want to upload.
	 * @return view of uploading pictures.
	 */
	@Security.Authenticated(RestaurantFilter.class)
	public static Result showFileUpload(int id) {
		Meal m = Meal.find(id);
		return ok(views.html.restaurant.fileUploadMeal.render("",Session.getCurrentUser(ctx()).email,m,Restaurant.all(),m.image)); // NOT FINISHED
	}
	
	
	/**
	 * Show all images of one meal.
	 * @param id of meal that you want to see images.
	 * @return  redirection to file upload meal view.
	 */
	public static Result MealIMGList(int id){
		Meal m = Meal.find(id);
		return ok(views.html.restaurant.fileUploadMeal.render("","",m, Restaurant.all(),m.image)); // NOT FINISHED
	}
	
	
	/**
	 * Method that transaction boolean validate sets to true.
	 * @param id of transaction.
	 * @return redirection to index view.
	 */
	public static Result askForRefund(int id){
		TransactionU transaction = TransactionU.find(id);
		
		transaction.refund = true;
		transaction.update();
		
		return redirect("/");
	}
	
	/**
	 * Send you to about view of web application.
	 * @return about view..
	 */
	public static Result about() {
		return ok(views.html.about.render(email));
	}
	
	/**
	 * Adds phone number to users profile.
	 * @param email of user whose number we want to add.
	 * @return redirection to user profile.
	 */
	public static Result addPhone(String email) {
		DynamicForm form = Form.form().bindFromRequest();
		String phone = form.data().get("phone");
		User user = Session.getCurrentUser(ctx());
		if (user == null) {
			return redirect("/");
		}
		if (phone.equals(user.phone)) {
			flash("nothingChanged",
					"Same phone number!");
			return redirect("/user/" + email);
		} else {
			user.phone = phone;
			user.pin = Pin.generatePinCode(user);
			user.validated = false;
			user.update();
			String pin = Pin.getPinCode(user.id);
			SmsController.confirmNumber(pin);
		}
		flash("successVerify",
				"Your Phone number has now been successfully added!");
		return redirect("/user/" + email);
	}
}