import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		User.createAdmin("suad@suad.com", "123456");
		User.createRestaurant("restoran@njam.ba", "123456");
	}
}

