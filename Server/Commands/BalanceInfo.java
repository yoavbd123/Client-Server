package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;

import java.io.Serializable;

public class BalanceInfo implements Command<String[]> {
    private int ConnectionId;

    public BalanceInfo(int connectionId) {
        ConnectionId = connectionId;
    }
    @Override
    public String[] execute(String[] args) {
        String username = BBServer.getLoggedClients().get(ConnectionId);
        String[] ans = new String[1];
        if (username == null)
            ans[0] = "ERROR request balance failed";
        else
            ans[0] = "ACK balance " + BBServer.get_users().get(username).getBalance();
        return ans;

    }
}
