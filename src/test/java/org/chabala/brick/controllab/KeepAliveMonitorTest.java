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

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.Duration;

import static org.mockito.Mockito.*;

/**
 * Testing the {@link KeepAliveMonitor}.
 */
public class KeepAliveMonitorTest {

    private final Duration keepAliveDuration = Duration.ofMillis(500);
    private final Duration nineTenthsDuration = keepAliveDuration.multipliedBy(9).dividedBy(10);
    private final long keepAliveDurationMs = keepAliveDuration.toMillis();

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private SerialPort serialPort;

    @Test
    public void testMonitorSendsKeepAlives() throws Exception {
        when(serialPort.getPortName()).thenReturn("COM#");
        when(serialPort.isOpen()).thenReturn(true);
        try (KeepAliveMonitor monitor = new KeepAliveMonitor(serialPort, nineTenthsDuration)) {
            verify(serialPort, after(keepAliveDurationMs).times(1)).isOpen();
            verify(serialPort, times(1)).write(anyByte());
            verify(serialPort, after(keepAliveDurationMs).times(2)).isOpen();
            verify(serialPort, times(2)).write(anyByte());
        }
    }

    @Test
    public void testResetPreventsKeepAlives() throws Exception {
        long threeQuartersDurationMs = keepAliveDuration.multipliedBy(3).dividedBy(4).toMillis();
        try (KeepAliveMonitor monitor = new KeepAliveMonitor(serialPort, nineTenthsDuration)) {
            verify(serialPort, after(threeQuartersDurationMs).never()).isOpen();
            verify(serialPort, never()).write(anyByte());
            monitor.reset();
            verify(serialPort, after(threeQuartersDurationMs).never()).isOpen();
            verify(serialPort, never()).write(anyByte());
            verify(serialPort, only()).getPortName();
        }
    }

    @Test
    public void testClosePreventsKeepAlives() throws Exception {
        try (KeepAliveMonitor monitor = new KeepAliveMonitor(serialPort, nineTenthsDuration)) {
            monitor.close();
            verify(serialPort, after(keepAliveDuration.multipliedBy(2).toMillis()).only()).getPortName();
        }
    }
}
