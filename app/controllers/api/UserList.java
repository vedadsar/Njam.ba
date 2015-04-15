package controllers.api;

import java.util.List;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import models.Meal;
import models.Restaurant;
import models.User;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserList extends Controller {
	
	public static Result users() {
		List<User> users = User.allUsers();
		if (users != null) {
			return ok(UserList.usersList(users));
		}
		return ok(new ArrayNode(JsonNodeFactory.instance));
	}
	
	public static Result oneUser(){
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		User user = User.find(Integer.parseInt(id));
		if (user != null){
			return ok(UserList.userToApp(user));
		}
		return badRequest();
	}

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
