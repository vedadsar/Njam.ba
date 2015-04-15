package controllers.api;

import java.util.List;

import models.Meal;
import models.User;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MealList extends Controller {
	
	public static Result meals() {
		List<Meal> meals = Meal.all();
		if (meals != null) {
			return ok(MealList.mealList(meals));
		}
		return ok(new ArrayNode(JsonNodeFactory.instance));
	}
	
	public static Result oneMeal(){
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		Meal meal = Meal.find(Integer.parseInt(id));
		if (meal != null){
			return ok(MealList.mealToApp(meal));
		}
		return badRequest();
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
