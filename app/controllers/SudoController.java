package controllers;

import models.*;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
import play.db.ebean.Model.Finder;
import Utilites.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class SudoController extends Controller{

	static Form<User> inputForm = new Form<User>(User.class);
	static Form<Restaurant> inputR= new Form<Restaurant>(Restaurant.class);
	static Form<Location> inputL= new Form<Location>(Location.class);

	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);

	/* Not using this method anymore.
	@Security.Authenticated(AdminFilter.class)
	public static Result createRestaurant(){	
		
		String email = inputForm.bindFromRequest().get().email;
		String password = inputForm.bindFromRequest().get().hashedPassword;			
		String nameOfRestaurant = inputR.bindFromRequest().get().name;		
		/*
		 * Trying to create restaurant. Using logger and flash.
		 *
		try{
			User.createRestaurant(nameOfRestaurant, email, password);	
			Logger.info("Admin " +Session.getCurrentUser(ctx()).email +" just created restaurant "
					+ nameOfRestaurant);
			flash("successRestaurant", "Successfully added Restaurant");
		}catch(Exception e){
			Logger.error("Admin " +Session.getCurrentUser(ctx()).email +" just failed to create restaurant "
					+ nameOfRestaurant +"\nError message: " +e.getMessage());
			flash("failRestaurant", "Failed to create restaurant");
			return redirect("/admin/create");
		}		

		return redirect("/admin/create");

	}*/
 	
	
	
	@Security.Authenticated(AdminFilter.class)
	public static Result deleteRestaurant(int id){
		Restaurant r = Restaurant.find(id);
		User u = r.user;
		//For logger.
		String resName = r.name;
		String userMail = u.email;
		
		List<Meal> allMeals = Meal.allById(u);
		
		for(Meal m: allMeals){
			m.restaurant = null;
			//Deleting meals. Using try catch for logging errors.
			try{	
				Meal.delete(m);
			}catch(Exception e){
				Logger.error("Failed to delete meal " +m.id +": "
					+m.name +".\nError message: " +e.getMessage());
			}
		}
		r.user = null;
		u.restaurant = null;
		u.location = null;
		r.save();		
		u.save();

		try{
			Restaurant.delete(id);
			Location.delete(id);
			User.deleteUser(u);
			Logger.info("Deleted restaurant " +resName +" and his owner " +userMail);
			flash("successDeleteRestaurant", "Restaurant successfully deleted");
		}catch(Exception e){
			Logger.error("Failed to delete restaurant/user " +resName +", " +userMail);
			flash("failDeleteRestaurant", "Restaurant failed to  delete");
		}
		return redirect("/admin/" +Session.getCurrentUser(ctx()).email);		

	}
	
	@Security.Authenticated(AdminFilter.class)
	public static Result administrator(String email) {

		List<Meal> meals = findM.all(); 
		List<Restaurant> restaurants = findR.all();
		List<String> logs = lastLogs();
		return ok(admin.render(email,meals, restaurants, logs));
	}
	
	@Security.Authenticated(AdminFilter.class)
	public static Result logs(){
		List logs = logList();
		return TODO;
	}
	
	/**
	 * Method reads application.log file which contains all the logs.
	 * It return all logs as list of strings.
	 * @return list of logs as strings.
	 */
	public static List<String> logList(){
		File f = new File("./logs/application.log");
		List<String> listOfLogs = new ArrayList<String>();
		Scanner sc = null;
		try {
			sc = new Scanner(f);
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				line.trim();
				
				if(line.isEmpty())
					continue;
				if(line.charAt(0) != '2' || line.length() < 1){
					continue;
				}
				String log = "";
				if(line.charAt(0) == '2'){
					log = line;
					line = sc.nextLine();
					if(!line.isEmpty()){
						log += "\n" +line ;
					}
					log += "\n";
				}
				listOfLogs.add(log);
			}
			
		} catch (FileNotFoundException e) {
			Logger.error("Could not list logs");
		}finally{
			sc.close();
		}	
		return listOfLogs;
	}	
	
	
	public static List<String> lastLogs(){
		List<String> allLogs = logList();
		ArrayList<String> lastLogs = new ArrayList<String>();		
		int counter = 0;
		
		if(allLogs.isEmpty())
			return null;
		
		for(String log: allLogs){			
			lastLogs.add(log);
			counter++;						
			if(counter >= 10)
				break;
		
		}		
		Collections.reverse(lastLogs);
		
		return lastLogs;
	}
	
	public static Result approveRestaurant(int id){
		
		Restaurant restaurant = Restaurant.find(id);
		
		User userRestaurant = restaurant.user;
				
		userRestaurant.validated = true;
		userRestaurant.update();
		
		flash("successApprovedRestaurant", "Restaurant successfully approved!");	
		return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
		
	}
}