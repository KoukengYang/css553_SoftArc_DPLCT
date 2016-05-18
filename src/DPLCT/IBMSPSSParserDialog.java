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
 * @author Koukeng Yang
 */
public class IBMSPSSParserDialog 
{
    private final int MAX_NUM_OF_COMPLETED_MESSAGES_IN_HISTORY = 10;
    
    //a queue to hold the ID of previous msgs to avoid redo-ing the same msg
    private LinkedList msgCompleted = new LinkedList();
    
    //checks for notifications and sees if it has to do with this class
    //return 1 - if this message applies to this class
    //return 0 - if this message DOESN'T APPLY to this class
    //return 2 - if this message has already been handled
    public int checkNotification(Notification n, String[] thisComponentsNames)
    {
        //check to see if this message applies to us
        boolean msgForUs = false;
        for(int i = 0; i < thisComponentsNames.length; i++)
        {
            if(thisComponentsNames[i].equals(n.name()))
            {
                msgForUs = true;
            }
        }
        
        if(msgForUs == true)
        {
            //check to see if this message has already been dealt with
            String firstHalfID = new String(n.getParameter("MSGSender").toString());
            String secondHalfID = new String(n.getParameter("ID").toString());
            String fullID = new String(firstHalfID.concat(secondHalfID));
            
            if((n.hasParameter("ID")) && (this.isDuplicatedMsg(fullID) == false))
            {
                //the request has to do with us and it's not been handeld yet
                return 1;
            }
            else
            {
                //the request has to do with us BUT it's already been handled
                return 2;
            }
        }
        else //this request doesn't apply to us at all
        {
            return 0;
        }
    }
    
    //checks for requests and sees if it has to do with this class
    //return 1 - if this message applies to this class
    //return 0 - if this message DOESN'T APPLY to this class
    //return 2 - if this message has already been handled
    public int checkRequest(Request r, String[] thisComponentsNames)
    {
        //check to see if this message applies to us
        boolean msgForUs = false;
        for(int i = 0; i < thisComponentsNames.length; i++)
        {
            if(thisComponentsNames[i].equals(r.name()))
            {
                msgForUs = true;
            }
        }
        
        if(msgForUs == true)
        {
            //check to see if this message has already been dealt with
            String firstHalfID = new String(r.getParameter("MSGSender").toString());
            String secondHalfID = new String(r.getParameter("ID").toString());
            String fullID = new String(firstHalfID.concat(secondHalfID));
            
            if((r.hasParameter("ID")) && (this.isDuplicatedMsg(fullID) == false))
            {
                //the request has to do with us and it's not been handeld yet
                return 1;
            }
            else
            {
                //the request has to do with us BUT it's already been handled
                return 2;
            }
        }
        else //this request doesn't apply to us at all
        {
            return 0;
        }
    }
    
    
    //check to see if this msg has already been done by this component
    private boolean isDuplicatedMsg(String requestID)
    {
        //go through the history log to check requests already completed
        for(int i = 0; i < msgCompleted.size(); i++)
        {
            if(msgCompleted.get(i).equals(requestID))
            {
                return true;
            }
        }
        
        //else the request is not a duplicate and add it to the history list
        
        //pop the oldest request out to make room for this new request in history
        if(msgCompleted.size() > MAX_NUM_OF_COMPLETED_MESSAGES_IN_HISTORY)
        {
            msgCompleted.pollLast();
        }
        //add in the new request to the history of request completed
        msgCompleted.addFirst(requestID);
        return false;
    }
    
    //accessor for the history of requests completed
    public final LinkedList getMsgHistory()
    {
        return msgCompleted;
    } 
}
