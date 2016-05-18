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
    private storageDialog storageDialog = new storageDialog(); 
    
    //-The inner component doing the work
    private storage theStorage = new storage();
    
    //-The random number generator for ID's
    private Random IDGenerator = new Random();
    
    private final int MAX_NUM_PAST_MESSAGE_ID_IN_HISTORY = 20;
    
    private LinkedList pastMsgID = new LinkedList();
    //------------------------------------------------------------------------//
    
    //----------MESSAGE PACKAGE STRUCTURE (IN ORDER) FOR REFERENCE------------//
    // Both message types, requests (r) and notifications (n) use same structure
    //    
    // r./n. name  = (name of the component you wish to talk to)
    // r./n. addParameter = ("ID", randomNumIDforThisMessage)
    // r./n. addparameter = ("MSGSender", the string name of the component sending this message)
    // r./n. addParameter = ("FunctionNameToInvoke", FunctionNameToInvoke as a string)
    // r./n. addParameter = ("the paramater name", the paramater value)
    // and the parameter continues from there on...
    //
    //----------------MESSAGE PACKAGE STRUCTURE FOR REFERENCE-----------------/
    
    public void start()
    {
        //start the component with the other components
        super.start();
    }
    
    
    
    //------------------------------------------------------------------------//
    //Methods of this storage component
    //------------------------------------------------------------------------//
    //this method handles the incoming requests coming up from down below
    protected void handle(Request r)
    {
        //USED FOR TEST 1 ONLY--------------------------------------------------
        System.out.println("The request Test 1 got to the destination (storage)");
        
        //USED FOR TEST 1 ONLY--------------------------------------------------
        
        if(storageDialog.checkRequest(r, thisComponentNames) == 1)
        {
            //it's a valid request to this component
            //interpret the request command
            (this).interpretRequest(r);
        }
        else if(storageDialog.checkRequest(r, thisComponentNames) == 0)
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

    //by this point the message has already been checked to see if has been
    //dealt with already. By this point there are no duplicate requests going
    //through.
    private void interpretRequest(Request r)
    {
        String functionToInvoke = r.getParameter("functionToInvoke").toString();
        
        if(functionToInvoke.equals("requestUnparsedFileData"))
        {
            //Log parser asking for unparsed file to parse
            //---------the message here needs to have these parts---------------
            // r.name = storage
            // r.hasParameter = functionToInvoke, requestUnparsedFile
            // r.hasParameter = MSGSender , (the name of the log parser who sent this request)
            // r.hasParameter = filePath, (the string location to the file)
            // r.hasParameter = requestingFunctionName, (the string name of the function to invoke with this unparsed file)
            //------------------------------------------------------------------
            
            if(r.hasParameter("filePath"))
            {
                //Convert the message parameter to the needed argument
                String fileLocation = new String(r.getParameter("filePath").toString());
                
                //tell the internal class to do the work and get the file
                ArrayList<String> theUnparsedFileData = new ArrayList<String>(theStorage.requestUnparsedFileData(fileLocation));
                
                //check to see if the file could be found
                if(theUnparsedFileData != null)
                {
                    //the file was found and retrieved
                    //create new notification (going downwards) with the name of the recieving component
                    String returnTo = new String(r.getParameter("MSGSender").toString());
                    Notification n = new Notification(returnTo);
                    
                    //attach an ID to this message
                    n.addParameter("ID", (this).getNewID());
                    
                    //attach the sender of this message to the message
                    n.addParameter("MSGSender", thisComponentNames[0]);
                    
                    //attach the function name that needs to be invoked from
                    //this message
                    n.addParameter("functionToInvoke", (r.getParameter("requestingFunctionName").toString()));
                    
                    //attach the file to the message
                    n.addParameter("theFileData", theUnparsedFileData);
                    
                    //send the notification downwards
                    send(n);
                }
                else
                {
                    //the unparsedfile could not be read from
                    //WHICH COMPONENT TO CONTACT?
                }
                
            }
            else
            {
                //no filepath was found in the message
                //WHICH COMPONENT TO CONTACT?
            } 
        }
        if(functionToInvoke.equals("requestParsedFileData"))
        {
//Log parser asking for unparsed file to parse
            //---------the message here needs to have these parts---------------
            // r.name = storage
            // r.hasParameter = functionToInvoke, requestUnparsedFile
            // r.hasParameter = MSGSender , (the name of the log parser who sent this request)
            // r.hasParameter = filePath, (the string location to the file)
            // r.hasParameter = requestingFunctionName, (the string name of the function to invoke with this unparsed file)
            //------------------------------------------------------------------
            
            if(r.hasParameter("filePath"))
            {
                //Convert the message parameter to the needed argument
                String fileLocation = new String(r.getParameter("filePath").toString());
                
                //tell the internal class to do the work and get the file
                ArrayList<String> theUnparsedFileData = new ArrayList<String>(theStorage.requestUnparsedFileData(fileLocation));
                
                //check to see if the file could be found
                if(theUnparsedFileData != null)
                {
                    //the file was found and retrieved
                    //create new notification (going downwards) with the name of the recieving component
                    String returnTo = new String(r.getParameter("MSGSender").toString());
                    Notification n = new Notification(returnTo);
                    
                    //attach an ID to this message
                    n.addParameter("ID", (this).getNewID());
                    
                    //attach the sender of this message to the message
                    n.addParameter("MSGSender", thisComponentNames[0]);
                    
                    //attach the function name that needs to be invoked from
                    //this message
                    n.addParameter("functionToInvoke", (r.getParameter("requestingFunctionName").toString()));
                    
                    //attach the file to the message
                    n.addParameter("theFileData", theUnparsedFileData);
                    
                    //send the notification downwards
                    send(n);
                }
                else
                {
                    //the unparsedfile could not be read from
                    //WHICH COMPONENT TO CONTACT?
                }
                
            }
            else
            {
                //no filepath was found in the message
                //WHICH COMPONENT TO CONTACT?
            } 
            
            
        }
        if(functionToInvoke.equals("sendParsedFileDataToSave"))
        {
            //Log parser asking to save this parsed file
            //---------the message here needs to have these parts---------------
            // r.name = storage
            // r.hasParameter = functionToInvoke, sendParsedFileToSave
            // r.hasParameter = requestingComponentName , (name of log parser)
            // r.hasParameter = filePath, (the string location to save the file) //OPTIONAL
            // r.hasParameter = theFileData, (Array or arraylist containing the data to save)
            //------------------------------------------------------------------
            
            //default location to save if one is not specified
            String locationToSaveTo = new String("");
            
            if(r.hasParameter("filePath"))
            {
                locationToSaveTo = new String(r.getParameter("filePath").toString());
            }
            
            if(r.hasParameter("theFileData"))
            {
    
                ArrayList<String> theFileDataToSave = (ArrayList<String>)r.getParameter("theFileData");
                
                //tell the internal class to do the work and save the file
                boolean savedSuccessfully = theStorage.sendParsedFileDataToSave(theFileDataToSave, locationToSaveTo);

                if(savedSuccessfully == true)
                {
                    //send a message to someone...?
                    //inform SOME component the file was saved correctly
                }
                else
                {
                    //the file was not saved successfully
                    //inform SOME component the file was not saved correctly
                }
            }
            else
            {
                //THIS MESSAGE CONTAINS NO FILE DATA TO SAVE
            }
            
        }
        if(functionToInvoke.equals("sendW3CProvFileDataToSave"))
        {
            //Log Parser asking to save this completed W3C Prov file
            
        }
    }
    
    
    private String getNewID()
    {
        String newUnusedID = new String(); 
        boolean newIDFound = false;
        
        //generate a random ID and check if it's been used recently
        while(newIDFound == false)
        {
            boolean goodID = true;
                    
            //generate a new random ID
            String newID = Integer.toString(IDGenerator.nextInt());
            
            //loop through the array of past USED ID's
            for(int i = 0; i < pastMsgID.size(); i++)
            {
                //if this new ID matches a past used ID
                if(newID.equals(pastMsgID.get(i)))
                {
                    //this is a bad ID to use since it's been used recently
                    goodID = false;
                }
            }
            
            if(goodID == true)
            {
                newIDFound = true;
                
                //set the good ID to return back
                newUnusedID = newID;
                
                //make space for this new ID in the history list
                if(pastMsgID.size() >= MAX_NUM_PAST_MESSAGE_ID_IN_HISTORY)
                {
                    pastMsgID.pollLast();
                }
                
                //add this ID to the history list
                pastMsgID.addFirst(newID);
            }
        }
        
        //return the new good ID to use
        return newUnusedID;
    }
    
    protected void handle(Notification n)
    {
       //This component does not handle any notifications at this time
    }
}
