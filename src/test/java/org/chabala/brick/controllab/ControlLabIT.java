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

import org.chabala.brick.controllab.sensor.LightSensorListener;
import org.chabala.brick.controllab.sensor.SensorListener;
import org.chabala.brick.controllab.sensor.TouchSensorListener;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;
import static javax.management.timer.Timer.ONE_SECOND;
import static org.awaitility.Awaitility.await;
import static org.chabala.brick.controllab.PortChooser.choosePort;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assume.assumeNoException;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Integration tests for the {@link ControlLab}.
 *
 * <p>These tests require a connection to the hardware. There's no way to validate
 * the behavior other than observing the control lab, so there are no assertions.
 */
@SuppressWarnings({"squid:S2699"})
public class ControlLabIT {
    private static final Logger log = getLogger(lookup().lookupClass());

    @Test
    public void testTurnOutputOff() throws Exception {
        try (ControlLab controlLab = ControlLab.newControlLab()) {
            try {
                controlLab.open(choosePort(controlLab));
            } catch (IOException e) {
                assumeNoException(e);
            }
            sleep(ONE_SECOND * 3);
            controlLab.turnOutputOn(OutputId.ALL);
            sleep(ONE_SECOND * 3);
            controlLab.turnOutputOff(EnumSet.range(OutputId.A, OutputId.D));
            sleep(ONE_SECOND * 3);
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
            sleep(ONE_SECOND);

            controlLab.turnOutputOn(OutputId.ALL);
            sleep(ONE_SECOND);

            controlLab.setOutputDirection(Direction.LEFT, EnumSet.of(OutputId.E, OutputId.F));
            sleep(ONE_SECOND);

            controlLab.getOutput(EnumSet.range(OutputId.E, OutputId.H)).reverseDirection();
            sleep(ONE_SECOND);

            controlLab.getOutput(OutputId.H).setDirection(Direction.RIGHT);
            sleep(ONE_SECOND);

            for (OutputId o : descendingRange(OutputId.H, OutputId.E)) {
                controlLab.getOutput(o).turnOff();
                sleep(ONE_SECOND);
            }

            controlLab.turnOutputOff(EnumSet.range(OutputId.A, OutputId.D));
            sleep(ONE_SECOND * 5);
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
            sleep(ONE_SECOND * 5);

            output.reverseDirection().setPowerLevel(PowerLevel.P8);
            sleep(ONE_SECOND * 5);
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
            sleep(ONE_SECOND);

            Output output = controlLab.getOutput(OutputId.A);
            output.setPowerLevel(PowerLevel.P1).turnOn();
            sleep(ONE_SECOND);

            for (PowerLevel p : EnumSet.range(PowerLevel.P2, PowerLevel.P8)) {
                output.setPowerLevel(p);
                sleep(ONE_SECOND);
            }

            output.setPowerLevel(PowerLevel.P0);
            sleep(ONE_SECOND);

            output.turnOn();
            sleep(ONE_SECOND);
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
            sleep(ONE_SECOND);
            controlLab.getInput(InputId.I1).addListener((TouchSensorListener) sensorEvent -> stop.set(true));
            Output outputs = controlLab.getOutput(OutputId.ALL);
            while (!stop.get()) {
                outputs.setPowerLevel(PowerLevel.P8).setDirection(Direction.RIGHT).turnOn();
                sleep(ONE_SECOND);
                if (stop.get()) {
                    return;
                }

                for (PowerLevel p : descendingRange(PowerLevel.P7, PowerLevel.P1)) {
                    outputs.setPowerLevel(p);
                    sleep(ONE_SECOND);
                    if (stop.get()) {
                        return;
                    }
                }

                outputs.reverseDirection();
                sleep(ONE_SECOND);
                if (stop.get()) {
                    return;
                }

                for (PowerLevel p : EnumSet.range(PowerLevel.P2, PowerLevel.P8)) {
                    outputs.setPowerLevel(p);
                    sleep(ONE_SECOND);
                    if (stop.get()) {
                        return;
                    }
                }
            }
        }
    }

    @SuppressWarnings({"squid:S2925"})
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            String msg = "Interrupted while sleeping";
            log.error(msg, e);
            Thread.currentThread().interrupt();
            assumeNoException(msg, e);
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
