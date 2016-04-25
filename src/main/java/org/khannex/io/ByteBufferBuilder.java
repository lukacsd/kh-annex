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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferBuilder {
    private ByteBuffer buffer;

    public ByteBufferBuilder( int capacity ) {
        this.buffer = ByteBuffer.allocate( capacity );
        this.buffer.order( getByteOrderSetting( ) );
    }

    public boolean isFull() {
        return buffer.remaining( ) == 0;
    }

    public ByteBufferBuilder put( int value ) {
        buffer.putInt( value );
        return this;
    }

    public ByteBufferBuilder put( byte value ) {
        buffer.put( value );
        return this;
    }

    public int buildInt() {
        buffer.position( 0 );
        return buffer.getInt( );
    }

    public byte[ ] buildBytes() {
        buffer.position( 0 );
        return buffer.array( );
    }

    public ByteOrder getByteOrderSetting() {
        ByteOrder retval = ByteOrder.LITTLE_ENDIAN;

        try {
            if ( "BIG_ENDIAN".equalsIgnoreCase( System.getProperty( "org.khannex.io.endianness" ) ) ) {
                retval = ByteOrder.BIG_ENDIAN;
            }
        } catch ( Exception ex ) {
        }

        return retval;
    }

}
