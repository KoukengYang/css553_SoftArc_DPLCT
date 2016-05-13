package c2.framework;

import java.lang.*;
import java.util.*;
import c2.framework.*;

class Conn extends ConnectorThread
{
  public Conn (String _name) {
	super (_name);
  }
  
  // category: message passing implementation
  public synchronized void handle(Request r) {
     System.out.println ("Handled request " + r.name);
  }
  public synchronized void handle(Notification n) {
     System.out.println ("Handled notification " + n.name);
  }
}

class Comp extends ComponentThread
{
  // category: constructor
  public Comp (String _name) throws java.lang.ClassNotFoundException {
	super(_name, FIFOPort.classType());
  }
 
  // category: message passing implementation
  public synchronized void handle(Request r) {
     System.out.println ("Handled request " + r.name);
  }
  public synchronized void handle(Notification n) {
     System.out.println ("Handled notification " + n.name);
  }
}

class Arch extends Architecture 
{
  // category: constructor
  public Arch (String _name)
	throws java.lang.ClassNotFoundException {
	super(_name, FIFOPort.classType());
  }
 
  // category: message passing implementation
  public synchronized void handle(Request r)
  {
     System.out.println ("Handled request " + r.name);
  }
  public synchronized void handle(Notification n)
  {
     System.out.println ("Handled notification " + n.name);
  }
}

public class TestFramework extends Object
{
  static public void main(String argv[]) {
	Vector brick_set = new Vector ();
	try {
	  Arch arch = new Arch ("SampleArchitecture");
	  Comp comp1 = new Comp("A");
	  Comp comp2 = new Comp("B");
	  Comp comp3 = new Comp("C");
	  Comp comp4 = new Comp("D");
	  Comp comp5 = new Comp("E");
	  Comp comp6 = new Comp("F");
	  Conn conn1 = new Conn("conn1");
	  Conn conn2 = new Conn("conn2");
	  Conn conn3 = new Conn("conn3");

        brick_set.addElement (comp1);
        brick_set.addElement (comp2);
        brick_set.addElement (conn3);
        brick_set.addElement (comp3);
        brick_set.addElement (comp4);

        arch.addComponent (comp1);
        arch.addComponent (comp2);
        arch.addComponent (comp3);
        arch.addComponent (comp4);
        arch.addComponent (comp5);
        arch.addComponent (comp6);
        arch.addConnector (conn1);
        arch.addConnector (conn2);
        arch.addConnector (conn3);

	arch.weld (comp1, conn1);
	arch.weld (comp2, conn1);
	arch.weld (comp3, conn1);
	arch.weld (conn1, conn2);
	arch.weld (conn1, conn2);
	arch.weld (conn1, conn2);
	arch.weld (conn1, conn2);
	arch.weld (conn1, conn2);
	arch.weld (comp2, conn2);
	arch.weld (comp4, conn2);
        System.out.println (arch.toString());

	arch.weld (conn2, comp5);
	arch.weld (conn1, comp5);
        arch.weld (comp5, conn3);
	arch.weld (comp1, conn3);
	arch.weld (conn2, comp1);
        arch.weld (conn3, comp6);
        System.out.println (arch.toString());

        arch.weld (brick_set, conn1);
        System.out.println (arch.toString());

        arch.unweld (conn2, brick_set);
        System.out.println (arch.toString());

        arch.weld (brick_set, conn1);
        System.out.println (arch.toString());

        arch.unweld (conn2, brick_set);
        System.out.println (arch.toString());

        arch.unweld (comp1, conn1);
        arch.unweld (comp2, conn1);
        System.out.println (arch.toString());

        arch.unweld (conn2, comp3);
        arch.unweld (conn2, comp4);
        arch.unweld (conn2, comp6);
        System.out.println (arch.toString());

        arch.unweld (conn1, conn2);
        arch.unweld (conn3, conn1);
        System.out.println (arch.toString());

        arch.detachTop (comp6);
        arch.detachComponent (comp5);
        arch.detachBottom (comp6);
        System.out.println (arch.toString());

	arch.weld (conn2, conn1);
	arch.weld (conn3, conn1);
        System.out.println (arch.toString());

        arch.detachBottom (conn2);
        System.out.println (arch.toString());

        arch.detachConnector (conn1);
        arch.detachConnector (conn2);
        arch.detachConnector (conn3);
        arch.detachComponent (comp1);
        arch.detachComponent (comp2);
        arch.detachComponent (comp3);
        arch.detachComponent (comp4);
        arch.detachComponent (comp5);
        arch.detachComponent (comp6);
        System.out.println (arch.toString());

	} catch (java.lang.ClassNotFoundException e) {
	  System.err.println(e.toString());
	}
  }
}
