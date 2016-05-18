/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPLCT;
import c2.framework.*;
import java.util.*;
import java.io.*;

/**
 *
 * @author 
 */
public class DPLCTUserInterfaceComponent extends ComponentThread
{
    //------------------------------------------------------------------------//
    //create this component in the architecture
    //------------------------------------------------------------------------//    
    public DPLCTUserInterfaceComponent()
    {
        super.create("DPLCTUserInterface", FIFOPort.class);
    }
    
    //------------------------------------------------------------------------//
    //Initialize the internal parts of the component
    //------------------------------------------------------------------------//
    //-A list of name for this component 
    private String[] thisComponentNames = {"DPLCTUserInterface"};

    //-The dialog class checking messages for this component
    private DPLCTUserInterfaceDialog UIDialog = new DPLCTUserInterfaceDialog(); 
    
    //-The inner component doing the work
    private DPLCTUserInterface theUI = new DPLCTUserInterface();
    
    //-The random number generator for ID's
    private Random IDGenerator = new Random();
    
    //the max number of past used ID's in history
    private final int MAX_NUM_PAST_MESSAGE_ID_IN_HISTORY = 20;
    
    //the list of past used message IDS
    private LinkedList pastMsgID = new LinkedList();
    //------------------------------------------------------------------------//
    
////    public void start()
////    {
////        super.start();
////    }

////    //------------------------------------------------------------------------//
////    //Methods of this component
////    //------------------------------------------------------------------------//
////    //handle the requests here
////    protected void handle(Request r)
////    {
////    
////    }
////    
////    
////    //handle the notifications here
////    protected void handle(Notification n)
////    {
////       
////    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    
//----------MESSAGE PACKAGE STRUCTURE (IN ORDER) FOR REFERENCE------------//
    // Both message types, requests (r) and notifications (n) use same structure
    //    
    // r./n. name  = (name of the component you wish to talk to)
    // r./n. addParameter = ("ID", randomNumIDforThisMessage as a string)
    // r./n. addparameter = ("MSGSender", the string name of the component sending this message)
    // r./n. addParameter = ("FunctionNameToInvoke", FunctionNameToInvoke as a string)
    // r./n. addParameter = ("the paramater name", the paramater value)
    // and the parameter continues from there on...
    //
    //----------------MESSAGE PACKAGE STRUCTURE FOR REFERENCE-----------------/
    
    public void start()
    {
        super.start();
        System.out.println("Hiya, Conducting Test 1 for the program");
        
        //this request will trigger the response below
        Request r = new Request("storage");
        r.addParameter("ID", "123456789");
        r.addParameter("functionToInvoke", "requestUnparsedFileData");
        r.addParameter("MSGSender", "DPLCTUserInterface");
        r.addParameter("filePath", "test.txt");
        r.addParameter("requestingFunctionName", "archTestFunction");
        send(r);
    }
    
    //handle the requests here
    protected void handle(Notification n)
    {        
        if(UIDialog.checkNotification(n, thisComponentNames) == 1)
        {
            //it's a valid request to this component
            //PRINT OUT TEST CASE
            if(n.hasParameter("functionToInvoke"))
            {
                if(n.getParameter("functionToInvoke").toString().equals("archTestFunction"))
                {
                    ArrayList<String> theTestData = new ArrayList<String>((ArrayList<String>)n.getParameter("theFileData"));
                    String nextLine;
                    for(int i = 0; i < theTestData.size(); i++)
                    {
                        nextLine = theTestData.get(i);
                        System.out.println(nextLine);
                    }
                }    
            }      
        }
        else if(UIDialog.checkNotification(n, thisComponentNames) == 0)
        {    
            //this message has nothing to do with this component, let the 
            //message continue on it's way up
            send(n);
        }
        else
        {
            //this message has already been proccessed
            //Do nothing AND DO NOT PASS THE MESSAGE ON  
        }      
    }
    
    //handle the notifications here
    protected void handle(Request r)
    {
       //class does not handle request at this time
    }
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    //**************************FOR TESTING PURPOSES ONLY!!*********************
    //**************************FOR TESTING PURPOSES ONLY!!*********************
}
