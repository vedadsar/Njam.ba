package Utilites;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Class which creates hashed password
 * and checks if password is correct.
 * 
 * Using org.mindrot bycript libraries.
 * @author Vedad
 *
 */
public class Hash {

	
	/**
	 * Method checks 
	 * @param password
	 * @param hashedPassword
	 * @return
	 */
	public static boolean checkPassword(String password, String hashedPassword){		
		if (password == null || hashedPassword == null)
			return false;
						
		return BCrypt.checkpw(password, hashedPassword);		
	}
	
	public static String hashPassword(String clearPassword){
		if (clearPassword == null) {
			return null;		//TODO returning exeption.
		}
		return BCrypt.hashpw(clearPassword, BCrypt.gensalt());
	}
}
