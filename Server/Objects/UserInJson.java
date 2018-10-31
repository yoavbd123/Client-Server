package bgu.spl181.net.impl.Objects;

import java.util.LinkedList;

public class UserInJson {
    // fields
    private String username;
    private String type;
    private String password;
    private String country;
    private LinkedList<MovieInUserInJson> movies;
    private String balance;

    public UserInJson(User user) {
        this.type = user.getType();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.country = user.getCountry();
        this.movies = moviesinUsertoMoviesInUserInJson(user.getMovies());
        this.balance = Integer.toString(user.getBalance());
    }




    //getters
    public String getBalance() {
        return balance;
    }

    public LinkedList<MovieInUserInJson> getMovies() {
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

    public void setMovies(LinkedList<MovieInUserInJson> movies) {
        this.movies = movies;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void addMovie(MovieInUserInJson movie) {
        movies.add(movie);
    }

    //was required after the change of int to string in the json
    private static LinkedList<MovieInUserInJson> moviesinUsertoMoviesInUserInJson(LinkedList<MovieInUser> movies) {
        LinkedList<MovieInUserInJson> movs = new LinkedList<>();
        for (MovieInUser mov : movies) {
            movs.add(new MovieInUserInJson(mov));

        }
        return movs;
    }

}

