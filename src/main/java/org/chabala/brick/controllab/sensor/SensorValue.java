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

import org.chabala.brick.controllab.InputId;

/**
 * The values obtained from reading an {@link InputId}.
 */
public interface SensorValue {

    static SensorValue newSensorValue(byte high, byte low) {
        return new SensorValueImpl(high, low);
    }

    /**
     * Analog value returned by sensor, in the range of 0 to 1023.
     * @return value from 0 to 1023, inclusive.
     */
    int getAnalogValue();

    /**
     * Six bit status code from the sensor, as a value from 0 to 63.
     * @return value from 0 to 63, inclusive.
     */
    int getStatusCode();

    /**
     * bit 3 looks like 0 for passive, 1 for active sensors.
     * @return true for passive sensors, false for active sensors
     */
    default boolean isPassive() {
        return (getStatusCode() & 0b000100) == 0;
    }

    /**
     * bit 4 looks like 0 for engaged, 1 for released.
     * (when four is low, sensor is lit on box)
     * @return true for engaged sensors, false for released sensors
     */
    default boolean isEngaged() {
        return (getStatusCode() & 0b001000) == 0;
    }
}
