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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Identifiers for the output ports on the control lab.
 */
public enum OutputId {
    /** Output A. */ A,
    /** Output B. */ B,
    /** Output C. */ C,
    /** Output D. */ D,
    /** Output E. */ E,
    /** Output F. */ F,
    /** Output G. */ G,
    /** Output H. */ H;

    /**
     * Convenience constant for specifying all output ports.
     */
    public static final Set<OutputId> ALL = Collections.unmodifiableSet(EnumSet.allOf(OutputId.class));

    /**
     * Encodes values from a {@link Set} of {@link Enum}s to a byte. Uses the
     * ordinal of the Enum as the position of the bit in the byte to set high.
     *
     * @param set set of values to be encoded
     * @param <T> type of Enum, only used with {@link OutputId} internally
     * @return a byte with high bits relating to the values in the set
     */
    public static <T extends Enum<T>> byte encodeSetToByte(Set<T> set) {
        byte r = 0;
        for (T value : set) {
            r |= 1 << value.ordinal();
        }
        return r;
    }

    /**
     * Decodes a byte into a {@link Set} of {@link OutputId}s.
     * @param b byte where each bit corresponds to the ordinal of an Output
     * @return a Set containing the desired Outputs
     */
    public static Set<OutputId> decodeByteToSet(byte b) {
        OutputId[] enums = OutputId.class.getEnumConstants();
        Set<OutputId> enumSet = EnumSet.noneOf(OutputId.class);
        for (int bit = 0; bit < Byte.SIZE; bit++) {
            if ((b & 0xFF & 1 << bit) > 0) {
                enumSet.add(enums[bit]);
            }
        }
        return enumSet;
    }
}
