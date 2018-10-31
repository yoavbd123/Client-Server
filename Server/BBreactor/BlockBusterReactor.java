package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.bidi.Connections;

import java.io.IOException;
import java.util.function.Supplier;

public class BlockBusterReactor extends Reactor implements BBServer {

    public BlockBusterReactor(int numThreads, int port, Supplier protocolFactory, Supplier readerFactory, Connections connections) {
        super(numThreads, port, protocolFactory, readerFactory, connections);


    }


}
