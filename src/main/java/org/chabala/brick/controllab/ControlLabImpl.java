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

import org.chabala.brick.controllab.jssc.JsscSerialPortFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * Object to represent interacting with the LEGO® control lab interface.
 */
class ControlLabImpl implements ControlLab {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SerialPortFactory portFactory;
    private SerialPort serialPort;
    private SerialListener serialListener = null;
    private KeepAliveMonitor keepAliveMonitor;

    /**
     * Default constructor using jSSC serial implementation.
     */
    ControlLabImpl() {
        this(new JsscSerialPortFactory());
    }

    ControlLabImpl(SerialPortFactory portFactory) {
        this.portFactory = portFactory;
    }

    @Override
    public void open(String portName) throws IOException {
        serialPort = portFactory.getSerialPort(portName);
        log.info("Opening port {}", serialPort.getPortName());
        serialPort.openPort();
        serialListener = new SerialListener(serialPort);
        serialPort.addEventListener(serialListener);

        sendCommand(Protocol.HANDSHAKE_CHALLENGE.getBytes());
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        if (!serialListener.isHandshakeSeen()) {
            close();
            throw new IOException("No response to handshake");
        }
        keepAliveMonitor = new KeepAliveMonitor(serialPort);
    }

    private void sendCommand(byte[] bytes) throws IOException {
        if (serialPort == null) {
            throw new IOException("Not connected to control lab");
        }
        if (keepAliveMonitor != null) {
            keepAliveMonitor.reset();
        }
        if (log.isInfoEnabled()) {
            if (bytes.length > 10) {
                log.info("TX -> {}", new String(bytes, ISO_8859_1));
            } else {
                StringBuilder sb = new StringBuilder();
                for (byte b : bytes) {
                    sb.append(String.format("0x%02X ", b));
                }
                log.info("TX -> {}", sb);
            }
        }
        serialPort.write(bytes);
    }

    private void sendCommand(byte b1, byte b2) throws IOException {
        sendCommand(new byte[]{b1, b2});
    }

    /** {@inheritDoc} */
    @Override
    public void turnOutputOff(Set<Output> outputs) throws IOException {
        sendCommand(Protocol.OUTPUT_OFF, Output.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public void turnOutputOn(Set<Output> outputs) throws IOException {
        sendCommand(Protocol.OUTPUT_ON, Output.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public void setOutputDirection(Direction direction, Set<Output> outputs) throws IOException {
        sendCommand(direction.getCode(), Output.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public void setOutputPowerLevel(PowerLevel powerLevel, Set<Output> outputs) throws IOException {
        sendCommand(powerLevel.getCode(), Output.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getAvailablePorts() {
        return portFactory.getAvailablePorts();
    }

    /** {@inheritDoc} */
    @Override
    public void addStopButtonListener(StopButtonListener listener) {
        if (serialListener != null) {
            serialListener.addStopButtonListener(listener);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void removeStopButtonListener(StopButtonListener listener) {
        if (serialListener != null) {
            serialListener.removeStopButtonListener(listener);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void close() throws IOException {
        if (keepAliveMonitor != null) {
            keepAliveMonitor.close();
            keepAliveMonitor = null;
        }
        if (serialPort != null) {
            if (serialPort.isOpen()) {
                serialPort.write(Protocol.DISCONNECT);
            }
            serialPort.close();
            serialPort = null;
        }
    }
}
