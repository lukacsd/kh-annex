package org.khannex.action;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.khannex.io.CardIO;
import org.khannex.io.Response;

public class GetLocation extends Command {

    @Override
    public Response execute( Context context ) {
        Response retval = null;

        CardIO cardio = new CardIO( );
        try {
            cardio.connect( );
            cardio.transmitReset( ).assertSuccessful( );
            cardio.transmitSelectDf( new byte[ ] { 0x50, 0x11 } ).assertSuccessful( );
            cardio.transmitSelectDf( new byte[ ] { ( byte ) 0x90, 0x01 } ).assertSuccessful( );
            cardio.transmitSelectCertificate( ).assertSuccessful( );
            byte[ ] payload = cardio.transmitGetBinary( ).assertSuccessful( ).getData( );

            retval = response( ).withResult( getCertificateLocality( payload ) ).build( );
        } catch ( Exception ex ) {
            context.setException( ex );

            retval = response( ).build( );
        } finally {
            cardio.disconnect( );
        }

        return retval;
    }

    private String getCertificateLocality( byte[ ] cert ) throws CertificateException, InvalidNameException {
        String retval = null;

        CertificateFactory factory = CertificateFactory.getInstance( "X509" );
        X509Certificate x509 = ( X509Certificate ) factory.generateCertificate( new ByteArrayInputStream( cert ) );
        LdapName subject = new LdapName( x509.getSubjectDN( ).toString( ) );
        for ( Rdn rdn : subject.getRdns( ) ) {
            if ( "L".equalsIgnoreCase( rdn.getType( ) ) ) {
                retval = rdn.getValue( ).toString( );
                break;
            }
        }

        return retval;
    }

}
