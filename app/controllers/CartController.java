package controllers;

import play.mvc.Result;
import views.html.*;
import java.util.Iterator;
import java.util.List;
import Utilites.AdminFilter;
import Utilites.Session;
import models.*;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Http.Context;
import play.mvc.Security;
import Utilites.*;

public class CartController extends Controller {
	
	public static Result showCart(){
		return ok(cart.render("Nesto"));
	}
	
	public static Result addMealToBasket(int id){
		Meal meal = Meal.find(id);
		return null;
	}

}
