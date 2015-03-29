import models.Faq;
import models.User;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		if(User.check("suad@suad.com") == false){
		User.createAdmin("suad@suad.com", "123456");	
	
		}
		User.createRestaurant("Lovac","restoran@njam.ba", "123456", "Sarajevo", "Fojnicka", "5");
	
		User.createRestaurant("Harambasa","restoran1@njam.ba", "123456","Banjaluka", "Pere Kvrzice", "18");
		User.createRestaurant("Klopa","restoran2@njam.ba", "123456","Tuzla", "Marsala Tita", "22");

		Faq.create("Kako ću znati da li je restoran primio moju narudžbu? ", 
				"Kada napravite finalni korak klikom na dugme Naruči, na vašem ekranu će se kroz nekoliko minuta "
				+ "ispisati status vaše narudžbe. Ukoliko ste prilikom narudžbe ostavili svoju email adresu, "
				+ "sve informacije o svojoj narudžbi ćete dobiti putem email-a, uključujući napomenu/e, "
				+ "procjenjeno vrijeme dostave ukoliko se radi o narudžbi za dostavu, ukupnu cijenu narudžbe "
				+ "kao i broj osvojenih bodova.");
	}
}
