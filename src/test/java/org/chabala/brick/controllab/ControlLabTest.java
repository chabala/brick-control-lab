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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Testing the {@link ControlLab}.
 */
public class ControlLabTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private SerialPortFactory portFactory;

    private ControlLab controlLab;

    @Before
    public void setUp() throws Exception {
        controlLab = new ControlLabImpl(portFactory);
    }

    @After
    public void tearDown() throws Exception {
        controlLab.close();
    }

    @Test
    public void testGetAvailablePorts() {
        when(portFactory.getAvailablePorts()).thenReturn(Arrays.asList("one", "two"));
        List<String> availablePorts = controlLab.getAvailablePorts();
        assertThat(availablePorts, is(not(empty())));
        assertThat(availablePorts, hasItems("one", "two"));
    }
}
