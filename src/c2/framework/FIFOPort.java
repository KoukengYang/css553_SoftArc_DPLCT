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

import java.util.*;


/** The simplest kind of communication ports is one that handles 
  * incoming and outgoing messages on a first-in-first-out (FIFO) basis.
  *
  * @see Port
  */
public class FIFOPort extends Port implements IPort
{
	/** Incoming message queue.
		*/
  protected Vector in_messages = new Vector(); 
	/** Outgoing message queue.
		*/
  protected Vector out_messages = new Vector();

  // category: constructor
  public FIFOPort() { }
  public FIFOPort(Brick b) { super(b); }

	/** Accesses the name of the class.
		* @return Port class name.
		*/
  static protected String className() { return "c2.framework.FIFOPort"; }

  // Redefine this even though we shouldn't have to.  Java does something
  // strange with static methods.
	/** Accesses the class itself.
		* @return Port class.
		*/
  static public Class classType() {
		try {
			return Class.forName(className());
		} catch (ClassNotFoundException e) {
			return null;
		}
  }

  // category: message passing
	/** Receives a message by adding it to the port's incoming queue and 
		* notifying its brick.
		* @param msg Received message.
		*/
  public void receive(Message msg) {
		synchronized (in_messages) {
			in_messages.addElement(msg);
		}
		super.receive(msg);
  }

	/** Sends a message by taking it off the port's outgoing queue and
		* transfering it to its link's incoming queue.
		* @param msg Message to send.
		*/
  public void send(Message msg) {
		// NOTE: there is no need to queue the messages as before, since
		// we immediately send them off to the receiving port.
		super.send(msg);
  }


	/** Selects the next message from the incoming queue.  Does so in a FIFO
		* fashion.
		*/
  public Message selectNextIncomingMessage() {
		// this implementation removes messages from the message queue in a 
		// FIFO fashion
		Message selected_msg = null;
		synchronized (in_messages) {
			if (in_messages.size() > 0) {
				selected_msg = (Message) in_messages.firstElement();
				in_messages.removeElement(selected_msg);
			}
		}
		return selected_msg;
  }
	
	/** Selects a message from the incoming queue with the given parameter and token.
	 *  If no messages match, return null.
	 * @param paramName the parameter name to match against
	 * @param token the parameter value to match against
	 */
	public Message selectNextIncomingMessage(String paramName, Object token) {
		synchronized (in_messages) {
			Enumeration e = in_messages.elements();
			while (e.hasMoreElements()) {
				Message m = (Message) e.nextElement();
				if (token.equals( m.getParameter(paramName) )) {
					// found message
					in_messages.removeElement(m);
					return m;
				}
			}
		}
		return null;
	}

	/** Selects the next message from the outgoing queue.  Does so in a FIFO
		* fashion.  No messages are removed off the queue if the port is not
		* linked to another.
		*/
  public Message selectNextOutgoingMessage() {
		// this implementation removes messages from the message queue in a 
		// FIFO fashion.  If there are messages on this port and the port is
		// attached to another, the message is moved to the link's incoming
		// message queue.
		Message selected_msg = null;
		synchronized (out_messages) {
			if ((out_messages.size() > 0) && (link != null)) {
				selected_msg = (Message)out_messages.firstElement();
				out_messages.removeElement(selected_msg);
			}
		}
		return selected_msg;
  }
 
 
  // category: debugging
	/** Displays the messages currently in the port's two queues.  Used for
		* debugging.
		*/
  public String toString() {
		Message current_msg;
		String return_string;
		
		return_string = super.toString();
		synchronized (in_messages) {
			if (!in_messages.isEmpty()) {
				return_string += "            Incoming Messages:" + "\n";
				for (int i=0; i < in_messages.size (); i++) {
					current_msg = (Message)in_messages.elementAt(i);
					return_string += "               " + current_msg.name() + 
					" of type " + current_msg.type() + "\n";
				}
			}
		}
		synchronized (out_messages) {
			if (!out_messages.isEmpty()) {
				return_string += "            Outgoing Messages:" + "\n";
				for (int i=0; i < out_messages.size (); i++) {
					current_msg = (Message)out_messages.elementAt(i);
					return_string += "               " + current_msg.name() + 
					" of type " + current_msg.type() + "\n";
				}
			}
		}
		return return_string;
  }
}




