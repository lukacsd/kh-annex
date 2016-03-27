package org.khannex.io;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BufferBuilder {
    private ByteBuffer buffer;

    public BufferBuilder( int capacity ) {
        this.buffer = ByteBuffer.allocate( capacity );
        this.buffer.order( getByteOrderSetting( ) );
    }

    public boolean isFull() {
        return buffer.remaining( ) == 0;
    }

    public BufferBuilder put( int value ) {
        buffer.putInt( value );
        return this;
    }

    public BufferBuilder put( byte value ) {
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
