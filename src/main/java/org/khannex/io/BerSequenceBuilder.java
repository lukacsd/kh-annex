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

public class BerSequenceBuilder {
    private List<BerType> octetStrings = new LinkedList<>( );

    public BerSequenceBuilder withOctetString( byte[ ] value ) {
        octetStrings.add( new OctetString( value ) );
        return this;
    }

    public byte[ ] build() {
        return new Sequence( octetStrings ).getBerValue( );
    }

    public static abstract class BerType {
        protected abstract byte[ ] getType();

        protected abstract byte[ ] getBytes();

        public byte[ ] getBerValue() {
            byte[ ] type = getType( );
            byte[ ] bytes = getBytes( );
            byte[ ] berLength = getBerLength( bytes );
            byte[ ] retval = new byte[ type.length + berLength.length + bytes.length ];

            int position = 0;
            System.arraycopy( type, 0, retval, position, type.length );
            position += type.length;
            System.arraycopy( berLength, 0, retval, position, berLength.length );
            position += berLength.length;
            System.arraycopy( bytes, 0, retval, position, bytes.length );

            return retval;
        }

        private byte[ ] getBerLength( byte[ ] value ) {
            byte[ ] length = null;

            if ( value.length < 128 ) {
                length = new byte[ ] { ( byte ) value.length };
            } else {
                int shift = 1;
                int octetCount = 0;
                do {
                    shift = value.length >> 8 * ++octetCount;
                } while ( shift != 0 && shift != value.length );

                length = new byte[ octetCount + 1 ];
                length[ 0 ] = ( byte ) ( 128 + octetCount );
                for ( int i = octetCount - 1; i >= 0; i-- ) {
                    length[ octetCount - i ] = ( byte ) ( value.length >> 8 * i );
                }
            }

            return length;
        }
    }

    public static class OctetString extends BerType {
        private byte[ ] bytes;

        public OctetString( byte[ ] bytes ) {
            if ( bytes == null || bytes.length == 0 ) {
                throw new IllegalArgumentException( "empty string is not allowed" );
            }

            this.bytes = bytes;
        }

        @Override
        public byte[ ] getType() {
            return new byte[ ] { 4 };
        }

        @Override
        public byte[ ] getBytes() {
            return bytes;
        }
    }

    public static class Sequence extends BerType {
        private byte[ ] bytes;

        public Sequence( List<BerType> types ) {
            if ( types == null || types.isEmpty( ) ) {
                throw new IllegalArgumentException( "empty sequence is not allowed" );
            }

            int byteCount = 0;
            List<byte[ ]> berTags = new LinkedList<byte[ ]>( );
            for ( BerType type : types ) {
                byte[ ] berValue = type.getBerValue( );
                byteCount += berValue.length;
                berTags.add( berValue );
            }

            int position = 0;
            byte[ ] bytes = new byte[ byteCount ];
            for ( byte[ ] ber : berTags ) {
                System.arraycopy( ber, 0, bytes, position, ber.length );
                position += ber.length;
            }

            this.bytes = bytes;
        }

        @Override
        protected byte[ ] getType() {
            return new byte[ ] { 0x30 };
        }

        @Override
        protected byte[ ] getBytes() {
            return bytes;
        }
    }

}
