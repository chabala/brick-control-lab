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

import jssc.SerialPortEvent;

/**
 * SerialPortEventListener.
 */
interface SerialPortEventListener extends jssc.SerialPortEventListener {

    @Override
    default void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR()) {
            serialEventRXCHAR(event.getEventValue());
        }
    }

    /**
     * This event happens when data is available to be read from the port.
     * @param availableBytes count of bytes available on the serial port
     */
    void serialEventRXCHAR(int availableBytes);

    boolean isHandshakeSeen();

    void addStopButtonListener(StopButtonListener listener);

    void removeStopButtonListener(StopButtonListener listener);
}
