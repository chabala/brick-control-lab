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

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static javax.management.timer.Timer.ONE_SECOND;
import static org.hamcrest.Matchers.*;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeThat;

/**
 * Integration tests for the {@link ControlLab}.
 *
 * <p>These tests require a connection to the hardware. There's no way to validate
 * the behavior other than observing the control lab, so there are no assertions.
 */
public class ControlLabIT {
    private static final Logger log = LoggerFactory.getLogger(ControlLabIT.class);

    private String choosePort(ControlLab controlLab) {
        List<String> availablePorts = controlLab.getAvailablePorts();
        assumeThat("a serial port should be available", availablePorts, is(not(empty())));
        log.debug("Available ports: {}", String.join(", ", availablePorts));
        return availablePorts.get(0);
    }

    @Test
    public void testTurnOutputOff() throws Exception {
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            Thread.sleep(ONE_SECOND * 3);
            controlLab.turnOutputOn(Output.ALL);
            Thread.sleep(ONE_SECOND * 3);
            controlLab.turnOutputOff(EnumSet.range(Output.A, Output.D));
            Thread.sleep(ONE_SECOND * 3);
        }
    }

    @Test
    public void testControlLabOutputs() throws Exception {
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            Thread.sleep(ONE_SECOND);

            controlLab.turnOutputOn(Output.ALL);
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.LEFT, EnumSet.of(Output.E, Output.F));
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.REVERSE, EnumSet.of(Output.E, Output.F, Output.G, Output.H));
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.RIGHT, EnumSet.of(Output.H));
            Thread.sleep(ONE_SECOND);

            for (Output o : Arrays.asList(Output.H, Output.G, Output.F, Output.E)) {
                controlLab.turnOutputOff(EnumSet.of(o));
                Thread.sleep(ONE_SECOND);
            }

            controlLab.turnOutputOff(EnumSet.range(Output.A, Output.D));
            Thread.sleep(ONE_SECOND * 5);
        }
    }

    @Test
    public void testOutputPowerLevels() throws Exception {
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputPowerLevel(PowerLevel.P1, EnumSet.of(Output.A));
            controlLab.turnOutputOn(EnumSet.of(Output.A));
            Thread.sleep(ONE_SECOND);
            controlLab.turnOutputOn(EnumSet.of(Output.A));

            for (PowerLevel p : EnumSet.range(PowerLevel.P2, PowerLevel.P8)) {
                controlLab.setOutputPowerLevel(p, EnumSet.of(Output.A));
                Thread.sleep(ONE_SECOND);
            }

            controlLab.setOutputPowerLevel(PowerLevel.P0, EnumSet.of(Output.A));
            Thread.sleep(ONE_SECOND);

            controlLab.turnOutputOn(EnumSet.of(Output.A));
            Thread.sleep(ONE_SECOND);
        }
    }

    @Ignore
    @Test
    public void testRunUntilStopButtonPressed() throws Exception {
        AtomicBoolean stop = new AtomicBoolean(false);
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            controlLab.addStopButtonListener(new StopButtonListener() {
                @Override
                public void stopButtonPressed(StopButtonEvent stopButtonEvent) {
                    stop.set(true);
                }
            });
            while (!stop.get()) {
                Thread.sleep(ONE_SECOND / 2);
            }
        }
    }
}
