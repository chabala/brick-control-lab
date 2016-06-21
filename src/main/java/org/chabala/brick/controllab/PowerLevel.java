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
            return (byte) (0b10110000 + (level-1 & 0x07));
        }
    }
}
