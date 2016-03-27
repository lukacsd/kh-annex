package org.khannex.io;

import java.util.Base64;

public class ResponseBuilder {
    private String errorCode = "0";
    private byte[ ] errorText = "No error.".getBytes( );
    private byte[ ] result = "".getBytes( );

    public ResponseBuilder withResult( String result ) {
        return withResult( result.getBytes( ) );
    }

    public ResponseBuilder withResult( byte[ ] result ) {
        if ( result != null ) {
            this.result = result;
        }

        return this;
    }

    public Response build() {
        Response retval = new Response( );

        retval.setErrorCode( errorCode );
        retval.setErrorText( Base64.getEncoder( ).encodeToString( errorText ) );
        retval.setResult( Base64.getEncoder( ).encodeToString( result ) );

        return retval;
    }

}
