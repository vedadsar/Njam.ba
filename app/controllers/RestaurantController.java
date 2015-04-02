package controllers;

import java.util.Iterator;
import java.util.List;

import Utilites.AdminFilter;
import Utilites.Session;
import models.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.Context;
import play.mvc.Security;
import views.html.*;
import Utilites.*;

/**
 * Controller class for restaurant. Restaurant is able to create/modify/delete
 * meal.
 * 
 * @author vedad & nedim
 *
 */
public class RestaurantController extends Controller {

	static Form<Meal> inputForm = new Form<Meal>(Meal.class);
	static Finder<Integer, Restaurant> findR =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);



	/**
	 * Method to create meal.
	 * Use Meal.create method from models.
	 * @return
	 */
	@Security.Authenticated(RestaurantFilter.class)
	public static Result createMeal() {
		User u= Session.getCurrentUser(ctx());
		if(!u.role.equalsIgnoreCase("RESTAURANT")){
			return ok(wrong.render("Cannot create meal if you're not reastaurant! "));
		}
		
		String mealName = inputForm.bindFromRequest().field("name").value();
		String mealPrice = inputForm.bindFromRequest().field("price").value();
		
		mealPrice = mealPrice.replace(',', '.');
		Double price = Double.parseDouble(mealPrice);
		Restaurant currentUser=u.restaurant;
		
		if ((Meal.create(mealName, price,currentUser)) == true) {
			Meal m = findM.where().eq("name", mealName).eq("price", mealPrice).eq("restaurant_id",u.restaurant.id ).findUnique();
			String userEmail= Session.getCurrentUser(ctx()).email;
			Logger.debug(m.name);
			 session("email", userEmail);
			 flash("successMeal", "Succesfully created meal!");
			 Logger.info("Restaurant " +currentUser.name +" just created meal");
			 return ok(fileUploadMeal.render("",userEmail, m,Restaurant.all(),m.image));
			 // return redirect("/restaurantOwner/" + userEmail);
		}
		Logger.error("Restaurant " +currentUser.name +" failed to create meal.");
		return TODO;
	}

	/**
	 * Method to delete meal by
	 * using meal ID which is unique for each meal.
	 * @return
	 */
	@Security.Authenticated(RestaurantFilter.class)
	public static Result deleteMeal(int id) {		
		Meal m = Meal.find(id);
		Restaurant r = m.restaurant;
		
		m.restaurant = null;
		List<Meal> rMeals = r.meals;
		Iterator<Meal> it = rMeals.iterator();
		
		while(it.hasNext()){
			int index = 0;
			Meal current = it.next();
			if(current.id == id){
				rMeals.remove(index);
				break;
			}	
			index++;
		}
		try{
			Meal.delete(m);
			Logger.info("Restaurant " +r.name +" just deleted meal.");
			flash("deletedMeal", "You have successfully deleted your meal");
		}catch(Exception e){
			Logger.info("Restaurant " +r.name +" just failed to delete meal.");
		}
		return redirect("/restaurantOwner/" + Session.getCurrentUser(ctx()).email);
	}
	
	@Security.Authenticated(RestaurantFilter.class)
		public static Result editMealURL(int id) {	
		String userEmail= Session.getCurrentUser(ctx()).email;
        Meal oldMeal = Meal.find(id);
		return ok(restaurantOwnerEditMeal.render(oldMeal, userEmail));
	}
	
	
	
	public static Result editMeal(int id) {		
		
		String userEmail= Session.getCurrentUser(ctx()).email;
		
		Meal oldMeal = Meal.find(id);
		
		String mealName = inputForm.bindFromRequest().field("name").value();
		String mealPrice = inputForm.bindFromRequest().field("price").value();
		
		mealPrice = mealPrice.replace(',', '.');
		Double price = Double.parseDouble(mealPrice);

		try{
			Meal.modifyMeal(oldMeal, mealName,price);
			flash("successEdited", "You have successfully edited your meal");
			Logger.info("User " +userEmail +" just edited meal " +oldMeal.id);
		}catch(Exception e){
			Logger.error("User " +userEmail +" failed to edit meal " +oldMeal.id);
		}
		return redirect("/restaurantOwner/" + userEmail);
	}
	
	
	
	
	/**
	 * Method that returns list of restaurants.
	 * @return list of all meals from Restaurant
	 */
	public static Result list(){
		List<Meal> meals = Meal.all();
		return TODO;
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Result details(int id){
		return TODO;
	}	
	
	@Security.Authenticated(RestaurantFilter.class)
	public static Result restaurant(String email){			
		List <Restaurant> restaurants = findR.all();		
		User u = User.find(email);
		List <Meal> meals = Meal.allById(u);
		return ok(restaurantOwner.render(email, meals, restaurants));
	}
	

	public static Result editRestaurantURL(String email){
		String userEmail = Session.getCurrentUser(ctx()).email;	
		User user = User.find(userEmail);
		Restaurant  restaurant = Restaurant.find(user.id);
		return ok(restaurantOwnerEditProfile.render(userEmail, restaurant));
		
	}
	
	public static Result editRestaurant(String email){
		
		DynamicForm form = Form.form().bindFromRequest();
		User currentUser = Session.getCurrentUser(ctx());
				
		String hashedPassword = form.data().get("hashedPassword");
		if ( !hashedPassword.isEmpty()){
			currentUser.hashedPassword = Hash.hashPassword(hashedPassword);
		}
		String name = form.data().get("name");
		String city = form.data().get("city");
		String street = form.data().get("street");
		String number = form.data().get("number");

		
		currentUser.restaurant.name = name;
		currentUser.location.city = city;
		currentUser.location.street = street;
		currentUser.location.number = number;
		currentUser.restaurant.update();
		currentUser.location.update();
		currentUser.update();
		
		/*
		
		DynamicForm form = Form.form().bindFromRequest();	
		
		User currentUser = Session.getCurrentUser(ctx());
		int id = currentUser.id;
		Restaurant restaurant = findR.byId(id);
		
		String userEmail = form.data().get("email");
		String userHashedPassword = form.data().get("hashedPassword");
		User.modifyUser(currentUser, userEmail, userHashedPassword);
		
		String name = form.data().get("name");
		Restaurant.modifyRestaurant(restaurant, userEmail, userHashedPassword, name);
		
		Location location =  restaurant.user.location;
		String city = form.data().get("city");
		String street = form.data().get("street");
		String number = form.data().get("number");
		
		Location.modifyLocation(location, city,street, number);
		location.save();
		restaurant.save();
		currentUser.save();
		*/
		
		flash("successEdited", "You have successfully edited your restaurant Profile");
		
		return redirect("/restaurantOwner/"+ email);
		
	}
}