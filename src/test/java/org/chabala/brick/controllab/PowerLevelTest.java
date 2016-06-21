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
