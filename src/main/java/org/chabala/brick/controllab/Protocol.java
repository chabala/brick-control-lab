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

/**
 * Constants relating to the control lab communication protocol.
 */
final class Protocol {
    static final String HANDSHAKE_CHALLENGE = "p\0###Do you byte, when I knock?$$$";
    static final String HANDSHAKE_RESPONSE =  "###Just a bit off the block!$$$";

    static final byte DISCONNECT = (byte) 0b01110000;

    static final byte OUTPUT_OFF = (byte) 0b10010000;
    static final byte OUTPUT_ON =  (byte) 0b10010001;

    private Protocol() {
        throw new UnsupportedOperationException();
    }
}
