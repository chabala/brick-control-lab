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
 * Temperature sensor.
 */
public class TemperatureSensor implements SensorValue {

    private SensorValue sensorValue;

    public TemperatureSensor(SensorValue sensorValue) {
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

    public String temperatureValue() {
        if (isPassive() && isEngaged()) {
            double degreesF = (760 - getAnalogValue()) / 4.4 + 32;
            return String.format("%.2f", degreesF);
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        return "TemperatureSensor{" +
                "value=" + String.format("0x%02X", getAnalogValue()) +
                ", status=" + getStatusCode() +
                ", temp=" + temperatureValue() +
                '}';
    }
}
