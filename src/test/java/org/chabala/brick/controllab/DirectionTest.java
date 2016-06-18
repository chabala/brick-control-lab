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

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

/**
 * Testing {@link Direction}.
 */
public class DirectionTest {

    @Test
    public void testThereAreThreeDirections() throws Exception {
        assertThat(Direction.values(), arrayWithSize(3));
    }

    @Test
    public void testEveryDirectionHasAUniqueCode() throws Exception {
        Set<Byte> codes = new HashSet<>();
        for (Direction d : Direction.values()) {
            codes.add(d.getCode());
        }
        assertThat(codes, hasSize(Direction.values().length));
    }
}
