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

import java.lang.invoke.MethodHandles;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

/**
 * Intergration tests use this class to pick a serial port to connect to. If the
 * default selection doesn't suit your system, you can override it with a system
 * property: {@value OVERRIDE_TEST_PORT_PROPERTY}.
 */
final class PortChooser {
    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /** System property to override the IT serial port with a specific port identifier. */
    public static final String OVERRIDE_TEST_PORT_PROPERTY = "brick-control-lab.overrideTestPort";

    /**
     * Picks the first available serial port based on what the serial library finds
     * @param controlLab control lab instance for querying the serial library
     * @return a system specific serial port identifier
     */
    static String choosePort(ControlLab controlLab) {
        String overrideTestPort = System.getProperty(OVERRIDE_TEST_PORT_PROPERTY, "");
        if (overrideTestPort.isEmpty()) {
            List<String> availablePorts = controlLab.getAvailablePorts();
            assumeThat("a serial port should be available", availablePorts, is(not(empty())));
            log.debug("Available ports: {}", String.join(", ", availablePorts));
            return availablePorts.get(0);
        } else {
            log.debug("{} property is defined: {}", OVERRIDE_TEST_PORT_PROPERTY, overrideTestPort);
            return overrideTestPort;
        }
    }

    private PortChooser() {
        throw new UnsupportedOperationException();
    }
}
