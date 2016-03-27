package org.khannex;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.khannex.action.Command;
import org.khannex.action.CommandFactory;
import org.khannex.action.Context;
import org.khannex.io.ChromeIO;
import org.khannex.io.Request;
import org.khannex.io.Response;

public class KhAnnex {
    private ChromeIO chromeio;
    private CommandFactory commandFactory;

    public KhAnnex( ChromeIO chromeio, CommandFactory commandFactory ) {
        this.chromeio = chromeio;
        this.commandFactory = commandFactory;
    }

    private void startSession() throws IOException {
        Request request = null;
        Context context = new Context( );

        while ( ( request = chromeio.getRequest( ) ) != null ) {
            Command command = commandFactory.createCommand( request );
            Response response = command.execute( context );

            chromeio.sendResponse( response );
        }
    }

    public static void main( String[ ] args ) throws IOException {
        Options options = new Options( );
        options.addOption( "h", "help", false, "Prints this message." );
        options.addOption( "dr", "dry-run", false, "Executes basic card operations to test setup." );

        try {
            CommandLine cl = new DefaultParser( ).parse( options, args );
            if ( cl.hasOption( "help" ) ) {
                new HelpFormatter( ).printHelp( "kh-annex",
                        "Smart card signing utility for K&H EBank. "
                                + "When executed without parameters, stdin will be parsed according to "
                                + "Google Chrome Native Messaging Host semantics.\n\n",
                        options, "" );
                System.exit( 0 );
            } else if ( cl.hasOption( "dry-run" ) ) {
                new DryRun( new CommandFactory( ) ).execute( );
                System.exit( 0 );
            }
        } catch ( ParseException e ) {
        }

        new KhAnnex( new ChromeIO( ), new CommandFactory( ) ).startSession( );
    }

}
