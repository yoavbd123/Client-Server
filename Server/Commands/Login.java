package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;


public class Login implements Command<String[]> {

    private int ClientId;

    public Login (int clientId){
        ClientId = clientId;

    }



    @Override
    public String[] execute(String[] args) {
        String user = args[0];
        String pass = args[1];
        HashMap<String,User> users = BBServer.get_users();
        ConcurrentHashMap<Integer, String> LoggedClients = BBServer.getLoggedClients();
        String ans[] = new String[1];


        /* Synced to avoid a condition in which 2 clients try to login on the same username at the same time
        since two threads can check all the conditions of the if, and go to the else statement and log on the same user
         */
        synchronized (BBServer.getLoggedClientsLock()) {

            /*
        Checks if client is already logged in, OR username already logged in OR username DNE in the database
        OR username exists but password is wrong
         */
            if (LoggedClients.containsKey(ClientId) || LoggedClients.containsValue(user)
                    || !users.containsKey(user) || (users.containsKey(user) && !users.get(user).getPassword().equals(pass)))
                ans[0] = "ERROR login failed";
                // adds the client id along with the username, to the logged in map
            else {
                BBServer.getLoggedClients().put(ClientId, user);
                ans[0] = "ACK login succeeded";
            }


        }

        return ans;

    }

}
