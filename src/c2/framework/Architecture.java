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

/**
 * A C2 architecture is a network of components hooked together by
 * connectors.  Components and connectors may be added to an architecture,
 * removed from it, welded together, and detached from each other.<p>
 *
 * Hierarchical architectures are supported by allowing an entire
 * architecture to act as a single component in a new, bigger
 * architecture.  <p>
 *
 * NOTE: This class should not be used directly.  Use either 
 * SimpleArchitecture or one of its derived classes.
 *
 * @see Component
 * @see Connector
 * @see SimpleArchitecture
 *
 * @version
 */
public abstract class Architecture extends Component
{
/** Components in the architecture.
  */
  protected Vector components = new Vector ();
/** Connectors in the architecture.
  */
  protected Vector connectors = new Vector ();
/** Flag determines when to stop sending timeStep() messages to components/connectors
  * in the architecture.
  */
	protected boolean quit = false;
/** Components/connectors that need timeStep() periodically (ie that don't run in their
  * own thread of control.
  */
	protected Vector needsTimeStep = new Vector();

//	protected Vector 
/** Creates and initializes an instance of the Architecture.
  * @param name Name of the architecture.
  * @param portClass The type of communication ports used in the
  * architecture.
  *
  * @see Port
  * @see FIFOPort
 */
  public void create(String name, Class portClass) {
		super.create(name, portClass);
  }
  
  // category: constructors
  public Architecture() {}
  public Architecture(String name, Class portClass) {
		super(name, portClass);
  }


  // category: accessors
/** Accesses the components in the architecture.
  * @return The components in the architecture.
  */
  public Vector Components () { return components; }
/** Accesses the connectors in the architecture.
  * @return The connectors in the architecture.
  */
  public Vector Connectors () { return connectors; }


  // category: startup/cleanup
/** Starts up architecture's execution by calling the start() method on 
  * all of its components and connectors.
  */
  public void start() {
		start(true);
  }

	/** Starts up architecture's execution.
	 * @param startBricks if true, also calls the start() method on 
   * all of its components and connectors.
   */
	public void start(boolean startBricks) {
		super.start();
		if (startBricks) {
			for (int i = 0; i < components.size(); i++) {
				Brick b = (Brick) components.elementAt(i);
			  if (!b.isStarted()) b.start();
			}
			for (int i = 0; i < connectors.size(); i++) {
			  Brick b = (Brick) connectors.elementAt(i);
			  if (!b.isStarted()) b.start();
			}
		}
		run();		// start sending timeStep()'s
	}
	
/** Finishes architecture's execution by calling the finish() method on 
  * all of its components and connectors.
  */
  public void finish() {
    for (int i = 0; i < components.size(); i++) {
      ((Brick) components.elementAt(i)).finish();
		}
    for (int i = 0; i < connectors.size(); i++) {
      ((Brick) connectors.elementAt(i)).finish();
		}
  }

/** Tests whether all components and connectors in an architecure have 
  * finished executing.
  * @return true if no components or connectors are executing any longer.
  */
  public boolean finished() {
    boolean done = true;
    for (int i = 0; i < components.size(); i++)
      done &= ((Brick) components.elementAt(i)).finished();
    for (int i = 0; i < connectors.size(); i++)
      done &= ((Brick) connectors.elementAt(i)).finished();
    return done;
  }

	public void timeStep() {
		int len = needsTimeStep.size();
		for (int i = 0; i < len; i++) {
			Brick b = (Brick) needsTimeStep.elementAt(i);
      if (b.isStarted()) b.timeStep();
		}
	}

/** Starts the architecture running by repeatedly calling timeStep() on all components
  * and connectors within the architecture.  Call quit() to stop the architecture.
  */
	protected void run() {
		while (!quit) {
			try {
				timeStep();
				Thread.currentThread().sleep(10);
			} catch (java.lang.InterruptedException e) { }
		}
	}
/** Stops execution of the architecture.  Calls finish() to stop all the components and
  * connectors, then sets a flag to stop sending timeStep() messages all the components
	* and connectors.
	*/
	public void quit() {
		quit = true;
	}

  // category: adding components and connectors to and removing them from
  // the architecture
/** Adds a component to the architecture.
  * @param c Component to be added.
  */
  public void addComponent(Component c) {
    components.addElement(c);
    if (!(c instanceof ComponentThread))
      needsTimeStep.addElement(c);
  } 

/** Adds a connector to the architecture.
  * @param c Connector to be added.
  */
  public void addConnector(Connector c) {
		connectors.addElement(c);
		if (!(c instanceof ConnectorThread))
			needsTimeStep.addElement(c);
  } 

/** Removes a component from the architecture.
  * @param c Component to be removed.
  */
  public void removeComponent(Component c) {
		components.removeElement(c);
		if (!(c instanceof ComponentThread))
			needsTimeStep.removeElement(c);
  } 

/** Removes a connector from the architecture.
  * @param c Connector to be removed.
  */
  public void removeConnector (Connector c) {
		connectors.removeElement(c);
		if (!(c instanceof ConnectorThread))
			needsTimeStep.removeElement(c);
  }
  
