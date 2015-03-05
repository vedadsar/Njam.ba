package controllers;

import models.*;
import play.data.Form;
import play.mvc.Controller;

public class SudoController extends Controller{

	static Form<User> inputForm = new Form<User>(User.class);
	
	public static void createRestaurant(){
		String email = inputForm.bindFromRequest().get().email;
		String password = inputForm.bindFromRequest().get().hashedPassword;
		
		User.createRestaurant(email, password);
		
		User user = User.find(email);
		Restaurant.create("No Name", user);
	}
	
	public static void all(){
		
	}
}