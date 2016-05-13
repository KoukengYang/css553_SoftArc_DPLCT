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
import c2.filtering.*;

/**
 * <i> Connectors </i> encapsulate the means by which components communicate
 * with one another. <p>
 *
 * The Connector class provides the simplest implementation of a connector.
 * It allows several components (and other connectors) to be added above
 * and below the connector by maintaining a list of Ports.
 * Any request received from below
 * is broadcast to all components (or connectors) above.  Any notificaiton
 * received from above is broadcast to all components (or connectors) below.
 * <p>
 *
 * The ConnectorThread class provides the same functionality, but runs in
 * its own thread of control. <p>
 *
 * To declare a new connector, inherit from Connector and override the the
 * handle() and send() methods.  handle() can be used to filter messages.
 * send() may be used to limit message broadcasting. <p>
 *
 * <pre>
 *	class MyConnector extends Connector {
 *	    protected void handle(Request r) {
 *		  // filter the request
 *	    }
 *	    protected void handle(Notification n) {
 *		  // filter the notification
 *	    }
 *	}
 * </pre>
 *
 * To use a connector, create an instance of it and call the start() method:
 * <pre>
 *	MyConnector c = new MyConnector("Test");
 *	c.start();
 *	...
 * </pre>
 *
 * @see Port
 * @see Message
 * @see ConnectorThread
 *
 * @version $RCSfile: Connector.java,v $, $Revision: 1.10 $
 */

public class Connector extends Brick
{
  protected Vector top = new Vector();
  protected Vector bottom = new Vector();

  protected EventVisualization eventVis = null;
  protected boolean event_filtering = false;

  // if the connector is a filtering connector, the two vectors record
  // all the messages handled on the top and on the bottom, respectively
  // to be used in IPC connectors
  protected Vector top_messages = new Vector();
  protected Vector bottom_messages = new Vector();


  // category: constructors
  public Connector() { }
  public Connector(String name) { create(name, Brick.connector); }
  public void create(String name) {	super.create(name, Brick.connector); }


  // category: message filtering
  

  public void addInEventToVisualization (String msg,
                                         String params, Port p)
  { 
     int count = 0;
     boolean found = false;
     while ((count < top.size()) && (!found))
     {
        if (p == ((Port)top.elementAt(count)))
           found = true;
        else
           count ++;
     } 
     
     if (found)
        eventVis.addTopInEvent (count, msg, params);
     else // p is a bottom port
     {
        count = 0;
        while ((count < bottom.size()) && (!found))
        {
           if (p == ((Port)bottom.elementAt(count)))
              found = true;
           else
              count ++;
        }

        eventVis.addBottomInEvent (count, msg, params);
     }
  }


  public void addOutEventToVisualization (String msg,
                                          String params, Port p)
  { 
     int count = 0;
     boolean found = false;
     while ((count < top.size()) && (!found))
     {
        if (p == ((Port)top.elementAt(count)))
           found = true;
        else
           count ++;
     }
    
     if (found)
        eventVis.addTopOutEvent (count, msg, params);
     else // p is a bottom port
     {
        count = 0;
        while ((count < bottom.size()) && (!found))
        {
           if (p == ((Port)bottom.elementAt(count)))
              found = true;
           else
              count ++;
        }

        eventVis.addBottomOutEvent (count, msg, params);
     }
  }


  public void enableMonitoring (String ports, String side, 
                                String direction)
  {
     if (eventVis == null)
        eventVis = new EventVisualization (name(), top.size(), 
                                           bottom.size());

     if (ports.equalsIgnoreCase("all"))
     {
        // enable monitoring for all ports with this side & direction
        if (side.equalsIgnoreCase("both") ||
            side.equalsIgnoreCase("all"))
        {
           for (int i = 0; i < top.size(); i ++)
              enableTopPortMonitoring (i, direction);
           for (int i = 0; i < bottom.size(); i ++)
              enableBottomPortMonitoring (i, direction);
        }
        else if (side.equalsIgnoreCase("top"))
        {
           for (int i = 0; i < top.size(); i ++)
              enableTopPortMonitoring (i, direction);
        }
        else if (side.equalsIgnoreCase("bottom"))
        {
           for (int i = 0; i < bottom.size(); i ++)
              enableBottomPortMonitoring (i, direction);
        }
     }
  }


