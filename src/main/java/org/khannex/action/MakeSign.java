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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.khannex.io.CardIO;
import org.khannex.io.Response;

import openjdk7.BerEncoder;

public class MakeSign extends Command {

    @Override
    public Response execute( Context context ) {
        Response retval = null;

        Optional<String> param = getNextParam( );
        if ( param.isPresent( ) ) {
            CardIO cardio = new CardIO( );
            try {
                byte[ ] digest = DigestUtils.sha1( param.get( ) );
                byte[ ] pin = readPin( ).getBytes( );

                cardio.connect( );
                cardio.transmitReset( ).assertSuccessful( );
                cardio.transmitSelectDf( new byte[ ] { 0x50, 0x11 } ).assertSuccessful( );
                cardio.transmitSelectDf( new byte[ ] { ( byte ) 0x90, 0x02 } ).assertSuccessful( );
                cardio.transmitVerify( pin ).assertSuccessful( );
                cardio.transmitSelectPrivateKey( ).assertSuccessful( );
                byte[ ] signature = cardio.transmitSign( digest ).assertSuccessful( ).getData( );

                String result = Base64.getEncoder( ).encodeToString( getBerEncodedResult( digest, signature ) );
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
     * The result is encoded according to the Basic Encoding Rules for ASN.1
     * Schematic representation as follows:
     * 
     *   SignResult ::= SEQUENCE {
     *     digest OCTET STRING,
     *     signature OCTET STRING
     *   }
     */
    private byte[ ] getBerEncodedResult( byte[ ] digest, byte[ ] signature ) {
        byte[ ] arr = new byte[ 64 + digest.length + signature.length ];
        BerEncoder encoder = new BerEncoder( arr );
        encoder.openSequence( );
        encoder.putOctetString( signature );
        encoder.putOctetString( digest );
        encoder.closeSequence( );
        int length = encoder.trim( );
        byte[ ] retval = new byte[ length ];
        System.arraycopy( arr, 0, retval, 0, length );
        return retval;
    }

}
