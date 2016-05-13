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
  * NOTE: This class should not be used directly.  Use either
  * FIFOPort or one of its derived classes.<p>
  *
  * @see Component
  * @see Connector
  * @see FIFOPort
  *
  * @version
  */
public abstract class Port extends Object implements IPort
{
/** Component or connector to which the port belongs.
  */
  protected Brick belongs_to = null;

  protected boolean inMonitoring = false;
  protected boolean outMonitoring = false;
  protected boolean filtering = false;
  protected Vector in_filter = null;
  protected Vector out_filter = null;


/** Component or connector to which the port is linked.
  */
  protected Port  link = null;

  // category: constructors
  public Port() { }
  public Port(Brick b) {
	belongs_to = b;
  }


/** Accesses the name of the class.
  * @return Port class name.
  */
  static protected String className() { return "c2.framework.Port"; }


/** Accesses the class itself.
  * @return Port class.
  */
  static public Class classType() {
	try {
	  return Class.forName(className());
	} catch (ClassNotFoundException e) {
	  System.err.println("Port:classType() Class Not Found!");
	  return null;
	}
  }
  
 
  // category: accessors
/** Accesses the port to which the port is linked.
  * @return Port to which this port is linked.
  */
  public Port Link() { return link; }

/** Accesses the brick to which the port belongs.
  * @return Brick to which the port belongs.
  */
  public Brick belongsTo() { return belongs_to; }

/** (Re)assigns a port to a brick.
  * @param brick Brick to which the port is assigned.
  */
  public void belongsTo(Brick b) { belongs_to = b; }
  
  // category: welding
/** Welds this port to another port.
  * param p Port to which this port is welded.
  */
  public void weld(Port p) { link = p; }


  // category: event monitoring
  public void enableInMonitoring ()
  {
     inMonitoring = true;
  }
 
  public void disableInMonitoring ()
  {
     inMonitoring = false;
  }

  public void enableOutMonitoring ()
  {
     outMonitoring = true;
  }
 
  public void disableOutMonitoring ()
  {
     outMonitoring = false;
  }

  public void enableEventMonitoring ()
  {
     inMonitoring = true;
     outMonitoring = true;
  }
 
  public void disableEventMonitoring ()
  {
     inMonitoring = false;
     outMonitoring = false;
  }

 
  public Vector enableEventFiltering()
  {
     Vector result = new Vector();

     // only filter events on connector-to-component links
     if (belongs_to.typeOf().equals("Connector") &&
         link.belongsTo().typeOf().equals("Component"))
     {
        filtering = true;
        out_filter = ((Component)link.belongsTo()).getInMessageInterface(link);
        in_filter = ((Component)link.belongsTo()).getOutMessageInterface(link);


        // return all incoming and outgoing messages the vector handles
        // to be used to perform filtering in IPC connectors
        for (int i = 0; i < out_filter.size(); i++)
           result.addElement(out_filter.elementAt(i)); 
        for (int i = 0; i < in_filter.size(); i++)
           result.addElement(in_filter.elementAt(i)); 

     }

     return result;
  }


  public void disableEventFiltering()
  {
     filtering = false;
  }


  public boolean eventFilteringEnabled()
  {
     return filtering;
  }

 
  
 
  // category: message passing
/** Receives a message and notifies its brick.
  // category: message passing
/** Receives a message and notifies its brick.
  * @param msg Received message.
  */
  public void receive(Message msg) {
     if (inMonitoring)
     {
        Hashtable params = msg.getAllParameters ();
        belongsTo().addInEventToVisualization (msg.name(), 
                                               params.toString(), 
                                               this);
     }

     if (filtering) 
     {
        if (in_filter.contains(msg.name()))
           belongsTo().newMessage( (IPort) this);
     }
     else // no filtering
        belongsTo().newMessage( (IPort) this);
  }

/** Sends a message if the port is attached to another port.
  * @param msg Message to be sent.
  */
  public void send(Message msg) {
     if (outMonitoring)
     {
        Hashtable params = msg.getAllParameters ();
        belongsTo().addOutEventToVisualization (msg.name(),
                                                params.toString(), 
                                                this);
     }

     if (link != null) 
     {
        if (filtering) 
        {
           if (out_filter.contains(msg.name()))
              link.receive(msg);
        }
        else
           link.receive(msg);
     }
  }

/** Selects a message from the incoming queue.  Not implemented in this
  * class.
  */
  public abstract Message selectNextIncomingMessage();
	
	/** Selects a message from the incoming queue with the given parameter and token.
	 *  If no messages match, return null. Not implemented in this class.
	 * @param paramName the parameter name to match against
	 * @param token the parameter value to match against
	 */
	public abstract Message selectNextIncomingMessage(String paramName, Object token);

/** Selects a message from the outgoing queue.  Not implemented in this
  * class.
  */
  public abstract Message selectNextOutgoingMessage();

  // category: debugging
/** Displays information about the port.  Used for debugging.
 */
  public String toString() {
	String return_string;
	
	return_string = "Port";
	if (link != null)
	  return_string += " is attached to " + link.belongsTo().name();
	return_string += "\n";
	return return_string;
  }

/** Prints a string to System.err.  See debugPrintln in Brick.
  * @see Brick
  */
  public void debugPrintln(String s) {
	if (Brick.debugging)
	  System.err.println(s);
  }
/** Prints a string to System.err.  See debugPrintln in Brick.
  * @see Brick
  */
  public void debugPrint(String s) {
	if (Brick.debugging)
	  System.err.print(s);
  }
}
