package models;

import java.util.List;

import javax.persistence.*;

import play.db.ebean.Model;

@Entity
public class Meal extends Model {

	@Id
	public int id;
	@ForeignKey
	public static int restaurantId;
	
	@Required
	public String name;
	public double price;
	
	static Finder<Integer, Meal> find =  new Finder<Integer, Meal>(Integer.class, Meal.class);
	
	
	public Meal(int resraurantId, String name, double price){
		this.restaurantId = restaurantId;
		this.name=name;
		this.price =price;
	}
	public static void create(int restaurantID, String name, double price){
		new Meal(restaurantId,name, price);
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
	
	public static List<Meal> all(int restaurantId){
		return find.where().eq("retaurantID", restaurantId).findList();
	}
		
}
