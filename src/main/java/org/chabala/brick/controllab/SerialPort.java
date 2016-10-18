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

import jssc.SerialPortEventListener;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface to separate serial port clients from implementation.
 */
interface SerialPort extends Closeable {

    /**
     * The platform specific identifier for this port.
     * @return the port name
     */
    String getPortName();

    /**
     * Open the port to start a connection.
     * @throws IOException if any number of possible communication issues occurs
     */
    void openPort() throws IOException;

    /**
     * Returns true if the port is open.
     * @return true if the port is open
     */
    boolean isOpen();

    /**
     * Writes a single byte to the serial port, if it's open.
     * @param b the byte to write
     * @return true if the byte was successfully written
     * @throws IOException if any number of possible communication issues occurs
     */
    boolean write(byte b) throws IOException;

    /**
     * Writes a byte array to the serial port, if it's open.
     * @param bytes the byte array to write
     * @return true if the bytes were successfully written
     * @throws IOException if any number of possible communication issues occurs
     */
    boolean write(byte[] bytes) throws IOException;

    /**
     * Reads an array of bytes from the serial port.
     * @param byteCount the number of bytes to read
     * @return an array of bytes read from the serial port
     * @throws IOException if any number of possible communication issues occurs
     */
    byte[] readBytes(int byteCount) throws IOException;

    /**
     * Adds a listener to the serial port to handle incoming data and other events.
     * @param listener the listener to add
     * @throws IOException if any number of possible communication issues occurs
     */
    void addEventListener(SerialPortEventListener listener) throws IOException;

    /**
     * Disconnects from the serial port and releases any resources.
     * @throws IOException if any number of possible communication issues occurs
     */
    @Override
    void close() throws IOException;
}
