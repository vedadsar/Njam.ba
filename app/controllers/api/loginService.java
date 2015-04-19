package controllers.api;

import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class loginService extends Controller {
	
	public static Result loginToJson(){

		JsonNode login = request().body().asJson();
		if (login == null) {
			return badRequest("Expecting Json data");
		}
		String email = login.findPath("email").textValue();
		String password = login.findPath("password").textValue();

		if (User.checkA(email, password) == true) {
			User u = User.find(email);
			return ok(UserApi.userToApp(u));
		}
		return badRequest();
	}
}
