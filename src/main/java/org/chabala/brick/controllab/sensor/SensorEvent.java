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
package org.chabala.brick.controllab.sensor;

import org.chabala.brick.controllab.InputId;

import java.util.EventObject;
import java.util.function.Function;

import static org.chabala.brick.controllab.sensor.BinaryStringFormatter.printInBinary;

/**
 * The event triggered by receiving a {@link SensorValue} from an {@link InputId}.
 * @param <T> specific {@link SensorValue} subclass contained in this event
 */
public class SensorEvent<T extends SensorValue> extends EventObject {

    private final byte[] oldValue;
    private final byte[] newValue;
    private final T value;

    /**
     * Constructs a SensorEvent.
     *
     * @param input    The input port that triggered this event.
     * @param oldValue The last known value from this input, as a two byte array.
     * @param newValue The new value from this input, as a two byte array.
     * @param value    The new value wrapped in the SensorValue class.
     */
    public SensorEvent(InputId input, byte[] oldValue, byte[] newValue, T value) {
        super(input);
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.value = value;
    }

    /**
     * A copy constructor for subclasses, to easily wrap the {@link SensorValue} in a
     * more specific subclass.
     * @param sensorEvent   The SensorEvent to be copied
     * @param sensorWrapper The constructor method reference for a SensorValue subclass
     */
    public SensorEvent(SensorEvent<SensorValue> sensorEvent, Function<SensorValue, T> sensorWrapper) {
        this(sensorEvent.getInput(),
             sensorEvent.getOldValue(),
             sensorEvent.getNewValue(),
             sensorWrapper.apply(sensorEvent.getValue()));
    }

    /**
     * The input port that triggered this event.
     * @return the input port that triggered this event.
     */
    public InputId getInput() {
        return (InputId) getSource();
    }

    /**
     * The last known value from this input, prior to the event, as a two byte array.
     * @return the last known value from this input as a two byte array
     */
    public byte[] getOldValue() {
        return oldValue;
    }

    /**
     * The new value from this input, as a two byte array.
     * @return the new value from this input as a two byte array
     */
    public byte[] getNewValue() {
        return newValue;
    }

    /**
     * The new value from this input wrapped in the SensorValue class, or sensor specific subclass.
     * @return SensorValue class, or sensor specific subclass
     */
    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%1s value changed: %2s -> %3s aka %4s",
                source, printInBinary(oldValue), printInBinary(newValue), value);
    }
}
