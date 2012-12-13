/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threeBodyOperators;
import java.io.*;
/**
 *
 * @author roudnev
 */
public class InitState {
    public static int initialChannel,initialVibState, initialAngState;
    static
    {
        initialChannel=0;
        initialVibState=0;
        initialAngState=0;
        try
        {
          BufferedReader in = new BufferedReader(new FileReader("./InitState.conf"));
          String[] s=in.readLine().split("\\s+");
          initialChannel=Integer.parseInt(s[0]);
          initialVibState=Integer.parseInt(s[1]);
          initialAngState=Integer.parseInt(s[2]);
          if (initialAngState!=0)
          {
            System.err.println("Error: excited angular state analysis is not implemented yet!!!!");
            System.err.println("Resetting initialAngState to zero!!!!!!!!!!!!!!!!!");
            initialAngState=0;
          }

        }
        catch(Exception e)
        {
            System.err.println(e);
            initialChannel=0;
            initialVibState=0;
            initialAngState=0;
            System.err.println("Using the default initial channel");
        }
    
    }


}
