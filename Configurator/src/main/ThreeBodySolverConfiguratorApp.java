package main;

/*
 * ThreeBodySolverConfiguratorApp.java
 */

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import gridGenerator.AutoGridGenerator;
import potentialCollection.PotentialsRegister;
/**
 * The main class of the application.
 */
public class ThreeBodySolverConfiguratorApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new ThreeBodySolverConfiguratorView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ThreeBodySolverConfiguratorApp
     */
    public static ThreeBodySolverConfiguratorApp getApplication() {
        return Application.getInstance(ThreeBodySolverConfiguratorApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        if (args.length==0)
          launch(ThreeBodySolverConfiguratorApp.class, args);
        else
            launchBatchMode();
    }
    private static void launchBatchMode()
    {
      ConfiguratorEssentials configurator=new ConfiguratorEssentials();
      PotentialsRegister potentials=configurator.potentials;
      String potentialName;
      double reducedMass;
      double massNorm;
      double cutOff;
      int nComponents=configurator.nComponents;
      try
      {
        for (int i=0;i<nComponents;i++)
        {
            potentialName=configurator.interactions[i];
            reducedMass=configurator.reducedMass[i];
            massNorm=configurator.massNorm;
            cutOff=configurator.xMax[i];

            AutoGridGenerator gridGenerator = new AutoGridGenerator(potentialName, reducedMass, cutOff, massNorm);
            gridGenerator.run();
            configurator.gridNames[i]=gridGenerator.getGridName();
        }
        configurator.writeConfigurations();
      }
      catch (Exception ex)
      {
        System.err.println(ex.getMessage());
        System.exit(13);
      }
      System.exit(0);
    }


}
