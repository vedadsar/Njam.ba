package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import models.orders.Cart;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class MetaItem extends Model{

	@Id
	public int id;
	
	@OneToOne
	public TransactionU transaction;
	@Required
	public String name;
	@Required
	public double price;
	@Required
	public int quantity;
	@Required
	public double totalPrice;
	
	
	public MetaItem(String name, double price, int quantity, double totalPrice, TransactionU transaction){
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.transaction = transaction;
	}
	
}
