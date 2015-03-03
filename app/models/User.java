package models;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import play.data.format.Formats.DateTime;
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
    @DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;
    
    public boolean isAdmin;
	
    	
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
			if(check.hashedPassword.equals(password))
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
	
	
	public boolean hasRole(String email, String role) {

		User user = User.all().filter("email", email).get();

		if (user == null) {
			return false;
		}

		return user.hasRole(role);

	}

	public void addRole(String email, String role) {

		User user = User.all().filter("email", email).get();

		if (user == null) {
			return;
		}

		user.addRole(role);
		user.save();
	}

}
