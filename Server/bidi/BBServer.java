package bgu.spl181.net.impl.bidi;


import bgu.spl181.net.impl.Objects.MovieInDB;
import bgu.spl181.net.impl.Objects.MovieInJson;
import bgu.spl181.net.impl.Objects.User;
import bgu.spl181.net.impl.Objects.UserInJson;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import java.util.concurrent.ConcurrentHashMap;

public interface BBServer extends Server{
    //fields

    Object UserDBLock = new Object();
    Object MoviesDBLock = new Object();
    Object LoggedClientsLock = new Object();

    String UserDBPath = "Database/Users.json";
    String MovieDBPath = "Database/Movies.json";
    HashMap<String,ArrayList<MovieInDB>> MoviesDB = new HashMap<>();
    HashMap<String,User> Users = init_users();
    HashMap<String,MovieInDB> Movies = init_movies();
    Connections connections =  new ClientsConnections();
    ConcurrentHashMap<Integer, String> LoggedClients = new ConcurrentHashMap<>();





    //creators of the DBs
    static HashMap<String,User> init_users() {
        synchronized (UserDBLock) {
            Type USERTYPE = new TypeToken<HashMap<String, ArrayList<User>>>() {
            }.getType();
            Gson gson = new Gson();
            try {
                JsonReader reader = new JsonReader(new FileReader(UserDBPath));
                HashMap<String,ArrayList<User>> data = gson.fromJson(reader, USERTYPE);
                HashMap<String,User> ans = new HashMap<>();
                if(data.size()>0)
                {
                    for (User user:data.get("users"))
                        ans.put(user.getUsername(),user);
                }
                return ans;
            } catch (IOException E) {
                throw new RuntimeException("cannot read file");
            }
        }
    }
    static HashMap<String,MovieInDB> init_movies() {
        synchronized (MoviesDBLock) {
            Type USERTYPE = new TypeToken<HashMap<String, ArrayList<MovieInDB>>>() {
            }.getType();
            Gson gson = new Gson();
            try {
                JsonReader reader = new JsonReader(new FileReader(MovieDBPath));
                HashMap<String, ArrayList<MovieInDB>> data = gson.fromJson(reader, USERTYPE);
                HashMap<String,MovieInDB> ans = new HashMap<>();
                if(data.size()>0)
                {
                    for (MovieInDB movie:data.get("movies"))
                        ans.put(movie.getName(),movie);
                }
                return ans;
            } catch (IOException E) {
                throw new RuntimeException("cannot read file");
            }
        }
    }
    static void writeToMoviesDBFile(HashMap<String,MovieInDB> movies) {

        Collection<MovieInDB> moviesList = movies.values();
        HashMap<String,Collection<MovieInJson>> moviesFile = new HashMap<>();
        Collection<MovieInJson> movieInDBS = transformToJsonFormatMovies(moviesList);
        moviesFile.put("movies", movieInDBS);
        try (Writer writer = new FileWriter(MovieDBPath)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(moviesFile, writer);
        } catch (IOException E) {
        }

    }

    static Collection<MovieInJson> transformToJsonFormatMovies(Collection<MovieInDB> moviesFile) {
        Vector<MovieInJson> collections = new Vector<>();
        for (MovieInDB movie:moviesFile){
            collections.add(new MovieInJson(movie));
        }
        return collections;
    }

    static void writeToUsersDBFile(HashMap<String,User> users) {

        Collection<User> usersList = users.values();
        HashMap<String,Collection<UserInJson>> usersFile = new HashMap<>();
        Collection<UserInJson> usersInDB = ConvertUsersToUsersInJson(usersList);
        usersFile.put("users",usersInDB);
        try (Writer writer = new FileWriter(UserDBPath)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(usersFile, writer);
        } catch (IOException E) {
        }

    }

    static Collection<UserInJson> ConvertUsersToUsersInJson(Collection<User> usersList) {
        Vector<UserInJson> userstoDB = new Vector<>();
        for(User user : usersList){
            userstoDB.add(new UserInJson(user));
        }
        return userstoDB;
    }

    //writing to DB
    static void writeToMoviesDB(MovieInDB movie) {
        synchronized (MoviesDBLock) {
            HashMap<String,MovieInDB> movies = get_movies();
            movies.put(movie.getName(),movie);
            writeToMoviesDBFile(movies);
        }
    }
    static void removeFromMoviesDB(String movieName) {
        synchronized (MoviesDBLock) {
            HashMap<String,MovieInDB> moviesDB = get_movies();
            moviesDB.remove(movieName);
            writeToMoviesDBFile(moviesDB);
        }
    }
    static void writeToUserDB(User user) {
        synchronized (UserDBLock) {
            HashMap<String,User> users = get_users();
            users.put(user.getUsername(),user);

            writeToUsersDBFile(users);
        }
    }
    //  getters
    static HashMap<String,User> get_users() {
        return Users;

    }
    static HashMap<String,MovieInDB> get_movies() {
        return Movies;
    }
    static Connections getConnections() {
        return connections;
    }
    static Object getUserLock() {
        return UserDBLock;
    }
    static Object getMoviesLock() {
        return MoviesDBLock;
    }
    static Object getLoggedClientsLock() {
        return LoggedClientsLock;
    }

    static ConcurrentHashMap<Integer, String> getLoggedClients() {
        return LoggedClients;
    }


    static void addMovie(MovieInDB toAdd) {
        writeToMoviesDB(toAdd);
        Movies.put(toAdd.getName(),toAdd);
    }
    static void addUser(User toAdd) {
        writeToUserDB(toAdd);
        Users.put(toAdd.getUsername(),toAdd);
    }
}
