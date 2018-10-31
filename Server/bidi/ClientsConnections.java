package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.impl.BBreactor.BidiNonBlockingConnectionHandler;
import org.omg.CORBA.ObjectHelper;

import java.util.concurrent.ConcurrentHashMap;

public class ClientsConnections implements Connections<String> {
    private final Object LockConnections = new Object();
    private static Integer Counter = 1;
    private ConcurrentHashMap<Integer,ConnectionHandler> connections; //<ConnectionId, ConnectionHandler>
    public Object getLockConnections(){return LockConnections;}
    public ConcurrentHashMap<Integer,ConnectionHandler> getConnections() {
        return connections;
    }

    @Override
    // adds a client to the connections
    public int addConnection(ConnectionHandler connection) {
        connections.put(Counter,connection);
        return Counter++;
    }

    public ClientsConnections (){
        connections = new ConcurrentHashMap();
    }


    public boolean send(int connectionId, String msg) {

            ConnectionHandler connectionHandler = connections.get(connectionId);
                if (connectionHandler == null) {
                    return false;
                } else {
                    connectionHandler.send(msg);
                    return true;
                }

        }



    /**
     * sync to prevent items from connections to be deleted while iterating
     */
    @Override
    public void broadcast(String msg) {         //broadcast to all handlers
        synchronized (this.LockConnections){
            for(int client : connections.keySet()){
                send(client,msg);
            }
        }


    }
    /**
     * sync LockConnections to prevent from CH to be deleted by SIGNOUT while sending a msg.
     *
     */
    @Override
    public void disconnect(int connectionId) {
        synchronized (this.LockConnections) {
            connections.remove(connectionId);
        }
    }

}
