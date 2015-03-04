package models;

import java.util.List;

import javax.persistence.*;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Meal extends Model {

	@Id
	public int id;
	@Required
	public String name;
	@Required
	public double price;
	
	@OneToOne
	public Restaurant restaurant;
	
	static Finder<Integer, Meal> find =  new Finder<Integer, Meal>(Integer.class, Meal.class);

	
	public Meal(String name, double price){
		this.name=name;
		this.price =price;
	}
	
	public boolean create(int id){
		Meal meal = find.byId(id);
		if(meal != null){
			return false;
		} else {
			new Meal(name, price);
		}
			return true;
	}
	
	public static boolean create(Meal m) {
		Meal meal = find.findUnique();
		if (meal != null) {
			return false;
		} else {
			m.save();
		}
		return true;
	}
	
	public static Meal find(int id){
		return find.byId(id);
	}
	
	public static Meal findByName(String name){
		return find.where().eq("name", name).findUnique();
	}
	
	public static void delete(int id){
		find.byId(id);
	}
	
	public static List<Meal> all(int id){
		return find.where().eq("id", id).findList();
	}
	
	public static List<Meal> all(String name){
		return find.where().eq("name", name).findList();
	}
	
	public static List<Meal> all(Restaurant restaurant){
		return find.where().eq("restaurant", restaurant).findList();
	}

}
