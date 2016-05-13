/*
   Copyright (c) 1995, 1996 Regents of the University of California.
   All rights reserved.

   This software was developed by the Arcadia project
   at the University of California, Irvine.

   Redistribution and use in source and binary forms are permitted
   provided that the above copyright notice and this paragraph are
   duplicated in all such forms and that any documentation,
   advertising materials, and other materials related to such
   distribution and use acknowledge that the software was developed
   by the University of California, Irvine.  The name of the
   University may not be used to endorse or promote products derived
   from this software without specific prior written permission.
   THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
   IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
   WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
*/



package c2.framework;

import java.lang.*;
import c2.framework.*;


/** Implements a stand-alone C2 architecture that is not connected
  * to other components in a system, and thus does not handle any messages.
  * 
  * @see Architecture
  */
public class SimpleArchitecture extends Architecture 
{
  // category: constructor
  public SimpleArchitecture() { }
  public SimpleArchitecture(String _name) { create(_name); }

/** Creates and initializes an instance of the SimpleArchitecture.
  * @param name Name of the architecture.
 */

  public void create(String _name) {
	super.create(_name, FIFOPort.classType());
  }
 
  // category: message passing implementation
/** Intended to handle requests the architecture receives.  It is 
  * empty since SimpleArchitecture does not receive any messages.
  */
  public synchronized void handle(Request r)
  {
  }

/** Intended to handle notifications the architecture receives.  
  * It is empty since SimpleArchitecture does not receive any messages.
  */
  public synchronized void handle(Notification n)
  {
  }
}
