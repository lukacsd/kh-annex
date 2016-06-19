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
package org.khannex.io;

import java.util.Arrays;
import java.util.List;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

@SuppressWarnings("restriction")
public class CardIO {

    private static final byte[] RESET = {0x00, (byte) 0xA4, 0x04, 0x0C, 0x10, (byte) 0xA0, 0x00, 0x00, 0x00, 0x77, 0x01, 0x03, 0x03,
        0x00, 0x00, 0x00, (byte) 0xF1, 0x00, 0x00, 0x00, 0x02};
    private static final byte[] SELECT = {0x00, (byte) 0xA4, 0x01, 0x0C, 0x02};
    private static final byte[] SELECT_CERT = {0x00, (byte) 0xA4, 0x02, 0x00, 0x02, 0x20, 0x00};
    private static final byte[] SELECT_PRIVATE_KEY = {0x00, 0x22, 0x41, (byte) 0xB6, 0x07, (byte) 0x80, 0x01, 0x11, (byte) 0x81,
        0x02, 0x30, 0x00};
    private static final byte[] VERIFY = {0x00, 0x20, 0x00, (byte) 0x81, 0x40};
    private static final byte[] SIGN = {0x00, 0x2A, (byte) 0x9E, (byte) 0x9A, 0x14};

    private Card card;
    private CardChannel channel;

    public void connect() {
        try {
            final TerminalFactory factory = TerminalFactory.getDefault();
            final List<CardTerminal> terminals = factory.terminals().list();
            if (terminals.size() > 0) {
                card = terminals.get(0).connect("*");
                channel = card.getBasicChannel();
            } else {
                throw new RuntimeException("No terminals");
            }
        } catch (CardException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        if (card != null) {
            try {
                card.disconnect(false);
            } catch (CardException e) {
            }
        }
    }

    public CardResponse transmitReset() {
        return transmit(RESET);
    }

    public CardResponse transmitSelectDf(byte[] df) {
        final byte[] apdu = new byte[SELECT.length + df.length];
        System.arraycopy(SELECT, 0, apdu, 0, SELECT.length);
        System.arraycopy(df, 0, apdu, SELECT.length, df.length);

        return transmit(apdu);
    }

    public CardResponse transmitSelectCertificate() {
        return transmit(SELECT_CERT);
    }

    public CardResponse transmitSelectPrivateKey() {
        return transmit(SELECT_PRIVATE_KEY);
    }

    public CardResponse transmitVerify(byte[] pin) {
        final byte[] paddedPin = new byte[64];
        final byte[] apdu = new byte[VERIFY.length + paddedPin.length];

        System.arraycopy(VERIFY, 0, apdu, 0, VERIFY.length);
        Arrays.fill(paddedPin, (byte) 0xFF);
        System.arraycopy(pin, 0, paddedPin, 0, pin.length);
        System.arraycopy(paddedPin, 0, apdu, VERIFY.length, paddedPin.length);

        return transmit(apdu);
    }

    public CardResponse transmitSign(byte[] value) {
        final byte[] apdu = new byte[SIGN.length + value.length];

        System.arraycopy(SIGN, 0, apdu, 0, SIGN.length);
        System.arraycopy(value, 0, apdu, SIGN.length, value.length);

        return transmit(apdu);
    }

    public CardResponse transmitGetBinary() {
        byte[] retval = new byte[0];

        CardResponse response;
        do {
            response = transmit(new byte[]{0x00, (byte) 0xB0, (byte) (retval.length >> 8), (byte) (retval.length & 0xFF),
                (byte) 0xFF});
            retval = response.assertSuccessful().appendDataTo(retval);
        } while (0xFF == response.getDataSize());

        return new CardResponse(0x90, 0x00, retval);
    }

    protected CardResponse transmit(byte[] reset) {
        try {
            return new CardResponse(channel.transmit(new CommandAPDU(reset)));
        } catch (CardException e) {
            throw new RuntimeException(e);
        }
    }

    public class CardResponse {

        private final int sw1;
        private final int sw2;
        private final byte[] data;

        public CardResponse(ResponseAPDU response) {
            this.sw1 = response.getSW1();
            this.sw2 = response.getSW2();
            this.data = response.getData();
        }

        public CardResponse(int sw1, int sw2, byte[] data) {
            this.sw1 = sw1;
            this.sw2 = sw2;
            this.data = data;
        }

        public byte[] getData() {
            return data;
        }

        public int getDataSize() {
            return data.length;
        }

        public CardResponse assertSuccessful() {
            if (sw1 != 0x90 && sw2 != 0x00) {
                throw new IllegalStateException();
            }

            return this;
        }

        public byte[] appendDataTo(byte[] appendTo) {
            final byte[] retval = new byte[appendTo.length + getData().length];

            System.arraycopy(appendTo, 0, retval, 0, appendTo.length);
            System.arraycopy(getData(), 0, retval, appendTo.length, getData().length);

            return retval;
        }
    }

}
