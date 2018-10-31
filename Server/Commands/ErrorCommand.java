package bgu.spl181.net.impl.Commands;

import bgu.spl181.net.impl.bidi.Command;

import java.io.Serializable;

public class ErrorCommand implements Command<String[]> {


    @Override
    public String[] execute(String[] arg) {
        String[] ans = new String[1];
        ans[0] = "ERROR invalid input";
        return ans;
    }
}
