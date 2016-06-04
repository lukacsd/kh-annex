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

import java.io.IOException;
import java.io.InputStreamReader;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChromeIO {

    private static final Logger LOG = LoggerFactory.getLogger(ChromeIO.class);

    private final InputStreamReader stream;
    private final ObjectMapper mapper;

    public ChromeIO() {
        this.stream = new InputStreamReader(System.in);
        this.mapper = new ObjectMapper();
    }

    public Request getRequest() throws IOException {
        String msg = getMessage();

        LOG.debug(String.format("in=[%s]", msg));

        return fromJson(msg, Request.class);
    }

    public void sendResponse(Response response) throws IOException {
        String msg = toJson(response);

        LOG.debug(String.format("out=[%s]", msg));

        sendMessage(msg);
    }

    private String getMessage() throws IOException {
        ByteBufferBuilder buffer = new ByteBufferBuilder(4);
        int input;
        while ((input = stream.read()) != -1) {
            buffer.put((byte) input);

            if (buffer.isFull()) {
                int inputLength = buffer.buildInt();
                char[] chars = new char[inputLength];
                stream.read(chars);

                return String.valueOf(chars);
            }
        }

        return null;
    }

    private <T> T fromJson(String json, Class<T> type) throws IOException {
        if (json != null && !json.isEmpty()) {
            return mapper.readValue(json, type);
        }

        return null;
    }

    private void sendMessage(String message) throws IOException {
        System.out.write(encodeMessageBytes(message));
    }

    private byte[] encodeMessageBytes(String message) throws IOException {
        byte[] msg = message.getBytes();
        byte[] length = new ByteBufferBuilder(4).put(msg.length).buildBytes();

        byte[] retval = new byte[length.length + msg.length];
        System.arraycopy(length, 0, retval, 0, 4);
        System.arraycopy(msg, 0, retval, 4, msg.length);

        return retval;
    }

    private String toJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

}
