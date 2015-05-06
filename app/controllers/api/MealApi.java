package controllers.api;

import java.util.List;

import models.*;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MealApi extends Controller {
	
	static Finder<Integer, Meal> findM =  new Finder<Integer,Meal>(Integer.class, Meal.class);
	
	public static Result mealsWithImages() {
		List<Image> images = Image.all();
		if (images != null) {
			return ok(MealApi.mealListWithImages(images));
		}
		return ok(new ArrayNode(JsonNodeFactory.instance));
	}
	
	/**
	 * 
	 * @return list of meals (JSON format)
	 */
	public static Result meals() {
		List<Meal> meals = Meal.all();
		if (meals != null) {
			return ok(MealApi.mealList(meals));
		}
		return ok(new ArrayNode(JsonNodeFactory.instance));
	}
	
	/**
	 * 
	 * @return Meal by id.
	 */
	public static Result oneMeal(){
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		Meal meal = Meal.find(Integer.parseInt(id));
		if (meal != null){
			return ok(MealApi.mealToApp(meal));
		}
		return badRequest();
	}
	
	/**
	 * 
	 * @return Meals of One Restaurant.
	 */
	public static Result mealsOfRestaurant() {
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		Restaurant restaurant = Restaurant.find(Integer.parseInt(id));
		List<Meal> meals = findM.where().eq("restaurant", restaurant).findList();
		if (meals != null){
			return ok(MealApi.mealList(meals));
		}
		return badRequest();
	}

	
	public static ArrayNode mealListWithImages(List<Image> images) {
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
		for(Image image : images){
			ObjectNode imageNode = Json.newObject();
			imageNode.put("id", image.meal.id);
			imageNode.put("name", image.meal.name );
			imageNode.put("price", image.meal.price );
			imageNode.put("image", image.imgLocation);
			array.add(imageNode);
		}
		return array;
	}
	
	public static ArrayNode mealList(List<Meal> meals) {
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
		for (Meal meal : meals) {
			ObjectNode mealNode = Json.newObject();
			mealNode.put("restaurant_id", meal.restaurant.id);
			mealNode.put("id",meal.id);
			mealNode.put("name", meal.name);
			mealNode.put("restaurant", meal.restaurant.name);
			mealNode.put("restaurantCity", meal.restaurant.user.locations.iterator().next().city);
			mealNode.put("price", meal.price);
			if(meal.image.iterator().hasNext()){
			mealNode.put("image", meal.image.iterator().next().imgLocation);}
			else{
				mealNode.put("image", "images/chicken.jpg");
			}
				
			array.add(mealNode);
		}
		return array;
	}
		
	public static ObjectNode mealToApp(Meal m) {
		ObjectNode meal = Json.newObject();
		meal.put("id", m.id);
		meal.put("restaurant_id", m.restaurant.id);
		meal.put("restaurantName", m.restaurant.name);
		meal.put("restaurantWorkingHours", m.restaurant.workingTime);
		meal.put("restaurantCity", m.restaurant.user.locations.get(0).city);
		meal.put("restaurantStreet", m.restaurant.user.locations.get(0).street);
		meal.put("name", m.name);
		meal.put("meal_description", m.description);
		meal.put("price", m.price);
		if (m.image.iterator().hasNext()) {
			meal.put("image", m.image.iterator().next().imgLocation);
		} else {
			meal.put("image", "images/chicken.jpg");
		}
		return meal;
	}
}