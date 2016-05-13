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

import c2.filtering.*;
import java.util.*;

/**
 * System functionality is encapsulated into <i> components </i>.  Components
 * communicate with one another by passing messages via <i> connectors
 * </i>.  Messages come in two flavors: <p>
 * <ul>
 * <li> <i>Requests</i> denote requests for functionality.  They are sent from
 *      components that are located below the component.
 * <li> <i>Notifications</i> denote change of component state.  They are
 *      sent from components that are located above the component.<p>
 * </ul>
 * 
 * To declare a new component, inherit from Component and override the the
 * handle() methods.  Use send() to send Requests or Notifications to other
 * components.
 * Inherit from ComponentThread if you want the component to run in its own
 * thread of control. <p>
 * <pre>
 *	class MyComponent extends Component {
 *	    protected void handle(Request r) {
 *		  // process a request
 *	    }
 *	    protected void handle(Notification n) {
 *		  // process a notification
 *	    }
 *	}
 * </pre>
 *
 * To use a component, create an instance of it and call the start() method:
 * <pre>
 *	MyComponent c = new MyComponent("Test", FIFOPort.classType());
 *	c.start();
 *	...
 * </pre>
 *
 * See the Request and Notification classes for details on messages. <p>
 * @see Request
 * @see Notification
 * @see ComponentThread
 * @see Brick#start
 *
 * @version $RCSfile: Component.java,v $, $Revision: 1.12 $
 */

public abstract class Component extends Brick
{
	protected Port top = null;
	protected Port bottom = null;

	protected Vector top_in_messages = new Vector();
	protected Vector top_out_messages = new Vector();
	protected Vector bottom_in_messages = new Vector();
	protected Vector bottom_out_messages = new Vector();

	protected EventVisualization eventVis = null;


	// category: constructors
	/**
	 * Creates an uninitialized instance of the component.  Use the create()
	 * method to initialize the component.  This constructor exists so that
	 * components can be dynamically loaded using Class.newInstance()
	 * @see java.lang.Class
	 */
	public Component() { }
	/** Creates and initializes an instance of the component. <p>
	 * @param name the name of the component
	 * @param portClass the Class of the port to use.  The port processes
	 *   all incomming and outgoing messages into and out of the component.
	 * @see Port
	 */
	public Component(String name, Class portClass) { create(name, portClass); }
	/**
	 * Initializes the component.
	 * NOTE: Only call this method once, and only if you used the empty
	 * constructor.<p>
	 * @param name the name of the component
	 * @param portClass the Class of the port to use.  The port processes
	 *   all incomming and outgoing messages into and out of the component.
	 * @see Port
	 */
	public void create(String name, Class portClass) {
		super.create(name, Brick.component);
		try {
			top = (Port) portClass.newInstance();
			top.belongsTo(this);
			bottom = (Port) portClass.newInstance();
			bottom.belongsTo(this);
		} catch (Exception e) {
			System.err.println("Couldn't instantiate port " + portClass.getName());
			e.printStackTrace();
			e.toString();
		}
	}

	// category: accessors
	/** Returns the top port of the component */
	public Port topPort() { return top; }
	/** Returns the bottom port of the component */
	public Port bottomPort() { return bottom; }

	// category: message passing
	/**
	 * This method is invoked when the component receives a <i> request </i>
	 * from a component below it.  Override this method to process requests.
	 */
	protected abstract void handle(Request r);
	/**
	 * This method is invoked when the component receives a <i> notification </i>
	 * from a component below it.  Override this method to process notifications.
	 */
	protected abstract void handle(Notification n);

	/**
	 * Call this method to send a request to a component above your
	 * component.
	 * @see Request
	 */
	protected void send(Request r) 
	{
		top.send(r); 
	}
	/**
	 * Call this method to send a notification to a component below your
	 * component.
	 * @see Notification
	 */
	protected void send(Notification n) 
	{
		bottom.send(n); 
	}


