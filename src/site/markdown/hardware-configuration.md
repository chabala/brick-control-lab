## Hardware configuration

#### Cabling

The control lab is an [RS&#8209;232][1] serial device. It is also a
[DTE][2] serial device, which means that to connect it to
one's computer, which is also a DTE device, one must use a [null modem][3]
cable or adapter.

[1]: https://en.wikipedia.org/wiki/RS-232
[2]: https://en.wikipedia.org/wiki/Data_terminal_equipment
[3]: https://en.wikipedia.org/wiki/Null_modem

OEM null modem cables:

|Set # | Description | References |
|:---:|:---:|:---:|
|9768|Control Lab Serial Cable for IBM PC and Compatible (9 and 25 pin)|[Peeron][4] [BrickLink][5]|
|9769|Control Lab Serial Cable for Macintosh (8 pin)|[Peeron][6] [BrickLink][7]|

[4]: http://www.peeron.com/inv/sets/9768-1
[5]: https://www.bricklink.com/v2/catalog/catalogitem.page?S=9768-1
[6]: http://www.peeron.com/inv/sets/9769-1
[7]: https://www.bricklink.com/v2/catalog/catalogitem.page?S=9769-1

##### Power adapter

The control lab was supplied with a region specific AC to AC power transformer,
also known as a wall wart. It was the same wall wart supplied with other products
of that era, as opposed to the AC to DC transformers in more current sets.

OEM power transformers:

|Part # | Description | References |
|:---:|:---:|:---:|
|70931 |Train Speed Regulator 9V Power Adaptor for 120V 60Hz|[Peeron][8] [BrickLink][9]|
|70928 |Train Speed Regulator 9V Power Adapter for 230V 50Hz (Continental European)|[Peeron][10] [BrickLink][11]|
|70928b|Train Speed Regulator 9V Power Adapter for 240V (UK)|[Peeron][12] [BrickLink][13]|
|70930 |Train Speed Regulator 9V Power Adapter for 240V (Australia)|[BrickLink][14]|

[8]: http://www.peeron.com/inv/parts/70931
[9]: https://www.bricklink.com/v2/catalog/catalogitem.page?P=70931
[10]: http://www.peeron.com/inv/parts/70938
[11]: https://www.bricklink.com/v2/catalog/catalogitem.page?P=70928
[12]: http://www.peeron.com/inv/parts/70938b
[13]: https://www.bricklink.com/v2/catalog/catalogitem.page?P=70928b
[14]: https://www.bricklink.com/v2/catalog/catalogitem.page?P=70930

#### Serial ports

If one's computer has what is now considered a legacy serial port, which would
appear as a [DE&#8209;9 or DB&#8209;25][15] male connector, or [Mini&nbsp;DIN&#8209;8][16] in the
case of the Macintosh, connecting the null modem cable is all that's needed to connect to the control lab.

[15]: https://en.wikipedia.org/wiki/DE-9
[16]: https://en.wikipedia.org/wiki/Mini_DIN-8

However, most modern computers have stopped including legacy serial ports, favoring to provide USB ports
exclusively. This is not an issue though, as there are many inexpensive USB to serial adapters available.
Operating systems can often use them out of the box, without any additional drivers. The number of serial
ports one can have with such adapters is nearly unlimited, just add additional adapters as needed.

Note that while these adapters add ports, and they are often at the end of a cable, they still need a null
modem adapter before they can connect to the control lab.

#### Port identification

Operating systems assign logical handles to serial ports during to the boot process, such as `COM1`, `COM2`,
etc. While the names assigned to legacy ports are usually consistent between boots, one may find that
names assigned to USB adapter based ports are less consistent, particularly if more than one adapter is
in use, or if they are removed and reinserted.

Under linux, port names may vary by type. Legacy serial ports often appear as `/dev/ttyS0`, `/dev/ttyS1`, etc,
while USB adapter based ports may prefer `/dev/ttyUSB0`, `/dev/ttyUSB1`, etc.

#### Port access

Under linux, non-root users don't have access to serial ports out of the box. The ports are in the `dialout`
group, and adding your user to that group will enable access:

    sudo usermod -a -G dialout $USER

#### Serial configuration

The details of the serial configuration are handled by brick-control-lab and do not vary, but are documented
here for reference:

* **baud rate:** 9600  
* **data bits:** 8  
* **parity:** none  
* **stop bits:** 1  
