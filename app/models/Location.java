package models;

import java.util.List;

import javax.persistence.*;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;

import Utilites.Session;
import models.orders.Cart;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
import scala.reflect.internal.Trees.This;

@Entity
public class Location extends Model {
	
	static Finder<Integer, Location> find =  new Finder<Integer, Location>(Integer.class, Location.class);
	static Finder<Integer, Cart> findC =  new Finder<Integer, Cart>(Integer.class, Cart.class);

	static Finder<Integer, Restaurant> findRestaurnt =  new Finder<Integer, Restaurant>(Integer.class, Restaurant.class);

	@Id
	public Integer id;		
	@Required
	public String city;
	@Required
	public String street;
	@Required
	public String number;
	
	@ManyToOne
	public User user;
	

	public Location( String city,String street, String number){				
		this.city = city;
		this.street = street;
		this.number = number;
	}
	
	public Location( String city,String street, String number, Cart cart){				
		this.city = city;
		this.street = street;
		this.number = number;
	}
	
	
	public static boolean create(int id, String city, String street, String number){
		Location location = find.byId(id);

		if(location != null){
			return false;
		} else {
			new Location(city, street, number).save();
		}
			return true;
	}
	
	public void createLocation(String city, String street, String number){
		new Location(city, street, number).save();
	}
	
	public static boolean create(Location l){
		Location location = find.findUnique();
		if(location != null){
			return false;
		} else {
			l.save();
		}
			return true;
	}
	
	public static boolean delete(int id){
		Location l = Location.findByID(id);
		l.delete();
		if (findByID(id)!=null){
			return false;
		}
	    return true;
	}
	
	
	public static Location findByID(int id){
		return find.byId(id);
	}
	
	public static Location findByCity(String city){
		return find.where().eq("city", city).findUnique();
	}
	
	public static List<Location> all(){
		return find.all();
	}
	
	public static List<Location> all(int id){
		return find.where().eq("id", id).findList();
	}
		
	public static List<Location> all(Restaurant restaurant){
		return find.where().eq("restaurant", restaurant).findList();
	}
	
	public static List <Restaurant> all(String city){
		return findRestaurnt.where().eq("city", city).findList();
	}
	
	public static Location lastLocation(int userId){

		List<Location> locations = find.where().eq("user_id", userId).findList();;
		int size = locations.size();
		if (size == 0){
			return new Location("", "", "");
		}
		Location lastLocation = locations.get(size-1);
		if( lastLocation==null)
			return new Location("", "", "");
		System.out.println("Last location: " + lastLocation);
		return lastLocation;
	}
	
	public static Location getLocationByCart( int cartId){
		return find.where().eq("cart_id", cartId).findUnique();
	}
	
	public static boolean findUnique(String city, String street, String number) {
		Location l = User.lastLocation();
		l = find.where().eq("city", city).eq("street", street)
				.eq("number", number).findUnique();
		if (l != null) {
			return true;
		}
		return false;
	}

}