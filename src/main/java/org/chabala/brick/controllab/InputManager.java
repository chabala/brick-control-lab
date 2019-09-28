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

/**
 * Manages registration of input and stop button event listeners, and parsing input
 * data into events for those listeners.
 */
class InputManager implements MutatesInputListeners {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<StopButtonListener> stopButtonListeners;
    private final Map<Input, byte[]> sensorData;
    private final Map<Input, Set<SensorListener>> sensorListeners;
    private final List<Input> frameInputOrder =
            Arrays.asList(Input.I4, Input.I8, Input.I3, Input.I7, Input.I2, Input.I6, Input.I1, Input.I5);
    private boolean stopDepressed = false;

    InputManager() {
        stopButtonListeners = Collections.synchronizedSet(new HashSet<>(2));
        sensorData = Collections.synchronizedMap(new EnumMap<>(Input.class));
        sensorListeners = Collections.synchronizedMap(new EnumMap<>(Input.class));
        Arrays.stream(Input.values()).forEach(i -> {
            sensorData.put(i, new byte[] {0, 0});
            sensorListeners.put(i, Collections.synchronizedSet(new HashSet<>(2)));
        });
    }

    /** {@inheritDoc} */
    @Override
    public void addStopButtonListener(StopButtonListener listener) {
        stopButtonListeners.add(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeStopButtonListener(StopButtonListener listener) {
        stopButtonListeners.remove(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void addSensorListener(Input input, SensorListener listener) {
        sensorListeners.get(input).add(listener);
    }

    /** {@inheritDoc} */
    @Override
    public void removeSensorListener(Input input, SensorListener listener) {
        sensorListeners.get(input).remove(listener);
    }

    /**
     * Expects 19 bytes of data.
     * @param inputFrame byte array of 19 bytes
     */
    void processInputSensors(byte[] inputFrame) throws IOException {
        int frameIndex = 0;
        if (inputFrame.length != Protocol.FRAME_SIZE) {
            StringBuilder sb = new StringBuilder();
            for (byte b : inputFrame) {
                sb.append(String.format("0x%02X ", b));
            }
            throw new IOException("Expected 19 bytes, got " + inputFrame.length + " - " + sb.toString());
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

    private boolean isChecksumValid(byte[] inputFrame) {
        int checksum = 0;
        for (byte b : inputFrame) {
            checksum += Byte.toUnsignedInt(b);
        }
        return (checksum & 0xFF) == 0xFF;
    }

    private void processStopButton(byte b) {
        if (0x00 != b) {
            if (!stopDepressed) {
                StopButtonEvent event = new StopButtonEvent(this, b);
                synchronized (stopButtonListeners) {
                    stopButtonListeners.forEach(l -> l.stopButtonPressed(event));
                }
                log.info("Stop button depressed {}", String.format("0x%02X", b));
                stopDepressed = true;
            }
        } else {
            if (stopDepressed) {
                StopButtonEvent event = new StopButtonEvent(this, b);
                synchronized (stopButtonListeners) {
                    stopButtonListeners.forEach(l -> l.stopButtonReleased(event));
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
                sensorListeners.get(input).forEach(l -> l.sensorEventReceived(event));
            }
        }
    }
}
