## About brick-control-lab

Library for controlling the [LEGO® control lab interface][1].

[1]: http://www.peeron.com/inv/sets/9751-1

### What is the control lab?

<a href="https://www.bricklink.com/catalogItemPic.asp?S=9751-1" class="nodecorate">
<img src="https://web.archive.org/web/20200103084341if_/https://www.bricklink.com/SL/9751-1.jpg" 
alt="control lab" align="right" width="372" height="240"/>
</a>

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

[9]: https://www.bricklink.com/SL/9751-1.jpg
[10]: https://www.bricklink.com/catalogItemPic.asp?S=9751-1
[11]: https://en.wikipedia.org/wiki/Lego_Mindstorms#RCX

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
JRE modifications.

[14]: https://en.wikibooks.org/wiki/Serial_Programming/Serial_Java
[15]: https://github.com/scream3r/java-simple-serial-connector

### Licensing
Licensed under [GNU Lesser General Public License 3.0](https://www.gnu.org/licenses/lgpl-3.0.en.html)

See also: [Licenses](licenses.html)

---

LEGO®, DACTA®, TECHNIC®, and MINDSTORMS® are trademarks and/or copyrights of the LEGO Group,
which does not sponsor, authorize or endorse this software.
