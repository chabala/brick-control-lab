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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;

/**
 * Testing {@link InputId}.
 */
public class InputIdTest {

    private static final int activeInputs = 4;
    private static final int passiveInputs = 4;
    private static final int totalInputs = activeInputs + passiveInputs;

    @Test
    public void testThereAreEightInputs() {
        assertThat(InputId.values(), arrayWithSize(totalInputs));
    }

    @Test
    public void testThereAreFourPassiveInputs() {
        assertThat(Arrays.stream(InputId.values())
                   .map(InputId::getInputType)
                   .filter(InputType.PASSIVE::equals)
                   .collect(Collectors.toList()), hasSize(passiveInputs));
    }

    @Test
    public void testThereAreFourActiveInputs() {
        assertThat(Arrays.stream(InputId.values())
                   .map(InputId::getInputType)
                   .filter(InputType.ACTIVE::equals)
                   .collect(Collectors.toList()), hasSize(activeInputs));
    }
}
