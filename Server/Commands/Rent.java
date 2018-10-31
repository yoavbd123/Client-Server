package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.MovieInDB;
import bgu.spl181.net.impl.Objects.MovieInUser;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Rent implements Command<String[]> {
    int ConnectionId;
    public Rent(int connectionId){ConnectionId = connectionId;}
    @Override
    // synced both on user and movie, so that the change will be safe
    //note that the order of sync is consistent to avoid deadlocks
    public String[] execute(String[] args) {
        String user = BBServer.getLoggedClients().get(ConnectionId);
        String[] ans = new String[1];
        synchronized (BBServer.getUserLock()) {
            synchronized (BBServer.getMoviesLock()) {
                MovieInDB movie = BBServer.get_movies().get(args[0]);
                // checks that the user is logged in and the movie exists
                if (user == null || movie == null) ans[0] = "ERROR request rent failed";
                else {
                    MovieInUser movInUser = new MovieInUser(movie.getId(), movie.getName());
                    User usr = BBServer.get_users().get(user);
                    ArrayList<String> countries = new ArrayList<>(Arrays.asList(movie.getBannedCountries()));
                /* checks that the user has enough money to rent the movie
                 that the user's country is not in the banned countries list
                 that there the user doesn't already have the movie
                 and that the movie has available copies to rent
                */
                    if (usr.getBalance() < movie.getPrice() || countries.contains(usr.getCountry())
                            || movie.getAvailableAmount() == 0 || usr.getMovies().contains(movInUser))
                        ans[0] = "ERROR request rent failed";
                    else {
                        ans = new String[2];
                        // decrease the number of available copies by one
                        movie.setAvailableAmount(movie.getAvailableAmount() - 1);
                        BBServer.addMovie(movie);
                        //add the movie to the user's movie array
                        //decrease the user balance by the price of the movie
                        usr.addMovie(movInUser);
                        usr.setBalance(usr.getBalance() - movie.getPrice());
                        BBServer.addUser(usr);

                        ans[1] = "BROADCAST movie \"" + movie.getName() + "\" "
                                + movie.getAvailableAmount() + " " + movie.getPrice();
                        ans[0] = "ACK rent \"" + movie.getName() + "\" success";

                    }
                }
            }
        }
        return ans;
    }
}
