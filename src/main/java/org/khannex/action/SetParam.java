package org.khannex.action;

import org.khannex.io.Response;

public class SetParam extends Command {

    @Override
    public Response execute( Context context ) {
        return response( ).build( );
    }

}
