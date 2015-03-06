import models.Meal;
import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		if(User.check("suad@suad.com") == false){
		User.createAdmin("suad@suad.com", "123456");
		}
		User.createRestaurant("restoran@njam.ba", "123456");
		User.createRestaurant("restoran1@njam.ba", "123456");
		User.createRestaurant("restoran2@njam.ba", "123456");
		User.createRestaurant("restoran3@njam.ba", "123456");
		User.createRestaurant("restoran4@njam.ba", "123456");
			}
}

