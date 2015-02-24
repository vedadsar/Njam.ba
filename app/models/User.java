package models;

import javax.persistence.*;

import play.db.ebean.Model;

public class User extends Model {

	@Id
	public int id;
	
	@Column(unique = true)
	public String email;
	
	public String hashedPassword;
	
}
