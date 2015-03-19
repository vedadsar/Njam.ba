import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		if(User.check("suad@suad.com") == false){
		User.createAdmin("sanela.grcic@bitcamp.ba", "123456");
		User.createAdmin("gorjan.kalauzovic@bitcamp.ba", "123456");
		
		}
		User.createRestaurant("Lovac","restoran@njam.ba", "123456", "Sarajevo", "Fojnicka", "5");
		User.createRestaurant("Harambasa","restoran1@njam.ba", "123456","Banjaluka", "Pere Kvrzice", "18");
		User.createRestaurant("Klopa","restoran2@njam.ba", "123456","Tuzla", "Marsala Tita", "22");
		User.createRestaurant("Klopa","restoran2@njam.ba", "123456","Zenica", "Daria Dzamonje", "33");
		User.createRestaurant("Klopa","restoran2@njam.ba", "123456","Mostar", "Alekse Santica", "20");

	}
}

