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
	public double price;
	
	@OneToOne
	public Restaurant restaurant;
	
	static Finder<Integer, Meal> find =  new Finder<Integer, Meal>(Integer.class, Meal.class);

	
	public Meal(String name, double price){
		this.name=name;
		this.price =price;
	}
	public static void create(String name, double price){
		new Meal(name, price).save();
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
		
}
