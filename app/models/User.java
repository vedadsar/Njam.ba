package models;

import javax.persistence.*;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@Entity
public class User extends Model {

	@Id
	public int id;	
	@Required
	@Column(unique = true)	
	public String email;
	@Required
	public String hashedPassword;
	
	static Finder<Integer, User> find = new Finder<Integer, User>(Integer.class, User.class);
	
	public User(String email, String clearPassword){
		this.email = email;
		this.hashedPassword =clearPassword;
		//TODO hashing password.
	}
	
	/**
	 * Method for creating user.
	 * @param email
	 * @param password
	 */
	public static void createUser(String email, String password){
		//First we check if user already exists.
		User check = find.where().eq("email", email).findUnique();
		if(check == null){
			//User already exists !
			return;
		}
		new User(email, password).save();
	}
	
	public static void createUser(User u){
		u.save();
	}
	
	/**
	 * private class for creating hash.
	 * TODO method for hashing password.
	 * @author vedad
	 *
	 */
	private static class Hash{
		
		public static String md5(String clearPassword){
			return clearPassword;			
		}
	}
}
