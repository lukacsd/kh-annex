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
