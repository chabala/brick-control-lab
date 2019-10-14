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

import static org.chabala.brick.controllab.BinaryStringFormatter.printByteInBinary;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Testing {@link BinaryStringFormatter}.
 */
public class BinaryStringFormatterTest {

    @Test
    public void testPrintByteInBinary() throws Exception {
        assertThat(printByteInBinary((byte) 0b00000000), is("00000000"));
        assertThat(printByteInBinary((byte) 0b00000001), is("00000001"));
        assertThat(printByteInBinary((byte) 0b10000000), is("10000000"));
        assertThat(printByteInBinary((byte) 0b10000001), is("10000001"));
        assertThat(printByteInBinary((byte) 0b10110111), is("10110111"));
    }
}
