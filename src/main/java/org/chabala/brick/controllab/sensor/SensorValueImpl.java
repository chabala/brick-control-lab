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
package org.chabala.brick.controllab.sensor;

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

    /** Analog value is 10 bits, so we steal 2 bits from the low byte. */
    private static final int HIGH_SHIFT = 2;
    private static final int LOW_SHIFT = Byte.SIZE - HIGH_SHIFT;
    private static final int STATUS_MASK = (1 << LOW_SHIFT) - 1;

    private int extractValue(byte b1, byte b2) {
        int high = Byte.toUnsignedInt(b1) << HIGH_SHIFT; //000000xxxxxxxx00
        int low = Byte.toUnsignedInt(b2) >>> LOW_SHIFT;  //00000000000000xx
        return high + low;
    }

    private int extractStatus(byte b2) {
        return b2 & STATUS_MASK;
    }

    @Override
    public String toString() {
        return "{" +
                "value=" + String.format("0x%02X", analogValue) +
                ", status=" + statusCode +
                '}';
    }
}
