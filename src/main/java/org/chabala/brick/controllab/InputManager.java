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
 * Manages registration of input event listeners, and parsing input
 * data into events for those listeners.
 */
class InputManager {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Map<InputId, byte[]> sensorData;
    private final Map<InputId, Set<SensorListener>> sensorListeners;
    private final List<InputId> frameInputOrder =
            Arrays.asList(InputId.I4, InputId.I8, InputId.I3, InputId.I7,
                          InputId.I2, InputId.I6, InputId.I1, InputId.I5);
    private ByteConsumer processStopButton = null;

    InputManager() {
        sensorData = Collections.synchronizedMap(new EnumMap<>(InputId.class));
        sensorListeners = Collections.synchronizedMap(new EnumMap<>(InputId.class));
        Arrays.stream(InputId.values()).forEach(i -> {
            sensorData.put(i, new byte[] {0, 0});
            sensorListeners.put(i, Collections.synchronizedSet(new HashSet<>(2)));
        });
    }

    /**
     * Attach a listener for {@link SensorEvent}s.
     *
     * <p>Multiple listeners are allowed. A listener instance will only be registered
     * once even if it is added multiple times.
     * @param input    input to add the listener to
     * @param listener listener to add
     */
    void addSensorListener(InputId input, SensorListener listener) {
        sensorListeners.get(input).add(listener);
    }

    /**
     * Remove a listener for {@link SensorEvent}s.
     * @param input    input to remove the listener from
     * @param listener listener to remove
     */
    void removeSensorListener(InputId input, SensorListener listener) {
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
            //TODO: make event listener for output feedback
            log.info("Ports affected by last command {}",
                    OutputId.decodeByteToSet(inputFrame[lastCommandIndex]));
        }
        for (InputId in : frameInputOrder) {
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
        if (processStopButton != null) {
            processStopButton.accept(b);
        }
    }

    private void setSensorValue(InputId input, byte high, byte low) {
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

    void setStopButtonCallback(ByteConsumer processStopButton) {
        this.processStopButton = processStopButton;
    }

    @FunctionalInterface
    public interface ByteConsumer {

        /**
         * Performs this operation on the given argument.
         *
         * @param value the input argument
         */
        void accept(byte value);
    }
}
