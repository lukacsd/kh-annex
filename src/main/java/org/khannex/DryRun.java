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
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.khannex.action.CommandFactory;
import org.khannex.action.Context;
import org.khannex.io.ByteBufferBuilder;
import org.khannex.io.Request;
import org.khannex.io.Response;

public class DryRun {

    private final CommandFactory commandFactory;

    public DryRun(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void execute() {
        printImplementationDetails();
        System.out.println();
        printVariables();
        System.out.println();
        executeCardCommands();
    }

    private void printImplementationDetails() {
        final Attributes manifest = getManifest();
        System.out.println(
                String.format("%s %s %s %s", manifest.getValue(Attributes.Name.IMPLEMENTATION_TITLE), manifest.getValue("Built-By"),
                        manifest.getValue(Attributes.Name.IMPLEMENTATION_VERSION), manifest.getValue("Build-Time")));
    }

    private void printVariables() {
        System.out.println(
                String.format("Java version:\t%s, %s", System.getProperty("java.version"), System.getProperty("java.vendor")));
        System.out.println(String.format("PC/SC impl:\t%s", System.getProperty("sun.security.smartcardio.library")));
        System.out.println(String.format("Log directory:\t%s", System.getProperty("org.khannex.logDir")));
        System.out.println(String.format("Endianness:\t%s", new ByteBufferBuilder(0).getByteOrderSetting()));
    }

    private void executeCardCommands() {
        final Context context = new Context();
        final Response getLocationResponse = commandFactory.createCommand(new Request("getLocation")).execute(context);
        if (!context.hasException()) {
            String locality = getLocationResponse.getResult();
            System.out.println(String.format("Locality:\t%s", locality));

            final Response signResponse = commandFactory.createCommand(new Request("makeSign", locality)).execute(context);
            if (!context.hasException()) {
                System.out.println(String.format("Signature:\t%s", signResponse.getResult()));
                return;
            }
        }

        System.out.println("Card comms error");
    }

    private Attributes getManifest() {
        final Attributes retval = new Attributes();

        try {
            final Enumeration<URL> list = getClass().getClassLoader().getResources(JarFile.MANIFEST_NAME);
            if (list.hasMoreElements()) {
                URL url = list.nextElement();
                Manifest manifest = new Manifest(url.openStream());
                retval.putAll(manifest.getMainAttributes());
            }
        } catch (IOException e) {
        }

        return retval;
    }

}
