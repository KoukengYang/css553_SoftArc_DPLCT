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
 * All communication between components occurs through message passing.
 * The Message class is the base class for other types of messages. <p>
 *
 * Each message as a name, a type, and parameters.  In C2, the type is
 * either Request or Notification.  Parameters are named and may be of any
 * type. <p>
 *
 * NOTE: This class should not be used directly.  Use either Request or
 * Notification instead.
 * @see Request
 * @see Notification
 *
 * @version $RCSfile: Message.java,v $, $Revision: 1.5 $
 */

public class Message extends Object implements Serializable
{
  /** A request message type */
  public final static String typeRequest = "Request";
  /** A notification message type */
  public final static String typeNotification = "Notification";

  /** Type (request or notification) of the message */
  protected String type;
  /** Name of the message */
  protected String name;
  /** List of named parameters in the message */
  protected Hashtable params;

  protected void create(String _type) {
	type = _type;
	params = new Hashtable();
  }

  protected Message(String _type) { create(_type); }
  protected Message(String _type, String _name) {
	create(_type);
	setName(_name);
  }

  public Object clone ()
  {
     Message clomsg = new Message (type, name);
     clomsg.params = (Hashtable)params.clone();
     return clomsg;
  }

  // category: accessors
  /** Returns the name of the message. */
  public String name() { return name; }
  /** Sets the name of the message. */
  public void setName(String _name) { name = _name; }
  /**
   * Returns the type of the message. The type is set by specific message
   * types, like Request and Notification.
   * @see Request
   * @see Notification
   */
  public String type() { return type; }

  /** Sets the type of the message, but only if it is either 
   *  a notification or a request type 
   */
  public void setType (String new_type) 
  { 
     if (new_type.equals(typeRequest) || new_type.equals(typeNotification))
        type = new_type; 
  }


  // category: parameters
  /** Adds a parameter to the message.
	* @param name The name of the parameter.
	* @param value The value of the parameters.  Note that the value is not
	* cloned.
    */

  public void setAllParameters (Hashtable new_params)
  {
     params = (Hashtable)new_params.clone();
  }

  public Hashtable getAllParameters ()
  {
     return params;
  }
 
  public void addParameter(String name, Object value) {
	params.put(name, value);
  }
  /** Retrieves the value of a parameter in the message.
	* @param name The name of the parameter to retrieve.
	* @return Object Returns the object, or null if it doesn't not exist.
    */
  public Object getParameter(String name) { return params.get(name); }
  /** Removes a parameter from the message.
	* @param name The name of the parameter to remove.
    */
  public void removeParameter(String name) { params.remove(name); }
  
  /** Returns a String representation of the Message, including the 
	* messages's name, type and parameters.
    */
  public String toString() {
		return name + "[" + type + "]" + params.toString() + ")";
  }

  public boolean hasParameter(String name) {
     return params.containsKey(name);
  }
}

