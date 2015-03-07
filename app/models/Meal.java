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

	@OneToOne
	public Restaurant restaurant;

	static Finder<Integer, Meal> find = new Finder<Integer, Meal>(
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

	public static boolean create(String name, double price) {
		Meal m = new Meal(name, price);
		User u = Session.getCurrentUser(Context.current());
		m.restaurant = u.restaurant;
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

	public static Meal find(int id) {
		return find.byId(id);
	}

	public static Meal findByName(String name) {
		return find.where().eq("name", name).findUnique();
	}

	public static boolean modifyMeal(Meal m, String newName, double newPrice) {

		m.name = newName;
		m.price = newPrice;
		m.update();
		Meal meal = find.findUnique();
		if (!meal.name.equals(newName) || meal.price != newPrice) {
			return false;
		}
		else
			return true;
	}
}
