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

import java.util.EventListener;

/**
 * The listener interface for receiving stop button events.
 *
 * @see StopButtonEvent
 */
@FunctionalInterface
public interface StopButtonListener extends EventListener {

    /**
     * Invoked when the stop button is first pressed. The hardware will have
     * a flashing LED next to the stop button to indicate a paused state.
     * <p>
     * While the OEM implementation of the control lab software would halt
     * program operation in this state, the hardware does not hamper the flow
     * of input data and continues to track output commands, only the physical
     * outputs are prevented from receiving power. Actual behavior is left to
     * the implementor of the interface.
     *
     * @param stopButtonEvent The event that triggered this callback.
     */
    void stopButtonPressed(StopButtonEvent stopButtonEvent);

    /**
     * Invoked when the stop button is disengaged. The hardware will have
     * an unlit LED next to the stop button to indicate the computer is
     * connected. Any outputs that were powered prior to the stop button
     * being pressed will return to their prior state. Any output commands
     * issued while in the stopped state will also take effect.
     * <p>
     * The default implementation is no-op.
     *
     * @param stopButtonEvent The event that triggered this callback.
     */
    default void stopButtonReleased(StopButtonEvent stopButtonEvent) {
    }
}
