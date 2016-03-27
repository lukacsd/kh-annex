package org.khannex.action;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.khannex.io.CardIO;
import org.khannex.io.Response;

public class MakeSign extends Command {

    @Override
    public Response execute( Context context ) {
        Response retval = null;

        Optional<String> param = getNextParam( );
        if ( param.isPresent( ) ) {
            CardIO cardio = new CardIO( );
            try {
                byte[ ] challenge = DigestUtils.sha1( param.get( ) );
                byte[ ] pin = readPin( ).getBytes( );

                cardio.connect( );
                cardio.transmitReset( ).assertSuccessful( );
                cardio.transmitSelectDf( new byte[ ] { 0x50, 0x11 } ).assertSuccessful( );
                cardio.transmitSelectDf( new byte[ ] { ( byte ) 0x90, 0x02 } ).assertSuccessful( );
                cardio.transmitVerify( pin ).assertSuccessful( );
                cardio.transmitSelectPrivateKey( ).assertSuccessful( );
                byte[ ] payload = cardio.transmitSign( challenge ).assertSuccessful( ).getData( );

                String result = Base64.getEncoder( ).encodeToString( collate( challenge, payload ) );
                retval = response( ).withResult( result ).build( );
            } catch ( Exception ex ) {
                context.setException( ex );

                retval = response( ).build( );
            } finally {
                cardio.disconnect( );
            }
        }

        return retval;
    }

    private String readPin() throws IOException {
        return new String( Files.readAllBytes( Paths.get( System.getProperty( "user.home" ), ".khpass" ) ) );
    }

    /*
     * FIXME: this doesn't feel right. the static bytes are following a pattern
     * i saw in the official messages. i'm pretty sure though it is some rule
     * based format and not completely arbitrary .. some asn1 maybe?
     * anyhow, unless we are dealing with other than 20 byte hashes this should
     * work. in other cases, i would imagine the effect is that the server cannot
     * deserialize the response, discard it and deny the operation.
     */
    private byte[ ] collate( byte[ ] array1, byte[ ] array2 ) {
        byte[ ] retval = new byte[ 5 + array1.length + 3 + array2.length ];
        int offset = 0;
        retval[ offset++ ] = 0x30;
        retval[ offset++ ] = ( byte ) 0x81;
        retval[ offset++ ] = ( byte ) 0x99;
        retval[ offset++ ] = 0x04;
        retval[ offset++ ] = ( byte ) array1.length;
        System.arraycopy( array1, 0, retval, offset, array1.length );
        offset += array1.length;
        retval[ offset++ ] = 0x04;
        retval[ offset++ ] = ( byte ) 0x81;
        retval[ offset++ ] = ( byte ) array2.length;
        System.arraycopy( array2, 0, retval, offset, array2.length );
        return retval;
    }

}
