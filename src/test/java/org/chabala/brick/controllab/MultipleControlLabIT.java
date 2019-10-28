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

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static javax.management.timer.Timer.ONE_SECOND;
import static org.awaitility.Awaitility.await;
import static org.chabala.brick.controllab.PortChooser.choosePort;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeNoException;

/**
 * Integration tests for working with multiple control labs.
 *
 * <p>These tests require a connection to the hardware. They expect
 * four control labs to be connected.
 */
@SuppressWarnings({"squid:S2699","squid:S2925"})
public class MultipleControlLabIT {

    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int AVAILABLE_CONTROL_LABS = 4;

    /**
     * Attempts to connect to every serial port in turn, connect to a control lab,
     * and waits for the stop button to be pressed before moving on.
     * @throws Exception on any issue with the test
     */
    @Ignore("Requires interaction with stop button to complete, only run manually")
    @Test
    public void testPortIdentification() throws Exception {
        for (int portPosition = 1; portPosition <= AVAILABLE_CONTROL_LABS; portPosition++) {
            AtomicBoolean stop = new AtomicBoolean(false);
            try (ControlLab controlLab = ControlLab.newControlLab()) {
                try {
                    controlLab.open(choosePort(controlLab, portPosition));
                } catch (IOException e) {
                    assumeNoException(e);
                }
                controlLab.getStopButton().addListener(stopButtonEvent -> stop.set(true));
                await().forever().until(stop::get);
            }
        }
    }

    /**
     * Starts all control labs and iterates through turning on their outputs in unison.
     * @throws Exception on any issue with the test
     */
    @Test
    public void testOperatingInUnison() throws Exception {
        Set<ControlLab> controlLabs = new HashSet<>();
        try {
            for (int portPosition = 1; portPosition <= AVAILABLE_CONTROL_LABS; portPosition++) {
                ControlLab controlLab = ControlLab.newControlLab();
                controlLabs.add(controlLab);
                try {
                    controlLab.open(choosePort(controlLab, portPosition));
                } catch (IOException e) {
                    assumeNoException(e);
                }
            }
            OutputId prev = OutputId.H;
            for (OutputId next : OutputId.ALL) {
                switchOutput(controlLabs, prev, next);
                prev = next;
            }
        } finally {
            controlLabs.parallelStream().forEach(c -> {
                try {
                    c.close();
                } catch (IOException e) {
                    assumeNoException(e);
                }
            });
        }
    }

    private void switchOutput(Set<ControlLab> controlLabs, OutputId off, OutputId on) throws InterruptedException {
        controlLabs.parallelStream().forEach(c -> {
            try {
                c.getOutput(off).turnOff();
                c.getOutput(on).turnOn();
            } catch (IOException e) {
                assumeNoException(e);
            }
        });
        Thread.sleep(ONE_SECOND / 2);
    }

    /**
     * Example of advanced coordination of multiple control labs, activating the motors in
     * a chasing pattern. To use, arrange the control labs in a grid with two rows, start
     * the test, wait for all the control labs show they are connected, then press each
     * stop button in a clockwise direction from the top left. Pressing any stop button
     * after the pattern has started will end the test.
     * @throws Exception on any issue with the test
     */
    @Ignore("Requires interaction with stop button to complete, only run manually")
    @Test
    public void testOperatingInTandem() throws Exception {
        List<ControlLab> controlLabs = new ArrayList<>();
        AtomicBoolean stop = new AtomicBoolean(false);
        List<String> portNameOrder = getSerialPortsInOrderOfStopButtonPressed();
        log.info("Ports in order pressed: {}", String.join(", ", portNameOrder));
        assertThat(portNameOrder, hasSize(AVAILABLE_CONTROL_LABS));
        try {
            for (String portName : portNameOrder) {
                ControlLab controlLab = ControlLab.newControlLab();
                controlLabs.add(controlLab);
                try {
                    controlLab.open(portName);
                } catch (IOException e) {
                    assumeNoException(e);
                }
                controlLab.getStopButton().addListener(event -> stop.set(true));
            }
            List<Output> outputOrder = getOutputOrder(controlLabs);
            Output prev = null;
            Output next = null;
            Iterator<Output> outputIterator = new CyclicalIterator<>(outputOrder);
            while (outputIterator.hasNext()) {
                next = outputIterator.next();
                next.turnOn();
                if (prev != null) {
                    prev.turnOff();
                }
                prev = next;
                Thread.sleep(ONE_SECOND / 8);
                if (stop.get()) {
                    break;
                }
            }
            if (next != null) {
                next.turnOff();
            }
        } finally {
            controlLabs.parallelStream().forEach(c -> {
                try {
                    c.close();
                } catch (IOException e) {
                    assumeNoException(e);
                }
            });
        }
    }

