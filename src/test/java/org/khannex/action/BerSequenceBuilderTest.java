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
package org.khannex.action;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.khannex.io.BerSequenceBuilder.BerType;
import org.khannex.io.BerSequenceBuilder.OctetString;
import org.khannex.io.BerSequenceBuilder.Sequence;

public class BerSequenceBuilderTest {

    @Test
    public void shouldEncodeShortFormLength_1() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 1 );

        Assert.assertArrayEquals( new byte[ ] { 66, 1, 76 }, tst.getBerValue( ) );
    }

    @Test
    public void shouldEncodeShortFormLength_127() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 127 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 129, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 127, 76 }, Arrays.copyOf( berValue, 3 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_128() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 128 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 131, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 129, ( byte ) 128, 76 }, Arrays.copyOf( berValue, 4 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_255() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 255 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 258, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 129, ( byte ) 255, 76 }, Arrays.copyOf( berValue, 4 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_256() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 256 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 260, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 130, 1, 0, 76 }, Arrays.copyOf( berValue, 5 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_65535() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 65535 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 65539, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 130, ( byte ) 255, ( byte ) 255, 76 }, Arrays.copyOf( berValue, 5 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_65536() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 65536 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 65541, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 131, 1, 0, 0, 76 }, Arrays.copyOf( berValue, 6 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_16777215() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 16777215 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 16777220, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 131, ( byte ) 255, ( byte ) 255, ( byte ) 255, 76 },
                Arrays.copyOf( berValue, 6 ) );
    }

    @Test
    public void shouldEncodeLongFormLength_16777216() {
        AutofillTestBer tst = new AutofillTestBer( 66, 76, 16777216 );
        byte[ ] berValue = tst.getBerValue( );

        Assert.assertEquals( 16777222, berValue.length );
        Assert.assertArrayEquals( new byte[ ] { 66, ( byte ) 132, 1, 0, 0, 0, 76 }, Arrays.copyOf( berValue, 7 ) );
    }

    @Test
    public void shouldCreateBerThatConsistsOfTypeLengthValue() {
        AutofillTestBer tst = new AutofillTestBer( 60, 70, 255 );
        byte[ ] berValue = tst.getBerValue( );
        byte[ ] expectedBody = new byte[ 255 ];
        Arrays.fill( expectedBody, ( byte ) 70 );

        Assert.assertArrayEquals( new byte[ ] { 60 }, Arrays.copyOfRange( berValue, 0, 1 ) );
        Assert.assertArrayEquals( new byte[ ] { ( byte ) 129, ( byte ) 255 }, Arrays.copyOfRange( berValue, 1, 3 ) );
        Assert.assertArrayEquals( expectedBody, Arrays.copyOfRange( berValue, 3, 258 ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullOctetString() {
        new OctetString( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowZeroLengthOctetString() {
        new OctetString( new byte[ 0 ] );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowNullSequence() {
        new Sequence( null );
    }

    @Test( expected = IllegalArgumentException.class )
    public void shouldNotAllowZeroLengthSequence() {
        new Sequence( new LinkedList<>( ) );
    }

    public class AutofillTestBer extends BerType {
        private int type;
        private byte[ ] bytes;

        public AutofillTestBer( int type, int pattern, int length ) {
            this.type = type;
            this.bytes = new byte[ length ];
            Arrays.fill( bytes, ( byte ) pattern );
        }

        @Override
        protected byte[ ] getType() {
            return new byte[ ] { ( byte ) type };
        }

        @Override
        protected byte[ ] getBytes() {
            return bytes;
        }
    }

}
