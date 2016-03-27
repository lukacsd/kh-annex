package org.khannex.io;

public class Request {
    private String command;
    private String param1;
    private String param2;

    public Request( ) {
    }

    public Request( String command ) {
        this( command, null, null );
    }

    public Request( String command, String param1 ) {
        this( command, param1, null );
    }

    public Request( String command, String param1, String param2 ) {
        this.command = command;
        this.param1 = param1;
        this.param2 = param2;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand( String command ) {
        this.command = command;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1( String param1 ) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2( String param2 ) {
        this.param2 = param2;
    }

    @Override
    public String toString() {
        return command + "( \"" + param1 + "\", \"" + param2 + "\" )";
    }

}
