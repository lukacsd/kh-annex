/*
 * Copyright 2016 David Lukacs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
