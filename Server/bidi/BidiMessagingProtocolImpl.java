package bgu.spl181.net.impl.bidi;

import bgu.spl181.net.impl.bidi.Connections;
import bgu.spl181.net.impl.bidi.UserTextBasedProtocol;
import bgu.spl181.net.impl.bidi.Command;
import bgu.spl181.net.impl.Commands.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


public class BidiMessagingProtocolImpl extends UserTextBasedProtocol<String> {

    public int getConnectionId() {
        return ConnectionId;
    }

    private int ConnectionId;
    private Connections connections;
    private boolean shouldterminate = false;

    @Override
    public void start(int connectionId, Connections connections) {
        ConnectionId = connectionId;
        this.connections = connections;

    }
    void broadcast(String msg) {
        //synchronized (connections.getLockConnections()) {
        synchronized (BBServer.getLoggedClientsLock()){
            for (int ConnectionId : BBServer.getLoggedClients().keySet()) {
                connections.send(ConnectionId, msg);
            }
        }
    }

    @Override
    /*
    parses the message and checks which command it is
     */
    public void process(String message) {

        String[] splitMsg = message.split(" ");
        int length = splitMsg.length;

        Command cmd;
        Serializable result;

        switch (splitMsg[0]) {
            case "REGISTER": {
                result = this.register(Arrays.copyOfRange(splitMsg, 1, length));
                break;
            }
            case "LOGIN": {
                result = this.login(Arrays.copyOfRange(splitMsg, 1, length));
                break;
            }
            case "SIGNOUT": {
                result = this.signout(splitMsg);
                break;
            }
            case "REQUEST": {
                result = this.request(splitMsg);
                break;
            }
            default:
                result = new ErrorCommand().execute(null);
                break;
        }


        String[] res = (String[]) result;
        connections.send(ConnectionId,res[0]);
        // if the array is of length 2, it means it has a broadcast
        if(res.length == 2){
            this.broadcast(res[1]);
            //connections.broadcast(res[1]);
        }

        if (res[0].equals("ACK signout succeeded")){
            this.terminate();
            connections.disconnect(ConnectionId);

        }

    }

    @Override
    protected Serializable register(String[] msg) {
        Command cmd = new Register(ConnectionId);

        msg = parseArgs(msg);

        return cmd.execute(msg);
    }

    @Override
    protected Serializable login(String[] msg) {
        Command cmd = new Login(ConnectionId);
        msg = parseArgs(msg);
        return cmd.execute(msg);
    }

    @Override
    protected Serializable signout(String[] msg) {
        Command cmd = new Signout(ConnectionId, connections,this);
        msg = parseArgs(msg);
        return cmd.execute(msg);
    }

    @Override
    // request has extra parsing
    protected Serializable request(String[] msg) {
        int length = msg.length;
        Command cmd = null;
        int BeginTrim = 0;
        if (length > 1) {
            switch (msg[1]) {
                case "balance": {
                    switch (msg[2]) {
                        case "info": {
                            cmd = new BalanceInfo(ConnectionId);
                            BeginTrim = 3;
                            break;
                        }
                        case "add":
                            cmd = new BalanceAdd(ConnectionId);
                            BeginTrim = 3;
                            break;
                    }
                    break;
                }
                case "info":
                    cmd = new Info(ConnectionId);
                    BeginTrim = 2;
                    break;
                case "rent":
                    cmd = new Rent(ConnectionId);
                    BeginTrim = 2;
                    break;
                case "return":
                    cmd = new Return(ConnectionId);
                    BeginTrim = 2;
                    break;
                case "addmovie":
                    cmd = new AddMovie(ConnectionId);
                    BeginTrim = 2;
                    break;
                case "remmovie":
                    cmd = new RemoveMovie(ConnectionId);
                    BeginTrim = 2;
                    break;
                case "changeprice":
                    cmd = new ChangePrice(ConnectionId);
                    BeginTrim = 2;
                    break;

            }

        }

        msg = Arrays.copyOfRange(msg,BeginTrim,length);
        msg = parseArgs(msg);
        return cmd.execute(msg);
    }

    @Override
    public boolean shouldTerminate() {
        return shouldterminate;
    }

    //gets a string[] with parameters and '"' in them..
    //returns string[] without "" and unites names into one index
    private static String[] parseArgs(String[] args){
        String[] ans;
        ans = new String[args.length];
        int i=0;
        for (int j=0;j<args.length;j++){
            if ((args[j].charAt(0)=='"' && args[j].charAt(args[j].length()-1)=='"'))
                ans[j]=args[j].substring(1,args[j].length()-1);

            else if ((args[j].charAt(0)=='"')||args[j].startsWith("country")) {
                ans[j] = args[j];
                int p = j+1;
                for (; p < args.length; p++) {
                    ans[j] = ans[j] +" "+ args[p];
                    if(args[p].charAt(args[p].length() - 1) == '"') {
                        j = p;
                        break;
                    }
                }
            }
            else
                ans[j] = args[j];
        }
        //trim the string[] and remove " from its items.
        ArrayList<String> list = new ArrayList<String>();
        for (String s : ans) {
            if (s != null && !s.equals("")){
                if (s.charAt(0) == '"' && s.charAt(s.length()-1) == '"')
                    s = s.substring(1, s.length() - 1);
                list.add(s);
            }

        }

        ans = list.toArray(new String[list.size()]);

        return ans;
    }

    public void terminate() {
        shouldterminate = true;
    }
}
