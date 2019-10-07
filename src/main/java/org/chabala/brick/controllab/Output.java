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
import java.util.EnumSet;
import java.util.Set;

/**
 * Handle for an output port (or group of ports) on a specific control lab instance.
 * Obtain via {@link ControlLab#getOutput(OutputId)} or {@link ControlLab#getOutputGroup(Set)}.
 */
public class Output {

    private final ControlLab controlLab;
    private final Set<OutputId> outputIdSet;

    Output(ControlLab controlLab, OutputId outputId) {
        this(controlLab, EnumSet.of(outputId));
    }

    Output(ControlLab controlLab, Set<OutputId> outputIdSet) {
        this.controlLab = controlLab;
        this.outputIdSet = outputIdSet;
    }

    public Output turnOff() throws IOException {
        controlLab.turnOutputOff(outputIdSet);
        return this;
    }

    public Output turnOn() throws IOException {
        controlLab.turnOutputOn(outputIdSet);
        return this;
    }

    public Output setDirection(Direction direction) throws IOException {
        controlLab.setOutputDirection(direction, outputIdSet);
        return this;
    }

    public Output reverseDirection() throws IOException {
        return setDirection(Direction.REVERSE);
    }

    public Output setPowerLevel(PowerLevel powerLevel) throws IOException {
        controlLab.setOutputPowerLevel(powerLevel, outputIdSet);
        return this;
    }
}
