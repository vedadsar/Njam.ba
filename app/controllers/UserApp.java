package controllers;

import models.User;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class UserApp extends Controller {

	static Form<User> createForm = new Form<User>(User.class);
	static Form<User> loginForm = new Form<User>(User.class);
	
	  public static Result createUser() {
	       String email = createForm.bindFromRequest().get().email;
	       String password = createForm.bindFromRequest().get().hashedPassword;
	       if(User.createUser(email, password) == true){
	    	   session("name", email);
	    	   return TODO; /* return to user panel */
	       }
	       //else return to fail to create panel.
		  return null;
	    }
	  
	  public static Result authenticate(){
		  String email = loginForm.bindFromRequest().get().email;
	      String password = loginForm.bindFromRequest().get().hashedPassword;
	      if(User.authenticate(email, password) == true){
	    	  session("name", email);
	    	  return TODO; //redirect to user panel
	      }
	      return TODO; //authentication failed, return to fail panel. 	      		  
	  }
}
