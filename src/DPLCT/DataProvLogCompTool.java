/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DPLCT;

//IMPORT the C2 Connectors and framework
import c2.framework.*;

/**
 * FULL COMMENTS CAN GO LATER ONCE WE ARE 100% FINISHED
 * THIS CLASS (99%) DONE
 * 
 * @author koukeng Yang (Keng)
 */
public class DataProvLogCompTool
{
    public static void main(String[] args)
    {
/*--------------LUNAR LANDER EXAMPLE----------------------------------------------------        
                    //Create the components
                    Component clock = new Clock();
                    Component gameState = new GameState();
                    Component gameLogic = new GameLogic();
                    Component gui = new GUI();

                    //Create the connectors
                    Connector bus = new ConnectorThread("bus");

                    //Add the components and connectors to the architecture
                    lunarLander.addComponent(clock);
                    lunarLander.addComponent(gameState);
                    lunarLander.addComponent(gameLogic);
                    lunarLander.addComponent(gui);
                    lunarLander.addConnector(bus);

        
                    //Create the welds (links) between components and connectors            
                    //----------------------------------------------------------
                    //NOTICE THE ORDER WITH RESPECT TO THE DIAGRAM                    
                                [-----]        [----------]
                                [clock]        [Game State]
                                [-----]        [----------]
                                   |                 |
                                   |                 |
                                   |                 |
                           [----------------------------------]
                           [      BUS (the C2 connector)      ]
                           [----------------------------------]
                                   |                 |
                                   |                 |
                                   |                 |
                             [----------]          [---]
                             [game Logic]          [GUI]
                             [----------]          [---]
                    //----------------------------------------------------------
                    lunarLander.weld(clock, bus);
                    lunarLander.weld(gameState, bus);
                    lunarLander.weld(bus, gameLogic);
                    lunarLander.weld(bus, gui);

                    //Start the application
                    lunarLander.start();
--------------LUNAR LANDER EXAMPLE----------------------------------------------------*/         

//------------OUR CODE BEGINS HERE------------------------------------------------------        
//------------OUR CODE BEGINS HERE------------------------------------------------------
//------------OUR CODE BEGINS HERE------------------------------------------------------
        
        //Create the DataProvLogCompTool architecture               
        Architecture DataProvLogCompTool = new SimpleArchitecture("DataProvLogCompTool");
        
        //Create the components
        Component storageComponent = new storageComponent(); //<------------------------------95% Completed
        Component IBMSPSSParserComponent = new IBMSPSSParserComponent(); //<------------------50%
        Component MATLABParserComponent = new MATLABParserComponent(); //<--------------------50%
        Component changeTracParserComponent = new ChangeTracParserComponent(); //<------------50% 
        Component ProvenanceLogCompilerComponent = new ProvenanceLogCompilerComponent(); //<--50%
        Component DPLCTUserInterface = new DPLCTUserInterfaceComponent(); //<-----------------50%
        
        //Create the connectors
        Connector C3 = new ConnectorThread("C3");
        Connector C2 = new ConnectorThread("C2");
        Connector C1 = new ConnectorThread("C1");
        
        //Add the components and connectors to the architecture
        DataProvLogCompTool.addComponent(storageComponent);
        DataProvLogCompTool.addComponent(IBMSPSSParserComponent);
        DataProvLogCompTool.addComponent(MATLABParserComponent);
        DataProvLogCompTool.addComponent(changeTracParserComponent);
        DataProvLogCompTool.addComponent(ProvenanceLogCompilerComponent);
        DataProvLogCompTool.addComponent(DPLCTUserInterface);
        
        DataProvLogCompTool.addConnector(C3);
        DataProvLogCompTool.addConnector(C2);
        DataProvLogCompTool.addConnector(C1);
        
        //Make the connections
        /*SEE THE DIAGRAM AND FOLLOW THE welding order-------------------------

        [storage]
            |
            |
        [---|---------------------------------------------------------------]
        [                            Connector C3                           ]
        [-----|-------------|-------------------|-------------------|-------]
              |             |                   |                   |
              |     [IBM SPSS PARSER]    [MATLAB PARSER]   [changeTrac PARSER]
              |             |                   |                   |
        [-----|-------------|-------------------|-------------------|-------]
        [                            Connector C2                           ]
        [-----|-------------------------------------------------------------]
              |
        [ProvLogCompiler]
              |
        [-----|-------------------------------------------------------------]
        [                            Connector C1                           ]
        [--------------------------------------------|----------------------]
                                                     |
                                              [User Interface]
         
        //--------------------------------------------------------------------*/

        DataProvLogCompTool.weld(storageComponent, C3);
        DataProvLogCompTool.weld(C3, C2);
        DataProvLogCompTool.weld(C3,IBMSPSSParserComponent);
        DataProvLogCompTool.weld(C3,MATLABParserComponent);
        DataProvLogCompTool.weld(C3,changeTracParserComponent);
        DataProvLogCompTool.weld(IBMSPSSParserComponent, C2);
        DataProvLogCompTool.weld(MATLABParserComponent, C2);
        DataProvLogCompTool.weld(changeTracParserComponent, C2);
        DataProvLogCompTool.weld(C2,ProvenanceLogCompilerComponent);
        DataProvLogCompTool.weld(ProvenanceLogCompilerComponent, C1);
        DataProvLogCompTool.weld(C1,DPLCTUserInterface);
              
        //Start the application
        DataProvLogCompTool.start();
    }
}
