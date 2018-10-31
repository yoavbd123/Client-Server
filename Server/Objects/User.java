package bgu.spl181.net.impl.Objects;

import java.util.LinkedList;

public class User {


    // fields
    private String username;
    private String type;
    private String password;
    private String country;
    private LinkedList<MovieInUser> movies;
    private int balance;

    public User (String username, String password){
        this.type = "normal";
        this.username = username;
        this.password = password;
        this.country = null;
        this.movies = new LinkedList<>();
        this.balance = 0;
    }



    //getters
    public int getBalance() {
        return balance;
    }
    public LinkedList<MovieInUser> getMovies() {
        return movies;
    }
    public String getCountry() {
        return country;
    }
    public String getPassword() {
        return password;
    }
    public String getType() {
        return type;
    }
    public String getUsername() {
        return username;
    }

    //setters
    public void setUsername(String username) {
        this.username = username;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setMovies(LinkedList<MovieInUser> movies) {
        this.movies = movies;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }
    public void setCountry (String country){
        this.country = country;
    }

    public void addMovie(MovieInUser movie){
        movies.add(movie);
    }
}
