package bgu.spl181.net.impl.bidi;

import java.io.Serializable;

public interface Command<T> extends Serializable {

    Serializable execute(T arg);
}