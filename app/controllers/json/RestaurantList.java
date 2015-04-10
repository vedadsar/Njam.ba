package controllers.json;

import java.util.List;

import play.libs.Json;
import models.Restaurant;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class RestaurantList {

	public static ArrayNode restaurantsList(List <Restaurant> restaurants) {
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
	
		for (Restaurant r : restaurants) {
			ObjectNode restaurant = Json.newObject();
			restaurant.put("name", r.name);
			restaurant.put("minOrder", r.minOrder);
			array.add(restaurant);
		}
		return array;
	}
	
	

}
