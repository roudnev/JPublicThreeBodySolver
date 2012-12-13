package twoBodyOperators;
//import threebodysolverconfigurator.basicAbstractions.AnOperator;
import basicAbstractions.*;
import hermitSplines.*;
import decompositions.MyEigenvalueDecomposition;
import java.io.PrintWriter;
import Jama.Matrix;
import potentialCollection.WeightedPotential;
public class TwoBodyOperator extends FreeTwoBodyHamiltonian implements AnOperator
{
  WeightedPotential potential;
  double reducedMass;
  double massNorm;
  Matrix idMatrix;
  Matrix diff;
  Matrix diff2;
  private int numberOfBoundStates=0;
  public TwoBodyOperator(SimpleGrid theGrid, double z,int l, WeightedPotential potential,double reducedMass, double massNorm)
  { super(theGrid, z, l);
    this.potential=potential;
    this.reducedMass=reducedMass;
    this.massNorm=massNorm;
    refillTheMatrix(z, l);
//    System.err.print("Matrix filled ");
    invMatrix=theMatrix.inverse();
    theSpectrum=new MyEigenvalueDecomposition(getTheMatrix());
    countBoundStates();
//    System.err.println("Initilization OK ");
//    printSpectrum();
  }

  void refillTheMatrix(double z, int l)
  {
    double xi;
    double dl=l*(l+1.0);
    for (int i=0; i<nMax;i++)
    for (int j=0; j<nMax;j++)
    { theMatrixArray[i][j]=0.0;
      identityOperatorArray[i][j]=0.0;
    }
    for(int i=0;i<nMax;i++)
    { xi=getSplineColloc().mesh[i];
      for(int j=0;j<nMax;j++)
      { 
         if (Math.abs(i-j)<6)
         {
          theMatrixArray[i][j]=-splineSpace.d2BSpline(xi,j)
                             +(dl/xi/xi+potential.valueAt(xi) - z)*splineSpace.fBSpline(xi,j);
          identityOperatorArray[i][j]=getSplineSpace().fBSpline(xi,j);
         }
      }
//      System.out.println(xi+"   "+potential.valueAt(xi));
    }
    idMatrix=new Matrix(identityOperatorArray);
    theMatrix=new Matrix(theMatrixArray);
    theMatrix=idMatrix.inverse().times(getTheMatrix());
  }
  @Override
   public void printSpectrum(int nValues, PrintWriter output)
  {
    for (int i=0;i<nValues;i++)
   { //output.format("[%1$2d]:  %2$ 7g%3$+2fI  ",i,getTheSpectrum().getEigenvaluesRe()[i]/potential.getCoupling(),getTheSpectrum().getEigenvaluesIm()[i]/potential.getCoupling());
      output.format("  %2$ 15e  ",i,getTheSpectrum().getEigenvaluesRe()[i]/potential.getCoupling(),getTheSpectrum().getEigenvaluesIm()[i]/potential.getCoupling());
     // output.format("[%1$2d]:  %2$ 7g%3$+2fI  ",i,getTheSpectrum().getEigenvaluesRe()[i],getTheSpectrum().getEigenvaluesIm()[i]);
    }
    //output.println();
  }
   private void countBoundStates()
  {
    numberOfBoundStates=0;
    for (int i=0;i<nMax;i++)
      if (getTheSpectrum().getEigenvaluesRe()[i]<0.0) numberOfBoundStates++;
  }
  /**
   * @return the numberOfBoundStates
   */
  public int getNumberOfBoundStates()
  {
    return numberOfBoundStates;
  }
  //
  public void printLowEnergyParameters(PrintWriter output)
  { 
    for(int i=0;i<numberOfBoundStates;i++)
    { output.format("  %2$ 15e  ",i,getTheSpectrum().getEigenvaluesRe()[i]/potential.getCoupling(),getTheSpectrum().getEigenvaluesIm()[i]/potential.getCoupling());
    }
    int nContStates=3;
    double[] spectrum=getTheSpectrum().getEigenvaluesRe();
    double[] contStateEnergyArray=new double[nContStates];
    double[] deltaArray=new double[nContStates];
    double[] kctgArray=new double[nContStates];
    Matrix rhs=new Matrix(nContStates,1);
    Matrix matr=new Matrix(nContStates,3);
    Matrix res=new Matrix(nContStates,1);
    double k;
    double rMax=theGrid.getRightmostPoint();
//    System.out.println("rMax="+rMax);
    double skn2=0.0;
    double skn4=0.0;
    double skctg=0.0;
    double sk2kctg=0.0;
    for(int i=0;i<contStateEnergyArray.length;i++)
    {
        contStateEnergyArray[i]=spectrum[numberOfBoundStates+i];
//        System.out.println("Energy="+contStateEnergyArray[i]);
        k=Math.sqrt(contStateEnergyArray[i]);
        deltaArray[i]=(i+0.5)*Math.PI-k*rMax;
//        System.out.println("Delta="+deltaArray[i]);
        kctgArray[i]=k/Math.tan(deltaArray[i]);
        matr.set(i, 0, -1.0);
        matr.set(i, 1, 0.5*k*k);
        matr.set(i, 2, k*k*k*k);
        rhs.set(i, 0, kctgArray[i]);
//        skn2+=k*k;
//        skn4+=k*k*k*k;
//        skctg+=kctgArray[i];
//        sk2kctg+=k*k*kctgArray[i];
    }
//    matr.set(0, 0, 1.0*nContStates);
//    matr.set(0, 1, -0.5*skn2);
//    matr.set(1, 0, -0.5*skn2);
//    matr.set(0, 0, 0.25*skn4);
//    rhs.set(0, 0, -skctg);
//    rhs.set(1, 0, 0.5*sk2kctg);
    res=matr.solve(rhs);
    double a=1.0/res.getArray()[0][0]/potential.getScale();
    double r0=res.getArray()[1][0]/potential.getScale();
    double x=res.getArray()[2][0]/potential.getScale()/(r0*r0*r0);
//    for (int i=0;i<nContStates;i++)
//        System.out.print((-1.0/a+0.5*r0*contStateEnergyArray[i]+contStateEnergyArray[i]*contStateEnergyArray[i]*x)+"  ");
//    System.out.println();
//    for (int i=0;i<nContStates;i++)
//        System.out.print(kctgArray[i]+"  ");
//    System.out.println();
    output.format("  %1$ 15e  %2$ 15e    %3$ 15e  ",a,r0,x);
    output.println();
  }
}
