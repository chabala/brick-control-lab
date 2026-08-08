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

import jssc.SerialPortException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing {@link JsscSerialPort}.
 */
public class JsscSerialPortTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private jssc.SerialPort innerSerialPort;

    @Mock
    private SerialPortException serialPortException;

    @Mock
    private SerialPortEventListener serialPortEventListener;

    private final Random random = new Random();
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
        when(innerSerialPort.openPort()).thenThrow(serialPortException);
        try {
            serialPort.openPort();
        } catch (IOException ioe) {
            assertThat(ioe, isA(IOException.class));
            assertThat(ioe.getCause(), isA(SerialPortException.class));
        }
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
        when(innerSerialPort.writeByte(b)).thenThrow(serialPortException);
        try {
            serialPort.write(b);
        } catch (IOException ioe) {
            assertThat(ioe, isA(IOException.class));
            assertThat(ioe.getCause(), isA(SerialPortException.class));
        }
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
        when(innerSerialPort.writeBytes(bytes)).thenThrow(serialPortException);
        try {
            serialPort.write(bytes);
        } catch (IOException ioe) {
            assertThat(ioe, isA(IOException.class));
            assertThat(ioe.getCause(), isA(SerialPortException.class));
        }
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
        when(innerSerialPort.readBytes(byteCount)).thenThrow(serialPortException);
        try {
            serialPort.readBytes(byteCount);
        } catch (IOException ioe) {
            assertThat(ioe, isA(IOException.class));
            assertThat(ioe.getCause(), isA(SerialPortException.class));
        }
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
        when(innerSerialPort.isOpened()).thenReturn(true);
        when(innerSerialPort.closePort()).thenThrow(serialPortException);
        try {
            serialPort.close();
        } catch (IOException ioe) {
            assertThat(ioe, isA(IOException.class));
            assertThat(ioe.getCause(), isA(SerialPortException.class));
        }
    }

    @Test
    public void testAddEventListenerDelegatesToJssc() throws Exception {
        serialPort.addEventListener(serialPortEventListener);
        verify(innerSerialPort,
            times(1)).addEventListener(serialPortEventListener);
    }

    @Test
    public void testAddEventListenerOnlyThrowsIOExceptions() throws Exception {
        doThrow(serialPortException).when(innerSerialPort)
            .addEventListener(serialPortEventListener);
        try {
            serialPort.addEventListener(serialPortEventListener);
        } catch (IOException ioe) {
            assertThat(ioe, isA(IOException.class));
            assertThat(ioe.getCause(), isA(SerialPortException.class));
        }
    }
}
