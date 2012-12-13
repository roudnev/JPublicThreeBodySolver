

/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 1.0
 */
import threeBodyOperators.*;
import configReader.ConfiguratorEssentials;
import hermitSplines.HSplineBC;
import hermitSplines.HSplineFree;
import hermitSplines.SimpleGrid;
import basicAbstractions.*;
import auxiliary.*;
import java.io.*;
import potentialCollection.*;
public class Main3BScat extends basicAbstractions.AMain
{ ConfiguratorEssentials config;
  public Main3BScat()
  {  config=new ConfiguratorEssentials();
  }
   public void main(String[] args)
   throws Exception
   {
     // Configuration reader
     double erel=0.0;
     if (args.length!=0) erel=Double.parseDouble(args[0]);
     //
     System.out.println("Starting");
     ALinearEquation eq=new MulticomponentThreeBodyEquation(erel,config);
     System.out.println("Equation initialized");
     MulticomponentThreeBodyEquation theEq=(MulticomponentThreeBodyEquation)eq;
     System.out.println("Types casted");
     linearEquationSolvers.GMRESLinearEquationSolver solver=new linearEquationSolvers.GMRESLinearEquationSolver();
     solver.setAccuracy(1.0e-6);
     System.out.println("Rank="+eq.a.getRank());
     System.out.println("Erel="+erel);
     System.out.println("Z="+theEq.theA.getZ());
     //
     AFunction res=solver.calculateSolution(eq);
     // write down the T-matrix component

     NewSolutionAnaliser analizer=new NewSolutionAnaliser(theEq, res);
     //SolutionAnaliser analizer=new SolutionAnaliser(theEq, res);
     analizer.printKMatrix();
     //analizer.printPhaseShift();
     analizer.writeKmatrix();
     analizer.writePhase();
     analizer.writeComponents("FaddeevComponent.DAT");
//     SolutionAnaliser a1=new SolutionAnaliser(theEq, res);
//     a1.writeKmatrix();
     //analizer.ifritDumpComponent();
     //analizer.ifritDumpWaveFunction();
     //analizer.ifritDumpTComponent();
     /*
     Function3D theRSol=new Function3D(theEq.theA.getV().getRd3D(),res); //
     SplineFunction1D clusterWF=theEq.getClusterWF();
     SolutionAnaliser analizer=new SolutionAnaliser(theRSol, clusterWF, theEq.theA.getV().getRd3D(), erel); //
     analizer.writeTComponent("TC.DAT");
     // write down the right hand side
     theRSol=new Function3D(theEq.theA.getV().getRd3D(),eq.rhs);
     analizer=new SolutionAnaliser(theRSol, clusterWF, theEq.theA.getV().getRd3D(), erel); //
     analizer.writeTComponent("RHS.DAT");
     // analize what we get:
     res=theEq.getCompleteCoefficientsSet(res);
     Function3D theSol=new Function3D(d3D,res);
     analizer=new SolutionAnaliser(theSol, clusterWF, d3D, erel);
     analizer.printPhaseShift();
     analizer.printKMatrix();
     analizer.writeKmatrix();
     analizer.writeComponent("FC.DAT");
     System.out.println("Finished somehow...");
     */
   }
}
