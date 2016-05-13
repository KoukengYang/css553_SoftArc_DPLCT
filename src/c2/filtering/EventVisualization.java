package c2.filtering;

import java.lang.*;
import java.awt.*;

public class EventVisualization extends Frame
{
   TextArea topInList[];
   TextArea topOutList[];
   TextArea bottomInList[];
   TextArea bottomOutList[];
   int textAreaRows = 80;
   int textAreaCols = 120;
   int textAreaWidth = 170;
   int textAreaHeight = 120;
   int xInit = 8;
   int yInit = 50;
   int minorPad = 8;
   int majorPad = 16;
   int labelHeight = 30;
   int topPorts, bottomPorts;
   int numVisible;
   int time_stamp;
   static int global_time_stamp = 0;

   EventFilter topInFilterInclude[];
   EventFilter topOutFilterInclude[];
   EventFilter bottomInFilterInclude[];
   EventFilter bottomOutFilterInclude[];
   EventFilter topInFilterExclude[];
   EventFilter topOutFilterExclude[];
   EventFilter bottomInFilterExclude[];
   EventFilter bottomOutFilterExclude[];

   public EventVisualization (String title, int top, int bottom)
   {
      super (title);

      time_stamp = 0;

      topPorts = top;
      bottomPorts = bottom;

      int maxPorts;
      if (topPorts >= bottomPorts)
         maxPorts = topPorts;
      else
         maxPorts = bottomPorts;

      int frameWidth = xInit + 
                       maxPorts * 2 * textAreaWidth +  
                       (maxPorts + 1) * minorPad + 
                       (maxPorts - 1) * majorPad; 
      int frameHeight = yInit + labelHeight + 
                        2 * textAreaHeight + minorPad;

      topInFilterInclude = new EventFilter [topPorts];
      topOutFilterInclude = new EventFilter [topPorts];
      bottomInFilterInclude = new EventFilter [bottomPorts];
      bottomOutFilterInclude = new EventFilter [bottomPorts];
      topInFilterExclude = new EventFilter [topPorts];
      topOutFilterExclude = new EventFilter [topPorts];
      bottomInFilterExclude = new EventFilter [bottomPorts];
      bottomOutFilterExclude = new EventFilter [bottomPorts];
 
      setBackground (Color.black);
      setForeground (Color.white);
      setLayout (null);  // default layout; may change if needed
      move (0,0);
      resize (frameWidth, frameHeight);
      pack();
      show();

      topInList = new TextArea [topPorts];
      topOutList = new TextArea [topPorts];
      bottomInList = new TextArea [bottomPorts];
      bottomOutList = new TextArea [bottomPorts];


      for (int i = 0; i < topPorts; i ++)
      {
         int xOffset = xInit + i * (2*textAreaWidth + minorPad + majorPad);

         topInList[i] = new TextArea (textAreaRows, textAreaCols);
         topInList[i].setEditable(false);
         topInList[i].resize (textAreaWidth, textAreaHeight);
         topInList[i].setBackground (Color.white);
         topInList[i].setForeground (Color.black);
         topInList[i].move (xOffset, yInit);
         add (topInList[i]);
         topInList[i].hide();

         topOutList[i] = new TextArea (textAreaRows, textAreaCols);
         topOutList[i].setEditable(false);
         topOutList[i].resize (textAreaWidth, textAreaHeight);
         topOutList[i].setBackground (Color.white);
         topOutList[i].setForeground (Color.black);
         topOutList[i].move (xOffset + minorPad + textAreaWidth, yInit);
         add (topOutList[i]);
         topOutList[i].hide();
     }
 
      for (int i = 0; i < bottomPorts; i ++)
      {
         int xOffset = xInit + i * (2*textAreaWidth + minorPad + majorPad);
         int yOffset = yInit + labelHeight + textAreaHeight;

         bottomInList[i] = new TextArea (textAreaRows, textAreaCols);
         bottomInList[i].setEditable(false);
         bottomInList[i].resize (textAreaWidth, textAreaHeight);
         bottomInList[i].setBackground (Color.white);
         bottomInList[i].setForeground (Color.black);
         bottomInList[i].move (xOffset, yOffset);
         add (bottomInList[i]);
         bottomInList[i].hide();

         bottomOutList[i] = new TextArea (textAreaRows, textAreaCols);
         bottomOutList[i].setEditable(false);
         bottomOutList[i].resize (textAreaWidth, textAreaHeight);
         bottomOutList[i].setBackground (Color.white);
         bottomOutList[i].setForeground (Color.black);
         bottomOutList[i].move (xOffset + minorPad + textAreaWidth, yOffset);
         add (bottomOutList[i]);
         bottomOutList[i].hide();
      }

      numVisible = 0;

      paint (getGraphics());
   } 

