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
package org.chabala.brick.controllab.sensor;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Testing {@link SensorValue}.
 */
public class SensorValueTest {

    @Test
    public void testMinimumPossibleValues() throws Exception {
        SensorValue value = SensorValue.newSensorValue((byte) 0b0, (byte) 0b0);
        assertThat(value.getAnalogValue(), is(0));
        assertThat(value.getStatusCode(), is(0));
    }

    @Test
    public void testMaximumPossibleValues() throws Exception {
        SensorValue value = SensorValue.newSensorValue((byte) 0b11111111, (byte) 0b11111111);
        assertThat(value.getAnalogValue(), is(1023));
        assertThat(value.getStatusCode(), is(63));
    }

    @Test
    public void testValueSeparation() throws Exception {
        SensorValue value = SensorValue.newSensorValue((byte) 0b11111111, (byte) 0b11000000);
        assertThat(value.getAnalogValue(), is(1023));
        assertThat(value.getStatusCode(), is(0));
    }
}
