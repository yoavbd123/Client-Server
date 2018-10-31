package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.ClientsConnections;
import bgu.spl181.net.impl.bidi.Connections;
import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;



public class Register implements Command<String[]> {

    private int ClientId;

    public Register(int clientId) {
        ClientId = clientId;
    }

    @Override
    public String[] execute(String[] args) {


        // checks that the argument of the command is legal, that the user doesn't already exist in the data base
        // and that the client is not already logged in.
        // synced to prevent registering two users with the same username
        String[] ans = new String[1];
        synchronized (BBServer.getUserLock()) {
            if (args.length < 2 || args[0].length() == 0 || args[1].length() == 0 || BBServer.get_users().containsKey(args[0]) ||
                    BBServer.getLoggedClients().containsKey(ClientId) == true)
                ans[0] = "ERROR registration failed";
            else if (args.length == 2) {
                User userToAdd = new User(args[0], args[1]);
                BBServer.writeToUserDB(userToAdd);
                BBServer.addUser(userToAdd);
                ans[0] = "ACK registration succeeded";
            }
            else {
                User userToAdd = new User(args[0], args[1]);
                String country = args[2].substring(args[2].indexOf("\"") + 1, args[2].length() - 1);
                userToAdd.setCountry(country);
                //BBServer.writeToUserDB(userToAdd);
                BBServer.addUser(userToAdd);
                ans[0]  ="ACK registration succeeded";
            }

        }
        return ans;
    }
}