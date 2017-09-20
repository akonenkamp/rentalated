package com.libertymutual.goforcode.spark.myapp.controllers;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;


import java.util.Map;

import org.javalite.common.JsonHelper;
import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.myapp.models.User;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserApiController {
	
	public static final Route create = (Request req, Response res) -> {

		User user = new User();

		Map<String, String> map = JsonHelper.toMap(req.body());

		map.put("password", BCrypt.hashpw(map.get("password"), BCrypt.gensalt()));

		user.fromMap(map);

		try (AutoCloseableDb db = new AutoCloseableDb()) {

			user.save();

			res.status(201);

			return user.toJson(true);

		}

	};

}
