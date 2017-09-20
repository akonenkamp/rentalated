package com.libertymutual.goforcode.spark.myapp.filters;

import static spark.Spark.halt;

import spark.Filter;
import spark.Response;
import spark.Request;


public class SecurityFilters {

	public static Filter isAuthenticated = (Request req, Response res ) -> {
		if (req.session().attribute("currentUser") == null) {
    		res.redirect("/login?returnPath=" + req.pathInfo());
    		halt();
	}

	};
}