  public void enableMonitoring (int portNum, String side, 
                                String direction)
  {
     if (eventVis == null)
        eventVis = new EventVisualization (name(), top.size(), 
                                           bottom.size());

     portNum--; // port numbers start from zero

     if (side.equalsIgnoreCase("both") ||
         side.equalsIgnoreCase("all"))
     {
        enableTopPortMonitoring (portNum, direction);
        enableBottomPortMonitoring (portNum, direction);
     }
     else if (side.equalsIgnoreCase("top"))
        enableTopPortMonitoring (portNum, direction);
     else if (side.equalsIgnoreCase("bottom"))
        enableBottomPortMonitoring (portNum, direction);
  }


  protected void enableBottomPortMonitoring (int portNum,
                                             String direction)
  {
     if (direction.equalsIgnoreCase("both") ||
         direction.equalsIgnoreCase("all"))
     {
        ((Port)bottom.elementAt(portNum)).enableEventMonitoring();
        eventVis.enableBottomIn(portNum);
        eventVis.enableBottomOut(portNum);
     }
     else if (direction.equalsIgnoreCase("out"))
     {
        ((Port)bottom.elementAt(portNum)).enableOutMonitoring();
        eventVis.enableBottomOut(portNum);
     }
     else if (direction.equalsIgnoreCase("in"))
     {
        ((Port)bottom.elementAt(portNum)).enableInMonitoring();
        eventVis.enableBottomIn(portNum);
     }
  }


  protected void enableTopPortMonitoring (int portNum, 
                                          String direction)
  {
     if (direction.equalsIgnoreCase("both") ||
         direction.equalsIgnoreCase("all"))
     {
        ((Port)top.elementAt(portNum)).enableEventMonitoring();
        eventVis.enableTopIn(portNum);
        eventVis.enableTopOut(portNum);
     }
     else if (direction.equalsIgnoreCase("out"))
     {
        ((Port)top.elementAt(portNum)).enableOutMonitoring();
        eventVis.enableTopOut(portNum);
     }
     else if (direction.equalsIgnoreCase("in"))
     {
        ((Port)top.elementAt(portNum)).enableInMonitoring();
        eventVis.enableTopIn(portNum);
     }
  }


  public void disableMonitoring (String ports, String side,
                                 String direction)
  {
     if (ports.equalsIgnoreCase("all"))
     {
        // disable monitoring for all ports with this side & direction
        if (side.equalsIgnoreCase("both") ||
            side.equalsIgnoreCase("all"))
        {
           for (int i = 0; i < top.size(); i ++)
              disableTopPortMonitoring (i, direction);
           for (int i = 0; i < bottom.size(); i ++)
              disableBottomPortMonitoring (i, direction);
        }
        else if (side.equalsIgnoreCase("top"))
        {
           for (int i = 0; i < top.size(); i ++)
              disableTopPortMonitoring (i, direction);
        }
        else if (side.equalsIgnoreCase("bottom"))
        {
           for (int i = 0; i < bottom.size(); i ++)
              disableBottomPortMonitoring (i, direction);
        }
     }
  }


  public void disableMonitoring (int portNum, String side,
                                 String direction)
  {
     portNum--; // port numbers start from zero

     if (side.equalsIgnoreCase("both") ||
         side.equalsIgnoreCase("all"))
     {
        disableTopPortMonitoring (portNum, direction);
        disableBottomPortMonitoring (portNum, direction);
     }
     else if (side.equalsIgnoreCase("top"))
        disableTopPortMonitoring (portNum, direction);
     else if (side.equalsIgnoreCase("bottom"))
        disableBottomPortMonitoring (portNum, direction);
  }


  protected void disableBottomPortMonitoring (int portNum,
                                              String direction)
  {
     boolean visible = true;

     if (direction.equalsIgnoreCase("both") ||
         direction.equalsIgnoreCase("all"))
     {
        ((Port)bottom.elementAt(portNum)).disableEventMonitoring();
        visible = eventVis.disableBottomIn(portNum);
        visible = eventVis.disableBottomOut(portNum);
     }
     else if (direction.equalsIgnoreCase("out"))
     {
        ((Port)bottom.elementAt(portNum)).disableOutMonitoring();
        visible = eventVis.disableBottomOut(portNum);
     }
     else if (direction.equalsIgnoreCase("in"))
     {
        ((Port)bottom.elementAt(portNum)).disableInMonitoring();
        visible = eventVis.disableBottomIn(portNum);
     }
  }

