import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		User.createAdmin("sudo@njam.ba", "123456", "admin");
	}
}
