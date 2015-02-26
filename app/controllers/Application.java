package controllers;

import models.User;
import play.*;
import play.data.Form;
import play.mvc.*;
import views.html.*;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }
    
    static Form<User> newUser = new Form<User>(User.class);
    
    public static Result registration() {
    	String email = newUser.bindFromRequest().get().email;
    	String password = newUser.bindFromRequest().get().hashedPassword;
    	boolean isSuccess = User.createUser(email, password);
    	if (isSuccess == true) {
    		return ok(index.render("Registration success."));
    	} else {
    		return ok(index.render("Registration failed."));
    	}
    }
    
    public static Result login() {
    	return TODO;
    }

}
