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

import org.chabala.brick.controllab.sensor.*;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static javax.management.timer.Timer.ONE_SECOND;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;
import static org.junit.Assume.assumeThat;

/**
 * Integration tests for the {@link ControlLab}.
 *
 * <p>These tests require a connection to the hardware. There's no way to validate
 * the behavior other than observing the control lab, so there are no assertions.
 */
@SuppressWarnings({"squid:S2699","squid:S2925"})
public class ControlLabIT {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
            controlLab.turnOutputOn(OutputId.ALL);
            Thread.sleep(ONE_SECOND * 3);
            controlLab.turnOutputOff(EnumSet.range(OutputId.A, OutputId.D));
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

            controlLab.turnOutputOn(OutputId.ALL);
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.LEFT, EnumSet.of(OutputId.E, OutputId.F));
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.REVERSE, EnumSet.of(OutputId.E, OutputId.F, OutputId.G, OutputId.H));
            Thread.sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.RIGHT, EnumSet.of(OutputId.H));
            Thread.sleep(ONE_SECOND);

            for (OutputId o : Arrays.asList(OutputId.H, OutputId.G, OutputId.F, OutputId.E)) {
                controlLab.turnOutputOff(EnumSet.of(o));
                Thread.sleep(ONE_SECOND);
            }

            controlLab.turnOutputOff(EnumSet.range(OutputId.A, OutputId.D));
            Thread.sleep(ONE_SECOND * 5);
        }
    }

    @Test
    public void testFluentOutputControl() throws Exception {
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            Output output = controlLab.getOutput(OutputId.A);
            output.setDirection(Direction.LEFT).setPowerLevel(PowerLevel.P2).turnOn();
            Thread.sleep(ONE_SECOND * 5);

            output.reverseDirection().setPowerLevel(PowerLevel.P8);
            Thread.sleep(ONE_SECOND * 5);
        }
    }

    @Ignore("Requires interaction with stop button to complete, only run manually")
    @Test
    public void testControlLabInputs() throws Exception {
        AtomicBoolean stop = new AtomicBoolean(false);
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            controlLab.getStopButton().addListener(stopButtonEvent -> stop.set(true));
            Map<InputId, String> lastTouchValues = Collections.synchronizedMap(new EnumMap<>(InputId.class));
            List<InputId> passiveInputs = Arrays.stream(InputId.values())
                                                .filter(i -> i.getInputType().equals(InputType.PASSIVE))
                                                .collect(Collectors.toList());
            passiveInputs.forEach(i -> lastTouchValues.put(i, ""));
            TouchSensorListener touchSensorListener = sensorEvent -> {
                InputId input = sensorEvent.getInput();
                String newValue = sensorEvent.getValue().touchStatus();
                if ("".equals(newValue)) {
                    return;
                }
                String oldValue = lastTouchValues.put(input, newValue);
                if (!newValue.equals(oldValue)) {
                    log.info("{} value changed: {}", input, newValue);
                }
            };
            for (InputId passiveInput : passiveInputs) {
                controlLab.getInput(passiveInput).addListener(touchSensorListener);
            }
            LightSensorListener lightSensorListener = sensorEvent -> {
                InputId input = sensorEvent.getInput();
                log.info("{} value changed: {}", input, sensorEvent.getValue());
            };
            Arrays.stream(InputId.values())
                  .filter(i -> i.getInputType().equals(InputType.ACTIVE))
                  .map(controlLab::getInput)
                  .forEach(i -> i.addListener(lightSensorListener));

            await().forever().until(stop::get);
        }
    }

    @Ignore("Requires interaction with stop button to complete, only run manually")
    @Test
    public void testControlLabInputsRaw() throws Exception {
        AtomicBoolean stop = new AtomicBoolean(false);
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            controlLab.getStopButton().addListener(stopButtonEvent -> stop.set(true));
            SensorListener sensorListener = sensorEvent ->
                    log.info("{} value changed: {}", sensorEvent.getInput(), sensorEvent.getValue());
            Arrays.stream(InputId.values())
                  .map(controlLab::getInput)
                  .forEach(i -> i.addListener(sensorListener));

            await().forever().until(stop::get);
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

            Output output = controlLab.getOutput(OutputId.A);
            output.setPowerLevel(PowerLevel.P1).turnOn();
            Thread.sleep(ONE_SECOND);

            for (PowerLevel p : EnumSet.range(PowerLevel.P2, PowerLevel.P8)) {
                output.setPowerLevel(p);
                Thread.sleep(ONE_SECOND);
            }

            output.setPowerLevel(PowerLevel.P0);
            Thread.sleep(ONE_SECOND);

            output.turnOn();
            Thread.sleep(ONE_SECOND);
        }
    }

    @Ignore("Requires interaction with stop button to complete, only run manually")
    @Test
    public void testRunUntilStopButtonPressed() throws Exception {
        AtomicBoolean stop = new AtomicBoolean(false);
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            controlLab.getStopButton().addListener(stopButtonEvent -> stop.set(true));
            await().forever().until(stop::get);
        }
    }

    /**
     * This test starts all outputs at the highest power level, gradually decreases to the minimum power
     * level, then changes the direction and increases back to maximum. This continues until a touch sensor
     * on Input 1 is pressed. It can be used to test an output device to see how it behaves under different
     * power supply levels.
     * @throws Exception on any issue with the test
     */
    @Ignore("Requires interaction with Input 1 to complete, only run manually")
    @Test
    public void testMotorInBothDirections() throws Exception {
        AtomicBoolean stop = new AtomicBoolean(false);
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            Thread.sleep(ONE_SECOND);
            controlLab.getInput(InputId.I1).addListener((TouchSensorListener) sensorEvent -> stop.set(true));
            Set<OutputId> outputSet = OutputId.ALL;
            while (!stop.get()) {
                controlLab.setOutputPowerLevel(PowerLevel.P8, outputSet);
                controlLab.setOutputDirection(Direction.RIGHT, outputSet);
                controlLab.turnOutputOn(outputSet);
                Thread.sleep(ONE_SECOND);
                if (stop.get()) {
                    return;
                }

                for (PowerLevel p : descendingRange(PowerLevel.P1, PowerLevel.P7)) {
                    controlLab.setOutputPowerLevel(p, outputSet);
                    Thread.sleep(ONE_SECOND);
                    if (stop.get()) {
                        return;
                    }
                }

                controlLab.setOutputDirection(Direction.REVERSE, outputSet);
                Thread.sleep(ONE_SECOND);
                if (stop.get()) {
                    return;
                }

                for (PowerLevel p : EnumSet.range(PowerLevel.P2, PowerLevel.P8)) {
                    controlLab.setOutputPowerLevel(p, outputSet);
                    Thread.sleep(ONE_SECOND);
                    if (stop.get()) {
                        return;
                    }
                }
            }
        }
    }

    /**
     * Returns a list of enums in descending order. {@link EnumSet#range(Enum, Enum)} provides
     * an iterator for ascending order, but descending requires wrapping in a list and reversing
     * the collection.
     */
    private <E extends Enum<E>> List<E> descendingRange(E from, E to) {
        EnumSet<E> rangeSet;
        if (from.compareTo(to) > 0) {
            rangeSet = EnumSet.range(to, from);
        } else {
            rangeSet = EnumSet.range(from, to);
        }
        List<E> rangeList = new ArrayList<>(rangeSet);
        Collections.reverse(rangeList);
        return rangeList;
    }

    @Test
    public void testDescendingRange() throws Exception {
        List<PowerLevel> powerLevelList = new ArrayList<>(EnumSet.range(PowerLevel.P1, PowerLevel.P3));
        assertThat(powerLevelList, contains(PowerLevel.P1, PowerLevel.P2, PowerLevel.P3));
        assertThat(powerLevelList.get(0), is(PowerLevel.P1));
        assertThat(powerLevelList.get(2), is(PowerLevel.P3));

        powerLevelList = descendingRange(PowerLevel.P1, PowerLevel.P3);
        assertThat(powerLevelList, contains(PowerLevel.P3, PowerLevel.P2, PowerLevel.P1));
        assertThat(powerLevelList.get(0), is(PowerLevel.P3));
        assertThat(powerLevelList.get(2), is(PowerLevel.P1));

        powerLevelList = descendingRange(PowerLevel.P3, PowerLevel.P1);
        assertThat(powerLevelList, contains(PowerLevel.P3, PowerLevel.P2, PowerLevel.P1));
        assertThat(powerLevelList.get(0), is(PowerLevel.P3));
        assertThat(powerLevelList.get(2), is(PowerLevel.P1));
    }
}
