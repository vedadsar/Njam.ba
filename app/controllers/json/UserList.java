package controllers.json;

import java.util.List;

import play.libs.Json;
import models.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserList {

	public static ArrayNode usersList(List <User> users) {
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);

		for (User u : users) {
			ObjectNode user = Json.newObject();
			user.put("email", u.email);
			user.put("role", u.role);
			user.put("validated", u.validated);
			array.add(user);
		}
		return array;
	}

}
