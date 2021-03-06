package Utilites;

import models.User;
import controllers.Application;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http.Context;

public class UserFilter extends Security.Authenticator {

	@Override
	public String getUsername(Context ctx) {
		if(!ctx.session().containsKey("email"))
			return null;
		String username = ctx.session().get("email");
		User u = User.find(username);		
		
		//Checking if user exists in db.
		if (u == null)
			return null;
		
		//Depending on role we authorize user.
		String role = u.role;
		if(role.equals(User.USER)){
			return u.email;
		}else
			return null;
		
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		Application.flash("warning", "Login with user account.");
		return redirect("/login");
	}

}