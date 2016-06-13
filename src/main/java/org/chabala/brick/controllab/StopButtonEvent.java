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

import java.util.EventObject;

/**
 * The event triggered by interacting with the red stop button on the control
 * lab.
 */
public class StopButtonEvent extends EventObject {

    private final byte rawValue;

    /**
     * Constructs a StopButtonEvent.
     *
     * @param source The object on which the Event initially occurred.
     * @param rawValue The raw data that signaled this event.
     * @throws IllegalArgumentException if source is null.
     */
    public StopButtonEvent(Object source, byte rawValue) {
        super(source);
        this.rawValue = rawValue;
    }

    public byte getRawValue() {
        return rawValue;
    }
}
