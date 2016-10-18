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
 * Identifiers for the power level of output ports on the control lab.
 *
 * <p>When an output is turned on without specifying a power level, the
 * default power level is {@link #P8}.
 */
public enum PowerLevel {
    /**
     * Power level 0, turns the output off, and resets the default power level
     * back to {@link #P8}.
     */
    P0,
    /** Power level 1, lowest power level. */
    P1,
    /** Power level 2. */
    P2,
    /** Power level 3. */
    P3,
    /** Power level 4. */
    P4,
    /** Power level 5. */
    P5,
    /** Power level 6. */
    P6,
    /** Power level 7. */
    P7,
    /** Power level 8, highest power level. This is the default when not specified. */
    P8;

    private final byte code;

    /**
     * Enumerates possible power level values for the change power level command.
     */
    PowerLevel() {
        code = getCodeFromLevel(ordinal());
    }

    /**
     * The byte expected by the control lab for the change power level command.
     * @return a byte relating to this power level
     */
    public byte getCode() {
        return code;
    }

    private static byte getCodeFromLevel(int level) {
        if (level == 0) {
            return (byte) 0b10010010;
        } else {
            return (byte) (0b10110000 + (level - 1 & 0x07));
        }
    }
}
