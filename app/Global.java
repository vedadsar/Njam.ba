import java.util.Date;

import models.Blogger;
import models.Faq;
import models.Location;
import models.Meal;
import models.Restaurant;
import models.User;
import play.Application;
import play.GlobalSettings;
 
 
public class Global extends GlobalSettings {
	
	public void onStart(Application app){
		if(User.check("suad@suad.com") == false){
		User.createAdmin("suad@suad.com", "123456");	
	
		if(User.check("haris.krkalic@bitcamp.ba") == false){
		User testUser = User.createUserReturnUser("haris.krkalic@bitcamp.ba", "Banana88");
		testUser.validated = true;
		testUser.role = "USER";
		testUser.dateCreation = new Date();
		Location loc = new Location("", "", "");
		testUser.locations.add(loc);
		loc.save();
		testUser.update();
		
		}
		
		if(User.check("dastko@njam.ba") == false){
			User testUser = User.createUserReturnUser("dastko@njam.ba", "Davorike123");
			testUser.validated = true;
			testUser.role = "ADMIN";
			testUser.username = "njam.ba";
			testUser.dateCreation = new Date();
			Location loc = new Location("", "", "");
			testUser.locations.add(loc);
			loc.save();
			testUser.update();	
		}
		
		}
		User.createRestaurant("Lovac","restoran@njam.ba", "123456","08:00h - 18:00h", "Sarajevo", "Fojnicka", "5");	
		User.createRestaurant("Harambasa","restoran1@njam.ba", "123456","07:00h - 19:00h","Banjaluka", "Pere Kvrzice", "18");
		User.createRestaurant("Klopa","restoran2@njam.ba", "123456","09:00h - 20:00h", "Tuzla", "Marsala Tita", "22");
		
		Restaurant res3 = User.createRestaurantReturnRestaurant("Obala","restoran3@njam.ba", "123456","09:00h - 20:00h", "Sarajevo", "Jufkica 12", "21");
		Restaurant res4 = User.createRestaurantReturnRestaurant("Trovač","restoran4@njam.ba", "123456","09:00h - 20:00h", "Sarajevo", "Bananica 122", "27");
		
		Restaurant res1 = Restaurant.find(1);
		Restaurant res2 = Restaurant.find(2);
		for( int i=1; i<=8; i++){
			if (Meal.check(i)==false){
				Meal.create("Pizza", 5.00,"Pizza", res1 ,"Povrce, gljive, sos, sir.");
				Meal.create("Pizza vegetariana", 5.00,"Pizza", res1 ,"Povrce, gljive, sos, sir.");
				Meal.create("Supa", 3.00,"Bosnian cousine", res1,"Sjeckano povrce sa piletionm.");
				Meal.create("Cevapi", 5.00, "Bosnian cousine",res1, "Cevapi u lepini sa lukom.");
				Meal.create("Pjeskavica", 6.00, "Fast Food",res1,"Pljeskavica, pomfrit, salata, majoneza, ketchup.");
				User userRestaurant = res1.user;
				userRestaurant.validated = true;
				userRestaurant.update();
				
				Meal.create("Zeljanica", 5.00,"Bosnian cousine", res2 ,"Tjesto sa zeljem.");
				Meal.create("Burek", 8.00, "Bosnian cousine", res2, "Tjesto sa mesom.");
				Meal.create("Krompirusa", 5.00,"Bosnian cousine", res2, "Tjesto sa kropirom.");
				Meal.create("Sirnica", 6.00, "Bosnian cousine",res2, "Tjesto sa sirom.");
				
				Meal.create("Skuša", 5.00,"Fish dishes", res3 ,"");
				Meal.create("Školjke", 3.00,"Mediterian", res3, "");
				Meal.create("Hobotnica", 5.00, "Mediterian",res3, "");
				Meal.create("Krompir salata", 6.00, "Mediterian",res3,"");
				
				Meal.create("Gljive", 5.00,"Side dish", res4,"");
				Meal.create("Lazanje", 3.00,"Mediteran", res4,"");
				Meal.create("Lignje", 5.00, "Mediteran",res4,"");
				Meal.create("Riza", 5.00,"Side dish", res4,"");
				Meal.create("Kropmir", 3.00,"Side dish", res4,"");
			}
				
		}
		
		Blogger blog = new Blogger("Post Prvi", "At vero eos et accusamus", "vero, pero, zdero", "At vero eos et accusamus et iusto odio dignissimos ducimus qui blanditiis praesentium voluptatum deleniti atque corrupti quos dolores et quas molestias excepturi sint occaecati cupiditate non provident, similique sunt in culpa qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio cumque nihil impedit quo minus id quod maxime placeat facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur "
				+ "aut perferendis doloribus asperiores repellat.", User.find("dastko@njam.ba"));
		blog.save();
		
		if(Faq.check("Kako ću znati da li je restoran primio moju narudžbu? ") == false){
			Faq.create("Kako ću znati da li je restoran primio moju narudžbu? ", 
					"Kada napravite finalni korak klikom na dugme Naruči, na vašem ekranu će se kroz nekoliko minuta "
					+ "ispisati status vaše narudžbe. Ukoliko ste prilikom narudžbe ostavili svoju email adresu, "
					+ "sve informacije o svojoj narudžbi ćete dobiti putem email-a, uključujući napomenu/e, "
					+ "procjenjeno vrijeme dostave ukoliko se radi o narudžbi za dostavu, ukupnu cijenu narudžbe "
					+ "kao i broj osvojenih bodova.");
		}
		}
	
}