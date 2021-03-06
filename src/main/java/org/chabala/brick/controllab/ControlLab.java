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

import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Set;

/**
 * This is the main interface for interacting with the LEGO® control lab, instances of
 * which can be created with {@link org.chabala.brick.controllab.ControlLab#newControlLab()}.
 *
 * <p>Usage example: <pre class="prettyprint lang-java">
 *    ControlLab controlLab = ControlLab.newControlLab();
 *    {@code List<String>} availablePorts = controlLab.getAvailablePorts();
 *    try {
 *        controlLab.open(availablePorts.get(0));
 *        controlLab.getOutput(OutputId.A).setPowerLevel(PowerLevel.P4).turnOn();
 *    } catch (IOException e) {
 *        e.printStackTrace();
 *    } finally {
 *        try {
 *            controlLab.close();
 *        } catch (IOException e) {
 *            e.printStackTrace();
 *        }
 *    }</pre>
 * <script src="https://cdn.jsdelivr.net/gh/google/code-prettify@master/loader/run_prettify.js"></script>
 */
public interface ControlLab extends Closeable {

    /**
     * Returns a new ControlLab instance.
     * @return a new ControlLab instance
     */
    static ControlLab newControlLab() {
        return new ControlLabImpl(
                LoggerFactory.getLogger(MethodHandles.lookup().lookupClass()));
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
     * Returns the system specific serial port identifier the control lab is
     * connected to, or empty string if not connected.
     * @return system specific serial port identifier or empty string
     */
    String getConnectedPortName();

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
     * Return a handle for multiple outputs on this control lab instance.
     * @param outputs identifiers of the desired output ports
     * @return handle for the outputs specific to this control lab instance
     */
    Output getOutput(Set<OutputId> outputs);

    /**
     * Return a handle for the stop button on this control lab instance.
     * @return handle for the stop button on this control lab instance
     */
    StopButton getStopButton();

    /**
     * Return a handle for the input specified on this control lab instance.
     * @param inputId identifier of the desired input port
     * @return handle for the input specific to this control lab instance
     */
    Input getInput(InputId inputId);

    /**
     * Disconnects from the control lab and releases any resources.
     * @throws IOException if any number of possible communication issues occurs
     */
    @Override
    void close() throws IOException;
}
