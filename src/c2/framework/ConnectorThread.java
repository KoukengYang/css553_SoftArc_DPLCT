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
 * A connector that runs in its own thread of control. <p>
 *
 * See the Connector class for details on connectors. <p>
 *
 * @see Connector
 *
 * @version $RCSfile: ConnectorThread.java,v $, $Revision: 1.6 $
 */

public class ConnectorThread extends Connector implements Runnable
{
	protected Thread t;
	protected Semaphor sem = new Semaphor();

	// category: constructors
	public ConnectorThread() { }
	public ConnectorThread(String _name) { create(_name); }
	public void create(String name) { super.create(name); }

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
		sem.set();  // release the thread so that it may be stopped
	}
	public boolean finished() {
		if (isStarted())
			return sem.poll(); // it's running, check if it's waiting idle
		else
			return true;
	}
	public void timeStep() { }

	// category: threading support
	/** The run method is called when the thread is created.  It waits
	 * (on a semaphor) for requests and notifications to arrive, and calls
	 * the appropriate handle() method to process them. */
	public void run() {
		boolean pending;

		// wait for incomming messages and handle them
		while ( true ) {
			IPort p;
			Request r = null;
			Notification n = null;
			int len;
			do {
				pending = false;
				len = bottom.size();
				for (int i = 0; i < len; i++) {
					p = bottomPortAt(i);
					r = (Request) p.selectNextIncomingMessage();
					if (r != null) {
						pending = true;
						handle(r);
					}
				}

				len = top.size();
				for (int i = 0; i < len; i++) {
					p = topPortAt(i);
					n = (Notification) p.selectNextIncomingMessage();
					if (n != null) {
						pending = true;
						handle(n);
					}
				}
			} while (pending);

			sem.get(); // wait for messages
		}
	}
}  
