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
package org.khannex.action;

import org.khannex.io.Request;

public class CommandFactory {

    public Command createCommand(Request request) {
        Command retval = null;

        if (request.getCommand() != null) {
            switch (request.getCommand()) {
                case "setParam":
                    retval = new SetParam();
                    break;
                case "getLocation":
                    retval = new GetLocation();
                    break;
                case "setDeviceType":
                    retval = new SetDeviceType();
                    break;
                case "getDeviceType":
                    retval = new GetDeviceType();
                    break;
                case "makeSign":
                    retval = new MakeSign();
                    retval.addParam(request.getParam1());
                    break;
                case "getLastError":
                    retval = new GetLastError();
                    break;
                case "getNonstandardChars":
                    retval = new GetNonstandardChars();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("unknown command [%s]", request.getCommand()));
            }
        }

        return retval;
    }

}
