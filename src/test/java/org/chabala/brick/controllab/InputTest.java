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

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Testing {@link Input}.
 */
public class InputTest {

    @Test
    public void testThereAreEightInputs() throws Exception {
        assertThat(Input.values(), arrayWithSize(8));
    }

    @Test
    public void testThereAreFourPassiveInputs() throws Exception {
        assertThat(Arrays.stream(Input.values())
                   .map(Input::getInputType)
                   .filter(InputType.PASSIVE::equals)
                   .collect(Collectors.toList()), hasSize(4));
    }

    @Test
    public void testThereAreFourActiveInputs() throws Exception {
        assertThat(Arrays.stream(Input.values())
                   .map(Input::getInputType)
                   .filter(InputType.ACTIVE::equals)
                   .collect(Collectors.toList()), hasSize(4));
    }
}
