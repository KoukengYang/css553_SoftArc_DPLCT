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
import java.io.*;
import java.util.*;
import c2.framework.*;

/**
 * A component sends a notification whenever its internal state changes.  The
 * notification is meant to inform other components that are dependent on the
 * component. <p>
 *
 * See the Message class for methods to add and remove parameters from
 * notifications. <p>
 * @see c2.framework.Message
 * @see c2.framework.Request
 *
 * @version $RCSfile: Notification.java,v $, $Revision: 1.3 $
 */

public final class Notification extends Message implements Serializable
{
  /**
   * Creates an unnamed Notification message. Set the name using
   * Message.setName()
   * @see Message#setName
   */
  public Notification() {
	super(Message.typeNotification);
  }
  /** Creates a named  Notification message. */
  public Notification(String _name) {
	super(Message.typeNotification, _name);
  }

  public Object clone ()
  {
     Notification clomsg = new Notification (name);
     clomsg.params = (Hashtable)params.clone();
     return clomsg;
  }

}






