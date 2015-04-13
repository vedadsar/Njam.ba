package controllers.api;

import java.util.List;

import play.libs.Json;
import models.User;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserList {

	public static ArrayNode usersList(List <User> users) {
		ArrayNode arrayUsers = new ArrayNode(JsonNodeFactory.instance);

		for (User u : users) {
			ObjectNode user = Json.newObject();
			user.put("id", u.id);
			user.put("email", u.email);
			user.put("validated", u.validated);
			arrayUsers.add(user);
		}
		return arrayUsers;
	}
	
	public static ObjectNode userToApp(User u) {
		
		ObjectNode user = Json.newObject();
		user.put("id", u.id);
		user.put("name", u.email);
		user.put("email", u.validated);

		return user;
	}


}
