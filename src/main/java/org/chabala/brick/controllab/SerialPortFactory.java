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

import java.util.List;

/**
 * Interface to separate listing and obtaining serial ports from implementation.
 */
public interface SerialPortFactory {
    /**
     * List available serial ports on this machine. May change over time due to
     * hot pluggable USB serial port adapters and the like.
     *
     * @return a list of system specific string identifiers for serial ports
     */
    List<String> getAvailablePorts();

    /**
     * Obtain a {@link SerialPort} for the specified serial port. This will succeed
     * even if the port name is invalid, though operations on that object may fail.
     *
     * @param portName a system specific string identifier for a serial port
     * @return a serial port object for the specified port
     */
    SerialPort getSerialPort(String portName);
}
