package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.MovieInDB;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;
import java.util.Arrays;

public class AddMovie implements Command<String []> {
    private final int ConnectionId;

    public AddMovie(int connectionId) {
        ConnectionId = connectionId;
    }
    @Override
    public String[] execute(String[] args)
    {
        String username = BBServer.getLoggedClients().get(ConnectionId);
        User user = BBServer.get_users().get(username);
        String[] ans = new String[1];

        //checks that the client is logged in, and is an admin
        if(user == null || !user.getType().equals("admin"))
            ans[0] = "ERROR request addmovie failed";
        // checks that the movie doesn't already exist in the DB
        else if(BBServer.get_movies().containsKey(args[0]))
            ans[0] = "ERROR request addmovie failed";
        // checks that the # of copies and the price is positive
        else if(Integer.valueOf(args[1])<1 || Integer.valueOf(args[2])<1)
            ans[0] = "ERROR request addmovie failed";

        else {
        // synced to avoid writing problems to the DB
        synchronized (BBServer.getMoviesLock()) {
            MovieInDB toAdd = new MovieInDB(args[0], Integer.valueOf(args[2]),
                    Arrays.copyOfRange(args, 3, args.length), Integer.valueOf(args[1]), Integer.valueOf(args[1]));
            BBServer.addMovie(toAdd);
            ans = new String[2];
            ans[1] = "BROADCAST movie \"" + toAdd.getName() + "\" "
                    + toAdd.getAvailableAmount() + " " + toAdd.getPrice();
            ans[0] = "ACK addmovie \"" + args[0] + "\" success";
        }
        }
        return ans;

    }
}
