package org.khannex.action;

import org.khannex.io.Response;

public class GetDeviceType extends Command {

    @Override
    public Response execute( Context context ) {
        return response( ).withResult( "CARD" ).build( );
    }

}
