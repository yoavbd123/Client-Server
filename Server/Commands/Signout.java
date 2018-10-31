package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.ClientsConnections;
import bgu.spl181.net.impl.bidi.Connections;
import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocolImpl;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class Signout implements Command<String[] > {

    private int ClientId;
    private ClientsConnections Connections;
    private BidiMessagingProtocolImpl prot;

    public Signout(int clientid, Connections connections, BidiMessagingProtocolImpl protocol) {
        ClientId = clientid;
        Connections = (ClientsConnections) connections;
        prot = protocol;


    }

    @Override
    public String[] execute(String[] args) {
        ConcurrentHashMap LoggedClients = BBServer.getLoggedClients();
        String[] ans = new String[1];
        synchronized (BBServer.getLoggedClientsLock()) {
            if (!LoggedClients.containsKey(ClientId))
                ans[0] = "ERROR signout failed";
            else {
                BBServer.getLoggedClients().remove(ClientId);

                ans[0] = "ACK signout succeeded";
            }
        }

    return ans;
    }
}
