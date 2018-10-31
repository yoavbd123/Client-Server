package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.MovieInDB;
import bgu.spl181.net.impl.Objects.MovieInUser;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;

public class Return implements Command<String[]> {
    int ConnectionId;

    public Return(int connectionId) {
        ConnectionId = connectionId;
    }

    @Override
    public Serializable execute(String args[]) {

        String user = BBServer.getLoggedClients().get(ConnectionId);
        String[] ans = new String[1];
        // synced both on user and movie, so that the change will be safe
        //note that the order of sync is consistent to avoid deadlocks
        synchronized (BBServer.getUserLock()) {
            synchronized (BBServer.getMoviesLock()) {
                MovieInDB movie = BBServer.get_movies().get(args[0]);
                // checks that the user is logged in and the movie exists in the DB
                if (user == null || movie == null) ans[0] = "ERROR request return failed";
                else {
                    User usr = BBServer.get_users().get(user);
                    MovieInUser MovInUsr = new MovieInUser(movie.getId(), movie.getName());
                    // checks that the user is actualy renting the movie
                    if (!usr.getMovies().contains(MovInUsr)) ans[0] = "ERROR request return failed";
                    else {
                        ans = new String[2];
                        // removes the movie from the user movies list, and increases the available copies by one
                        usr.getMovies().remove(MovInUsr);
                        BBServer.addUser(usr);
                        movie.setAvailableAmount(movie.getAvailableAmount() + 1);
                        BBServer.addMovie(movie);
                        ans[1] ="BROADCAST movie \"" + movie.getName() + "\" "
                                + movie.getAvailableAmount() + " " + movie.getPrice();
                        ans[0] = "ACK return \"" + movie.getName() + "\" success";
                    }
                }
            }
        }
        return ans;
    }
}