  // category: welding components and connectors together. 
  // ASSUMPTION: The first brick in the parameter list goes above the second

/** Welds a component on top of a connector.
  * @param comp C2 component.
  * @param conn C2 connector.
  */
  public void weld (Component comp, Connector conn) 
  {
        FIFOPort new_port = new FIFOPort (conn);
        FIFOPort old_port;

        new_port.weld (comp.bottomPort ());

        if (comp.bottomPort().Link() != null) // unweld from previous conn
        {
           old_port = (FIFOPort)comp.bottomPort().Link();
           ((Connector)old_port.belongsTo()).removeTopPort(old_port);
        }

        comp.bottomPort().weld (new_port);
        conn.addTopPort (new_port);
  }

/** Detaches a component from the connector below.
  * @param comp C2 component.
  * @param conn C2 connector.
  */
  public void unweld (Component comp, Connector conn)
  {
        FIFOPort port;

        if (comp.bottomPort().Link()!= null && 
            conn.topPorts().contains(comp.bottomPort().Link()))
        {
           port = (FIFOPort)comp.bottomPort().Link();
           ((Connector)port.belongsTo()).removeTopPort(port);
           comp.bottomPort().weld(null);
        }
        // else comp and conn are not welded together
  }

 
  // single component below a connector
/** Welds a component under a connector.
  * @param comp C2 component.
  * @param conn C2 connector.
  */
  public void weld (Connector conn, Component comp)
  {
        FIFOPort new_port = new FIFOPort (conn);
        FIFOPort old_port;

        new_port.weld (comp.topPort ());

        if (comp.topPort().Link() != null) // unweld from previous conn
        {
           old_port = (FIFOPort)comp.topPort().Link();
           ((Connector)old_port.belongsTo()).removeBottomPort(old_port);
        }

        comp.topPort().weld (new_port);
        conn.addBottomPort (new_port);
  }

/** Detaches a component from the connector above.
  * @param comp C2 component.
  * @param conn C2 connector.
  */
  public void unweld (Connector conn, Component comp)
  {
        FIFOPort port;

        if (comp.topPort().Link()!=null && conn.bottomPorts().contains(comp.topPort().Link()))
        {
           port = (FIFOPort)comp.topPort().Link();
           conn.removeBottomPort(port);
           comp.topPort().weld(null);
        }
        // else comp and conn are not welded together
  }

 
  // single connector above a connector
/** Welds two connectors together, from the bottom of one to the top of the
  * other.
  * @param conn1 Top C2 connector.
  * @param conn2 Bottom C2 connector.
  */
  public void weld (Connector conn1, Connector conn2)
  {
        FIFOPort p1 = new FIFOPort (conn1);
        FIFOPort p2 = new FIFOPort (conn2);

        p1.weld (p2);
        p2.weld (p1);
        conn1.addBottomPort(p1);
        conn2.addTopPort(p2);
  }

/** Detaches all links between two connectors.
  * @param conn1 Top C2 connector.
  * @param conn2 Bottom C2 connector.
  */
  public void unweld (Connector conn1, Connector conn2)
  {
        // remove all links between conn1 and conn2.  Since the number of
        // conn1's bottom ports decreases as they are removed, num_hits
        // makes sure i stays within Vector bounds

        FIFOPort p1;
        FIFOPort p2;
        int num_hits = 0;
        int num_ports = conn1.bottomPorts().size();

        for (int i=0; i < num_ports; i++) 
        {
           p1 = (FIFOPort)conn1.bottomPorts().elementAt(i - num_hits);
           if (p1.Link()!=null && conn2.topPorts().contains(p1.Link()))
           {
              num_hits ++;
              p2 = (FIFOPort)p1.Link();
              conn2.removeTopPort(p2);
              conn1.removeBottomPort(p1);
           }
           // else comp and conn are not welded together
        }
  }



  // a set of bricks above a connector
/** Welds multiple components and/or connectors to the top of a connector.
  * @param brick_set Set of C2 components and/or connectors.
  * @param conn C2 connector.
  */
  public void weld (Vector brick_set, Connector conn) 
  {
        for (int i=0; i < brick_set.size(); i++)
           weld ((Brick)brick_set.elementAt(i), conn);
  }

/** Detaches multiple components and/or connectors from the connector below.
  * @param brick_set Set of C2 components and/or connectors.
  * @param conn C2 connector.
  */
  public void unweld (Vector brick_set, Connector conn) 
  {
        for (int i=0; i < brick_set.size(); i++)
           unweld ((Brick)brick_set.elementAt(i), conn);
  }


