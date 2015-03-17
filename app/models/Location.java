package models;

import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Location extends Model {
	
	static Finder<Integer, Location> find =  new Finder<Integer, Location>(Integer.class, Location.class);
	
	@Id
	public Integer id;	
	@Required
	public String address;
	@Required
	public String number;
	@Required
	public String city;
	@Required
	public String country;
	
	@OneToOne
	public Restaurant restaurant;
	

	public Location( String address, String number,String city, String country){		
		this.address = address;
		this.number = number;
		this.city = city;
		this.country = country;
	}
	
	public static boolean create(String address, String number,String city, String country){
		Location location = find.findUnique();
		if(location != null){
			return false;
		} else {
			new Location(address, number, city, country).save();
		}
			return true;
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
	
	public static List<Location> all(String city){
		return find.where().eq("city", city).findList();
	}
	
	public static List<Location> all(Restaurant restaurant){
		return find.where().eq("restaurant", restaurant).findList();
	}
}