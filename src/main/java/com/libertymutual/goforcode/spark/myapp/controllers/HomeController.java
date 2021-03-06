package com.libertymutual.goforcode.spark.myapp.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.template.velocity.VelocityTemplateEngine;
import com.libertymutual.goforcode.spark.myapp.models.Apartment;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;

public class HomeController {
	public static final Route index = (Request req, Response res) -> {
		try(AutoCloseableDb db = new AutoCloseableDb()) {
		List<Apartment> apartments = Apartment.where("is_active = ?", true);
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("apartments", apartments);
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		return MustacheRenderer.getInstance().render("home/index.html", model);
//		return new VelocityTemplateEngine().render(
//		        new ModelAndView(model, "templates/home/velocity.vm"));
		}
	};

}
