package bgu.spl181.net.impl.BBtpc;
import bgu.spl181.net.impl.bidi.BBServer;
import bgu.spl181.net.impl.bidi.BidiMessagingProtocolImpl;
import bgu.spl181.net.impl.bidi.LineMessageEncoderDecoder;

public class TPCMain {

    public static void main(String[] args) {
        BBServer BB = new BlockBustertpc(Integer.valueOf(args[0]),()->new BidiMessagingProtocolImpl(), ()-> new LineMessageEncoderDecoder());
        BB.serve();

    }
}
