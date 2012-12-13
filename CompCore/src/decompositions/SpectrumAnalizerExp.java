package decompositions;

import basicAbstractions.AnOperator;
import basicAbstractions.AFunction;
import auxiliary.DFunction;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class SpectrumAnalizerExp
{
// Fields:
  AnOperator f;
  AFunction ini;
  GrowableArnoldiContraction a;
  MyEigenvalueDecomposition ev;
  double accuracy=1.0e-14;
  public int nEV=3;
  int nRestart=200;
  int dimKrylov=200;
  double conversionFactor;
// Constructors:
  public SpectrumAnalizerExp(AnOperator f, int nEV, double conversionFactor)
  {
    this.f=f;
    this.nEV=nEV;
    this.conversionFactor=conversionFactor;
    this.dimKrylov=Math.max(nEV*2,this.dimKrylov);
    ini=new DFunction(f.getRank());
    ini.setRandom();
        try {
            initializeTryalVector();
        } catch (Exception ex) {
            Logger.getLogger(SpectrumAnalizerExp.class.getName()).log(Level.SEVERE, null, ex);
        }
  }
  public SpectrumAnalizerExp(AnOperator f, AFunction ini, int nEV, double conversionFactor)
  {
    this.conversionFactor=conversionFactor;
    this.f=f;
    this.nEV=nEV;
    this.ini=ini;
  }

// Methods:
  public void analize()
  {
    int niter=0;
    a=new GrowableArnoldiContraction(dimKrylov);
    do
    {
      a.ini(f,ini);
      do
      { a.makeStep();
      }
      while ((!isAccuracyOK())&&(a.getStepNumber()<dimKrylov));
      if (!isAccuracyOK()) reini();
    }
    while((!isAccuracyOK())&&(niter++<nRestart));
  }


  private void reini()
  { double[] rini=new double[f.getRank()];
    double w;
    for (int i=0;i<rini.length;i++)
    { rini[i]=0.0;
      for (int k=1;k<=Math.min(nEV, dimKrylov);k++)
      for (int j=0;j<dimKrylov;j++)
        rini[i]+=ev.getEigenvectorsRightRe()[j][dimKrylov-k]*a.getBasis()[j].coef[i]
                ;// *Math.exp(-Math.pow((dimKrylov-k)/(2.0*nEV),2));

    }
    ini=new DFunction(rini);
  }

  //

  public void setNRestart(int nRestart)
  {
    this.nRestart=nRestart;
  }

  //

  public void setAccuracy(double accuracy)
  {
    this.accuracy=accuracy;
  }

  //

  private boolean isAccuracyOK()
  { double eps=1.0;
    int n=a.getStepNumber();
    ev=new MyEigenvalueDecomposition(new Jama.Matrix(a.getMatrix(),n,n));
    ev.setIfSortAbs(false);
    eps=0.0;
    for (int i=0;i<Math.min(nEV,n);i++) eps+=Math.abs(ev.getEigenvectorsRightRe()[n-1][n-1-i])+Math.abs(ev.getEigenvectorsRightIm()[n-1][n-1-i]);
    System.out.println("-----------------"+n+"--------------------");
    for (int i=0; i<Math.min(n,nEV); i++)
    //for (int i=0; i<n; i++)
       {
         System.out.print("EV: "+((1-ev.getEigenvaluesRe()[n-1-i])/(1+ev.getEigenvaluesRe()[n-1-i]))*conversionFactor
                                  +"+"+(ev.getEigenvaluesIm()[n-1-i])*conversionFactor+"I");
         System.out.println("     "+"(+- "+ev.getEigenvectorsRightRe()[n-1][n-1-i]
				  +" + "+ev.getEigenvectorsRightIm()[n-1][n-1-i]+"I)");
//         System.out.println();
       }

    return (eps<accuracy);
  }

  //

  public void printAllEigenvalues(PrintStream out)
  { out.println("printAllEigenvalues output... to be implemented");
  }

  //

  public void printEigenvalues(PrintStream out)
  { out.println("printEigenvalues output... to be implemented");
  }

    public AFunction[] getEigenvectors()
    { 
      AFunction[] res=new AFunction[nEV];
      double[] resarray=new double[f.getRank()];
      int cDim=ev.getEigenvectorsRightRe().length;
      for (int k=0;k<nEV;k++)
      {
          for (int i=0; i<resarray.length; i++)
          {
              resarray[i]=0.0;
              for (int j=0; j<cDim; j++)
                  resarray[i]+=ev.getEigenvectorsRightRe()[j][cDim-k-1]*a.getBasis()[j].coef[i];
          }
          res[k]=new DFunction(resarray);
      }
      return res;
    }
    //
    public void initializeTryalVector() throws Exception
   {
     //MulticomponentThreeBodyEquation theEq=new MulticomponentThreeBodyEquation(0.0);
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
    //
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
}