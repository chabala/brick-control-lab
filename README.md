# brick-control-lab

Library for controlling the [LEGO® control lab interface][1].

[![Build Status][2]][3]
[![License: GNU LGPL][4]](http://www.gnu.org/licenses/lgpl-3.0.en.html)
[![Codacy Badge][5]][6]

### What is the control lab?

[![control lab][7]][8]

The control lab, also known as Interface B, was sold by the educational division of LEGO® in the DACTA® product line.
It was sold as part of set 9751, part number 70909. It came in both a PC and a Mac version, with different serial cables
and software, though the interface boxes are identical.

It has eight controllable outputs and eight sensor inputs, along with another always on output, and a stop button to
halt the running program.
It is compatible with elements from the 9 volt electric system, including motors, lights and sound blocks.
Compatible sensors include touch, temperature, light, and rotation. Many elements from the later [MINDSTORMS® RCX][9]
product can also be used.

### What is brick-control-lab?

brick-control-lab is a Java library to allow programs to control the control lab programmatically. It is not the first
piece of hobby software to target working with the control lab, but it is the first with these goals:
* Java-based
* as a library (e.g. no user interface, used by other programs)
* usable from Maven (TODO)

### Prerequisites to build from source
* Java 8
* Maven 3

### Building
Run ```mvn clean package``` to clean, compile, test, and produce the artifact.

### Licensing
Licensed under [GNU Lesser General Public License 3.0](http://www.gnu.org/licenses/lgpl-3.0.en.html)

[1]: http://www.peeron.com/inv/sets/9751-1
[2]: https://travis-ci.org/chabala/brick-control-lab.svg?branch=master
[3]: https://travis-ci.org/chabala/brick-control-lab
[4]: http://img.shields.io/badge/license-GNU_LGPL-brightgreen.svg?style=flat
[5]: https://api.codacy.com/project/badge/Grade/f05f0d18f49a48659b1066884a7fef68
[6]: https://www.codacy.com/app/chabala/brick-control-lab
[7]: http://www.bricklink.com/SL/9751-1.jpg
[8]: http://www.bricklink.com/catalogItemPic.asp?S=9751-1
[9]: https://en.wikipedia.org/wiki/Lego_Mindstorms#RCX

LEGO®, DACTA®, TECHNIC®, and MINDSTORMS® are trademarks and/or copyrights of the LEGO Group,
which does not sponsor, authorize or endorse this software.
