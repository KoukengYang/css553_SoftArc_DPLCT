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

import c2.framework.Request;
import c2.framework.Notification;

/**
 * Brick is the abstract super class of both components and connectors.  It
 * contains data and behavior that is shared between the two. <p>
 *
 * NOTE: This class should not be used directly.  Use either Component,
 * Connector, or one of their derived classes.
 * @see Component
 * @see Connector
 * @see ComponentThread
 * @see ConnectorThread
 */

public abstract class Brick extends Object
{
  protected static final String component = "Component";
  protected static final String connector = "Connector";

  /**
   * Enables debugging output.  If set to true, calls to debugPrint() and
   * debugPrintln() are forwarded to System.err.print().
   * @see Brick#debugPrint
   * @see Brick#debugPrintln
   * @see java.lang.System#err
   */
  public static boolean debugging = true;
  
  protected String name;
  protected String type;
	protected boolean running = false;

  // category: constructors
  public Brick() { }
  public Brick(String _name, String _type) { create(_name, _type); }
  public void create(String _name, String _type) {
		name = _name;
		type = _type;
  }

  // category: startup/cleanup
  /** Start the component or connector running.  This method should be called
   * before the component send or receives any messages. */
  public void start() { running = true; }
  /** Returns true if the brick has been start()'ed. */
  public boolean isStarted() { return running; }
  /** Immediately stops the execution of the component or connector (even
   * if it has outstanding messages to process). */
  public void finish() { running = false; }
  /** Returns true if the component or connector is idle (ie that it is
    * not processing any messages). */
  public boolean finished() { return true; }
  /** This function is called periodically to allow the brick to execute. */
	public abstract void timeStep();


  // category: message filtering
  public abstract void addInEventToVisualization (String name, 
                                                  String params, Port p);
  public abstract void addOutEventToVisualization (String name, 
                                                  String params, Port p);
  public abstract String typeOf();

  // category: accessors
  /** Returns the name of the component or connector. */
  public String name() { return name; }
  /** Returns the type; either "Component" or "Connector". */
  public String brickType() { return type; }

  // category: message passing
  /**
   * This method is invoked when the brick receives a <i> request </i>
   * from a brick below it.  Override this method to process requests.
   */
  protected abstract void handle(Request r);
  /**
   * This method is invoked when the brick receives a <i> notification </i>
   * from a brick below it.  Override this method to process notifications.
   */
  protected abstract void handle(Notification n);
  /**
   * Call this method to send a request to a brick above your
   * brick.
   * @see Request
   */
  protected abstract void send(Request r);
  /**
   * Call this method to send a notification to a brick below your
   * brick.
   * @see Notification
   */
  protected abstract void send(Notification n);
  /**
   * This method is invoked when the brick receives a new request or
   * notification.  By default, it invokes the appropriate handle() method.
   * Override this function only if you need to do special processing on
   * incomming messages.
   * @see Component#handle
   * @see Connector#handle
   */
  protected abstract void newMessage(IPort p);

  // category: debugging
  /** Returns a String representation of the Brick, including the 
	* brick's type and name.
    */
  public String toString() {
		return name + "(" + type + ")";
  }
  /**
   * Prints a string to System.err only if Brick.debugging is true.
   * @see Brick#debugging
   */
  public void debugPrintln(String s) {
	if (debugging)
	  System.err.println(s);
  }
  /**
   * Prints a string to System.err only if Brick.debugging is true.
   * @see Brick#debugging
   */
  public void debugPrint(String s) {
	if (debugging)
	  System.err.print(s);
  }
}