  // a set of bricks below a connector
/** Welds multiple components and/or connectors below a connector.
  * @param conn C2 connector.
  * @param brick_set Set of C2 components and/or connectors.
  */
  public void weld (Connector conn, Vector brick_set) 
  {
        for (int i=0; i < brick_set.size(); i++)
           weld (conn, (Brick)brick_set.elementAt(i));
  }
  
/** Detaches multiple components and/or connectors from the connector above.
  * @param conn C2 connector.
  * @param brick_set Set of C2 components and/or connectors.
  */
  public void unweld (Connector conn, Vector brick_set) 
  {
        for (int i=0; i < brick_set.size(); i++)
           unweld (conn, (Brick)brick_set.elementAt(i));
  }
 

  // detach a component from the architecture
/** Detaches the top side of a component from the connector, if any, to
  * which it was previously welded.
  * @param comp C2 component.
  */
  public void detachTop (Component comp)
  {
        if (comp.topPort().Link() != null)
        {
           unweld (((Connector)comp.topPort().Link().belongsTo()), comp);
        }
  }   

/** Detaches the bottom side of a component from the connector, if any, to
  * which it was previously welded.
  * @param comp C2 component.
  */
  public void detachBottom (Component comp)
  {
        if (comp.bottomPort().Link() != null)
        {
           unweld (comp, ((Connector)comp.bottomPort().Link().belongsTo()));
        }
  }   

/** Detaches both the top and bottom sides of a component from connectors, if 
  * any, to which it was previously welded.
  * @param comp C2 component.
  */
  public void detachComponent (Component comp)
  {
     detachTop (comp);
     detachBottom (comp);
  }   


  // detach a connector from the architecture
/** Detaches the top side of a connector from all components and connectors
  * to which it was previously welded.
  * @param conn C2 connector.
  */
  public void detachTop (Connector conn) 
  {
        FIFOPort p1;
        FIFOPort p2;
        int num_ports = conn.topPorts().size();

        for (int i=0; i < num_ports; i++) 
        {
           // elements are removed from the front of the Vector, so always get
           // the first element.  p1 is conn's first port; p2 is the port p1 is
           // attached to
           p1 = (FIFOPort)conn.topPorts().firstElement();
           p2 = (FIFOPort)p1.Link();
           unweld (p2.belongsTo(), conn);
        }
  }

/** Detaches the bottom side of a connector from all components and connectors
  * to which it was previously welded.
  * @param conn C2 connector.
  */
  public void detachBottom (Connector conn) 
  {
        FIFOPort p1;
        FIFOPort p2;
        int num_ports = conn.bottomPorts().size();

        for (int i=0; i < num_ports; i++) 
        {
           // elements are removed from the front of the Vector, so always get
           // the first element.  p1 is conn's first port; p2 is the port p1 is
           // attached to
           p1 = (FIFOPort)conn.bottomPorts().firstElement();
           p2 = (FIFOPort)p1.Link();
           unweld (conn, p2.belongsTo());
        }
  }

/** Detaches both the top and bottom sides of a connector from all 
  * components and connectors to which it was previously welded.
  * @param conn C2 connector.
  */
  public void detachConnector (Connector conn)
  {
     detachTop (conn);
     detachBottom (conn);
  }   


/** Displays the architecture (components, connectors, and their
  * interconnections) textually.
  */
  public String toString() {
	String return_string;

	return_string = "Architecture: " 
                        + getClass().getName() + "." + name + "\n";

	return_string += "   Components:" + "\n";
        for (int i=0; i < components.size(); i++)
           return_string += ((Component)components.elementAt(i)).toString ();

	return_string += "   Connectors:" + "\n";
        for (int i=0; i < connectors.size(); i++)
           return_string += ((Connector)connectors.elementAt(i)).toString ();

	return return_string;
  }



/////////////////////
// Private Methods //
/////////////////////

  // a brick above a connector
  private void weld (Brick b, Connector conn)
  {
        if (b.brickType().equals (Brick.component))
           weld ((Component)b, conn);
        else // b.brickType() == Brick.connector
           weld ((Connector)b, conn);
  }

  private void unweld (Brick b, Connector conn)
  {
        if (b.brickType().equals (Brick.component))
           unweld ((Component)b, conn);
        else // b.brickType() == Brick.connector
           unweld ((Connector)b, conn);
  }


  // a brick below a connector
  private void weld (Connector conn, Brick b)
  {
        if (b.brickType().equals (Brick.component))
           weld (conn, (Component)b);
        else // b.brickType() == Brick.connector
           weld (conn, (Connector)b);
  // a brick below a connector
  }

  private void unweld (Connector conn, Brick b)
  {
        if (b.brickType().equals (Brick.component))
           unweld (conn, (Component)b);
        else // b.brickType() == Brick.connector
           unweld (conn, (Connector)b);
  }
}
