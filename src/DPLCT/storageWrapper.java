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
public class storageWrapper 
{
    //CONSTRUCTOR taking in reference to outer class for msg transmitting
    public storageWrapper(storageComponent storageComponentReference)
    {
        msgTransmittor = storageComponentReference;
    }

    //Initialize all properties
    private storageComponent msgTransmittor;
    private storage theStorage; //<-- the inner storage class
    
    public void interpretRequest(Request r)
    {
        if(r.hasParameter("requestUnparsedFile"))
        {
            //Log parser asking for unparsed file to parse
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
}
