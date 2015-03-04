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
	public String city;
	@Required
	public String street;
	@Required
	public String number;
	
	@OneToOne(mappedBy="city")
	public Restaurant restaurant;
	

	public Location(String city, String street, String number){
		this.city = city;
		this.street = street;
		this.number = number;
	}
	
	public static boolean create(String city, String street, String number){
		Location location = find.where().eq("city", city).findUnique();
		if(location != null){
			return false;
		} else {
			new Location(city, street, number).save();
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
}
