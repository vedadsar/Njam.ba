package models;

import play.db.ebean.Model;

public class Jsoner extends Model{

	public TransactionU transaction;
	public String name;
	public double price;
	public int quantity;
	public double totalPrice;
	
	public Jsoner(String name, double price, int quantity, double totalPrice){
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}
}
