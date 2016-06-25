# brick-control-lab

Library for controlling the [LEGO® control lab interface][1].

[![Build Status][2]][3]
[![Coverage Status][7]][8]
[![Codacy Badge][5]][6]
[![License: GNU LGPL 3.0][4]](http://www.gnu.org/licenses/lgpl-3.0.en.html)

[![Maven Central][16]][17]
[![Dependency Status][12]][13]

### What is the control lab?

[![control lab][9]][10]

The control lab, also known as Interface B, was sold by the educational division of LEGO® in the DACTA® product line.
It was sold as part of set 9751, part number 70909. It came in both a PC and a Mac version, with different serial cables
and software, though the interface boxes are identical.

It has eight controllable outputs and eight sensor inputs, along with another always on output, and a stop button to
halt the running program.
It is compatible with elements from the 9 volt electric system, including motors, lights and sound blocks.
Compatible sensors include touch, temperature, light, and rotation. Many elements from the later [MINDSTORMS® RCX][11]
product can also be used.

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

The solution is a relatively new library called [Java Simple Serial Connector][15] (jSSC). While the future development
of the library is unclear, it has a release on Maven Central that allows cross platform serial port access without any
JDK modifications.

### Usage

To use brick-control-lab with Maven, add the following to your pom:

```xml
<dependency>
  <groupId>org.chabala.brick</groupId>
  <artifactId>brick-control-lab</artifactId>
  <version>0.1.0</version>
</dependency>
```

Refer to the Maven generated site for apidocs: https://chabala.github.io/brick-control-lab/

Some good examples of usage can be found in the
[integration tests](src/test/java/org/chabala/brick/controllab/ControlLabIT.java).

### Prerequisites to build from source
* Java 8
* Maven 3

### Building
Run ```mvn clean verify``` to clean, compile, test, and produce the artifact.

### Licensing
Licensed under [GNU Lesser General Public License 3.0](http://www.gnu.org/licenses/lgpl-3.0.en.html)

### References

* <http://www.lgauge.com/technic/LEGOInterfaceB/9751.htm>
* <http://www.blockcad.net/dacta/>
* <http://web.archive.org/web/20071130212114/http://troyda.eas.muohio.edu/paper2.html>

[1]: http://www.peeron.com/inv/sets/9751-1
[2]: https://travis-ci.org/chabala/brick-control-lab.svg?branch=master
[3]: https://travis-ci.org/chabala/brick-control-lab
[4]: http://img.shields.io/badge/license-GNU_LGPL_3.0-brightgreen.svg
[5]: https://api.codacy.com/project/badge/Grade/f05f0d18f49a48659b1066884a7fef68
[6]: https://www.codacy.com/app/chabala/brick-control-lab
[7]: https://coveralls.io/repos/github/chabala/brick-control-lab/badge.svg?branch=master
[8]: https://coveralls.io/github/chabala/brick-control-lab?branch=master
[9]: http://www.bricklink.com/SL/9751-1.jpg
[10]: http://www.bricklink.com/catalogItemPic.asp?S=9751-1
[11]: https://en.wikipedia.org/wiki/Lego_Mindstorms#RCX
[12]: https://www.versioneye.com/user/projects/575c4fd77757a0003bd4c053/badge.svg?style=flat
[13]: https://www.versioneye.com/user/projects/575c4fd77757a0003bd4c053
[14]: https://en.wikibooks.org/wiki/Serial_Programming/Serial_Java
[15]: https://github.com/scream3r/java-simple-serial-connector
[16]: https://maven-badges.herokuapp.com/maven-central/org.chabala.brick/brick-control-lab/badge.svg
[17]: https://maven-badges.herokuapp.com/maven-central/org.chabala.brick/brick-control-lab

LEGO®, DACTA®, TECHNIC®, and MINDSTORMS® are trademarks and/or copyrights of the LEGO Group,
which does not sponsor, authorize or endorse this software.
