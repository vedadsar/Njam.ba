package Utilites;

import play.mvc.Security;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import models.User;
import play.mvc.Result;
import play.mvc.Http.Context;
import sun.misc.BASE64Decoder;

public class ServiceAuth extends Security.Authenticator {
	
	@Override
	public String getUsername(Context ctx) {

		String[] auth = ctx.request().headers().get("Authorization");
		if (auth == null)
			return null;

		auth[0] = auth[0].replace("Basic ", "");
		BASE64Decoder decoder = new BASE64Decoder();
		String basic = null;
		try {
			basic = new String(decoder.decodeBuffer(auth[0]), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (basic == null)
			return null;

		String[] userPass = basic.split(":");

		if (userPass.length != 2)
			return null;

		String email = userPass[0];
		String pass = userPass[1];
		System.out.println("Username: " + email + " Password: " + pass);
		if (!User.authenticate(email, pass)) {
			return null;
		}
		return email;
	}

	public User getCurrentUser(Context ctx) {
		String email = getUsername(ctx);
		return User.find(email);
	}

	@Override
	public Result onUnauthorized(Context ctx) {
		return badRequest("How 'bout no");
	}

}
