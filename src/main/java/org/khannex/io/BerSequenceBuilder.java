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

import java.util.LinkedList;
import java.util.List;

import com.sun.jmx.snmp.BerEncoder;

@SuppressWarnings( "restriction" )
public class BerSequenceBuilder {
    private int dataLength = 0;
    private List<byte[ ]> octetStrings = new LinkedList<byte[ ]>( );

    public BerSequenceBuilder withOctetString( byte[ ] value ) {
        dataLength += value.length;
        octetStrings.add( 0, value );

        return this;
    }

    public byte[ ] build() {
        byte[ ] wrk = new byte[ 16 + dataLength ];

        BerEncoder enc = new BerEncoder( wrk );
        enc.openSequence( );
        for ( byte[ ] ocs : octetStrings ) {
            enc.putOctetString( ocs );
        }
        enc.closeSequence( );
        int length = enc.trim( );
        byte[ ] retval = new byte[ length ];
        System.arraycopy( wrk, 0, retval, 0, length );

        return retval;
    }

}
