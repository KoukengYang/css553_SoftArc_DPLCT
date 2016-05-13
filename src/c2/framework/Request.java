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
 * A component sends a request to invoke services of another component. <p>
 *
 * See the Message class for methods to add and remove parameters from
 * requests. <p>
 * @see Message
 * @see Notification
 *
 * @version $RCSfile: Request.java,v $, $Revision: 1.4 $
 */

public final class Request extends Message implements Serializable
{
  /**
   * Creates an unnamed Request message. Set the name using
   * Message.setName().
   * @see Message#setName
   */
  public Request() {
		super(Message.typeRequest);
  }
	
  /** Create a named Request message. */
  public Request(String _name) {
		super(Message.typeRequest, _name);
  }

  /** Create a reply notification to this Request. The name of the notification
   * message is the same as this Request. */
	public Notification reply() {
		return reply( this.name() );
	}
	
  /** Create a reply notification to this Request with the given name. */
	public Notification reply(String name) {
		Notification n = new Notification(name);
		Object token = this.getParameter("REPLY_TOKEN");
		if (null != token) {
			// if this request has a token, copy the token to its reply
			n.addParameter("REPLY_TOKEN", token );
		}
		return n;
	}
	
  public Object clone() {
     Request clomsg = new Request(name);
     clomsg.params = (Hashtable)params.clone();
     return clomsg;
  }
}
