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

    //------------------------------------------------------------------------//
    //Initialize the internal parts of the component
    //------------------------------------------------------------------------//
    //-A list of name for this component 
    private String[] thisComponentNames = {"ChangeTracPaser","Parser"};
    //    
    //MAY NEED MORE OBJECTS IN THIS COMPONENT, SEE Storage component for example
    //------------------------------------------------------------------------//
    

    //------------------------------------------------------------------------//
    //Methods of this component
    //------------------------------------------------------------------------//
    //handle the requests here
    protected void handle(Request r)
    {
    
    }
    
    
    //handle the notifications here
    protected void handle(Notification n)
    {
       
    }
}
