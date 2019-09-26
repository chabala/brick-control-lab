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

import org.chabala.brick.controllab.SensorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EventListener;

/**
 * The listener interface for receiving sensor input events.
 *
 * @see SensorEvent
 */
public interface SensorListener extends EventListener {

    Logger log = LoggerFactory.getLogger(SensorListener.class);

    /**
     * This is the entrypoint for raw sensor data events. Sensor specific listener
     * interfaces override this and delegate to more specific callbacks.
     * @param sensorEvent a sensor event containing a raw sensor value
     */
    default void sensorEventReceived(SensorEvent<SensorValue> sensorEvent) {
        log.info("{}", sensorEvent);
    }
}
