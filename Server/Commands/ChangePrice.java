package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.MovieInDB;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;
import java.util.HashMap;

public class ChangePrice implements Command<String[]> {
    private final int ConnectionId;

    public ChangePrice(int connectionId) {
        ConnectionId = connectionId;
    }

    @Override
    public String[] execute(String[] args) {
        String username = BBServer.getLoggedClients().get(ConnectionId);
        User user = BBServer.get_users().get(username);
        String[] ans = new String[1];
        //checks that the new price is positive
        if (Integer.valueOf(args[1]) <= 0)
            ans[0] = "ERROR request changeprice failed";
        // checks that the client is logged in and is an admin
        else if (user == null || !user.getType().equals("admin"))
            ans[0] = "ERROR request changeprice failed";
        // synced to avoid a case where a different thread is doing some other manipulation on the same movie,
        // such as rent or return, which might cause the data in the DB to be incorrect
        else {
            synchronized (BBServer.getMoviesLock()) {
                HashMap<String, MovieInDB> moviesDB = BBServer.get_movies();
                if (!moviesDB.containsKey(args[0]))
                    ans[0] = "ERROR request changeprice failed";
                else {
                    MovieInDB movie = moviesDB.get(args[0]);
                    movie.setPrice(Integer.valueOf(args[1]));
                    BBServer.addMovie(movie);
                    ans = new String[2];
                    ans[0] = "ACK changeprice \"" + args[0] + "\" success";
                    ans[1] = "BROADCAST movie \"" + args[0] + "\" " + movie.getAvailableAmount() + " " + movie.getPrice();
                }
            }
        }
        return ans;
    }

}


