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
 * @author kkeng94
 */
public class storageDialog 
{   
    private final int MAX_NUM_OF_COMPLETED_MESSAGES_IN_HISTORY = 10;
    
    //a queue to hold the ID of previous requests to avoid redo-ing the same msg
    private LinkedList requestCompleted = new LinkedList();
    
    //checks for request and sees if it has to do with this class
    //return 1 - if this message applies to this class
    //return 0 - if this message DOESN'T APPLY to this class
    //return 2 - if this message has already been handled
    public int checkRequest(Request r)
    {
        //check to see if this message applies to us
        if(r.name().equals("storage"))
        {
            //check to see if this message has already been dealt with
            if((r.hasParameter("ID")) && 
                    (this.isDuplicatedRequest(r.name() + (r.getParameter("ID").toString())) == false))
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
    
    
    //check to see if this request has already been done by this component
    private boolean isDuplicatedRequest(String requestID)
    {
        //go through the history log to check requests already completed
        for(int i = 0; i < requestCompleted.size(); i++)
        {
            if(requestCompleted.get(i) == requestID)
            {
                return true;
            }
        }
        
        //else the request is not a duplicate and add it to the history list
        
        //pop the oldest request out to make room for this new request in history
        if(requestCompleted.size() > MAX_NUM_OF_COMPLETED_MESSAGES_IN_HISTORY)
        {
            requestCompleted.pollLast();
        }
        //add in the new request to the history of request completed
        requestCompleted.addFirst(requestID);
        return false;
    }
    
    
    //accessor for the history of requests completed
    public final LinkedList getReqHistory()
    {
        return requestCompleted;
    }    
}
