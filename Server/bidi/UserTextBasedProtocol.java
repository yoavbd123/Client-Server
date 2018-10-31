package bgu.spl181.net.impl.bidi;


import java.io.Serializable;


// the UTBP layer, in which the message type is String
public abstract class UserTextBasedProtocol<T> implements BidiMessagingProtocol<T> {

    public abstract void process(String message);
    protected abstract Serializable register(String[] msg);
    protected abstract Serializable login(String[] msg);
    protected abstract Serializable signout(String[] msg);
    protected abstract Serializable request(String[] msg);




}
