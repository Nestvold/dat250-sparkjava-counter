package no.hvl.dat110.rest.counters;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import java.util.ArrayList;

import com.google.gson.Gson;

import no.hvl.dat110.rest.main.Todo;

/**
 * Hello world!
 *
 */
public class App {
	
	static Counters counters = null;
	public static ArrayList<Todo> testDB = new ArrayList<Todo>();
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		counters = new Counters();
		
		after((req, res) -> { res.type("application/json"); });
		
		get("/hello", (req, res) -> "Hello World!");
		
        get("/counters", (req, res) -> counters.toJson());
 
        get("/counters/red", (req, res) -> counters.getRed());

        get("/counters/green", (req, res) -> counters.getGreen());

        put("/counters", (req,res) -> {
        
        	Gson gson = new Gson();
        	
        	counters = gson.fromJson(req.body(), Counters.class);
        
            return counters.toJson();
        	
        });
    
	
		// Experiment 2: REST API for TODO-items

		// POST
		post("/todo/", (req, res) -> {
			Todo newTodo = new Gson().fromJson(req.body(), Todo.class);
            res.type("application/json");

            for (int i = 0; i < testDB.size(); i++) {
                if(testDB.get(i).id == newTodo.id)  {
                    return "Todo with ID:" + testDB.get(i).id + " already exists.";
                }
            }
            testDB.add(newTodo);
            return newTodo.toJson();
        });

		// GET
		get("/todo/:id", (req, res) -> {
            String id = req.params(":id");
			res.type("application/json");
            
            for (Todo todo : testDB) {
                if(todo.id == Long.parseLong(id)) {
                    return todo.toJson();
                }
            }
            return "No Todo with the given ID was found";
        });

		//UPDATE
		put("/todo/:id", (req, res) -> {
			Todo edit = new Gson().fromJson(req.body(), Todo.class);
            String id = req.params(":id");
			boolean edited = false;

			for (int i = 0; i < testDB.size(); i++) {
                if(testDB.get(i).id == Long.parseLong(id)) {

				}
			}	
		});

		//DELETE
		delete("/todo/:id", (req, res) -> {
            String id = req.params(":id");
			res.type("application/json");

            for (int i = 0; i < testDB.size(); i++) {
                if(testDB.get(i).id == Long.parseLong(id)) {
					Long deletedItem = testDB.get(i).id;
                    testDB.remove(deletedItem);
                    return deletedItem + ": was succesfully deleted";
                }
            }
            return "No Todo with given Id found";
        });
	}
}
