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
 * Identifiers for the input ports on the control lab.
 * <p>
 * Inputs 1-4 are passive, they read the resistence of the sensor
 * connected to them. They are colored yellow on the control lab.
 * <p>
 * Inputs 5-8 are active, they supply power to the connected sensor
 * in order for it to work. They are colored blue on the control lab.
 */
public enum InputId {
    /** Input 1. */ I1(InputType.PASSIVE),
    /** Input 2. */ I2(InputType.PASSIVE),
    /** Input 3. */ I3(InputType.PASSIVE),
    /** Input 4. */ I4(InputType.PASSIVE),
    /** Input 5. */ I5(InputType.ACTIVE),
    /** Input 6. */ I6(InputType.ACTIVE),
    /** Input 7. */ I7(InputType.ACTIVE),
    /** Input 8. */ I8(InputType.ACTIVE);

    private final InputType inputType;

    InputId(InputType inputType) {
        this.inputType = inputType;
    }

    /**
     * Returns an {@link InputType} to indicate if this {@link InputId} is
     * active or passive.
     * @return the input type for this input ID
     */
    public InputType getInputType() {
        return inputType;
    }
}
