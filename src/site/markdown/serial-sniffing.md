## Serial Sniffing

In order to learn how to communicate with the control lab, it is necessary to spy on the serial data
while it communicates with working software. In linux this is relatively easy using `socat` and `tee`
to pair a [pseudoterminal](https://en.wikipedia.org/wiki/Pseudoterminal) (pty) to the physical port,
and capture transmitted data at the same time.

These examples are modifed from this [StackExchange answer](https://unix.stackexchange.com/a/225904/19124).

capture serial data sent and received through pty to usb serial port:
```
socat /dev/ttyUSB0,raw,echo=0 \
SYSTEM:'tee in.txt |socat - "PTY,link=/tmp/ttyV0,raw,echo=0,waitslave" |tee out.txt'
```

capture only sent data:
```
socat /dev/ttyUSB0,raw,echo=0 \
SYSTEM:'socat - "PTY,link=/tmp/ttyV0,raw,echo=0,waitslave" |tee out.txt'
```
