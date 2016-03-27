package org.khannex.action;

import org.khannex.io.Response;

public class getNonstandardChars extends Command {

    @Override
    public Response execute( Context context ) {
        return response( ).build( );
    }

}
