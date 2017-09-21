package com.libertymutual.goforcode.spark.myapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;

import com.libertymutual.goforcode.spark.myapp.models.Apartment;
import com.libertymutual.goforcode.spark.myapp.models.User;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {
	public static final Route details = (Request req, Response res) -> {
		try(AutoCloseableDb db = new AutoCloseableDb()) {
		String idAsString = req.params("id");
		boolean currentUserHasLiked = false;
		boolean currentUserIsLister = false;
		User currentUser = req.session().attribute("currentUser");
		
	
		Apartment apartment = Apartment.findById(Integer.parseInt(req.params("id")));
		User lister = apartment.parent(User.class);
		List<User> likers = apartment.getAll(User.class);
		
		if (currentUser != null) {
			if (currentUser.getId()== lister.getId()) {
				currentUserIsLister = true;
			}
			for (User user : likers) {
				if (currentUser.getId()== user.getId()) {
					currentUserHasLiked = true;
				}
			}
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apartment", apartment);
		model.put("numberOfLikes", likers.size());
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		model.put("currentUserIsLister", currentUserIsLister);
		model.put("isActive", apartment.getIsActive());
		model.put("notActive", !apartment.getIsActive());
		model.put("currentUserHasLiked", !currentUserHasLiked && !currentUserIsLister);
		return MustacheRenderer.getInstance().render("apartments/details.html", model);
		}
	};
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("apartments/newform.html", model);
	};
	
	public static final Route create = (Request req, Response res) -> {
		User user = req.session().attribute("currentUser");
		try(AutoCloseableDb db = new AutoCloseableDb()) {
		Apartment apartment = new Apartment (
				Integer.parseInt(req.queryParams("rent")),
				Integer.parseInt(req.queryParams("number_of_bedrooms")),
				Integer.parseInt(req.queryParams("number_of_bathrooms")),
				Integer.parseInt(req.queryParams("square_footage")),
				req.queryParams("address"),
				req.queryParams("city"),
				req.queryParams("state"),
				req.queryParams("zip_code"),
				true
				);
		user.add(apartment);
		apartment.saveIt();
		res.redirect("/");
		return "";		
		}			
	};
	
	
	public static final Route index = (Request req, Response res) -> {
		User currentUser= req.session().attribute("currentUser");
		long id = (long) currentUser.getId();
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			List<Apartment> activeApartments = Apartment.where("is_active = ?", true);
			List<Apartment> inactiveApartments = Apartment.where("is_active = ?", false);

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("activeApartments", activeApartments);
			model.put("inactiveApartments", inactiveApartments);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			return MustacheRenderer.getInstance().render("apartments/index.html", model);
		}
	};
		
	public static final Route like = (Request req, Response res) ->  {
		long id = Long.parseLong(req.params("id"));
		try (AutoCloseableDb db = new AutoCloseableDb()) {
		Apartment apartment = Apartment.findById(id);
		User user = req.session().attribute("currentUser");
		apartment.add(user);
		apartment.saveIt();
		user.saveIt();
		res.redirect("/apartments/" + id);
		return "";
		}
	};
	
	public static final Route deactivate = (Request req, Response res) -> {
		long id = Long.parseLong(req.params("id"));
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(id);
			apartment.setIsActive(false);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};
	public static final Route activate = (Request req, Response res) -> {
		long id = Long.parseLong(req.params("id"));
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(id);
			apartment.setIsActive(true);
			apartment.saveIt();
			res.redirect("/apartments/" + id);
			return "";
		}
	};
		
			
	
	}


