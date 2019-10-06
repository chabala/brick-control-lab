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

import org.chabala.brick.controllab.InputId;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing {@link TouchSensorListener}.
 */
public class TouchSensorListenerTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private SensorValue sensorValue;

    @Test
    public void testTouchSensorEventReceived() throws Exception {
        final int analogValue = ThreadLocalRandom.current().nextInt();
        final int statusCode = ThreadLocalRandom.current().nextInt();
        final byte[] oldValue = {};
        final byte[] newValue= {};
        when(sensorValue.getAnalogValue()).thenReturn(analogValue);
        when(sensorValue.getStatusCode()).thenReturn(statusCode);
        SensorEvent<SensorValue> sensorEvent = new SensorEvent<>(InputId.I1, oldValue, newValue, sensorValue);
        TouchSensorListener listener = event -> {
            assertThat(event.getValue().getAnalogValue(), is(analogValue));
            assertThat(event.getValue().getStatusCode(), is(statusCode));
        };
        listener.sensorEventReceived(sensorEvent);
        verify(sensorValue, times(1)).getAnalogValue();
        verify(sensorValue, times(1)).getStatusCode();
    }
}
