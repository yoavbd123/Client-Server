package bgu.spl181.net.impl.bidi;


import org.omg.CORBA.ObjectHelper;

import java.util.concurrent.ConcurrentHashMap;

public interface Connections<T> {



    boolean send(int connectionId, T msg);

    void broadcast(T msg);

    void disconnect(int connectionId);


    //ADDED FUNCS
    ConcurrentHashMap<Integer, ConnectionHandler> getConnections();
    int addConnection(ConnectionHandler connection);
    Object getLockConnections();




}