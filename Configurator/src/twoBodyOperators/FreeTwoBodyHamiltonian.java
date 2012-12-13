package twoBodyOperators;
import basicAbstractions.*;
import hermitSplines.*;
import Jama.Matrix;
import java.io.PrintStream;
import java.io.PrintWriter;
import decompositions.MyEigenvalueDecomposition;
public class FreeTwoBodyHamiltonian implements AnOperator, Simple
{ //fields:
  double z;
  int l;
  SimpleGrid theGrid;
  HSplineBC splineSpace;
  private CollocGrid splineColloc;
  protected int nMax;
  double[][] theMatrixArray;
  double[][] identityOperatorArray;
  protected Matrix theMatrix;
  protected Matrix invMatrix;
  protected MyEigenvalueDecomposition theSpectrum;
  //constructors:
  public FreeTwoBodyHamiltonian(SimpleGrid theGrid, double z,int l)
  {
    this.z=z;
    this.l=l;
    this.theGrid=theGrid;
    double k=Math.sqrt(z);
    double kx=k*theGrid.mesh[theGrid.mesh.length-1];
    double tkx=-k*Math.tan(kx);
    double[] bcl=new double[2];
    bcl[0]=0.0;    bcl[1]=0.0;
    double[] bcr=new double[2];
    bcr[0]=tkx;    bcr[1]=-z;
    splineSpace=new HSplineBC(theGrid,'Q','D','N',bcl,bcr);
    splineColloc=new CollocGrid(getSplineSpace());
    this.nMax=splineColloc.mesh.length;
    theMatrixArray=new double[splineColloc.mesh.length][splineColloc.mesh.length];
    identityOperatorArray=new double[splineColloc.mesh.length][splineColloc.mesh.length];
    fillTheMatrix(z,l);
    //theSpectrum=new MyEigenvalueDecomposition(theMatrix);
    //printSpectrum();
    invMatrix=theMatrix.inverse();
  }
  public FreeTwoBodyHamiltonian(HSplineBC splineSpace, double z,int l)
  {
    this.z=z;
    this.l=l;
    theGrid=splineSpace.getGrid();
//    double k=Math.sqrt(z);
//    double kx=k*theGrid.mesh[theGrid.mesh.length-1];
//    double tkx=-k*Math.tan(kx);
//    double[] bcl=new double[2];
//    bcl[0]=0.0;    bcl[1]=0.0;
//    double[] bcr=new double[2];
//    bcr[0]=tkx;    bcr[1]=-z;
    this.splineSpace=splineSpace;
    splineColloc=new CollocGrid(getSplineSpace());
    this.nMax=splineColloc.mesh.length;
    theMatrixArray=new double[splineColloc.mesh.length][splineColloc.mesh.length];
    identityOperatorArray=new double[splineColloc.mesh.length][splineColloc.mesh.length];
    fillTheMatrix(z,l);
    //theSpectrum=new MyEigenvalueDecomposition(theMatrix);
    //printSpectrum();
    invMatrix=theMatrix.inverse();
  }
  //methods:
  void fillTheMatrix(double z, int l)
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
      { if (Math.abs(i-j)<6)
        {  theMatrixArray[i][j]=-splineSpace.d2BSpline(xi,j)
                                             +(dl/xi/xi - z)*getSplineSpace().fBSpline(xi,j);
           identityOperatorArray[i][j]=getSplineSpace().fBSpline(xi,j);
        }
      }
    }
    Matrix tmpId=new Matrix(identityOperatorArray);
    theMatrix=new Matrix(theMatrixArray);
    theMatrix=tmpId.inverse().times(theMatrix);
  }

  public AFunction times(AFunction arg)
  {
    AFunction res=arg.completeClone();
    int nM=arg.getArrayLength();
    //System.err.println("Method times called from Simple TwoBodyOperator");
    for(int i=0;i<nM;i++)
    { res.coef[i]=0.0;
      for(int j=0;j<nM;j++)
      { res.coef[i]+=getTheMatrix().getArray()[i][j]*arg.coef[j];
      }
    }
    return res;
  }

  public AFunction solve(AFunction rhs)
  {
    AFunction res=rhs.completeClone();
    int nM=rhs.getArrayLength();
    /* System.err.println("In FreeTwoBodyHamiltonian: rank="+nM); */
    for(int i=0;i<nM;i++)
    { res.coef[i]=0.0;
      for(int j=0;j<nM;j++)
      { res.coef[i]+=getInvMatrix().getArray()[i][j]*rhs.coef[j];
      }
    }
    return res;
  }

  public void printSpectrum(PrintWriter output)
  {
    for (int i=0;i<getNMax();i++)
    { output.println(getTheSpectrum().getEigenvaluesRe()[i]+" "+getTheSpectrum().getEigenvaluesIm()[i]+"I");
    }
  }

  public void printSpectrum(int nValues, PrintWriter output)
  {
    for (int i=0;i<nValues;i++)
    { output.format("[%1$2d]:  %2$ 13g%3$+2fI  ",i,getTheSpectrum().getEigenvaluesRe()[i],getTheSpectrum().getEigenvaluesIm()[i]);
    }
    output.println();
  }

    @Override
  public int getRank()
  { return getNMax();
  }

  /**
   * @return the splineColloc
   */
  public CollocGrid getSplineColloc()
  {
    return splineColloc;
  }

  /**
   * @return the nMax
   */
  public int getNMax()
  {
    return nMax;
  }

  /**
   * @return the theMatrix
   */
  public Matrix getTheMatrix()
  {
    return theMatrix;
  }

  /**
   * @return the invMatrix
   */
  public Matrix getInvMatrix()
  {
    return invMatrix;
  }

  /**
   * @return the theSpectrum
   */
  public MyEigenvalueDecomposition getTheSpectrum()
  {
    return theSpectrum;
  }

  /**
   * @return the splineSpace
   */
  public HSplineBC getSplineSpace()
  {
    return splineSpace;
  }

  /**
   * @param splineSpace the splineSpace to set
   */
  public void setSplineSpace(HSplineBC splineSpace)
  {
    this.splineSpace = splineSpace;
  }
}
