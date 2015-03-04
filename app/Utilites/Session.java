package Utilites;


import controllers.routes;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Session extends Security.Authenticator{

	@Override
	public  String getUsername(Context ctx){
		if(!ctx.session().containsKey("user_id"))
			return null;
		int id = Integer.parseInt(ctx.session().get("user_id"));
		User u = User.find(id);
		if(u != null){
			return u.email;
		}
		return null;	
	}
	
	@Override
	public  Result onUnauthorized(Context ctx){
		return redirect(routes.Application.index());		
	}
	
	public static User getCurrentUser(Context ctx){
		if(!ctx.session().containsKey("user_id"))
			return null;
		int id = Integer.parseInt(ctx.session().get("user_id"));
		User u = User.find(id);
		return u;
	}
}