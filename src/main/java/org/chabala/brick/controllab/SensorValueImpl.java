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
 * along with brick-control-lab.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.chabala.brick.controllab;

/**
 * {@inheritDoc}.
 */
class SensorValueImpl implements SensorValue {
    private final int analogValue;
    private final int statusCode;

    SensorValueImpl(byte high, byte low) {
        analogValue = extractValue(high, low);
        statusCode = extractStatus(low);
    }

    /** {@inheritDoc} */
    @Override
    public int getAnalogValue() {
        return analogValue;
    }

    /**
     * {@inheritDoc}.
     * bit 5 looks like 'in flux'.
     */
    @Override
    public int getStatusCode() {
        return statusCode;
    }

    private int extractValue(byte b1, byte b2) {
        int high = (b1 & 0xFF) << 2; //000000xxxxxxxx00
        int low = (b2 & 0xFF) >> 6;  //00000000000000xx
        return high + low;
    }

    private int extractStatus(byte b2) {
        return b2 & 0x3F;
    }

    @Override
    public String toString() {
        return "{" +
                "value=" + String.format("0x%02X", analogValue) +
                ", status=" + statusCode +
                '}';
    }
}
