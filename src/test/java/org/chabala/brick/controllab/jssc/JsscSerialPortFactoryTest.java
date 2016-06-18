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
package org.chabala.brick.controllab.jssc;

import org.chabala.brick.controllab.SerialPort;
import org.chabala.brick.controllab.SerialPortFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Testing {@link JsscSerialPortFactory}.
 */
public class JsscSerialPortFactoryTest {

    private SerialPortFactory serialPortFactory;

    @Before
    public void setUp() throws Exception {
        serialPortFactory = new JsscSerialPortFactory();
    }

    @Test
    public void getAvailablePorts() throws Exception {
        List<String> availablePorts = serialPortFactory.getAvailablePorts();
        assertThat(availablePorts, is(not(nullValue())));
    }

    @Test
    public void getSerialPort() throws Exception {
        SerialPort serialPort = serialPortFactory.getSerialPort("test");
        assertThat(serialPort, is(not(nullValue())));
        assertThat(serialPort, is(instanceOf(JsscSerialPort.class)));
    }
}
