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

import org.chabala.brick.controllab.sensor.SensorEvent;
import org.chabala.brick.controllab.sensor.SensorListener;

/**
 * Implementors of this interface can add or remove listeners for input and stop button events.
 */
public interface MutatesInputListeners {

    /**
     * Attach a listener for {@link SensorEvent}s.
     *
     * <p>Multiple listeners are allowed. A listener instance will only be registered
     * once even if it is added multiple times.
     * @param input    input to add the listener to
     * @param listener listener to add
     */
    void addSensorListener(Input input, SensorListener listener);

    /**
     * Remove a listener for {@link SensorEvent}s.
     * @param input    input to remove the listener from
     * @param listener listener to remove
     */
    void removeSensorListener(Input input, SensorListener listener);

    /**
     * Attach a listener for {@link StopButtonEvent}s.
     *
     * <p>Multiple listeners are allowed. A listener instance will only be registered
     * once even if it is added multiple times.
     * @param listener listener to add
     */
    void addStopButtonListener(StopButtonListener listener);

    /**
     * Remove a listener for {@link StopButtonEvent}s.
     * @param listener listener to remove
     */
    void removeStopButtonListener(StopButtonListener listener);
}
