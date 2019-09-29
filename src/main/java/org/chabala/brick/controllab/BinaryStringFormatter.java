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

/**
 * Helper methods for making readable binary strings of data bytes.
 */
public final class BinaryStringFormatter {

    /**
     * Takes a byte array of two bytes and returns a string of those bytes
     * represented in binary.
     * @param highLow byte array containing two bytes
     * @return a sixteen character string of zeros and ones
     */
    public static String printInBinary(byte[] highLow) {
        return printByteInBinary(highLow[0]) + printByteInBinary(highLow[1]);
    }

    /**
     * Takes a single byte of data and returns an eight character string
     * representing that byte in binary. Leading zeros are preserved.
     * @param data a single byte of data
     * @return an eight character string of zeros and ones
     */
    public static String printByteInBinary(int data) {
        return Integer.toBinaryString((data & 0xFF) + 0x100).substring(1);
    }

    private BinaryStringFormatter() {
        throw new UnsupportedOperationException();
    }
}
