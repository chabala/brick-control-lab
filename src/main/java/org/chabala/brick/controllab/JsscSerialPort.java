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

import java.io.IOException;

/**
 * jSSC based implementation of {@link SerialPort}.
 *
 * @see <a href="https://github.com/scream3r/java-simple-serial-connector">
 *               https://github.com/scream3r/java-simple-serial-connector</a>
 */
class JsscSerialPort implements SerialPort {
    /** The wrapped jSSC {@link jssc.SerialPort} that all methods delegate to. */
    private final jssc.SerialPort serialPort;

    /**
     * jSSC based implementation of {@link SerialPort}.
     * @param portName system specific serial port identifier
     */
    JsscSerialPort(String portName) {
        this(new jssc.SerialPort(portName));
    }

    /**
     * This constructor is for unit testing.
     */
    JsscSerialPort(jssc.SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /** {@inheritDoc} */
    @Override
    public String getPortName() {
        return serialPort.getPortName();
    }

    /** {@inheritDoc} */
    @Override
    public void openPort() throws IOException {
        try {
            serialPort.openPort();
            setParams();
            setEventsMask();
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOpen() {
        return serialPort.isOpened();
    }

    /** {@inheritDoc} */
    @Override
    public boolean write(byte b) throws IOException {
        try {
            return serialPort.writeByte(b);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean write(byte[] bytes) throws IOException {
        try {
            return serialPort.writeBytes(bytes);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public byte[] readBytes(int byteCount) throws IOException {
        try {
            return serialPort.readBytes(byteCount);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (serialPort.isOpened()) {
            try {
                serialPort.closePort();
            } catch (SerialPortException e) {
                throw new IOException("Error closing serial port", e);
            }
        }
    }

    private boolean setParams() throws SerialPortException {
        return serialPort.setParams(
                jssc.SerialPort.BAUDRATE_9600,
                jssc.SerialPort.DATABITS_8,
                jssc.SerialPort.STOPBITS_1,
                jssc.SerialPort.PARITY_NONE,
                true,
                true);
    }

    private boolean setEventsMask() throws SerialPortException {
        return serialPort.setEventsMask(
                jssc.SerialPort.MASK_RXCHAR |
                jssc.SerialPort.MASK_CTS |
                jssc.SerialPort.MASK_DSR);
    }

    /** {@inheritDoc} */
    @Override
    public void addEventListener(SerialPortEventListener listener) throws IOException {
        try {
            serialPort.addEventListener(listener);
        } catch (SerialPortException e) {
            throw new IOException(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JsscSerialPort{" +
                "portName='" + getPortName() + '\'' +
                ", open=" + isOpen() +
                '}';
    }
}
