package controllers.api;

import java.util.List;

import models.Image;
import models.Meal;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MealApi extends Controller {
	
	public static Result mealsWithImages() {
		List<Image> images = Image.all();
		if (images != null) {
			return ok(MealApi.mealListWithImages(images));
		}
		return ok(new ArrayNode(JsonNodeFactory.instance));
	}
	
	public static Result meals() {
		List<Meal> meals = Meal.all();
		if (meals != null) {
			return ok(MealApi.mealList(meals));
		}
		return ok(new ArrayNode(JsonNodeFactory.instance));
	}
	
	public static Result oneMeal(){
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		Meal meal = Meal.find(Integer.parseInt(id));
		if (meal != null){
			return ok(MealApi.mealToApp(meal));
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
			mealNode.put("name", meal.name);
			mealNode.put("price", meal.price);
			array.add(mealNode);
		}
		return array;
	}
		
	public static ObjectNode mealToApp(Meal m) {
		ObjectNode meal = Json.newObject();
		meal.put("id", m.id);
		meal.put("name", m.name);
		meal.put("price", m.price);
		meal.put("description", m.description);
		
		return meal;
	}
}