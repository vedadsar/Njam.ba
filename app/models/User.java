package models;

import java.util.List;

import javax.persistence.*;

import Utilites.Hash;
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
		this.hashedPassword = Hash.hashPassword(clearPassword);
		//TODO hashing password.
	}
	
	/**
	 * Method for creating user.
	 * @param email
	 * @param password
	 */
	public static boolean createUser(String email, String password){
		//First we check if user already exists.
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			//User already exists !
			return false;
		} else {
			new User(email, password).save();
			return true;
		}
	}
	
	public static boolean createUser(User u){
		//First we check if user already exists.
		User check = find.where().eq("email", u.email).findUnique();
		if(check == null){
			//User already exists !
			return false;
		}
		u.save();
		return true;
	}
	
	/**
	 * Method for authenticating user who is trying to login.
	 * First it checks if user with that email exists in our
	 * database and then its checking passwords.
	 * TODO to check hashed password.
	 * @param email
	 * @param password
	 * @return
	 */
	public static boolean authenticate(String email, String password){
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			if(Hash.checkPassword(password, check.hashedPassword))
				return true;
		}		
		return false;
	}	
	
	// Added for testing
		/**
		 * Creates new User by giving two paramters email and password.
		 * @param email
		 * @param password
		 */
		public static void create(String email, String password){
			new User(email, password).save();
		}
		// Added for testing
		/**
		 * Finds user by id.
		 * @param id  of User
		 * @return User's data.
		 */
		public static User find(int id){
			return find.byId(id);
		}
		
		// Added for testing
		public static void delete( int id){
			find.byId(id).delete();
		}
		
		// Added for testing
		public static List<User> all( String hashedPassword){
			return find.where().eq("hashedPassword", hashedPassword).findList();
		}	
}
