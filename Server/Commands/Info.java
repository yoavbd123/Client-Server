package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.MovieInDB;

import java.io.Serializable;
import java.util.HashMap;

public class Info implements Command<String[]> {

    private int ConnectionId;

    public Info(int connectionId) {
        ConnectionId = connectionId;
    }
    @Override
    public String[] execute(String[] args) {
        String username = BBServer.getLoggedClients().get(ConnectionId);
        String[] ans = new String[1];
        // checks if the client is logged in
        if (username == null)
            ans[0] = "ERROR request info failed";
        else {
            String ret = new String();
            HashMap<String, MovieInDB> movies = BBServer.get_movies();
            if (args.length == 0) {
                for (String movie : movies.keySet()) {
                    ret = ret + " " + "\"" + movie + "\"";

                }
                ans[0] = "ACK info" + ret;

            }
            else {
                // checks if the movie exists in the database
                MovieInDB movie = movies.get(args[0]);
                if (movie == null)
                    ans[0] = "ERROR request info failed";
                else {
                    ret = "\"" + movie.getName() + "\"" + " " + movie.getAvailableAmount() +
                            " " + movie.getPrice() + " ";

                    for (String country : movie.getBannedCountries()) {
                        ret = ret + "\"" + country + "\"" + " ";
                    }
                    ans[0] = "ACK info " + ret;
                }
            }

        }
    return ans;
    }
}
