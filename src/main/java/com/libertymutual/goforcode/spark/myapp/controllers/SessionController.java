package com.libertymutual.goforcode.spark.myapp.controllers;

import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.myapp.models.User;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class SessionController {
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("returnPath", req.queryParams("returnPath"));
		return MustacheRenderer.getInstance().render("session/login.html", model);
		

	};
	public static final Route create = (Request req, Response res) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");

	try(AutoCloseableDb db = new AutoCloseableDb()) {
		User user = User.findFirst("email = ?", email);
		if (user != null && BCrypt.checkpw(password, user.getPassword())) {
		req.session().attribute("currentUser", user);
		}
	}
		
	res.redirect(req.queryParamOrDefault("returnPath", "/"));
	
		return "";
	};

	public static final Route destroy = (Request req, Response res) -> {
		req.session().attribute("currentUser", null);
		res.redirect("/");
		return "";
	};
	

}
