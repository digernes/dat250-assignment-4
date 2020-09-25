package no.hvl.dat250.todos;

import com.google.gson.Gson;

import javax.persistence.EntityNotFoundException;

import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App 
{
    static Todo todo = null;
    static TodoDAO todoDao = null;
    public static void main( String[] args )
    {
        if (args.length > 0) {
            port(Integer.parseInt(args[0]));
        } else {
            port(8080);
        }

        todo = new Todo();
        todoDao = new TodoDAO();

        after((req, res) -> {
            res.type("application/json");
        });

        get("/todos", (req, res) ->  {
            Gson gson = new Gson();

            return gson.toJson(todoDao.getAll());
        });

        post("/todos", (req,res) -> {
            Gson gson = new Gson();

            todo = gson.fromJson(req.body(), Todo.class);
            todoDao.save(todo);

            return todo.toJson();
        });

        put("/todos/:id", (req,res) -> {
            Gson gson = new Gson();

            Long id = Long.parseLong(req.params(":id"));

            Todo updatedTodo = gson.fromJson(req.body(), Todo.class);
            updatedTodo.setId(id);

            todoDao.update(updatedTodo);

            return updatedTodo.toJson();
        });

        delete("todos/:id", (req,res) -> {
            Gson gson = new Gson();

            Long id = Long.parseLong(req.params(":id"));

            Todo toDelete;
            try{
                toDelete = todoDao.getById(id);
            }
            catch (EntityNotFoundException e) {
                return gson.toJson("No todo task with id "+id);
            }

            todoDao.delete(toDelete);

            res.status(201);

            return toDelete.toJson();

        });



    }
}
