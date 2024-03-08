# brick-control-lab

Library for controlling the [LEGO® control lab interface][1].

[![Build Status][2]][3]
[![Coverage Status][7]][8]
[![Codacy Badge][5]][6]
[![License: GNU LGPL 3.0][4]](https://www.gnu.org/licenses/lgpl-3.0.en.html)

[![Maven Central][16]][17]

### What is the control lab?

[![control lab][9]][10]

The control lab is a computer control interface for LEGO® electrical elements, like motors and sensors. It
is a predecessor to LEGO® MINDSTORMS® products, and is compatible with many of the elements that work with
the [MINDSTORMS® RCX][11] product. Also known as Interface&#160;B, it was sold by the educational division of LEGO®
in the DACTA® product line, as part of set 9751, part number 70909. It came in both a PC and a Mac version,
with different serial cables and software, though the interface boxes are identical.

While MINDSTORMS® products are battery powered, compact, and geared toward mobile robots, the control lab is
stationary, tethered to a power source and the computer that controls it. The advantage for the control lab
is that without the need to be compact it can connect to many more motors and sensors.

It has eight controllable outputs and eight sensor inputs, along with another always on output, and a stop
button to halt the running program. It is compatible with elements from the 9 volt electric system,
including motors, lights and sound blocks. Compatible sensors include touch, temperature, light, and rotation.

### What is brick-control-lab?

brick-control-lab is a Java library to allow programs to control the control lab programmatically. It is not the first
piece of hobby software to target working with the control lab, but it is the first with these goals:
* Java-based
* as a library (e.g. no user interface, used by other programs)
* usable from Maven

As software that uses a serial port, usability from Maven was a challenge. The standard Java Communications API
(JavaComm) never had a reference implementation that worked on all major platforms. There is an independent serial
library, RxTx, but neither it nor JavaComm are usable from Maven, as they have setup procedures that involve adding
files into the JRE on the target platform. See [Serial Java][14] for more details.

The solution is a relatively new library called [Java Simple Serial Connector][15] (jSSC). It has a release on Maven 
Central that allows cross-platform serial port access without any JRE modifications.

### Usage

Refer to the [Maven generated site](https://chabala.github.io/brick-control-lab/usage.html) for usage
instructions and additional documentation.

### Licensing
Licensed under [GNU Lesser General Public License 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html)

[1]: http://www.peeron.com/inv/sets/9751-1
[2]: https://github.com/chabala/brick-control-lab/actions/workflows/build.yml/badge.svg?branch=master
[3]: https://github.com/chabala/brick-control-lab/actions/workflows/build.yml
[4]: https://img.shields.io/badge/license-GNU_LGPL_3.0-brightgreen.svg
[5]: https://app.codacy.com/project/badge/Grade/1635550c03fb4874889f145d3d9bd237?branch=master
[6]: https://app.codacy.com/gh/chabala/brick-control-lab/dashboard
[7]: https://coveralls.io/repos/github/chabala/brick-control-lab/badge.svg?branch=master
[8]: https://coveralls.io/github/chabala/brick-control-lab?branch=master
[9]: https://www.bricklink.com/SL/9751-1.jpg
[10]: https://www.bricklink.com/catalogItemPic.asp?S=9751-1
[11]: https://en.wikipedia.org/wiki/Lego_Mindstorms#RCX
[14]: https://en.wikibooks.org/wiki/Serial_Programming/Serial_Java
[15]: https://github.com/java-native/jssc
[16]: https://maven-badges.herokuapp.com/maven-central/org.chabala.brick/brick-control-lab/badge.svg
[17]: https://maven-badges.herokuapp.com/maven-central/org.chabala.brick/brick-control-lab

<!---
Potential replacements for maven-badges.herokuapp.com
[16]: https://img.shields.io/maven-central/v/org.chabala.brick/brick-control-lab.svg
[17]: https://mvnrepository.com/artifact/org.chabala.brick/brick-control-lab
-->

LEGO®, DACTA®, TECHNIC®, and MINDSTORMS® are trademarks and/or copyrights of the LEGO Group,
which does not sponsor, authorize or endorse this software.
