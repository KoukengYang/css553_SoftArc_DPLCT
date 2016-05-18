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
 * @author 
 */
public class ChangeTracParserComponent extends ComponentThread
{
    //------------------------------------------------------------------------//
    //create this component in the architecture
    //------------------------------------------------------------------------//    
    public ChangeTracParserComponent()
    {
        super.create("ChangeTracParser", FIFOPort.class);
    }

    public void start()
    {
        //start the component with the other components
        super.start();
    }
    
    //------------------------------------------------------------------------//
    //Initialize the internal parts of the component
    //------------------------------------------------------------------------//
    //-A list of name for this component 
    private String[] thisComponentNames = {"ChangeTracParser","Parser"};
        
    //-The dialog class checking messages for this component
    private ChangeTracDialog ChangeTracParserDialog = new ChangeTracDialog(); 
    
    //-The inner component doing the work
    private ChangeTracParser theChangeTracParser = new ChangeTracParser();
    
    //-The random number generator for ID's
    private Random IDGenerator = new Random();
    
    //the max number of past used ID's in history
    private final int MAX_NUM_PAST_MESSAGE_ID_IN_HISTORY = 20;
    
    //the list of past used message IDS
    private LinkedList pastMsgID = new LinkedList();
    //------------------------------------------------------------------------//
    

    //------------------------------------------------------------------------//
    //Methods of this component
    //------------------------------------------------------------------------//
    //handle the requests here
    protected void handle(Request r)
    {
        //USED FOR TEST 1 ONLY--------------------------------------------------
        System.out.println("The request Test 1 got to the ChangeTracParser");
        //USED FOR TEST 1 ONLY--------------------------------------------------
        
        if(ChangeTracParserDialog.checkRequest(r, thisComponentNames) == 1)
        {
            //it's a valid request to this component
            //interpret the request command
            (this).interpretRequest(r);
        }
        else if(ChangeTracParserDialog.checkRequest(r, thisComponentNames) == 0)
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
    
    //TO-DO <---------------------------------------------------------------------------TO-DO
    private void interpretRequest(Request r)
    {
//        String functionToInvoke = r.getParameter("functionToInvoke").toString();
//        
//        if(functionToInvoke.equals("             "))
//        {
//            
//        }
//        else if(functionToInvoke.equals("        "))
//        {
//        
//        }
    }
    
    //handle the notifications here
    protected void handle(Notification n)
    {
        if(ChangeTracParserDialog.checkNotification(n, thisComponentNames) == 1)
        {
            //it's a valid request to this component
            //interpret the request command
            (this).interpretNotification(n);
        }
        else if(ChangeTracParserDialog.checkNotification(n, thisComponentNames) == 0)
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
    
    //TO-DO <---------------------------------------------------------------------------TO-DO
    private void interpretNotification(Notification n)
    {
//        String functionToInvoke = n.getParameter("functionToInvoke").toString();
//        
//        if(functionToInvoke.equals("             "))
//        {
//            
//        }
//        else if(functionToInvoke.equals("        "))
//        {
//        
//        }
    }
}
