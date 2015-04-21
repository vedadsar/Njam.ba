package models;

import models.orders.Cart;
import play.db.ebean.Model;

public class MetaItem extends Model{

	public String name;
	
	public double price;
	
	public int quantity;
	
	public double totalPrice;
	
	
	public MetaItem(String name, double price, int quantity, double totalPrice){
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
	
}
