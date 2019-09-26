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

import org.chabala.brick.controllab.sensor.SensorEvent;
import org.chabala.brick.controllab.sensor.SensorListener;
import org.chabala.brick.controllab.sensor.SensorValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static org.chabala.brick.controllab.Protocol.HANDSHAKE_RESPONSE;

/**
 * Handles serial events.
 */
class SerialListener implements SerialPortEventListener {

    private static final int FRAME_SIZE = 19;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SerialPort serialPort;
    private final Map<Input, byte[]> sensorData;
    private final Map<Input, Set<SensorListener>> sensorListeners;
    private final Set<StopButtonListener> stopButtonListeners = Collections.synchronizedSet(new HashSet<>(2));
    private final List<Input> frameInputOrder =
            Arrays.asList(Input.I4, Input.I8, Input.I3, Input.I7, Input.I2, Input.I6, Input.I1, Input.I5);
    private final boolean ignoreBadHandshake;

    private boolean handshakeSeen = false;
    private boolean stopDepressed = false;
    private String handshake = "";

    SerialListener(SerialPort serialPort) {
        this.serialPort = serialPort;
        sensorData = Collections.synchronizedMap(new EnumMap<>(Input.class));
        sensorListeners = Collections.synchronizedMap(new EnumMap<>(Input.class));
        Arrays.stream(Input.values()).forEach(i -> {
            sensorData.put(i, new byte[] {0, 0});
            sensorListeners.put(i, Collections.synchronizedSet(new HashSet<>(2)));
        });
        ignoreBadHandshake = Boolean.valueOf(
                System.getProperty("brick-control-lab.ignoreBadHandshake", "false"));
    }

    @Override
    public boolean isHandshakeSeen() {
        return handshakeSeen;
    }

    /** {@inheritDoc} */
    @Override
    public void serialEventRXCHAR(int availableBytes) {
        if (!handshakeSeen) {
            processHandshake(availableBytes);
        } else {
            if (availableBytes >= FRAME_SIZE) {
                try {
                    byte[] buffer = serialPort.readBytes(FRAME_SIZE);
                    processInputSensors(buffer);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private void processHandshake(int availableBytes) {
        try {
            while (!handshakeSeen && availableBytes > 0) {
                availableBytes--;
                String newChar = new String(serialPort.readBytes(1), ISO_8859_1);
                handshake += newChar;
                if (handshake.endsWith(HANDSHAKE_RESPONSE)) {
                    handshakeSeen = true;
                    log.info(handshake);
                    handshake = "";
                } else if (handshake.length() >= HANDSHAKE_RESPONSE.length()) {
                    log.error("Bad handshake: {}", handshake);
                    if (ignoreBadHandshake) {
                        handshakeSeen = true;
                        handshake = "";
                    } else {
                        serialPort.close();
                    }
                }
            }
            if (!handshakeSeen) {
                log.debug(handshake);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void addStopButtonListener(StopButtonListener listener) {
        stopButtonListeners.add(listener);
    }

    @Override
    public void removeStopButtonListener(StopButtonListener listener) {
        stopButtonListeners.remove(listener);
    }

    @Override
    public void addSensorListener(Input input, SensorListener listener) {
        sensorListeners.get(input).add(listener);
    }

    @Override
    public void removeSensorListener(Input input, SensorListener listener) {
        sensorListeners.get(input).remove(listener);
    }

    /**
     * Expects 19 bytes of data.
     * @param inputFrame byte array of 19 bytes
     */
    private void processInputSensors(byte[] inputFrame) throws IOException {
        int frameIndex = 0;
        if (inputFrame.length != FRAME_SIZE) {
            StringBuilder sb = new StringBuilder();
            for (byte b : inputFrame) {
                sb.append(String.format("0x%02X ", b));
            }
            throw new IOException(
                    "Expected 19 bytes, got " + inputFrame.length +
                    " - " + sb.toString());
        }
        if (!isChecksumValid(inputFrame)) {
            log.warn("Bad checksum received");
            return;
        }
        processStopButton(inputFrame[frameIndex++]);
        int lastCommandIndex = frameIndex++;
        if (0x00 != inputFrame[lastCommandIndex]) {
            log.info("Ports affected by last command {}",
                    Output.decodeByteToSet(inputFrame[lastCommandIndex]));
        }
        for (Input in : frameInputOrder) {
            setSensorValue(in, inputFrame[frameIndex++], inputFrame[frameIndex++]);
        }
    }

    private void processStopButton(byte b) {
        if (0x00 != b) {
            if (!stopDepressed) {
                synchronized (stopButtonListeners) {
                    for (StopButtonListener listener : stopButtonListeners) {
                        listener.stopButtonPressed(new StopButtonEvent(this, b));
                    }
                }
                log.info("Stop button depressed {}", String.format("0x%02X", b));
                stopDepressed = true;
            }
        } else {
            if (stopDepressed) {
                synchronized (stopButtonListeners) {
                    for (StopButtonListener listener : stopButtonListeners) {
                        listener.stopButtonReleased(new StopButtonEvent(this, b));
                    }
                }
                log.info("Stop button released {}", String.format("0x%02X", b));
                stopDepressed = false;
            }
        }
    }

    private void setSensorValue(Input input, byte high, byte low) {
        byte[] newValue = {high, low};
        byte[] oldValue = sensorData.put(input, newValue);
        if (!Arrays.equals(newValue, oldValue)) {
            SensorEvent<SensorValue> event =
                    new SensorEvent<>(input, oldValue, newValue, SensorValue.newSensorValue(high, low));
            synchronized (sensorListeners) {
                for (SensorListener listener : sensorListeners.get(input)) {
                    listener.sensorEventReceived(event);
                }
            }
        }
    }

    private boolean isChecksumValid(byte[] inputFrame) {
        int checksum = 0;
        for (byte b : inputFrame) {
            checksum += Byte.toUnsignedInt(b);
        }
        return (checksum & 0xFF) == 0xFF;
    }
}
