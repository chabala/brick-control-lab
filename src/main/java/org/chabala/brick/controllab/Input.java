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
 * Handle for an input port on a specific control lab instance. Obtain via {@link ControlLab#getInput(InputId)}.
 */
public class Input {
    private final InputManager inputManager;
    private final InputId inputId;

    Input(InputManager inputManager, InputId inputId) {
        this.inputManager = inputManager;
        this.inputId = inputId;
    }

    /**
     * Attach a listener for {@link SensorEvent}s.
     *
     * <p>Multiple listeners are allowed. A listener instance will only be registered
     * once even if it is added multiple times.
     * @param listener listener to add
     */
    public void addListener(SensorListener listener) {
        inputManager.addSensorListener(inputId, listener);
    }

    /**
     * Remove a listener for {@link SensorEvent}s.
     * @param listener listener to remove
     */
    public void removeListener(SensorListener listener) {
        inputManager.removeSensorListener(inputId, listener);
    }
}
