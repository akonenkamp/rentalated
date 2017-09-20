package com.libertymutual.goforcode.spark.myapp.controllers;

import com.libertymutual.goforcode.spark.myapp.models.Apartment;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.JsonHelper;

import spark.Response;
import spark.Request;
import spark.Route;
import static spark.Spark.notFound;

import java.util.Map;

public class ApartmentApiController {
	
	public static final Route details = (Request req, Response res) -> {
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			if (apartment != null)
			{
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
				}
			notFound("did not find that.");
			return "";
			}
	};
	public static final Route create = (Request req, Response res) -> {
		String json = req.body();
		Map map = JsonHelper.toMap(json);
		Apartment apartment = new Apartment();
		apartment.fromMap(map);
		
		try(AutoCloseableDb db = new AutoCloseableDb()) {
			apartment.saveIt();
			res.status(201);
			return apartment.toJson(true);
		}
		
	};
	
	

}
