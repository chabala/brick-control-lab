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

import java.util.Random;

/**
 * Helper for choosing random enum instances for tests.
 */
public class RandomEnum {

    private Random random = new Random();

    public <T extends Enum<?>> T get(Class<T> clazz) {
        T[] enumValues = clazz.getEnumConstants();
        int ordinal = random.nextInt(enumValues.length);
        return enumValues[ordinal];
    }
}
