
import models.User;
import play.Application;
import play.GlobalSettings;
import play.Logger;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		try{		
			if(User.check("suad@suad.com") == false){			
				User.createAdmin("suad@suad.com", "123456");
			}		
			User.createRestaurant("Lovac","restoran@njam.ba", "123456");
			User.createRestaurant("Harambasa","restoran1@njam.ba", "123456");
			User.createRestaurant("Klopa","restoran2@njam.ba", "123456");		
		}catch(Exception e){
			Logger.error("Failed to apply own global settings.");
		}
	}
}

