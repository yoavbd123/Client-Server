package bgu.spl181.net.impl.BBreactor;

import bgu.spl181.net.impl.BBtpc.BlockBustertpc;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl181.net.impl.bidi.LineMessageEncoderDecoder;

public class ReactorMain {
    public static void main(String[] args){
        BBServer BB = new BlockBusterReactor(5, Integer.valueOf(args[0]),()-> new BidiMessagingProtocolImpl(),()-> new LineMessageEncoderDecoder()
        , BBServer.getConnections());
        BB.serve();
    }
}