	/**
	 * This method is invoked when the component receives a new request or
	 * notification.  By default, it invokes the appropriate handle() method.
	 * Override this function only if you need to do special processing on
	 * incomming messages.
	 * @see Component#handle
	 */
	protected void newMessage(IPort p) {
		// NOTE: Component doesn't care when it gets new messages, since
		// the port queues the message and the component's timeStep()
		// function is called periodically to handle them.
	}
	
	public void timeStep() {
		// In each time slice, process one incoming message from each port.
		Message m;

		m = top.selectNextIncomingMessage();
		if (m != null) {
			handle((Notification) m);
		}

		m = bottom.selectNextIncomingMessage();
		if (m != null) {
			handle((Request) m);
		}
	}

	// category: event monitoring
	public void enableMonitoring (String side, String direction)
	{
		if (eventVis == null)
			eventVis = new EventVisualization (name(), 1, 1);

		if (side.equalsIgnoreCase("both") ||
				side.equalsIgnoreCase("all"))
		{
			if (direction.equalsIgnoreCase("both") ||
					direction.equalsIgnoreCase("all"))
			{
				top.enableEventMonitoring();
				bottom.enableEventMonitoring();
				eventVis.enableTopIn(0);
				eventVis.enableBottomIn(0);
				eventVis.enableTopOut(0);
				eventVis.enableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("out"))
			{
				top.enableOutMonitoring();
				bottom.enableOutMonitoring();
				eventVis.enableTopOut(0);
				eventVis.enableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("in"))
			{
				top.enableInMonitoring();
				bottom.enableInMonitoring();
				eventVis.enableTopIn(0);
				eventVis.enableBottomIn(0);
			}
		}
		else if (side.equalsIgnoreCase("top"))
		{
			if (direction.equalsIgnoreCase("both") ||
					direction.equalsIgnoreCase("all"))
			{
				top.enableEventMonitoring();
				eventVis.enableTopIn(0);
				eventVis.enableTopOut(0);
			}
			else if (direction.equalsIgnoreCase("out"))
			{
				top.enableOutMonitoring();
				eventVis.enableTopOut(0);
			}
			else if (direction.equalsIgnoreCase("in"))
			{
				top.enableInMonitoring();
				eventVis.enableTopIn(0);
			}
		}
		else if (side.equalsIgnoreCase("bottom"))
		{
			if (direction.equalsIgnoreCase("both") ||
					direction.equalsIgnoreCase("all"))
			{
				bottom.enableEventMonitoring();
				eventVis.enableBottomIn(0);
				eventVis.enableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("out"))
			{
				bottom.enableOutMonitoring();
				eventVis.enableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("in"))
			{
				bottom.enableInMonitoring();
				eventVis.enableBottomIn(0);
			}
		}
	}


	public void disableMonitoring (String side, String direction)
	{
		boolean visible = true;

		if (side.equalsIgnoreCase("both") || 
				side.equalsIgnoreCase("all"))
		{
			if (direction.equalsIgnoreCase("both") || 
					direction.equalsIgnoreCase("all"))
			{
				top.disableEventMonitoring();
				bottom.disableEventMonitoring();
				visible = eventVis.disableTopIn(0);
				visible = eventVis.disableBottomIn(0);
				visible = eventVis.disableTopOut(0);
				visible = eventVis.disableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("out"))
			{
				top.disableOutMonitoring();
				bottom.disableOutMonitoring();
				visible = eventVis.disableTopOut(0);
				visible = eventVis.disableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("in"))
			{  
				top.disableInMonitoring();
				bottom.disableInMonitoring();
				visible = eventVis.disableTopIn(0);
				visible = eventVis.disableBottomIn(0);
			}
		}
		else if (side.equalsIgnoreCase("top"))
		{
			if (direction.equalsIgnoreCase("both") || 
					direction.equalsIgnoreCase("all"))
			{
				top.disableEventMonitoring();
				visible = eventVis.disableTopIn(0);
				visible = eventVis.disableTopOut(0);
			}
			else if (direction.equalsIgnoreCase("out"))
			{
				top.disableOutMonitoring();
				visible = eventVis.disableTopOut(0);
			}
			else if (direction.equalsIgnoreCase("in"))
			{
				top.disableInMonitoring();
				visible = eventVis.disableTopIn(0);
			}
		}
		else if (side.equalsIgnoreCase("bottom"))
		{
			if (direction.equalsIgnoreCase("both") ||
					direction.equalsIgnoreCase("all"))
			{
				bottom.disableEventMonitoring();
				visible = eventVis.disableBottomIn(0);
				visible = eventVis.disableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("out"))
			{
				bottom.disableOutMonitoring();
				visible = eventVis.disableBottomOut(0);
			}
			else if (direction.equalsIgnoreCase("in"))
			{
				bottom.disableInMonitoring();
				visible = eventVis.disableBottomIn(0);
			}
		}

		if (!visible)
			eventVis = null;
	}


