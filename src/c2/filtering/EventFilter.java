package c2.filtering;

import java.lang.*;
import java.util.*;
import java.awt.*;

public class EventFilter
{
   Vector filterList;

   public EventFilter ()
   {
      filterList = new Vector();
   }

   public boolean isEmpty ()
   {
      return filterList.isEmpty();
   }

   public void addFilter (String filter)
   {
      filterList.addElement (filter);
   }

   public void removeFilter (String filter)
   {
      int count = 0;
      boolean found = false;

      while ((count <= filterList.size()) && (!found))
         if (((String)filterList.elementAt(count)).equalsIgnoreCase(filter))
            found = true;
         else
            count ++;

      if (found)
         filterList.removeElementAt (count);
   }


   public boolean applyFilter (String str)
   {
      int count = 0;
      boolean found = false;

      // assume that filters are always supposed to be contained 
      // in the strings they are to filter out, not the other way
      // around.  Stop when first such string is found.
      while ((count < filterList.size()) && (!found))
         if (str.indexOf((String)filterList.elementAt(count)) >= 0)
            found = true;
         else
            count ++;
  
      return found; 
   }
}
