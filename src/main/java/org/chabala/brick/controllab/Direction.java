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
 * Identifiers for the direction of output ports on the control lab.
 * <p>
 * When an output is turned on without specifying a direction, the
 * default direction is {@link #RIGHT}.
 */
public enum Direction {
    /** Left direction, as indicated by the LED on the control lab. */
    LEFT   ((byte) 0b10010100),
    /** Right direction, as indicated by the LED on the control lab. */
    RIGHT  ((byte) 0b10010011),
    /** Reverse direction, relative to the existing direction of the port. */
    REVERSE((byte) 0b10010101);

    private final byte code;

    /**
     * Enumerates possible direction values for the change direction command.
     * @param code a byte relating to this direction
     */
    Direction(byte code) {
        this.code = code;
    }

    /**
     * The byte expected by the control lab for the change direction command.
     * @return a byte relating to this direction
     */
    public byte getCode() {
        return code;
    }
}
