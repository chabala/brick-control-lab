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
 * Test the encoding and decoding methods of {@link OutputId}.
 */
public class OutputIdTest {

    @Test
    public void testAllOutputSetEncodesByteWithAllBitsSet() throws Exception {
        assertThat(OutputId.encodeSetToByte(OutputId.ALL), is((byte) 0b11111111));
    }

    @Test
    public void testOutputAEncodesByteWithLowBitSet() throws Exception {
        assertThat(OutputId.encodeSetToByte(EnumSet.of(OutputId.A)), is((byte) 0b00000001));
    }

    @Test
    public void testOutputHEncodesByteWithHighBitSet() throws Exception {
        assertThat(OutputId.encodeSetToByte(EnumSet.of(OutputId.H)), is((byte) 0b10000000));
    }

    @Test
    public void testByteWithAllBitsSetDecodesToAllOutputs() throws Exception {
        assertThat(OutputId.decodeByteToSet((byte) 0b11111111), is(OutputId.ALL));
    }

    @Test
    public void testByteWithLowBitSetDecodesToOutputA() throws Exception {
        assertThat(OutputId.decodeByteToSet((byte) 0b00000001), is(EnumSet.of(OutputId.A)));
    }

    @Test
    public void testByteWithEvenBitsSetDecodesToOutputsBDFH() throws Exception {
        assertThat(OutputId.decodeByteToSet((byte) 0b10101010), is(EnumSet.of(OutputId.B, OutputId.D, OutputId.F, OutputId.H)));
    }

    @Test
    public void testByteWithHighBitSetDecodesToOutputH() throws Exception {
        assertThat(OutputId.decodeByteToSet((byte) 0b10000000), is(EnumSet.of(OutputId.H)));
    }

    @Test
    public void testThereAreEightOutputs() throws Exception {
        assertThat(OutputId.values(), arrayWithSize(8));
    }
}