  protected void disableTopPortMonitoring (int portNum,
                                           String direction)
  {
     boolean visible = true;

     if (direction.equalsIgnoreCase("both") ||
         direction.equalsIgnoreCase("all"))
     {
        ((Port)top.elementAt(portNum)).disableEventMonitoring();
        visible = eventVis.disableTopIn(portNum);
        visible = eventVis.disableTopOut(portNum);
     }
     else if (direction.equalsIgnoreCase("out"))
     {
        ((Port)top.elementAt(portNum)).disableOutMonitoring();
        visible = eventVis.disableTopOut(portNum);
     }
     else if (direction.equalsIgnoreCase("in"))
     {
        ((Port)top.elementAt(portNum)).disableInMonitoring();
        visible = eventVis.disableTopIn(portNum);
     }
  }

  public void addInclusionFilter (String ports, String side,
                                  String direction, String filter)
  {
     if (eventVis != null)
     {
        if (ports.equalsIgnoreCase("all"))
        {
           if (side.equalsIgnoreCase("both") ||
               side.equalsIgnoreCase("all"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.addInclusionFilter (side, direction, filter, i);
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.addInclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("top"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.addInclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("bottom"))
           {
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.addInclusionFilter (side, direction, filter, i);
           }
        }
     }
  }


  public void addInclusionFilter (int portNum, String side,
                                  String direction, String filter)
  {
     portNum--; // port numbers start from zero

     if (eventVis != null)
        eventVis.addInclusionFilter (side, direction, filter, portNum);
  }



  public void removeInclusionFilter (String ports, String side,
                                  String direction, String filter)
  {
     if (eventVis != null)
     {
        if (ports.equalsIgnoreCase("all"))
        {
           if (side.equalsIgnoreCase("both") ||
               side.equalsIgnoreCase("all"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.removeInclusionFilter (side, direction, filter, i);
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.removeInclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("top"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.removeInclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("bottom"))
           {
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.removeInclusionFilter (side, direction, filter, i);
           }
        }
     }
  }


  public void removeInclusionFilter (int portNum, String side,
                                     String direction, String filter)
  {
     portNum--; // port numbers start from zero

     if (eventVis != null)
        eventVis.removeInclusionFilter (side, direction, filter, portNum);
  }


  public void addExclusionFilter (String ports, String side,
                                  String direction, String filter)
  {
     if (eventVis != null)
     {
        if (ports.equalsIgnoreCase("all"))
        {
           if (side.equalsIgnoreCase("both") ||
               side.equalsIgnoreCase("all"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.addExclusionFilter (side, direction, filter, i);
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.addExclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("top"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.addExclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("bottom"))
           {
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.addExclusionFilter (side, direction, filter, i);
           }
        }
     }
  }


  public void addExclusionFilter (int portNum, String side,
                                  String direction, String filter)
  {
     portNum--; // port numbers start from zero

     if (eventVis != null)
        eventVis.addExclusionFilter (side, direction, filter, portNum);
  }


 
  public void removeExclusionFilter (String ports, String side,
                                  String direction, String filter)
  {
     if (eventVis != null)
     {
        if (ports.equalsIgnoreCase("all"))
        {
           if (side.equalsIgnoreCase("both") ||
              side.equalsIgnoreCase("all"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.removeExclusionFilter (side, direction, filter, i);
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.removeExclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("top"))
           {
              for (int i = 0; i < top.size(); i ++)
                 eventVis.removeExclusionFilter (side, direction, filter, i);
           }
           else if (side.equalsIgnoreCase("bottom"))
           {
              for (int i = 0; i < bottom.size(); i ++)
                 eventVis.removeExclusionFilter (side, direction, filter, i);
           }
        }
     }
  }

 
  public void removeExclusionFilter (int portNum, String side,
                                     String direction, String filter)
  {
     portNum--; // port numbers start from zero

     if (eventVis != null)
        eventVis.removeExclusionFilter (side, direction, filter, portNum);
  }


/****************************************************
  Message Filtering for Point-to-Point Communication
 ****************************************************/
  public void enableFiltering(String policy)
  {
     event_filtering = true;
     setFilter(policy);
  }


  private void setFilter(String policy)
  {
     Vector result = new Vector();

     if (policy.equals("notification_filtering") || 
         policy.equals("message_filtering"))
     {
        for (int i = 0; i < top.size(); i++)
        {
           result = ((Port)top.elementAt(i)).enableEventFiltering();
           for (int j = 0; j < result.size(); j++)
              if (!top_messages.contains(result.elementAt(j)))
              {
                 top_messages.addElement(result.elementAt(j));
              }
        }
        for (int i = 0; i < bottom.size(); i++)
        {
           result = ((Port)bottom.elementAt(i)).enableEventFiltering();
           for (int j = 0; j < result.size(); j++)
              if (!bottom_messages.contains(result.elementAt(j)))
              {
                 bottom_messages.addElement(result.elementAt(j));
              }
        }
     }
     // do nothing if the policy is message_sink
  }


  public void disableFiltering()
  {
     event_filtering = false;

     for (int i = 0; i < top.size(); i++)
        ((Port)top.elementAt(i)).disableEventFiltering();
     for (int i = 0; i < bottom.size(); i++)
        ((Port)bottom.elementAt(i)).disableEventFiltering();
  }


  public boolean filteringEnabled()
  {
     return event_filtering;
  }


  public String typeOf()
  {
     return "Connector";
  }


  // category: connections
  /** Adds a port to the top of the connector.  A new port is added for
   * each component (or connector) communicating with the connector. */
  public void addTopPort(IPort p) { top.addElement(p); }
  /** Adds a port to the bottom of the connector.  A new port is added for
   * each component (or connector) communicating with the connector. */
  public void addBottomPort(IPort p) { bottom.addElement(p); }
  /** Removes a port from the top of the connector. */
  public void removeTopPort(IPort p) { top.removeElement(p); }  
  /** Removes a port from the bottom of the connector. */
  public void removeBottomPort(IPort p) { bottom.removeElement(p); }  

  // category: accessors
  /** Returns a list of the top ports of the connector. */
  public Vector topPorts() { return top; }
  /** Returns a list of the bottom ports of the connector. */
  public Vector bottomPorts() { return bottom; }

  // category: message passing
  protected void newMessage(IPort p) {
		// NOTE: Connector doesn't care when it gets new messages, since
		// the ports queue messages and the connector's timeStep()
		// function is called periodically to handle them.
	}
	
	public void timeStep() {
		// handle one message from each port
		Message m;
		int len = top.size();
		for (int i = 0; i < len; i++) {
			m = ((IPort)top.elementAt(i)).selectNextIncomingMessage();
			if (m != null) {
				handle((Notification) m);
			}
		}
		len = bottom.size();
		for (int i = 0; i < len; i++) {
			m = ((IPort)bottom.elementAt(i)).selectNextIncomingMessage();
			if (m != null) {
				handle((Request) m);
			}
		}
	}

  protected synchronized void handle(Request r) 
  { 
     send(r); 
  }

  protected synchronized void handle(Notification n) 
  { 
     send(n); 
  }

  /**
   * Broadcasts the specified request to all components (or connectors) above
   * the connector.  Override this method to provide alternative behavior.
   * @param r The request to broadcast.
   */
  protected void send(Request r) {
		int len = top.size();
		for (int i = 0; i < len; i++)
			 ((IPort)top.elementAt(i)).send(r);
  }
  /**
   * Broadcasts the specified notification to all components (or connectors)
   * below the connector.  Override this method to provide alternative
   * behavior.
   * @param r The request to broadcast.
   */
  protected void send(Notification n) {
		int len = bottom.size();
		for (int i = 0; i < len; i++)
			 ((IPort)bottom.elementAt(i)).send(n);
  }

  // category: port access helpers
  /** Returns the port at the specified index, null if the index is invalid. */
  protected synchronized IPort bottomPortAt(int n) {
		if (n < bottom.size())
			return (IPort)bottom.elementAt(n);
		else
			return null;
  }
  /** Returns the port at the specified index, null if the index is invalid. */
  protected synchronized IPort topPortAt(int n) {
		if (n < top.size())
			return (IPort)top.elementAt(n);
		else
			return null;
  }
  
  // category: debugging
  /** Returns a String representation of the Connector, including the 
	* connector's type, name, and list of top and bottom ports.
    */
  public String toString() {
		String return_string;
		IPort port;

		return_string = "      " + getClass().getName() + "." + name + "\n";
		for (int i=0; i < top.size(); i++) {
			port = (IPort) top.elementAt(i);
			return_string += "         top" + port.toString();
		}

		for (int i=0; i < bottom.size(); i++) {
			port = (IPort) bottom.elementAt(i);
			return_string += "         bottom" + port.toString();
		}
		return return_string;
  }
}






