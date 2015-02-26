package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {
	
    static Form<User> newUser = new Form<User>(User.class);
	
    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    public static Result toRegistration() {
    	return ok(registration.render());
    }
    
    public static Result toLogin() {
    	return ok(login.render());
    }
    
    public static Result registration() {
    	
    	String email = newUser.bindFromRequest().get().email;
    	String hashedPassword = newUser.bindFromRequest().get().hashedPassword;
    	boolean isSuccess = User.createUser(email, hashedPassword);
    	if (isSuccess == true) {
    		return ok(index.render("..."));
    	} else {
    		return redirect("/registration");
    	}
    	
    }
    
    public static Result login() {
    	
    	String email = newUser.bindFromRequest().get().email;
    	String hashedPassword = newUser.bindFromRequest().get().hashedPassword;
    	boolean isSuccess = User.authenticate(email, hashedPassword);
    	if (isSuccess == true) {
    		session("email", email);
    		return ok(user.render(email));
    	} else {
    		return redirect("/login");
    	}
    	
    }
    
}
