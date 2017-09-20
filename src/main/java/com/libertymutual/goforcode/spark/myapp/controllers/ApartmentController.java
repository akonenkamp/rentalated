package com.libertymutual.goforcode.spark.myapp.controllers;

import java.util.HashMap;
import java.util.Map;

import com.libertymutual.goforcode.spark.myapp.models.Apartment;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {
	public static final Route details = (Request req, Response res) -> {
		try(AutoCloseableDb db = new AutoCloseableDb()) {
		String idAsString = req.params("id");
		Apartment apartment = Apartment.findById(Integer.parseInt(req.params("id")));
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apartment", apartment);
		return MustacheRenderer.getInstance().render("apartments/details.html", model);
		}
	};
	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("apartments/newform.html", null);
	};
	public static final Route create = (Request req, Response res) -> {
		try(AutoCloseableDb db = new AutoCloseableDb()) {
		Apartment apartment = new Apartment (
				Integer.parseInt(req.queryParams("rent")),
				Integer.parseInt(req.queryParams("number_of_bedrooms")),
				Integer.parseInt(req.queryParams("number_of_bathrooms")),
				Integer.parseInt(req.queryParams("square_footage")),
				req.queryParams("address"),
				req.queryParams("city"),
				req.queryParams("state"),
				req.queryParams("zipCode")
				);
			
		apartment.saveIt();
		res.redirect("/");
		return "";
				
				
				
				}
				
	};

}
