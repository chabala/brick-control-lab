package org.chabala.brick.controllab;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Testing {@link PowerLevel}.
 */
public class PowerLevelTest {

    @Test
    public void testPowerLevelZeroHasCorrectCode() throws Exception {
        assertThat(PowerLevel.P0.getCode(), is((byte) 0b10010010));
    }

    @Test
    public void testPowerLevelOneHasCorrectCode() throws Exception {
        assertThat(PowerLevel.P1.getCode(), is((byte) 0b10110000));
    }

    @Test
    public void testPowerLevelEightHasCorrectCode() throws Exception {
        assertThat(PowerLevel.P8.getCode(), is((byte) 0b10110111));
    }

    @Test
    public void testThereAreNinePowerLevels() throws Exception {
        assertThat(PowerLevel.values(), arrayWithSize(9));
    }

    @Test
    public void testEveryPowerLevelHasAUniqueCode() throws Exception {
        Set<Byte> codes = new HashSet<>();
        for (PowerLevel p : PowerLevel.values()) {
            codes.add(p.getCode());
        }
        assertThat(codes, hasSize(PowerLevel.values().length));
    }
}
