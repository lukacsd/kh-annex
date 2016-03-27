package org.khannex;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.khannex.action.CommandFactory;
import org.khannex.action.Context;
import org.khannex.io.BufferBuilder;
import org.khannex.io.Request;
import org.khannex.io.Response;

public class DryRun {
    private CommandFactory commandFactory;

    public DryRun( CommandFactory commandFactory ) {
        this.commandFactory = commandFactory;
    }

    public void execute() {
        printImplementationDetails( );
        System.out.println( );
        printVariables( );
        System.out.println( );
        executeCardCommands( );
    }

    private void printImplementationDetails() {
        Attributes manifest = getManifest( );
        System.out.println(
                String.format( "%s %s %s %s", manifest.getValue( Attributes.Name.IMPLEMENTATION_TITLE ), manifest.getValue( "Built-By" ),
                        manifest.getValue( Attributes.Name.IMPLEMENTATION_VERSION ), manifest.getValue( "Build-Time" ) ) );
    }

    private void printVariables() {
        System.out.println(
                String.format( "Java version:\t%s, %s", System.getProperty( "java.version" ), System.getProperty( "java.vendor" ) ) );
        System.out.println( String.format( "PC/SC impl:\t%s", System.getProperty( "sun.security.smartcardio.library" ) ) );
        System.out.println( String.format( "Log directory:\t%s", System.getProperty( "org.khannex.logDir" ) ) );
        System.out.println( String.format( "Endianness:\t%s", new BufferBuilder( 0 ).getByteOrderSetting( ) ) );
    }

    private void executeCardCommands() {
        Context context = new Context( );
        Response getLocationResponse = commandFactory.createCommand( new Request( "getLocation" ) ).execute( context );
        if ( !context.hasException( ) ) {
            String locality = getLocationResponse.getResult( );
            System.out.println( String.format( "Cert locality:\t%s", locality ) );

            Response signResponse = commandFactory.createCommand( new Request( "makeSign", locality ) ).execute( context );
            if ( !context.hasException( ) ) {
                System.out.println( String.format( "Signed hash:\t%s", signResponse.getResult( ) ) );
                return;
            }
        }

        System.out.println( "Card comms error" );
    }

    private Attributes getManifest() {
        Attributes retval = new Attributes( );

        try {
            Enumeration<URL> list = getClass( ).getClassLoader( ).getResources( JarFile.MANIFEST_NAME );
            if ( list.hasMoreElements( ) ) {
                URL url = list.nextElement( );
                Manifest manifest = new Manifest( url.openStream( ) );
                retval.putAll( manifest.getMainAttributes( ) );
            }
        } catch ( IOException e ) {
        }

        return retval;
    }

}
