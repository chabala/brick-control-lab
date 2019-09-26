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

/**
 * Touch sensor.
 */
public class TouchSensor implements SensorValue {

    private SensorValue sensorValue;

    public TouchSensor(SensorValue sensorValue) {
        this.sensorValue = sensorValue;
    }

    @Override
    public int getAnalogValue() {
        return sensorValue.getAnalogValue();
    }

    @Override
    public int getStatusCode() {
        return sensorValue.getStatusCode();
    }

    public String touchStatus() {
        switch (getStatusCode()) {
            case 0:
                return "depressed";
            case 8:
                return "released";
            case 24:
                return "releasing";
            case 16:
                return "depressing";
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "TouchSensor{" +
                "value=" + String.format("0x%02X", getAnalogValue()) +
                ", status=" + getStatusCode() +
                ", touch=" + touchStatus() +
                '}';
    }
}
