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

    private final ChromeIO chromeio;
    private final CommandFactory commandFactory;

    public KhAnnex(ChromeIO chromeio, CommandFactory commandFactory) {
        this.chromeio = chromeio;
        this.commandFactory = commandFactory;
    }

    private void startSession() throws IOException {
        Request request;
        final Context context = new Context();

        while ((request = chromeio.getRequest()) != null) {
            final Command command = commandFactory.createCommand(request);
            final Response response = command.execute(context);

            chromeio.sendResponse(response);
        }
    }

    public static void main(String[] args) throws IOException {
        final Options options = new Options();
        options.addOption("h", "help", false, "Prints this message.");
        options.addOption("dr", "dry-run", false, "Executes basic card operations to test setup.");

        try {
            final CommandLine cl = new DefaultParser().parse(options, args);
            if (cl.hasOption("help")) {
                new HelpFormatter().printHelp("kh-annex",
                        "Smart card signing utility for K&H e-bank. "
                        + "When executed without parameters, stdin will be parsed according to "
                        + "Google Chrome Native Messaging Host semantics.\n\n",
                        options, "");
                System.exit(0);
            } else if (cl.hasOption("dry-run")) {
                new DryRun(new CommandFactory()).execute();
                System.exit(0);
            }
        } catch (ParseException e) {
        }

        new KhAnnex(new ChromeIO(), new CommandFactory()).startSession();
    }

}