    private List<Output> getOutputOrder(List<ControlLab> controlLabs) {
        List<Output> outputOrder = new ArrayList<>();
        List<EnumSet<OutputId>> columnOrder = Arrays.asList(
            EnumSet.of(OutputId.A, OutputId.E),
            EnumSet.of(OutputId.B, OutputId.F),
            EnumSet.of(OutputId.C, OutputId.G),
            EnumSet.of(OutputId.D, OutputId.H)
        );
        int splitIndex = AVAILABLE_CONTROL_LABS / 2;
        List<ControlLab> leftToRight = controlLabs.subList(0, splitIndex);
        List<ControlLab> rightToLeft = controlLabs.subList(splitIndex, controlLabs.size());
        for (ControlLab controlLab : leftToRight) {
            for (EnumSet<OutputId> column : columnOrder) {
                outputOrder.add(controlLab.getOutput(column));
            }
        }
        Collections.reverse(columnOrder);
        for (ControlLab controlLab : rightToLeft) {
            for (EnumSet<OutputId> column : columnOrder) {
                outputOrder.add(controlLab.getOutput(column));
            }
        }
        return outputOrder;
    }

    /**
     * Cyclical iterator, no mutation supported. Easier than making a circular
     * linked list for traversal only.
     * @param <E> element contained by the iterable
     */
    private final class CyclicalIterator<E> implements Iterator<E> {

        private final Iterable<E> iterable;
        private Iterator<E> iterator;

        private CyclicalIterator(Iterable<E> iterable) {
            this.iterable = iterable;
            iterator = iterable.iterator();
        }

        /**
         * {@inheritDoc}
         * <p>Always {@code true}, unless the initial iterable was empty.
         * This method also handles mutation of the iterator reference when
         * the underlying iterator reaches its end.
         * @return {@inheritDoc}
         */
        @Override
        public boolean hasNext() {
            boolean hasNext = iterator.hasNext();
            if (!hasNext) {
                iterator = iterable.iterator();
                hasNext = iterator.hasNext();
            }
            return hasNext;
        }

        /**
         * {@inheritDoc}
         * <p>Guaranteed to return an element, as long as initial iterable was
         * not empty.
         * @return {@inheritDoc}
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return iterator.next();
        }
    }

    /**
     * Starts all control labs and records the order that their stop buttons are pressed.
     * @return list of system specific port names
     * @throws InterruptedException if interrupted
     */
    private List<String> getSerialPortsInOrderOfStopButtonPressed() throws InterruptedException {
        List<String> portNameOrder = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch portIdLatch = new CountDownLatch(AVAILABLE_CONTROL_LABS);
        List<ControlLab> controlLabs = Stream.generate(ControlLab::newControlLab)
                .limit(AVAILABLE_CONTROL_LABS)
                .parallel()
                .peek(controlLab -> controlLab.getStopButton().addListener(event -> {
                    portNameOrder.add(controlLab.getConnectedPortName());
                    try {
                        controlLab.close();
                    } catch (IOException e) {
                        assumeNoException(e);
                    } finally {
                        portIdLatch.countDown();
                    }
                })).collect(Collectors.toList());
        try {
            IntStream.rangeClosed(1, AVAILABLE_CONTROL_LABS)
                    .parallel()
                    .forEach(portPosition -> {
                        ControlLab controlLab = controlLabs.get(portPosition - 1);
                        try {
                            controlLab.open(choosePort(controlLab, portPosition));
                        } catch (IOException e) {
                            assumeNoException(e);
                        }
                    });
            portIdLatch.await();
        } finally {
            controlLabs.parallelStream().forEach(c -> {
                try {
                    c.close();
                } catch (IOException e) {
                    assumeNoException(e);
                }
            });
        }
        return portNameOrder;
    }
}
