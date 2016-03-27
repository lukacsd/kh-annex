package org.khannex.action;

import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.khannex.io.Response;
import org.khannex.io.ResponseBuilder;

public abstract class Command {
    private List<String> params;
    private int paramPosition;

    public Command( ) {
        params = new LinkedList<>( );
    }

    public abstract Response execute( Context context );

    public void addParam( String value ) {
        if ( value != null && !value.trim( ).isEmpty( ) ) {
            if ( org.apache.commons.codec.binary.Base64.isBase64( value.getBytes( ) ) ) {
                params.add( new String( Base64.getDecoder( ).decode( value ) ) );
            } else {
                params.add( value );
            }
        }
    }

    protected Optional<String> getNextParam() {
        if ( paramPosition + 1 <= params.size( ) ) {
            return Optional.of( params.get( paramPosition++ ) );
        }

        return Optional.empty( );
    }

    protected ResponseBuilder response() {
        return new ResponseBuilder( );
    }

}
