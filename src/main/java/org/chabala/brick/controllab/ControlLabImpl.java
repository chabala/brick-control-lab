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

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * Object to represent interacting with the LEGO® control lab interface.
 */
class ControlLabImpl implements ControlLab {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SerialPortFactory portFactory;
    private SerialPort serialPort;
    private KeepAliveMonitor keepAliveMonitor;
    private final InputManager inputManager;
    private final BiFunction<SerialPort, InputManager, SerialPortEventListener> listenerFactory;
    private final Map<OutputId, Output> outputMap;
    private final StopButton stopButton;
    private final Map<InputId, Input> inputMap;

    /**
     * Default constructor using jSSC serial implementation.
     */
    ControlLabImpl() {
        this(new JsscSerialPortFactory(), new InputManager(), SerialListener::new);
    }

    ControlLabImpl(SerialPortFactory portFactory,
                   InputManager inputManager,
                   BiFunction<SerialPort, InputManager, SerialPortEventListener> listenerFactory) {
        this.portFactory = portFactory;
        this.inputManager = inputManager;
        this.listenerFactory = listenerFactory;
        outputMap = Collections.unmodifiableMap(new EnumMap<>(
                Arrays.stream(OutputId.values()).collect(
                        Collectors.toMap(Function.identity(), id -> new Output(this, id)))));
        stopButton = new StopButton(inputManager);
        inputMap = Collections.unmodifiableMap(new EnumMap<>(
                Arrays.stream(InputId.values()).collect(
                        Collectors.toMap(Function.identity(), id -> new Input(inputManager, id)))));
    }

    @Override
    public void open(String portName) throws IOException {
        serialPort = portFactory.getSerialPort(portName);
        log.info("Opening port {}", serialPort.getPortName());
        serialPort.openPort();
        SerialPortEventListener serialListener = listenerFactory.apply(serialPort, inputManager);
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
    public void turnOutputOff(Set<OutputId> outputs) throws IOException {
        sendCommand(Protocol.OUTPUT_OFF, OutputId.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public void turnOutputOn(Set<OutputId> outputs) throws IOException {
        sendCommand(Protocol.OUTPUT_ON, OutputId.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public void setOutputDirection(Direction direction, Set<OutputId> outputs) throws IOException {
        sendCommand(direction.getCode(), OutputId.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public void setOutputPowerLevel(PowerLevel powerLevel, Set<OutputId> outputs) throws IOException {
        sendCommand(powerLevel.getCode(), OutputId.encodeSetToByte(outputs));
    }

    /** {@inheritDoc} */
    @Override
    public Output getOutput(OutputId outputId) {
        return outputMap.get(outputId);
    }

    /** {@inheritDoc} */
    @Override
    public StopButton getStopButton() {
        return stopButton;
    }

    /** {@inheritDoc} */
    @Override
    public Input getInput(InputId inputId) {
        return inputMap.get(inputId);
    }

    /** {@inheritDoc} */
    @Override
    public List<String> getAvailablePorts() {
        return portFactory.getAvailablePorts();
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
