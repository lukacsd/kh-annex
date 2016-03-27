package org.khannex.action;

import org.khannex.io.Response;

public class SetDeviceType extends Command {

    @Override
    public Response execute( Context context ) {
        return response( ).withResult( "0" ).build( );
    }

}
