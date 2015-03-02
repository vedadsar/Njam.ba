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
	@OneToOne(mappedBy="location")
	public Restaurant restaurant;
	@Required
	public String city;
	@Required
	public String street;
	@Required
	public String number;
	
	public Location(String city, String street, String number){
		this.city = city;
		this.street = street;
		this.number = number;
	}
	
	public static Location findByID(int id){
		return find.byId(id);
	}
	
	public static List<Location> all(){
		return find.all();
	}
}
