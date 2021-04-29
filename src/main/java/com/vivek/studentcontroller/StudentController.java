package com.vivek.studentcontroller;

import static spark.Spark.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.gson.Gson;
import com.vivek.dao.StudentDao;

public class StudentController {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String fileName = "config.properties";
		ClassLoader classLoader = StudentServices.class.getClassLoader();
	
		try(
		InputStream inputStream = classLoader.getResourceAsStream(fileName))
		{		Properties prop = new Properties();
				// load a properties file
				prop.load(inputStream);
	
				// get the property value
				String portNum = prop.getProperty("port");
				port(Integer.parseInt(portNum));
		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			e.printStackTrace();
		}
		@SuppressWarnings("unused")
		StudentDao dao = new StudentDao();
		Gson gson = new Gson();
	
		before("/*", (request, response) -> {response.type("application/json");});
	
		path("/student", () -> {
			post("/add", StudentServices.addStudent, gson::toJson);
			get("/find/:id", StudentServices.findStudent, gson::toJson);
			put("/update/:id", StudentServices.updateStudent, gson::toJson);
			delete("/delete/:id", StudentServices.deleteStudent, gson::toJson);
			get("/list", StudentServices.listStudent, gson::toJson);
		});
		}
}
