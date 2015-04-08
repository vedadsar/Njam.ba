package controllers.json;

import java.util.List;

import models.Meal;
import play.libs.Json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MealList {
	
	public static ArrayNode mealList(List<Meal> meals){
		
	ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
	
	for(Meal meal : meals){
		ObjectNode mealNode = Json.newObject();
		mealNode.put("name", meal.name);
		mealNode.put("price", meal.price);
		array.add(mealNode);
	}
		return array;
	}

}
