

/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 1.0
 */
import java.io.*;
import threeBodyOperators.*;
import configReader.ConfiguratorEssentials;
//import hermitSplines.HSplineBC;
//import hermitSplines.HSplineFree;
//import hermitSplines.SimpleGrid;
import basicAbstractions.*;
import auxiliary.*;
//import potentialCollection.mtVPotential;
import decompositions.*;
public class Main3BKernelAnalysis extends basicAbstractions.AMain
{
   AFunction ini;
   FaddeevOperatorResolvent theResolvent;
   ConfiguratorEssentials config;
   
   public Main3BKernelAnalysis()
   { config=new ConfiguratorEssentials();
   }

   public void main(String[] args)
   throws Exception
   {
   try{
     double erel=-1.0e-1;
     int nEV=2;
     InpReader reader=new InpReader();
     if (args.length>0) erel=Double.parseDouble(args[0]);
     if (args.length>1) nEV=Integer.parseInt(args[1]);
     ////////////// Experimental /////////////////
//     double dt=0.5e0;
//     AnOperator expHF=new ExpFaddeevOperator(dt);
//     ExpFaddeevOperator theExpHF=(ExpFaddeevOperator)expHF;
//     double conversionFactor=2/dt/theExpHF.getConversion();
//     AnOperator aResolvent=new FaddeevOperatorResolvent(erel);
//     theResolvent=(FaddeevOperatorResolvent) aResolvent;
//     SpectrumAnalizerExp anal=new SpectrumAnalizerExp(expHF, nEV, conversionFactor);
//     anal.analize();
     /////////////////////////////////////////////

     AnOperator aResolvent=new FaddeevOperatorResolvent(erel,config);
     theResolvent=(FaddeevOperatorResolvent) aResolvent;
     AnOperator fdKernel=theResolvent.theA;
     double conversionFactor=1.0;//theResolvent.theA.getEnergyConversionFactor();
     initializeTryalVector();
     SpectrumAnalizer anal=new SpectrumAnalizer(fdKernel, ini, nEV, conversionFactor);
             //SpectrumAnalizer(theResolvent,nEV,conversionFactor);
     anal.inversedOff();
     anal.setZShift(0.0);
     anal.nEV=nEV;
     anal.analize();
     AFunction[] res=anal.getEigenvectors();
     saveSolutions(res);
     System.out.println("\nThe following three-body equation kernel eigenvalues are found :");
     System.out.print("#3#  ");
     for (int i=0; i<nEV;i++)
        System.out.print(anal.eigenValues[i]+"  ");
     }
     catch(Exception e)
     { System.err.println("Exception in main() Main3BBound:"+e);
       e.printStackTrace();
     }
   }

   public void saveSolutions(AFunction[] res)
   {
     try{
      ObjectOutputStream out=new ObjectOutputStream(
                                   new FileOutputStream("solutionBound.dat")
                                 );
      out.writeObject(res);
      out.close();
    }
    catch(IOException e){ System.err.println(e.getMessage());}
   }

   public AFunction[] restoreSolutions() throws Exception
   {
     AFunction[] res;
     File iniFile=new File("solutionBound.dat");
     ObjectInputStream in=new ObjectInputStream(
                                   new FileInputStream(iniFile)
                                 );
     res=(AFunction[])in.readObject();
     in.close(); 
     return res;
   }

   public void initializeTryalVector() throws Exception
   {
     //MulticomponentThreeBodyEquation theEq=new MulticomponentThreeBodyEquation(0.0);
     ini=theResolvent.rhs.completeClone();
     ini=theResolvent.theA.v.times(theResolvent.theA.h0.solve(ini));
     try 
     {
       AFunction[] saved=restoreSolutions();
       AFunction trial=saved[0];
       if (ini.getArrayLength()==trial.getArrayLength())
       {
         for (int i=1; i<saved.length; i++)
         { trial.add(saved[i]);
         }
         ini=trial.completeClone();
       }
       else
       {  System.err.println("Read saved solution successfully, but it does not match the current configuration, proceeding with default intialization.");
       }
     }
     catch (Exception e)
     {
         System.err.print("Error reading the previous results: ");
         System.err.println(e.getMessage());
         System.err.println("Proceeding with default initialization.");
     }
   }
}
