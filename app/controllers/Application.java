package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

	static Form<User> newUser = new Form<User>(User.class);
	static String email = null;

	/**
	 * This method just redirects to index page.
	 * 
	 * @return
	 */
	public static Result index() {
		email = session("email");
		return ok(index.render("", email));
	}

	/**
	 * This method just redirects to registration page.
	 * 
	 * @return
	 */
	public static Result toRegistration() {
		if(email == null){
			return ok(registration.render(""));
		} else {
		return ok(index.render("", email));
		}
	}

	/**
	 * This method just redirects to login page.
	 * 
	 * @return
	 */
	public static Result toLogin() {
		if(email == null){
			return ok(login.render(""));
		} else {
			return ok(index.render("", email));
		}
	}

	/**
	 * This method registers new user. If user doesn't exist, method will
	 * register it and redirect to index page, so user can login. If user
	 * already exists, method will redirect to register page, with error
	 * message.
	 * 
	 * @return
	 */
	public static Result registration() {

		String email = newUser.bindFromRequest().get().email;
		String hashedPassword = newUser.bindFromRequest().get().hashedPassword;
		
		if(email.length() < 6)
			return ok(registration.render("Email length not valid"));
		if(hashedPassword.length() < 6)
			return ok(registration.render("Password length is not valid"));		

		if (email.contains("@") == false) {
			return ok(registration.render("Invalid e-mail"));
		}

		boolean isSuccess = User.createUser(email, hashedPassword);
		if (isSuccess == true) {
			return ok(login2.render("SUCCESSFUL REGISTRATION! PLEASE LOGIN!"));
		} else {
			return ok(registration.render("Already registered, please login!"));

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

		String email = newUser.bindFromRequest().get().email;
		String hashedPassword = newUser.bindFromRequest().get().hashedPassword;
		if (email.contains("@") == false) {
			return ok(login.render("Invalid e-mail"));
		}
		

		boolean isSuccess = User.authenticate(email, hashedPassword);
		if (isSuccess == true) {
			session("email", email);
			return ok(user.render(email));
		} else {
			return ok(login.render("Incorrect username or password, or you are already logged in."));
		}
	}
}
