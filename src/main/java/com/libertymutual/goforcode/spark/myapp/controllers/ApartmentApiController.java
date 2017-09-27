package com.libertymutual.goforcode.spark.myapp.controllers;

import com.libertymutual.goforcode.spark.myapp.models.Apartment;
import com.libertymutual.goforcode.spark.myapp.models.User;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.JsonHelper;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.Response;
import spark.Request;
import spark.Route;
import static spark.Spark.notFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;

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
	
    public static final Route index = (Request req, Response res) -> {

        try (AutoCloseableDb db = new AutoCloseableDb()) {

            LazyList<Apartment> apartments = Apartment.where("is_active = ?", true);
            res.header("Content-Type", "application/json");

            return apartments.toJson(true);

        }

    };	
    public static final Route myListings = (Request req, Response res) -> {
    	User currentUser = req.session().attribute("currentUser");
        try (AutoCloseableDb db = new AutoCloseableDb()) {

            LazyList<Apartment> apartments = currentUser.getAll(Apartment.class);
            res.header("Content-Type", "application/json");

            return apartments.toJson(true);

        }

    };	

}
