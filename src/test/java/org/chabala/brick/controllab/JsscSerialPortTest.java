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

import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Testing {@link JsscSerialPort}.
 */
public class JsscSerialPortTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    private jssc.SerialPort innerSerialPort;

    @Mock
    private SerialPortException serialPortException;

    @Mock
    private SerialPortEventListener serialPortEventListener;

    private Random random = new Random();
    private SerialPort serialPort;

    @Before
    public void setUp() throws Exception {
        serialPort = new JsscSerialPort(innerSerialPort);
    }

    @Test
    public void testGetPortNameDelegatesToJssc() throws Exception {
        final String portname = "one";
        when(innerSerialPort.getPortName()).thenReturn(portname);
        assertThat(serialPort.getPortName(), is(portname));
    }

    @Test
    public void testOpenPortDelegatesToJssc() throws Exception {
        serialPort.openPort();
        verify(innerSerialPort, times(1)).openPort();
        verify(innerSerialPort, times(1)).setParams(
                jssc.SerialPort.BAUDRATE_9600,
                jssc.SerialPort.DATABITS_8,
                jssc.SerialPort.STOPBITS_1,
                jssc.SerialPort.PARITY_NONE,
                true,
                true);
        verify(innerSerialPort, times(1)).setEventsMask(
                jssc.SerialPort.MASK_RXCHAR |
                jssc.SerialPort.MASK_CTS |
                jssc.SerialPort.MASK_DSR);
    }

    @Test
    public void testOpenPortOnlyThrowsIOExceptions() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectCause(is(serialPortException));
        when(innerSerialPort.openPort()).thenThrow(serialPortException);
        serialPort.openPort();
    }

    @Test
    public void testIsOpenDelegatesToJssc() throws Exception {
        boolean opened = random.nextBoolean();
        when(innerSerialPort.isOpened()).thenReturn(opened);
        assertThat(serialPort.isOpen(), is(opened));
    }

    @Test
    public void testWriteByteDelegatesToJssc() throws Exception {
        byte b = 10;
        final boolean result = true;
        when(innerSerialPort.writeByte(b)).thenReturn(result);
        assertThat(serialPort.write(b), is(result));
    }

    @Test
    public void testWriteByteOnlyThrowsIOExceptions() throws Exception {
        byte b = 10;
        thrown.expect(IOException.class);
        thrown.expectCause(is(serialPortException));
        when(innerSerialPort.writeByte(b)).thenThrow(serialPortException);
        serialPort.write(b);
    }

    @Test
    public void testWriteByteArrayDelegatesToJssc() throws Exception {
        byte[] bytes = new byte[2];
        random.nextBytes(bytes);
        final boolean result = true;
        when(innerSerialPort.writeBytes(bytes)).thenReturn(result);
        assertThat(serialPort.write(bytes), is(result));
    }

    @Test
    public void testWriteByteArrayOnlyThrowsIOExceptions() throws Exception {
        byte[] bytes = new byte[2];
        random.nextBytes(bytes);
        thrown.expect(IOException.class);
        thrown.expectCause(is(serialPortException));
        when(innerSerialPort.writeBytes(bytes)).thenThrow(serialPortException);
        serialPort.write(bytes);
    }

    @Test
    public void testReadBytesDelegatesToJssc() throws Exception {
        int byteCount = 5;
        byte[] bytes = new byte[byteCount];
        random.nextBytes(bytes);
        when(innerSerialPort.readBytes(byteCount)).thenReturn(bytes);
        byte[] readBytes = serialPort.readBytes(byteCount);
        assertThat(readBytes.length, is(byteCount));
        assertThat(readBytes, is(bytes));
    }

    @Test
    public void testReadBytesOnlyThrowsIOExceptions() throws Exception {
        int byteCount = 5;
        thrown.expect(IOException.class);
        thrown.expectCause(is(serialPortException));
        when(innerSerialPort.readBytes(byteCount)).thenThrow(serialPortException);
        serialPort.readBytes(byteCount);
    }

    @Test
    public void testCloseWhenClosed() throws Exception {
        when(innerSerialPort.isOpened()).thenReturn(false);
        serialPort.close();
        verify(innerSerialPort, never()).closePort();
    }

    @Test
    public void testCloseWhenOpen() throws Exception {
        when(innerSerialPort.isOpened()).thenReturn(true);
        serialPort.close();
        verify(innerSerialPort, times(1)).closePort();
    }

    @Test
    public void testCloseOnlyThrowsIOExceptions() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectCause(is(serialPortException));
        when(innerSerialPort.isOpened()).thenReturn(true);
        when(innerSerialPort.closePort()).thenThrow(serialPortException);
        serialPort.close();
    }

    @Test
    public void testAddEventListenerDelegatesToJssc() throws Exception {
        serialPort.addEventListener(serialPortEventListener);
        verify(innerSerialPort, times(1)).addEventListener(serialPortEventListener);
    }

    @Test
    public void testAddEventListenerOnlyThrowsIOExceptions() throws Exception {
        thrown.expect(IOException.class);
        thrown.expectCause(is(serialPortException));
        doThrow(serialPortException).when(innerSerialPort).addEventListener(serialPortEventListener);
        serialPort.addEventListener(serialPortEventListener);
    }
}
