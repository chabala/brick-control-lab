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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Testing the {@link ControlLab}.
 */
public class ControlLabTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private SerialPortFactory portFactory;

    @Mock
    private SerialPort serialPort;

    @Mock
    private SerialPortEventListener listener;

    private ControlLab controlLab;

    @Before
    public void setUp() throws Exception {
        controlLab = new ControlLabImpl(portFactory, sp -> listener);
    }

    @After
    public void tearDown() throws Exception {
        controlLab.close();
    }

    @Test
    public void testGetAvailablePorts() {
        when(portFactory.getAvailablePorts()).thenReturn(Arrays.asList("one", "two"));
        List<String> availablePorts = controlLab.getAvailablePorts();
        assertThat(availablePorts, is(not(empty())));
        assertThat(availablePorts, hasItems("one", "two"));
    }

    @Test
    public void testOpen() throws Exception {
        final String portName = "one";
        AtomicBoolean handshakeSeen = new AtomicBoolean(false);
        when(portFactory.getSerialPort(portName)).thenReturn(serialPort);
        when(serialPort.getPortName()).thenReturn(portName);
        when(serialPort.write(Protocol.HANDSHAKE_CHALLENGE.getBytes())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                handshakeSeen.set(true);
                return true;
            }
        });
        when(listener.isHandshakeSeen()).thenAnswer(i -> handshakeSeen.get());
        controlLab.open(portName);
        verify(serialPort, times(1)).openPort();
        verify(serialPort, times(1)).addEventListener(listener);
    }
}
