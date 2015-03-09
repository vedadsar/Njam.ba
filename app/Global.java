import models.Meal;
import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		if(User.check("suad@suad.com") == false){
		User.createAdmin("suad@suad.com", "123456");
		}
		User.createRestaurant("Lovac","restoran@njam.ba", "123456");
		}
}

