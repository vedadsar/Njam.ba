import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		User.createUser("suad@suad.com", "123456");
	}
}

