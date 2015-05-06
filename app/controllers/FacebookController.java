package controllers;

import models.Location;
import models.User;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;

public class FacebookController extends Controller {

	public static Result sendFbJson() {

		JsonNode data1 = request().body().asJson();
		if (data1 == null) {
			return badRequest("Expecting Json data");
		}
		String email = data1.findPath("email").textValue();
		if (User.check(email) == true) {
			User u = User.find(email);
			u.locations.add(new Location(" ", " ", " "));
			u.update();
			session("email", email);
			return ok("/user/" + email);
		}
		User user = new User(email, null);
		user.validated = true;
		user.save();
		
		session("email", email);
		return ok("/user/" + email);
	}
}
