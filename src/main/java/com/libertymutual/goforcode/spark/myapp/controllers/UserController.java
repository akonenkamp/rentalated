package com.libertymutual.goforcode.spark.myapp.controllers;

import java.util.Map;

import org.javalite.common.JsonHelper;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.myapp.models.User;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.Response;
import spark.Request;
import spark.Route;

public class UserController {
	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("users/new.html", null);
	};

	
	
	public static final Route create = (Request req, Response res) -> {
		String encryptedPassword = BCrypt.hashpw(req.queryParams("password"), BCrypt.gensalt());
		User user = new User (
				req.queryParams("email"),
				encryptedPassword,
				req.queryParams("firstName"),
				req.queryParams("lastName")
				
				);

				
				
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			user.saveIt();
			req.session().attribute("currentUser", user);
			res.redirect("/");
			return "";
		}

	};

}
