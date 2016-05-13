package c2.framework;

public abstract class ComponentThreadSync extends ComponentThread implements Runnable
{
	String waitingForMessageWithToken;
	
	// category: constructors
	public ComponentThreadSync() { }
	public ComponentThreadSync(String name, Class portClass) {
		super(name, portClass);
	}
	public void create(String name, Class portClass) {
		super.create(name, portClass);
	}

	String calcMessageToken(Request r) {
		return r.name() + Integer.toString( r.hashCode() );
	}
	
	/** Send a synchronous request. In other words, send a Request message
	 * and wait for its reply. For this to work, the receiver must create a
	 * notification to the request using Request.reply()
	 */
	public synchronized Notification sendSync(Request r) {
		// create a unique token and add it to the outgoing request
		waitingForMessageWithToken = calcMessageToken(r);
		r.addParameter("REPLY_TOKEN", waitingForMessageWithToken);
		super.send(r);
		// now wait until we get a notification that contains the token we sent up
if (Boolean.getBoolean("DEBUG")) System.err.println("sendSync begin block in " + name() + " for token [" + waitingForMessageWithToken + "]");
		do {
			// check to see if the newly arrived message is the one we've been waiting for
			Notification n = (Notification) top.selectNextIncomingMessage("REPLY_TOKEN", waitingForMessageWithToken);
			if (null != n) {
if (Boolean.getBoolean("DEBUG")) System.err.println("...end block for token [" + waitingForMessageWithToken + "]");
				return n; // reply arrived, continue processing where we left off!
			}
if (Boolean.getBoolean("DEBUG")) System.err.println("[" + name() + "'s incoming messages = " + top.toString() + "]");
			// wait until we get a reply
			sem.get();
		} while (true);
	}
}
