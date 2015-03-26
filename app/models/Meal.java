package models;

import java.util.List;

import javax.persistence.*;

import Utilites.Session;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.mvc.Http.Context;

@Entity
public class Meal extends Model {

	@Id
	public int id;
	@Required
	public String name;
	@Required
	public double price;

	@ManyToOne(cascade=CascadeType.ALL) 
	public Restaurant restaurant;

	@OneToMany(cascade=CascadeType.ALL) 
	public List <Image> image;
	
	
	public static Finder<Integer, Meal> find = new Finder<Integer, Meal>(
			Integer.class, Meal.class);

	public Meal(String name, double price) {
		this.name = name;
		this.price = price;
	}

	public Meal(String name, double price, Restaurant restaurant) {
		this.name = name;
		this.price = price;
		this.restaurant = restaurant;
	}

	public static List<Meal> all(String name) {
		return find.where().eq("name", name).findList();
	}

	public static List<Meal> all() {
		return find.all();
	}
	
	/**Method Returns all meals from a certain restaurant owner
	 * it  accepts the restaurant user.
	 * 
	 * @param u  User
	 * @return List of meals belonging to a certain User-restaurant 
	 */
	public static List<Meal> allById(User u) {		
		int id =u.id;
		Restaurant r = User.find(id).restaurant;
		List<Meal> mealsById=find.where().eq("restaurant", r).findList();
		return mealsById;
	}

	public static boolean create(String name, double price,Restaurant currentUserRestaurant) {
		Meal m = new Meal(name, price,currentUserRestaurant);
		m.save();

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

	public static void delete(int id) {
		find.byId(id).delete();
	}
	 /**
	  * 
	  * @param m   Meal object to be deleted
	  * Method deleats a meal Object received 
	  * if  the object still exists (not null) after the delete function it returns
	  * false if the object is successfully deleted it returns true
	  * 
	  * @return boolean
	  * 
	  */
	
	public static boolean delete(Meal m) {
		int idOfMeal = m.id;
		m.delete();
		if (find(idOfMeal)!=null){
			return false;
		}
	    return true;
	}

	public static Meal find(int id) {
		return find.byId(id);
	}

	public static Meal findByName(String name) {
		return find.where().eq("name", name).findUnique();
	}

	public static <Image> void create(){
		Image();
	}
	
	
	/**
	 * 
	 * @param m   Meal object
	 * @param newName The new  name that  the meal object will be changed to 
	 * @param newPrice  The new price  that  the meal object will be changed to 
	 * 
	 * The method reads an object then modifies its values, then it checks 
	 * if the modified values have changed 
	 * @return boolean 
	 * 
	 */
	public static boolean modifyMeal(Meal m, String newName, double newPrice) {

		m.name = newName;
		m.price = newPrice;
		m.update();
		
		Meal meal = find(m.id);
		if (!meal.name.equals(newName) || meal.price != newPrice) {
			return false;
		}
		else
			return true;
	}
	
}
