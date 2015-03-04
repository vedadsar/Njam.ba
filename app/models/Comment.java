package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
	
	@Entity
	public class Comment extends Model {
	 	    
	    @Required
	    @ManyToOne
	    public User author;
	    @Required
	    public Date postedAt;
	    @Required
	    public String title;
	    @Required	    
	    public String content;

}
