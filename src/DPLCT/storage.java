/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPLCT;
import java.io.*;
import java.util.*;
/**
 *
 * @author kkeng94
 */
public class storage 
{
    //this class handles the dirty work of doing the logic
    //E.g. opening the file, checking if the file exists
    //grabbing all data from the files and reading it into an arraylist of 
    //strings, and saving arraylist of data to storage
    
    public ArrayList<String> requestUnparsedFileData(String filePath)
    {
        //try to open the file
        try
        {         
            //create the arraylist
            ArrayList<String> theData = new ArrayList<String>();
            
            // This is used to read one line at a time
            String nextLine = null;
            
            // FileReader reads from file.
            FileReader fileReader = new FileReader(filePath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //while there is data to be read
            while((nextLine = bufferedReader.readLine()) != null) 
            {
                theData.add(nextLine);
            }
            
            //close file when done reading
            bufferedReader.close();  
            
            return theData;
        }
        catch (Exception e)
        {
            //Can't open the file
            return null; //return null if the file could not be found and opened
        }  
    }
    
    public ArrayList<String> requestParsedFileData(String filePath)
    {
        //try to open the file
        try
        {         
            //create the arraylist
            ArrayList<String> theData = new ArrayList<String>();
            
            // This is used to read one line at a time
            String nextLine = null;
            
            // FileReader reads from file.
            FileReader fileReader = new FileReader(filePath);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //while there is data to be read
            while((nextLine = bufferedReader.readLine()) != null) 
            {
                theData.add(nextLine);
            }
            
            //close file when done reading
            bufferedReader.close();  
            
            return theData;
        }
        catch (Exception e)
        {
            //Can't open the file
            return null; //return null if the file could not be found and opened
        }  

    }
    
    //return true if it can save the file
    //return false it can't
    public boolean sendParsedFileDataToSave(ArrayList<String> theParsedFileDataToSave, String fileLocation)
    {
        try
        {            
            File fileToWriteTo = new File(fileLocation);
            Writer output = new BufferedWriter(new FileWriter(fileToWriteTo));
            
            for(int i = 0; i < theParsedFileDataToSave.size(); i++)
            {
                String nextLine = theParsedFileDataToSave.get(i);
                output.write(nextLine);
            }
            
            //the file has finished writing and return true
            return true;
        }
        catch(Exception e)
        {
            //the file could not be saved
            return false;
        }
    }
    
    //return true if it can save the file
    //return false if it can't
    public boolean sendW3CProvFileDataToSave(ArrayList<String> theCompletedFileDataToSave, String fileLocation)
    {
        try
        {            
            File fileToWriteTo = new File(fileLocation);
            Writer output = new BufferedWriter(new FileWriter(fileToWriteTo));
            
            for(int i = 0; i < theCompletedFileDataToSave.size(); i++)
            {
                String nextLine = theCompletedFileDataToSave.get(i);
                output.write(nextLine);
            }
            
            //the file has finished writing and return true
            return true;
        }
        catch(Exception e)
        {
            //the file could not be saved
            return false;
        }
    }
}
