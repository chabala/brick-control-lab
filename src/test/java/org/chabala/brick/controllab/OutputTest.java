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

import org.junit.*;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.EnumSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Testing {@link Output}.
 */
public class OutputTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private ControlLab controlLab;

    private OutputId outputId;
    private Set<OutputId> outputIdSet;
    private Output output;

    private RandomEnum randomEnum = new RandomEnum();

    @Before
    public void setUp() {
        outputId = randomEnum.get(OutputId.class);
        outputIdSet = EnumSet.of(outputId);
        output = new Output(controlLab, outputId);
    }

    @After
    public void tearDown() {
        output = null;
        outputIdSet = null;
        outputId = null;
    }

    @Test
    public void testGetOutputIdSet() {
        Set<OutputId> outputIds = output.getOutputIdSet();
        assertThat(outputIds, contains(outputId));

        thrown.expect(UnsupportedOperationException.class);
        outputIds.add(randomEnum.get(OutputId.class));
    }

    @Test
    public void testTurnOff() throws Exception {
        Output newOutput = output.turnOff();
        assertThat(newOutput, is(output));
        verify(controlLab, times(1)).turnOutputOff(outputIdSet);
    }

    @Test
    public void testTurnOn() throws Exception {
        Output newOutput = output.turnOn();
        assertThat(newOutput, is(output));
        verify(controlLab, times(1)).turnOutputOn(outputIdSet);
    }

    @Test
    public void testReverseDirection() throws Exception {
        Output newOutput = output.reverseDirection();
        assertThat(newOutput, is(output));
        verify(controlLab, times(1)).setOutputDirection(Direction.REVERSE, outputIdSet);
    }

    @Test
    public void testSetPowerLevel() throws Exception {
        final PowerLevel powerLevel = randomEnum.get(PowerLevel.class);
        Output newOutput = output.setPowerLevel(powerLevel);
        assertThat(newOutput, is(output));
        verify(controlLab, times(1)).setOutputPowerLevel(powerLevel, outputIdSet);
    }

    @Test
    public void testToString() {
        assertThat(output + "", containsString("outputIdSet=[" + outputId.name() + "]"));
    }
}
