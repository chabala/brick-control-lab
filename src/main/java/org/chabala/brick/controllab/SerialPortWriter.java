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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * Owner of all writing activity for a serial port. Manages consistent logging
 * and ownership of the keep alive monitor for the port.
 */
class SerialPortWriter implements Closeable {
    /**
     * When sending commands longer than this threshold, log them as strings
     * instead of hex values. In practice, this is only needed for the initial
     * handshake, which makes more sense to show as a string.
     */
    private static final int STRING_LOGGING_THRESHOLD = 10;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SerialPort serialPort;
    private KeepAliveMonitor keepAliveMonitor;

    SerialPortWriter(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    void startKeepAlives() {
        keepAliveMonitor = new KeepAliveMonitor(this);
    }

    String getPortName() {
        return serialPort.getPortName();
    }

    void sendCommand(byte[] bytes, Logger log) throws IOException {
        if (serialPort == null) {
            throw new IOException("Not connected to control lab");
        }
        if (keepAliveMonitor != null) {
            keepAliveMonitor.reset();
        }
        if (serialPort.isOpen()) {
            if (log.isInfoEnabled()) {
                String portName = serialPort.getPortName();
                if (bytes.length > STRING_LOGGING_THRESHOLD) {
                    log.info("{} TX -> {}", portName, new String(bytes, ISO_8859_1));
                } else if (log.isDebugEnabled()) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : bytes) {
                        sb.append(String.format("0x%02X ", b));
                    }
                    log.debug("{} TX -> {}", portName, sb);
                }
            }
            serialPort.write(bytes);
        }
    }

    void sendCommand(byte[] bytes) throws IOException {
        sendCommand(bytes, log);
    }

    void sendCommand(byte b) throws IOException {
        sendCommand(new byte[]{b});
    }

    void sendCommand(byte b, Logger log) throws IOException {
        sendCommand(new byte[]{b}, log);
    }

    void sendCommand(byte b1, byte b2) throws IOException {
        sendCommand(new byte[]{b1, b2});
    }

    @Override
    public void close() {
        if (keepAliveMonitor != null) {
            keepAliveMonitor.close();
            keepAliveMonitor = null;
        }
    }
}
