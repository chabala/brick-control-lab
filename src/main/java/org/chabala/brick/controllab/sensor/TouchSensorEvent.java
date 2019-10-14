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
 * The event triggered by receiving a {@link SensorValue} from an {@link InputId}
 * that is known to be a {@link TouchSensor}.
 */
public class TouchSensorEvent extends SensorEvent<TouchSensor> {
    /**
     * Creates a TouchSensorEvent from a generic {@link SensorEvent}, and wraps
     * the {@link SensorValue} in the appropriate subclass.
     * @param sensorEvent generic sensor event
     */
    public TouchSensorEvent(SensorEvent<SensorValue> sensorEvent) {
        super(sensorEvent, TouchSensor::new);
    }
}
