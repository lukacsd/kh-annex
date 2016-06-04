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

import java.util.Base64;

public class ResponseBuilder {

    private final String errorCode = "0";
    private final byte[] errorText = "No error.".getBytes();
    private byte[] result = "".getBytes();

    public ResponseBuilder withResult(String result) {
        return withResult(result.getBytes());
    }

    public ResponseBuilder withResult(byte[] result) {
        if (result != null) {
            this.result = result;
        }

        return this;
    }

    public Response build() {
        Response retval = new Response();

        retval.setErrorCode(errorCode);
        retval.setErrorText(Base64.getEncoder().encodeToString(errorText));
        retval.setResult(Base64.getEncoder().encodeToString(result));

        return retval;
    }

}
