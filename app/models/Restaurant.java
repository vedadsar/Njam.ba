package models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Restaurant extends Model{

	@Id
	public int id;
	@Required
	public String name;
    @DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;
    
	@OneToOne
	public User user;
		
	@OneToOne
	public Location location;
	

	@OneToMany (mappedBy="name")
	public List <Meal> meals;
	
	public boolean isRestaurant;
	
	
	static Finder<Integer, Restaurant> find =  new Finder<Integer,Restaurant>(Integer.class, Restaurant.class);
	
	public Restaurant(String name){
		this.name = name;
	}
	
	public static  void create(String name){
		new Restaurant(name).save();
	}
	
	public static Restaurant find(int id){
		return find.byId(id);
	}
	
	public static Restaurant findByName(String name){
		return find.where().eq("name", name).findUnique();
	}
		
	public static void delete(int id){
		find.byId(id);
	}
	
	public static List<Restaurant> all(Location location){
		return find.where().eq("location", location).findList();
	}

}

