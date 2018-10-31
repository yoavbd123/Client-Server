/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.impl.bidi.Connections;

import java.io.Closeable;
import java.io.IOException;

/**
 * The ConnectionHandler interface for Message of type T
 */
public interface ConnectionHandler<T> extends Closeable {

    void send(T msg);
    void setConnections(Connections connections);
    void close() throws IOException;

    
}
