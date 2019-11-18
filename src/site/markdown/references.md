## References and Prior Art

*   [Eurobricks.com forum: Dacta Control Lab Software](
https://www.eurobricks.com/forum/index.php?/forums/topic/67665-dacta-control-lab-software/)

    This long running forum thread has lots of information about various software options and other resources
    for the control lab.
<br/><br/>

*   [Posts by Andy Carol in rec.toys.lego](
https://groups.google.com/forum/#!searchin/rec.toys.lego/Andy$20Carol%7Csort:date)  
    10 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/boBgQvACOA0/hAdQKhfvk2IJ>  
    11 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/pBq6lLhBXYI/xA89uXITzk4J>  
    12 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/IUPB82JODlI/3SskyO848bAJ>  
    14 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/4jji44_XZkQ/ZHG31MzxWQEJ>  
    21 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/OyniLO9dPUc/0R3Ul43iPLwJ>  
    24 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/8uKLaDhKbpI/GGk0S20gF-0J>  
    31 Jan 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/OroP8ptYajo/_IvM9akAI9cJ>  
    2 Feb 1994 - <https://groups.google.com/forum/#!msg/rec.toys.lego/KXR8tKfIhEM/5D2m0_Xvr9oJ>  
  
    Andrew Carol first posted about writing software for the control lab to the rec.toys.lego usenet group in 1994.
    He released software written in C for Macintosh, though most often it is his posts about the protocol used by the
    control lab that are referenced by others as their starting point for writing their own software.
<br/><br/>

*   <http://www.lgauge.com/technic/LEGOInterfaceB/9751.htm>

    The website of Tom Cook, L Gauge, is primarily devoted to train models, but he's also written up some research
    into the control lab and written C# software for it, along with the earlier 4.5V Interface A.
<br/><br/>

*   <http://www.blockcad.net/dacta/>

    The website of Anders Isaksson, with control lab software written in Delphi.
<br/><br/>

*   <https://www.drdobbs.com/embedded-systems/an-activex-control-for-real-time-compute/184410492>  
    <https://web.archive.org/web/20070818221317/http://troyda.eas.muohio.edu/Research.html>  
    <https://web.archive.org/web/20071130212114/http://troyda.eas.muohio.edu/paper2.html>  
    <https://web.archive.org/web/20060906192126/http://troyda.eas.muohio.edu/LegoCorrection.html>  

    Charles H. Huddleston and Douglas A. Troy cowrote this paper on producing an ActiveX control for the control lab,
    which was reproduced in Dr. Dobb's.
<br/><br/>

*   <http://www.timo.dk/wp/2009/01/25/java-control-class-for-dacta-70909/>

    The website of Timo Paukku Dinnesen, who wrote a Java control for the control lab as a thesis project. It requires
    JavaComm and is tightly coupled to being a Swing application.
<br/><br/>

*   <https://github.com/baronworks/degoworks>

    Another attempt at Java software, built off of Timo's control, using RxTx. Not clear if it actually works.
<br/><br/>

*   <http://lejos.org>

    leJOS is the most visible Java implementation for controlling MINDSTORMS® products, so I looked to it to see if
    there were any established patterns that I should follow for brick-control-lab. But I found code that always
    assumes a fixed number of ports with global scope, e.g.:

    ```java
    import lejos.nxt.Motor;
    
    public class MotorTutor1 {
      public static void main(String[] args) {
        Motor.A.forward();
      }
    }
    ```

    That isn't very object oriented, so it didn't set much precedent for brick-control-lab to follow. 

### Working with jSSC

* <https://code.google.com/p/java-simple-serial-connector/wiki/jSSC_examples>

---

LEGO®, DACTA®, TECHNIC®, and MINDSTORMS® are trademarks and/or copyrights of the LEGO Group,
which does not sponsor, authorize or endorse this software.