   public void enableTopIn (int i)
   {
      topInList[i].show();
      numVisible ++;
   }

   public void enableTopOut (int i)
   {
      topOutList[i].show();
      numVisible ++;
   }

   public void enableBottomIn (int i)
   {
      bottomInList[i].show();
      numVisible ++;
   }

   public void enableBottomOut (int i)
   {
      bottomOutList[i].show();
      numVisible ++;
   }


   public boolean disableTopIn (int i)
   {
      topInList[i].hide();
      numVisible --;

      if (numVisible <= 0)
      {
         hide();
         dispose();
         return false;
      }
      else
         return true;
   }

   public boolean disableTopOut (int i)
   {
      topOutList[i].hide();
      numVisible --;

      if (numVisible <= 0)
      {
         hide();
         dispose();
         return false;
      }
      else
         return true;
   }

   public boolean disableBottomIn (int i)
   {
      bottomInList[i].hide();
      numVisible --;

      if (numVisible <= 0)
      {
         hide();
         dispose();
         return false;
      }
      else
         return true;
   }

   public boolean disableBottomOut (int i)
   {
      bottomOutList[i].hide();
      numVisible --;

      if (numVisible <= 0)
      {
         hide();
         dispose();
         return false;
      }
      else
         return true;
   }

   public void addInclusionFilter (String side, String direction,
                                   String filter, int loc)
   {
     if (side.equalsIgnoreCase("both") ||
         side.equalsIgnoreCase("all"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           addTopInFilterInclude(loc, filter);
           addTopOutFilterInclude(loc, filter);
           addBottomInFilterInclude(loc, filter);
           addBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           addTopOutFilterInclude(loc, filter);
           addBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           addTopInFilterInclude(loc, filter);
           addBottomInFilterInclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("top"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           addTopInFilterInclude(loc, filter);
           addTopOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           addTopOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           addTopInFilterInclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("bottom"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           addBottomInFilterInclude(loc, filter);
           addBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           addBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           addBottomInFilterInclude(loc, filter);
        }
     }
   }


 
   public void addExclusionFilter (String side, String direction,
                                   String filter, int loc)
   {
     if (side.equalsIgnoreCase("both") ||
         side.equalsIgnoreCase("all"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           addTopInFilterExclude(loc, filter);
           addTopOutFilterExclude(loc, filter);
           addBottomInFilterExclude(loc, filter);
           addBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           addTopOutFilterExclude(loc, filter);
           addBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           addTopInFilterExclude(loc, filter);
           addBottomInFilterExclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("top"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           addTopInFilterExclude(loc, filter);
           addTopOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           addTopOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           addTopInFilterExclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("bottom"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           addBottomInFilterExclude(loc, filter);
           addBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           addBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           addBottomInFilterExclude(loc, filter);
        }
     }
   }


  public void removeInclusionFilter (String side, String direction,
                                     String filter, int loc)
  {
     if (side.equalsIgnoreCase("both") ||
         side.equalsIgnoreCase("all"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           removeTopInFilterInclude(loc, filter);
           removeTopOutFilterInclude(loc, filter);
           removeBottomInFilterInclude(loc, filter);
           removeBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           removeTopOutFilterInclude(loc, filter);
           removeBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           removeTopInFilterInclude(loc, filter);
           removeBottomInFilterInclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("top"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           removeTopInFilterInclude(loc, filter);
           removeTopOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           removeTopOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           removeTopInFilterInclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("bottom"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           removeBottomInFilterInclude(loc, filter);
           removeBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           removeBottomOutFilterInclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           removeBottomInFilterInclude(loc, filter);
        }
     }
  }


  public void removeExclusionFilter (String side, String direction,
                                     String filter, int loc)
  {
     if (side.equalsIgnoreCase("both") ||
         side.equalsIgnoreCase("all"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           removeTopInFilterExclude(loc, filter);
           removeTopOutFilterExclude(loc, filter);
           removeBottomInFilterExclude(loc, filter);
           removeBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           removeTopOutFilterExclude(loc, filter);
           removeBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           removeTopInFilterExclude(loc, filter);
           removeBottomInFilterExclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("top"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           removeTopInFilterExclude(loc, filter);
           removeTopOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           removeTopOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           removeTopInFilterExclude(loc, filter);
        }
     }
     else if (side.equalsIgnoreCase("bottom"))
     {
        if (direction.equalsIgnoreCase("both") ||
            direction.equalsIgnoreCase("all"))
        {
           removeBottomInFilterExclude(loc, filter);
           removeBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("out"))
        {
           removeBottomOutFilterExclude(loc, filter);
        }
        else if (direction.equalsIgnoreCase("in"))
        {
           removeBottomInFilterExclude(loc, filter);
        }
     }
  }



   public void addTopInFilterInclude (int port_num, String filter)
   {
      if (topInFilterInclude[port_num] == null)
         topInFilterInclude[port_num] = new EventFilter ();
      topInFilterInclude [port_num].addFilter(filter);
   }

   public void addTopOutFilterInclude (int port_num, String filter)
   {
      if (topOutFilterInclude [port_num] == null)
         topOutFilterInclude [port_num] = new EventFilter ();
      topOutFilterInclude [port_num].addFilter(filter);
   }

   public void addBottomInFilterInclude (int port_num, String filter)
   {
      if (bottomInFilterInclude [port_num] == null)
         bottomInFilterInclude [port_num] = new EventFilter ();
      bottomInFilterInclude [port_num].addFilter(filter);
   }

   public void addBottomOutFilterInclude (int port_num, String filter)
   {
      if (bottomOutFilterInclude [port_num] == null)
         bottomOutFilterInclude [port_num] = new EventFilter ();
      bottomOutFilterInclude [port_num].addFilter(filter);
   }

   public void removeTopInFilterInclude (int port_num, String filter)
   {
      if (topInFilterInclude[port_num] != null)
         topInFilterInclude [port_num].removeFilter(filter);
   }

   public void removeTopOutFilterInclude (int port_num, String filter)
   {
      if (topOutFilterInclude [port_num] != null)
         topOutFilterInclude [port_num].removeFilter(filter);
   }

   public void removeBottomInFilterInclude (int port_num, String filter)
   {
      if (bottomInFilterInclude [port_num] != null)
         bottomInFilterInclude [port_num].removeFilter(filter);
   }

   public void removeBottomOutFilterInclude (int port_num, String filter)
   {
      if (bottomOutFilterInclude [port_num] != null)
         bottomOutFilterInclude [port_num].removeFilter(filter);
   }


   public void addTopInFilterExclude (int port_num, String filter)
   {
      if (topInFilterExclude[port_num] == null)
         topInFilterExclude[port_num] = new EventFilter ();
      topInFilterExclude [port_num].addFilter(filter); 
   }

   public void addTopOutFilterExclude (int port_num, String filter)
   {
      if (topOutFilterExclude [port_num] == null)
         topOutFilterExclude [port_num] = new EventFilter ();
      topOutFilterExclude [port_num].addFilter(filter); 
   }

   public void addBottomInFilterExclude (int port_num, String filter)
   {
      if (bottomInFilterExclude [port_num] == null)
         bottomInFilterExclude [port_num] = new EventFilter ();
      bottomInFilterExclude [port_num].addFilter(filter); 
   }

   public void addBottomOutFilterExclude (int port_num, String filter)
   {
      if (bottomOutFilterExclude [port_num] == null)
         bottomOutFilterExclude [port_num] = new EventFilter ();
      bottomOutFilterExclude [port_num].addFilter(filter); 
   }

   public void removeTopInFilterExclude (int port_num, String filter)
   {
      if (topInFilterExclude [port_num] != null)
         topInFilterExclude [port_num].removeFilter(filter); 
   }

   public void removeTopOutFilterExclude (int port_num, String filter)
   {
      if (topOutFilterExclude [port_num] != null)
         topOutFilterExclude [port_num].removeFilter(filter); 
   }

   public void removeBottomInFilterExclude (int port_num, String filter)
   {
      if (bottomInFilterExclude [port_num] != null)
         bottomInFilterExclude [port_num].removeFilter(filter); 
   }

   public void removeBottomOutFilterExclude (int port_num, String filter)
   {
      if (bottomOutFilterExclude [port_num] != null)
         bottomOutFilterExclude [port_num].removeFilter(filter); 
   }
  

   // event filtering semantics: only include specifically requested
   // events; if there are no such events, make sure not to add the
   // events to be excluded 
   public void addTopInEvent (int port_num, String msg, 
                              String params)
   {
      time_stamp++;
      global_time_stamp++;
      if ((topInFilterInclude[port_num] != null) &&
          (!topInFilterInclude[port_num].isEmpty()))
      {
         if (topInFilterInclude[port_num].applyFilter(msg))
            topInList[port_num].appendText (msg + " - " + 
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                            " - " + params + "\n");
      }
      else // apply exclusion filter
      {
         if ((topInFilterExclude[port_num] == null) || 
             (!topInFilterExclude[port_num].applyFilter(msg)))
            topInList[port_num].appendText (msg + " - " +
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                            " - " + params + "\n");
      }  
   }

   public void addTopOutEvent (int port_num, String msg, 
                               String params)
   {
      time_stamp++;
      global_time_stamp++;
      if ((topOutFilterInclude[port_num] != null) &&
          (!topOutFilterInclude[port_num].isEmpty()))
      {
         if (topOutFilterInclude[port_num].applyFilter(msg))
            topOutList[port_num].appendText (msg + " - " +
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                             " - " + params + "\n");
      }
      else // apply exclusion filter
      {  
         if ((topOutFilterExclude[port_num] == null) || 
             (!topOutFilterExclude[port_num].applyFilter(msg)))
            topOutList[port_num].appendText (msg + " - " +
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                             " - " + params + "\n");
      }
   }

   public void addBottomOutEvent (int port_num, String msg, 
                                  String params)
   {
      time_stamp++;
      global_time_stamp++;
      if ((bottomOutFilterInclude[port_num] != null) &&
          (!bottomOutFilterInclude[port_num].isEmpty()))
      {
         if (bottomOutFilterInclude[port_num].applyFilter(msg))
            bottomOutList[port_num].appendText (msg + " - " + 
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                               " - " + params + "\n");
      }
      else // apply exclusion filter
      {  
         if ((bottomOutFilterExclude[port_num] == null) || 
             (!bottomOutFilterExclude[port_num].applyFilter(msg)))
            bottomOutList[port_num].appendText (msg + " - " +
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                                " - " + params + "\n");
      }
   }

   public void addBottomInEvent (int port_num, String msg, 
                                 String params)
   {
      time_stamp++;
      global_time_stamp++;
      if ((bottomInFilterInclude[port_num] != null) &&
          (!bottomInFilterInclude[port_num].isEmpty()))
      {
         if (bottomInFilterInclude[port_num].applyFilter(msg))
            bottomInList[port_num].appendText (msg + " - " + 
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                               " - " + params + "\n");
      }
      else // apply exclusion filter
      {  
         if ((bottomOutFilterExclude[port_num] == null) || 
             (!bottomInFilterExclude[port_num].applyFilter(msg)))
            bottomInList[port_num].appendText (msg + " - " + 
                                            global_time_stamp + 
                                            "(" + time_stamp + ")" +
                                               " - " + params + "\n");
      }
   }


   public void appendToTitle (String str)
   {
      setTitle (getTitle() + str);
   }


   public void paint (Graphics g)
   {
      for (int i = 0; i < topPorts; i ++)
      {
         int xOffset = xInit + i * (2*textAreaWidth + minorPad + majorPad);
         int yOffset = yInit - 5;

         paintObject (xOffset, yOffset, "TOP-IN-" + (i+1), 
                      "helvetica", "bold", 13, Color.white, g);

         paintObject (xOffset+textAreaWidth+minorPad, yOffset, 
                      "TOP-OUT-" + (i+1), "helvetica", 
                      "bold", 13, Color.white, g);
      }

      for (int i = 0; i < bottomPorts; i ++)
      {
         int xOffset = xInit + i * (2*textAreaWidth + minorPad + majorPad);
         int yOffset = yInit + labelHeight-5 + textAreaHeight;

         paintObject (xOffset, yOffset, "BOTTOM-IN-" + (i+1), "helvetica", 
                      "bold", 13, Color.white, g);
         paintObject (xOffset+textAreaWidth+minorPad, yOffset, 
                      "BOTTOM-OUT-" + (i+1), "helvetica", 
                      "bold", 13, Color.white, g);
      }
   }

   public void paintObject (int x, int y, String message, String font,
                            String style, int size, Color c, Graphics g)
   {
      if (style.equalsIgnoreCase("plain"))
         g.setFont(new Font(font, Font.PLAIN, size));
      else if (style.equalsIgnoreCase("bold"))
         g.setFont(new Font(font, Font.BOLD, size));
      else // style.equalsIgnoreCase("italic")
         g.setFont(new Font(font, Font.ITALIC, size));

      g.setColor(c);
      g.drawString(message, x, y);
   }

}
