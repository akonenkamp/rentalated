package com.libertymutual.goforcode.spark.myapp;

import static spark.Spark.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.mindrot.jbcrypt.BCrypt;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.libertymutual.goforcode.spark.myapp.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.myapp.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.myapp.controllers.HomeController;
import com.libertymutual.goforcode.spark.myapp.controllers.SessionController;
import com.libertymutual.goforcode.spark.myapp.controllers.UserApiController;
import com.libertymutual.goforcode.spark.myapp.controllers.UserController;
import com.libertymutual.goforcode.spark.myapp.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.myapp.models.Apartment;
import com.libertymutual.goforcode.spark.myapp.models.User;
import com.libertymutual.goforcode.spark.myapp.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.myapp.utilities.MustacheRenderer;

import spark.Response;
import spark.Request;

public class Application {
	public static void main(String[] args) {

		String encryptedPassword = BCrypt.hashpw("password", BCrypt.gensalt());

		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			User amanda = new User("akone@gmail.com", encryptedPassword, "Amanda", "Konenkamp");
			amanda.saveIt();

			Apartment.deleteAll();

			Apartment apartment = new Apartment(6200, 1, 0, 350, "123 main st", "San Francisco", "CA", "95125", true);
			amanda.add(apartment);
			apartment.saveIt();

			// new Apartment(2000, 2, 2, 1350, "123 cowboy st", "Houston", "TX",
			// "77006").save();
			// new Apartment(2200, 2, 2, 1250, "123 fork st", "Portland", "OR",
			// "87654").save();
			Base.close();
		}

		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);

			get("/new", ApartmentController.newForm);
			before("/mine", SecurityFilters.isAuthenticated);
			get("/mine", ApartmentController.index);
			post("/:id/like", ApartmentController.like);
			post("/:id/deactivations", ApartmentController.deactivate);
			post("/:id/activations", ApartmentController.activate);
			get("/:id", ApartmentController.details);
			post("", ApartmentController.create);
		});

		get("/", HomeController.index);
		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
		post("/users/new", UserController.create);
		get("/users/new", UserController.newForm);
		post("/logout", SessionController.destroy);

		path("/api", () -> {
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			post("/users", UserApiController.create);

		});
	}
}
