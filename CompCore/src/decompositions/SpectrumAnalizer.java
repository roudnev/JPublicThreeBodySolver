package decompositions;

import basicAbstractions.AnOperator;
import basicAbstractions.AFunction;
import auxiliary.DFunction;
import java.io.PrintStream;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class SpectrumAnalizer
{
// Fields:
  AnOperator f;
  AFunction ini;
  GrowableArnoldiContraction a;
  MyEigenvalueDecomposition ev;
  double accuracy=1.0e-6;
  double zShift=0.0;
  public int nEV=3;
  int nRestart=40;
  int dimKrylov=200;
  boolean ifShifted=true;
  boolean ifInversed=true;
  double conversionFactor;
  public double[] eigenValues;
// Constructors:
  public SpectrumAnalizer(AnOperator f, int nEV, double conversionFactor)
  {
    this.f=f;
    this.nEV=nEV;
    this.conversionFactor=conversionFactor;
    this.dimKrylov=Math.max(nEV*2,this.dimKrylov);
    ini=new DFunction(f.getRank());
    ini.setRandom();eigenValues=new double[nEV];
  }
  public SpectrumAnalizer(AnOperator f, AFunction ini, int nEV, double conversionFactor)
  {
    this.conversionFactor=conversionFactor;
    this.f=f;
    this.nEV=nEV;
    this.ini=ini;
    eigenValues=new double[nEV];
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
      for (int k=1;k<=Math.min(nEV+1, dimKrylov);k++)
      for (int j=0;j<dimKrylov;j++)
        rini[i]+=ev.getEigenvectorsRightRe()[j][dimKrylov-k]*a.getBasis()[j].coef[i];
                // *Math.exp(-Math.pow((dimKrylov-k)/(2.0*nEV),2));

    }
    ini=new DFunction(rini);
  }


  public void setZShift(double zShift)
  {
    this.zShift=zShift;
    ifShifted=(zShift!=0.0);
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
    eps=0.0;
    
    System.out.println("-----------------"+n+"--------------------");
    if (ifInversed)
    {
      ev.setIfSortAbs(true);
      for (int i=1;i<=Math.min(nEV,n);i++) eps+=Math.abs(ev.getEigenvectorsRightRe()[n-1][n-i])+Math.abs(ev.getEigenvectorsRightIm()[n-1][n-i]);

      for (int i=1; i<=Math.min(nEV,n); i++) //Math.min(nEV,n)
       {
         int nev=n-i;
         System.out.print("EV: "+(ev.getEigenvaluesInvRe()[nev]+zShift)/conversionFactor //+" ("+ev.getEigenvaluesRe()[nev]+") "
                                  +"+"+ev.getEigenvaluesInvIm()[nev]/conversionFactor+"I");
         System.out.println("     "+"(+- "+ev.getEigenvectorsRightRe()[n-1][nev]
				  +" + "+ev.getEigenvectorsRightIm()[n-1][nev]+"I)");
         eigenValues[i-1]=(ev.getEigenvaluesInvRe()[nev]+zShift)/conversionFactor;
//         System.out.println();
       }
     }
     else
     {
       for (int i=0;i<Math.min(nEV,n);i++) eps+=Math.abs(ev.getEigenvectorsRightRe()[n-1][i])+Math.abs(ev.getEigenvectorsRightIm()[n-1][i]);
       for (int i=0; i<Math.min(n,nEV); i++)
       {
         System.out.print("EV: "+(ev.getEigenvaluesRe()[i]+zShift)/conversionFactor
                                  +"+"+ev.getEigenvaluesIm()[i]/conversionFactor+"I");
         System.out.println("     "+"(+- "+ev.getEigenvectorsRightRe()[n-1][i]
				  +" + "+ev.getEigenvectorsRightIm()[n-1][i]+"I)");
         eigenValues[i]=(ev.getEigenvaluesRe()[i]+zShift)/conversionFactor;
//         System.out.println();
       }
     }
    return (eps<accuracy);
  }

  //

  public void inversedOn()
  { ifInversed=true;
  }

  //

  public void inversedOff()
  { ifInversed=false;
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
}