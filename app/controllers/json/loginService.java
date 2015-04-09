package controllers.json;

import models.Meal;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class loginService extends Controller {
	
	public static Result loginToJson(){

		JsonNode login = request().body().asJson();
		if (login == null) {
			System.out.println("Login");
			return badRequest("Expecting Json data");
		}

		String email = login.findPath("email").textValue();
		String password = login.findPath("password").textValue();

		if (User.checkA(email, password) == true) {
			User u = User.find(email);
			return ok("Vozdra, " + u.email + "!  HashedPassword" + u.hashedPassword);
		}
		return badRequest("Failed");

	}

}
