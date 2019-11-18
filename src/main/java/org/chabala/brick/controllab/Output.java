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

import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Handle for an output port (or group of ports) on a specific control lab instance.
 * Obtain via {@link ControlLab#getOutput(OutputId)} or {@link ControlLab#getOutput(Set)}.
 */
public class Output {

    private final ControlLab controlLab;
    private final Set<OutputId> outputIdSet;

    /**
     * Called internally by {@link ControlLab#getOutput(OutputId)}.
     * @param controlLab the control lab this output is a handle to
     * @param outputId the output port ID on that control lab
     */
    Output(ControlLab controlLab, OutputId outputId) {
        this(controlLab, EnumSet.of(outputId));
    }

    /**
     * Called internally by {@link ControlLab#getOutput(Set)}.
     * @param controlLab the control lab this output is a handle to
     * @param outputIdSet the output IDs this output should reference
     */
    Output(ControlLab controlLab, Set<OutputId> outputIdSet) {
        this.controlLab = controlLab;
        this.outputIdSet = outputIdSet;
    }

    /**
     * Returns set of {@link OutputId}s this {@link Output} relates to. In normal
     * usage, this is unlikely to be called as one would obtain the {@link Output}
     * from {@link ControlLab#getOutput(OutputId)} and immediately chain one of the
     * fluent method calls. But it can useful to inspect the {@link OutputId}s if the
     * {@link Output} reference is retained and seperated from the creation site.
     * @return set of {@link OutputId}s this {@link Output} relates to
     */
    public Set<OutputId> getOutputIdSet() {
        return Collections.unmodifiableSet(outputIdSet);
    }

    /**
     * Stops sending power to this output.
     * @return self reference to the output for chaining
     * @throws IOException if any number of possible communication issues occurs
     */
    public Output turnOff() throws IOException {
        controlLab.turnOutputOff(outputIdSet);
        return this;
    }

    /**
     * Starts sending power to this output.
     * @return self reference to the output for chaining
     * @throws IOException if any number of possible communication issues occurs
     */
    public Output turnOn() throws IOException {
        controlLab.turnOutputOn(outputIdSet);
        return this;
    }

    /**
     * Sets the {@link Direction} of this output. Direction may be changed
     * while the output is powered or unpowered.
     * @param direction desired direction
     * @return self reference to the output for chaining
     * @throws IOException if any number of possible communication issues occurs
     */
    public Output setDirection(Direction direction) throws IOException {
        controlLab.setOutputDirection(direction, outputIdSet);
        return this;
    }

    /**
     * Reverses the {@link Direction} of this output. This is a convenience method
     * that is the same as
     * {@link Output#setDirection(Direction) setDirection(}{@link Direction#REVERSE Direction.REVERSE)}.
     * @return self reference to the output for chaining
     * @throws IOException if any number of possible communication issues occurs
     */
    public Output reverseDirection() throws IOException {
        return setDirection(Direction.REVERSE);
    }

    /**
     * Sets the {@link PowerLevel} of this output. Power level may be changed
     * while the output is powered or unpowered.
     * @param powerLevel desired power level
     * @return self reference to the output for chaining
     * @throws IOException if any number of possible communication issues occurs
     */
    public Output setPowerLevel(PowerLevel powerLevel) throws IOException {
        controlLab.setOutputPowerLevel(powerLevel, outputIdSet);
        return this;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Output{" +
                "controlLab=" + controlLab +
                ", outputIdSet=" + outputIdSet +
                '}';
    }
}
