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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

/**
 * Handles serial events.
 */
class SerialListener implements SerialPortEventListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SerialPort serialPort;
    private final InputManager inputManager;
    private final boolean ignoreBadHandshake;

    private boolean handshakeSeen = false;
    private String handshake = "";

    SerialListener(SerialPort serialPort, InputManager inputManager) {
        this.serialPort = serialPort;
        this.inputManager = inputManager;
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
            if (availableBytes >= Protocol.FRAME_SIZE) {
                try {
                    byte[] buffer = serialPort.readBytes(Protocol.FRAME_SIZE);
                    inputManager.processInputSensors(buffer);
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
                if (handshake.endsWith(Protocol.HANDSHAKE_RESPONSE)) {
                    handshakeSeen = true;
                    log.info(handshake);
                    handshake = "";
                } else if (handshake.length() >= Protocol.HANDSHAKE_RESPONSE.length()) {
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
}
