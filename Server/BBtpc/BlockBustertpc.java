package bgu.spl181.net.impl.BBtpc;

import bgu.spl181.net.impl.bidi.MessageEncoderDecoder;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocol;
import bgu.spl181.net.impl.bidi.LineMessageEncoderDecoder;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocolImpl;

import java.io.IOException;


import java.util.function.Supplier;

public class BlockBustertpc extends BidiBaseServer implements BBServer {


    public BlockBustertpc(int port, Supplier<BidiMessagingProtocol<String>> protocolFactory, Supplier<MessageEncoderDecoder<String>> encdecFactory) {
        super(port, protocolFactory, encdecFactory);
    }


    protected void execute(BidiBlockingConnectionHandler handler) {
        handler.setConnections(connections);
        new Thread(handler).start();
    }

    @Override
    public void serve() {
       super.serve();
    }

    @Override
    public void close() throws IOException {

    }
}


