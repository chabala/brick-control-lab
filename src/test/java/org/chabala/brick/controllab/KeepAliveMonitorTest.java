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

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import javax.management.timer.Timer;

import static org.mockito.Matchers.anyByte;
import static org.mockito.Mockito.*;

/**
 * Testing the {@link KeepAliveMonitor}.
 */
public class KeepAliveMonitorTest {

    private final long twoSeconds = Timer.ONE_SECOND * 2;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private SerialPort serialPort;

    @Test
    public void testMonitorSendsKeepAlives() throws Exception {
        when(serialPort.isOpen()).thenReturn(true);
        try (KeepAliveMonitor monitor = new KeepAliveMonitor(serialPort)) {
            Thread.sleep(twoSeconds);
            verify(serialPort, times(1)).isOpen();
            verify(serialPort, times(1)).write(anyByte());
            Thread.sleep(twoSeconds);
            verify(serialPort, times(2)).isOpen();
            verify(serialPort, times(2)).write(anyByte());
        }
    }

    @Test
    public void testResetPreventsKeepAlives() throws Exception {
        long oneAndAHalfSeconds = Timer.ONE_SECOND / 2 * 3;
        try (KeepAliveMonitor monitor = new KeepAliveMonitor(serialPort)) {
            Thread.sleep(oneAndAHalfSeconds);
            verify(serialPort, never()).isOpen();
            verify(serialPort, never()).write(anyByte());
            monitor.reset();
            Thread.sleep(oneAndAHalfSeconds);
            verify(serialPort, never()).isOpen();
            verify(serialPort, never()).write(anyByte());
        }
    }

    @Test
    public void testClosePreventsKeepAlives() throws Exception {
        try (KeepAliveMonitor monitor = new KeepAliveMonitor(serialPort)) {
            monitor.close();
        }
        Thread.sleep(twoSeconds);
        verify(serialPort, never()).isOpen();
        verify(serialPort, never()).write(anyByte());
    }

}
