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

import java.io.*;
import java.util.*;

/** Components and connectors in an architecture exchange messages through
  * communication ports.  A port cannot exist independently of a brick
  * (component or connector) and can be linked to at most one other port.<p>
  *
  * @see Port
  * @see FIFOPort
  *
  * @version
  */
public interface IPort
{
  // category: message passing
	/** Receives a message and notifies its brick.
	 * @param msg Received message.
	 */
  public void receive(Message msg);

	/** Sends a message if the port is attached to another port.
	 * @param msg Message to be sent.
	 */
  public void send(Message msg);

	/** Selects a message from the incoming queue.  Not implemented in this
	 * class.
	 */
	public Message selectNextIncomingMessage();

	/** Selects a message from the incoming queue with the given parameter and token.
	 *  If no messages match, return null. Not implemented in this class.
	 * @param paramName the parameter name to match against
	 * @param token the parameter value to match against
	 */
	public Message selectNextIncomingMessage(String paramName, Object token);
	
  // category: debugging
	/** Displays information about the port.  Used for debugging.
	 */
	public String toString();
}

