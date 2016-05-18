/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPLCT;
import c2.framework.*;
import java.util.*;

/**
 *
 * @author koukeng Yang (Keng)
 */
public class storageComponent extends ComponentThread
{
    //------------------------------------------------------------------------//
    //create this component in the architecture
    //------------------------------------------------------------------------//    
    public storageComponent()
    {
            super.create("storage", FIFOPort.class);
    }    
    //------------------------------------------------------------------------// 
    
    //------------------------------------------------------------------------//
    //Initialize the internal parts of the component
    //------------------------------------------------------------------------//
    //-A list of name for this component 
    private String[] thisComponentNames = {"storage"};
    
    //-The dialog class checking messages for this component
    private storageDialog storageDialog; 
    
    //-The inner component doing the work
    private storage theStorage;
    
    //-The random number generator for ID's
    private Random IDGenerator = new Random();
    
    private final int MAX_NUM_PAST_MESSAGE_ID_IN_HISTORY = 10;
    
    private LinkedList pastMsgID = new LinkedList();
    //------------------------------------------------------------------------//
    
    //----------MESSAGE PACKAGE STRUCTURE (IN ORDER) FOR REFERENCE------------//
    // Both message types, requests (r) and notifications (n) use same structure
    //    
    // r./n. name  = (name of the component you wish to talk to)
    // r./n. addParameter = ("ID", randomNumForThisMessage)
    // r./n. addParameter = ("FunctionNameToInvoke", "FunctionNameToInvoke")
    // r./n. addParameter = ("the paramater name", the paramater value)
    // and the parameter continues from there on...
    //
    //----------------MESSAGE PACKAGE STRUCTURE FOR REFERENCE-----------------/
    
    //------------------------------------------------------------------------//
    //Methods of this component
    //------------------------------------------------------------------------//
    //this method handles the incoming requests coming up from down below
    protected void handle(Request r)
    {
        if(storageDialog.checkRequest(r) == 1)
        {
            //it's a valid request to this component
            //interpret the request command
            (this).interpretRequest(r);
        }
        else if(storageDialog.checkRequest(r) == 0)
        {    
            //this message has nothing to do with this component, let the 
            //message continue on it's way up
            send(r);
        }
        else
        {
            //this message has already been proccessed
            //Do nothing AND DO NOT PASS THE MESSAGE ON  
        }
    }

    
    public void interpretRequest(Request r)
    {
        if(r.hasParameter("requestUnparsedFile"))
        {
            //Log parser asking for unparsed file to parse
            //the message here needs to have:
            // r.name = storage
            // r.hasParameter = requestUnparsedFile
            // r.hasParameter = filePath
            
        }
        if(r.hasParameter("requestParsedFile"))
        {
            //Prov Compiler asking for the parsed file
            
        }
        if(r.hasParameter("sendParsedFileToSave"))
        {
            //Log parser asking to save this prased file
            
        }
        if(r.hasParameter("sendW3CProvFileToSave"))
        {
            //Log Parser asking to save this completed W3C Prov file
            
        }
    }
//////////////    protected void sendParsedFile()
//////////////    {
//////////////        Notification n = new Notification("provLogCompiler");
////////////////        n.addParameter("altitude", altitude);
////////////////        n.addParameter("fuel", fuel);
////////////////        n.addParameter("velocity", velocity);
////////////////        n.addParameter("time", time);
////////////////        n.addParameter("burnRate", burnRate);
////////////////        n.addParameter("landedSafely", landedSafely);
//////////////        send(n);
//////////////    }
    
    protected void handle(Notification n)
    {
       //This component does not handle any notifications at this time
    }
}
