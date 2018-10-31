package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.MovieInDB;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;
import java.util.HashMap;

public class RemoveMovie implements Command<String[]> {
    private final int ConnectionId;

    public RemoveMovie(int connectionId) {
        ConnectionId = connectionId;
    }
    @Override
    public String[] execute(String[] args) {

        String username = BBServer.getLoggedClients().get(ConnectionId);
        User user = BBServer.get_users().get(username);
        // checks that the client is logged in and is an admin
        String[] ans = new String[1];
        if(user == null || !user.getType().equals("admin"))
            ans[0] = "ERROR request remmovie failed";
        //synced to avoid a case in which another client tries to rent the movie in the middle of the remove process
        // which might cause a client to rent a movie that was removed
        else {
            synchronized (BBServer.getMoviesLock()) {
                HashMap<String, MovieInDB> moviesDB = BBServer.get_movies();
                if (!moviesDB.containsKey(args[0]))
                    ans[0] = "ERROR request remmovie failed";
                else if (moviesDB.get(args[0]).getAvailableAmount() != moviesDB.get(args[0]).getTotalAmount())
                    ans[0] = "ERROR request remmovie failed";
                else {
                    BBServer.removeFromMoviesDB(args[0]);
                    ans = new String[2];
                    ans[1] = "BROADCAST movie \"" + args[0] + "\" removed";
                    ans[0] = "ACK remmovie \"" + args[0] + "\" success";
                }
            }
        }
    return ans;
    }
}
