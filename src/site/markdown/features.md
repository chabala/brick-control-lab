## Features

* Output control
    * on and off
    * change power level
    * change direction
    * control outputs individually or as groups
* Stop button monitoring
    * can handle pressing and releasing events separately
* Input monitoring
    * Raw values can be obtained
    * Touch sensor handling can discern pressed vs not pressed

### Compatible elements

Many LEGO 9 volt elements are compatible with the control lab, mainly determined by their electrical connection. The
classic 9 volt connector looks like a 2x2x2/3 brick with electric contacts on the studs. Power Functions motors and
lights are also compatible, and can be connected by using the [Power Functions Extension Wire 8886](
https://www.lego.com/en-us/product/lego-power-functions-extension-wire-8886). Incompatible elements are from the later
control systems: NXT, EV3, Powered Up, Boost, and Control+.

For a good overview of LEGO motors see [Philippe Hurbain's motor comparison](
http://www.philohome.com/motors/motorcomp.htm).

For a good overview of other compatable elements and sensors, see [Tom Cook's website](
http://www.lgauge.com/technic/LEGOInterfaceB/9751.htm).

### Working with multiple control labs

Multiple control labs can be used together for larger projects, limited only by the amount of serial ports one
has to connect to them with.

![multiple control labs](images/multiple-control-labs.gif)

The source code for this example is in [MultipleControlLabIT](
xref-test/org/chabala/brick/controllab/MultipleControlLabIT.html#L122) as the `testOperatingInTandem()` test.

### Roadmap for future development

* Improved sensor handling and calibration of interpreted values
    * Light and temperature sensors need more analysis
    * Rotation sensor not attempted yet
    * Potential for third party RCX sensor support
* Additional command support for the outputs
    * native blink support
* stateful tracking of outputs
* virtual control lab hardware for simulation or testing against

---

LEGO®, DACTA®, TECHNIC®, and MINDSTORMS® are trademarks and/or copyrights of the LEGO Group,
which does not sponsor, authorize or endorse this software.
