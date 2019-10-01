/*
 * Copyright © 2016 Greg Chabala
 *
 * This file is part of brick-control-lab.
 *
 * brick-control-lab is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * brick-control-lab is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with brick-control-lab.  If not, see http://www.gnu.org/licenses/.
 */
package org.chabala.brick.controllab;

import org.junit.Test;

import java.util.EnumSet;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Test the encoding and decoding methods of {@link Output}.
 */
public class OutputTest {

    @Test
    public void testAllOutputSetEncodesByteWithAllBitsSet() throws Exception {
        assertThat(Output.encodeSetToByte(Output.ALL), is((byte) 0b11111111));
    }

    @Test
    public void testOutputAEncodesByteWithLowBitSet() throws Exception {
        assertThat(Output.encodeSetToByte(EnumSet.of(Output.A)), is((byte) 0b00000001));
    }

    @Test
    public void testOutputHEncodesByteWithHighBitSet() throws Exception {
        assertThat(Output.encodeSetToByte(EnumSet.of(Output.H)), is((byte) 0b10000000));
    }

    @Test
    public void testByteWithAllBitsSetDecodesToAllOutputs() throws Exception {
        assertThat(Output.decodeByteToSet((byte) 0b11111111), is(Output.ALL));
    }

    @Test
    public void testByteWithLowBitSetDecodesToOutputA() throws Exception {
        assertThat(Output.decodeByteToSet((byte) 0b00000001), is(EnumSet.of(Output.A)));
    }

    @Test
    public void testByteWithEvenBitsSetDecodesToOutputsBDFH() throws Exception {
        assertThat(Output.decodeByteToSet((byte) 0b10101010), is(EnumSet.of(Output.B, Output.D, Output.F, Output.H)));
    }

    @Test
    public void testByteWithHighBitSetDecodesToOutputH() throws Exception {
        assertThat(Output.decodeByteToSet((byte) 0b10000000), is(EnumSet.of(Output.H)));
    }

    @Test
    public void testThereAreEightOutputs() throws Exception {
        assertThat(Output.values(), arrayWithSize(8));
    }
}
