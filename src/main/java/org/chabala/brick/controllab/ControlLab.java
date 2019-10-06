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

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * This is the main interface for interacting with the LEGO® control lab, instances of
 * which can be created with {@link org.chabala.brick.controllab.ControlLab#newControlLab()}.
 *
 * <p>Usage example: <pre>   {@code
 *   ControlLab controlLab = ControlLab.newControlLab();
 *   List<String> availablePorts = controlLab.getAvailablePorts();
 *   try {
 *       controlLab.open(availablePorts.get(0));
 *       controlLab.setOutputPowerLevel(PowerLevel.P4, EnumSet.of(Output.A));
 *       controlLab.turnOutputOn(EnumSet.of(Output.A));
 *   } catch (IOException e) {
 *       e.printStackTrace();
 *   } finally {
 *       controlLab.close();
 *   }
 *       }</pre>
 */
public interface ControlLab extends Closeable, MutatesInputListeners {

    /**
     * Returns a new ControlLab instance.
     * @return a new ControlLab instance
     */
    static ControlLab newControlLab() {
        return new ControlLabImpl();
    }

    /**
     * List available serial ports on this machine. May change over time due to
     * hot pluggable USB serial port adapters and the like.
     *
     * @return a list of system specific string identifiers for serial ports
     */
    List<String> getAvailablePorts();

    /**
     * Opens a connection to the control lab on the specified port.
     * @param portName system specific serial port identifier
     * @throws IOException if any number of possible communication issues occurs
     */
    void open(String portName) throws IOException;

    /**
     * Stops sending power to the specified outputs.
     * @param outputs outputs to stop
     * @throws IOException if any number of possible communication issues occurs
     */
    void turnOutputOff(Set<OutputId> outputs) throws IOException;

    /**
     * Starts sending power to the specified outputs.
     * @param outputs outputs to start
     * @throws IOException if any number of possible communication issues occurs
     */
    void turnOutputOn(Set<OutputId> outputs) throws IOException;

    /**
     * Sets the {@link Direction} of the specified outputs. Direction may be changed
     * while the outputs are powered or unpowered.
     * @param direction desired direction
     * @param outputs which outputs to change
     * @throws IOException if any number of possible communication issues occurs
     */
    void setOutputDirection(Direction direction, Set<OutputId> outputs) throws IOException;

    /**
     * Sets the {@link PowerLevel} of the specified outputs. Power level may be changed
     * while the outputs are powered or unpowered.
     * @param powerLevel desired power level
     * @param outputs which outputs to change
     * @throws IOException if any number of possible communication issues occurs
     */
    void setOutputPowerLevel(PowerLevel powerLevel, Set<OutputId> outputs) throws IOException;

    /**
     * Return a handle for the output specified on this control lab instance.
     * @param outputId identifier of the desired output port
     * @return handle for the output specific to this control lab instance
     */
    Output getOutput(OutputId outputId);

    /**
     * Disconnects from the control lab and releases any resources.
     * @throws IOException if any number of possible communication issues occurs
     */
    @Override
    void close() throws IOException;
}
