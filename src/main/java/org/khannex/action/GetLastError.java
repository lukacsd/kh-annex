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

import org.khannex.io.Response;

public class GetLastError extends Command {

    @Override
    public Response execute( Context context ) {
        Response retval = null;

        if ( context.hasException( ) ) {
            retval = response( ).withResult( "-14" ).build( );
        } else {
            retval = response( ).withResult( "1" ).build( );
        }

        context.clear( );

        return retval;
    }

}
