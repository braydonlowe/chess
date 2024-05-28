package Handlers;

//Imports
import spark.Request;
import spark.Response;
import server.JsonUtil;

public class CreateGameHandler {
    public static Object createGame(Request req, Response res) {

        String authToken = req.headers("Authorization");

        //Make sure we've been given an auth token.

        //Retrieve auth token from the database

        //Make sure that this auth token is valid



        return null;


    }
}
