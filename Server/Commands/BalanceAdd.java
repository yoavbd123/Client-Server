package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.Objects.User;

import java.io.Serializable;

public class BalanceAdd implements Command<String[]> {

    private int ConnectionId;

    public BalanceAdd(int connectionId) {
        ConnectionId = connectionId;
    }
    @Override
    public String[] execute(String[] args) {
        String[] ans = new String[1];
        //checks if the client is logged in and if the amount to add is non-negative
        String username = BBServer.getLoggedClients().get(ConnectionId);
        if (username == null || Integer.parseInt(args[0]) <= 0) ans[0] = "ERROR request balance failed";
        // adds the input amount to the user balance

        else{
            synchronized (BBServer.getUserLock()){
                User user = BBServer.get_users().get(username);
                user.setBalance(user.getBalance() + Integer.parseInt(args[0]));
                BBServer.addUser(user);

                ans[0] = "ACK balance " + user.getBalance() + " added " + args[0];
            }
        }
        return ans;


    }
}
