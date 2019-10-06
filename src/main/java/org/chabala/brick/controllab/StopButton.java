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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Handle for stop button on a specific control lab instance. Obtain via {@link ControlLab#getStopButton()}.
 */
public class StopButton {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final Set<StopButtonListener> stopButtonListeners;
    private boolean stopDepressed = false;

    StopButton(InputManager inputManager) {
        stopButtonListeners = Collections.synchronizedSet(new HashSet<>(2));
        inputManager.setStopButtonCallback(this::processStopButton);
    }

    /**
     * Attach a listener for {@link StopButtonEvent}s.
     *
     * <p>Multiple listeners are allowed. A listener instance will only be registered
     * once even if it is added multiple times.
     * @param listener listener to add
     */
    public void addListener(StopButtonListener listener) {
        stopButtonListeners.add(listener);
    }

    /**
     * Remove a listener for {@link StopButtonEvent}s.
     * @param listener listener to remove
     */
    public void removeListener(StopButtonListener listener) {
        stopButtonListeners.remove(listener);
    }

    /**
     * Returns current status of stop button.
     * @return true if the stop button is in depressed state, false otherwise
     */
    public boolean isStopDepressed() {
        return stopDepressed;
    }

    @SuppressWarnings("squid:S2629")
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
}
