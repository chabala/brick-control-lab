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

/**
 * Identifiers for the input ports on the control lab.
 * <p>
 * Inputs 1-4 are passive, they read the resistence of the sensor
 * connected to them. They are colored yellow on the control lab.
 * <p>
 * Inputs 5-8 are active, they supply power to the connected sensor
 * in order for it to work. They are colored blue on the control lab.
 */
public enum Input {
    /** Input 1. */ I1,
    /** Input 2. */ I2,
    /** Input 3. */ I3,
    /** Input 4. */ I4,
    /** Input 5. */ I5,
    /** Input 6. */ I6,
    /** Input 7. */ I7,
    /** Input 8. */ I8
}