	public void addInclusionFilter (String side, String direction,
																	String filter)
	{
		if (eventVis != null)
			eventVis.addInclusionFilter (side, direction, filter, 0);
	}


	public void removeInclusionFilter (String side, String direction,
																		 String filter)
	{
		if (eventVis != null)
			eventVis.removeInclusionFilter (side, direction, filter, 0);
	}


	public void addExclusionFilter (String side, String direction,
																	String filter)
	{
		if (eventVis != null)
			eventVis.addExclusionFilter (side, direction, filter, 0);
	}


	public void removeExclusionFilter (String side, String direction,
																		 String filter)
	{
		if (eventVis != null)
			eventVis.removeExclusionFilter (side, direction, filter, 0);
	}


	public void addInEventToVisualization (String msg, 
																				 String params, 
																				 Port p)
	{
		if (p == top)
			eventVis.addTopInEvent (0, msg, params);
		else // p == bottom
			eventVis.addBottomInEvent (0, msg, params);
	}


	public void addOutEventToVisualization (String msg, 
																					String params, 
																					Port p)
	{
		if (p == top)
			eventVis.addTopOutEvent (0, msg, params);
		else // p == bottom
			eventVis.addBottomOutEvent (0, msg, params);
	}


	/******************************
	 Recording Message Interfaces
	 ******************************/

	public String typeOf()
	{
		return "Component";
	}


	public void setMessageInterface(String side, String dir, Vector interf)
	{
		if (side.equals("top"))
		{
			if (dir.equals("in"))
				top_in_messages = interf;
			else // dir.equals("out")
				top_out_messages = interf;
		}
		else // side.equals("bottom")
		{
			if (dir.equals("in"))
				bottom_in_messages = interf;
			else // dir.equals("out");
				bottom_out_messages = interf;
		}
	}


	public void addMessageToInterface(String side, String dir, String msg)
	{
		if (side.equals("top"))
		{
			if (dir.equals("in"))
				top_in_messages.addElement(msg);
			else // dir.equals("out")
				top_out_messages.addElement(msg);
		}
		else // side.equals("bottom")
		{
			if (dir.equals("in"))
				bottom_in_messages.addElement(msg);
			else // dir.equals("out");
				bottom_out_messages.addElement(msg);
		}
	}

	public Vector getInMessageInterface(Port p)
	{
		if (p == top)
			return top_in_messages;
		else
			return bottom_in_messages;
	}


	public Vector getOutMessageInterface(Port p)
	{
		if (p == top)
			return top_out_messages;
		else
			return bottom_out_messages;
	}



	// category: debugging
	/** Returns a String representation of the Component, including the 
	 * component's type, name, top and bottom ports.
	 */
	public String toString() {
		String return_string;
		return_string = "      " + getClass().getName() + "." + name + "\n";
		if (top != null) {
			return_string += "         top" + top.toString(); 
		}
		if (bottom != null) {
			return_string += "         bottom" + bottom.toString();
		}
		return return_string;
	}
}
