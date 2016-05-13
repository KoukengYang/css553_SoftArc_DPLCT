/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPLCT;

import c2.framework.*;
/**
 *
 * @author kkeng94
 */
public class storageComponent extends ComponentThread
{
    //Initialize the internal components
    private storageDomainTranslator storageDT; /*<--- this isn't really going to be used, just here cause of our G2 design doc*/
    private storageDialog storageDialog; //<-- used to check the message and see if it applies to this component
    private storageWrapper storageWrapper = new storageWrapper(this); //<-- This has the internal storage class in it

    //create this component in the architecture
    public storageComponent()
    {
            super.create("storage", FIFOPort.class);
    }

    //this method handles the incoming requests coming up from down below
    protected void handle(Request r)
    {
        if(storageDialog.checkRequest(r) == 1)
        {
            //it's a valid request to this component
            //interpret the request command
            storageWrapper.interpretRequest(r);
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
       //This component does not handle notifications at this time
    }
}
