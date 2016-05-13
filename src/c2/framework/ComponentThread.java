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

import java.util.Vector;

/**
 * A component that runs in its own thread of control. <p>
 *
 * See the Component class for details on components. <p>
 * @see Component
 *
 * @version $RCSfile: ComponentThread.java,v $, $Revision: 1.9 $
 */

public abstract class ComponentThread extends Component implements Runnable
{
	protected Thread t;
	protected Semaphor sem = new Semaphor();

	// category: constructors
	public ComponentThread() { }
	public ComponentThread(String name, Class portClass) {
		super(name, portClass);
	}
	public void create(String name, Class portClass) {
		super.create(name, portClass);
	}

	// category: message passing
	protected void newMessage(IPort p) {
		// set the semaphor so that the component's thread will wake up and
		// handle the message.
		sem.set();
	}

	// category: startup/cleanup
	public void start() {
		super.start();
		t = new Thread(this);
		t.start();
	}
	
	public void finish() {
		super.finish();
		t.stop();
		sem.set();  // release any threads that it may be stopped
	}
	
	public boolean finished() {
		if (isStarted())
			return sem.poll(); // it's running, check if it's waiting idle
		else
			return true;
	}
	
	public void timeStep() { }

	/**
	 * Handle one request and one notification (if any).
	 * 
	 * @return true if any messages where handled, false otherwise.
	 */
	protected boolean processEvents() {
		Request r = (Request) bottom.selectNextIncomingMessage();
		if (r != null)
			handle(r);

		Notification n = (Notification) top.selectNextIncomingMessage();
		if (n != null)
			handle(n);
		
		return (r != null) || (n != null);
	}
	
	// category: threading support
	/** The run method is called when the thread is created.  It waits
	 * (on a semaphor) for requests and notifications to arrive, and calls
	 * the appropriate handle() method to process them. */
	public void run() {
		// wait for incomming messages and handle them
		while (true) {
			// keep processing events until the event queues are empty
			while (processEvents())
				;
			sem.get(); // wait for messages
		}
	}
}

