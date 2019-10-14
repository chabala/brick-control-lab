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

import org.chabala.brick.controllab.sensor.SensorListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Testing {@link Input}.
 */
public class InputTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private InputManager inputManager;

    @Mock
    private SensorListener listener;

    private InputId inputId;

    private Input input;

    private RandomEnum randomEnum = new RandomEnum();

    @Before
    public void setUp() {
        inputId = randomEnum.get(InputId.class);
        input = new Input(inputManager, inputId);
    }

    @After
    public void tearDown() {
        input = null;
        inputId = null;
    }

    @Test
    public void testAddListener() {
        input.addListener(listener);
        verify(inputManager, times(1)).addSensorListener(inputId, listener);
    }

    @Test
    public void testRemoveListener() {
        input.removeListener(listener);
        verify(inputManager, times(1)).removeSensorListener(inputId, listener);
    }

    @Test
    public void testToString() {
        assertThat(input + "", containsString("inputId=" + inputId.name()));
    }
}
