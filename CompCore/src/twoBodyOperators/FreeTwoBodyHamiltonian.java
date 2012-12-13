package twoBodyOperators;
import basicAbstractions.*;
import hermitSplines.*;
import Jama.Matrix;
import decompositions.MyEigenvalueDecomposition;
public class FreeTwoBodyHamiltonian implements AnOperator, Simple
{ //fields:
  double z;
  int l;
  SimpleGrid theGrid;
  HSplineBC splineSpace;
  CollocGrid splineColloc;
  int nMax;
  double[][] theMatrixArray;
  double[][] identityOperatorArray;
  Matrix theMatrix;
  Matrix invMatrix;
  MyEigenvalueDecomposition theSpectrum;
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
    splineSpace=new HSplineBC(theGrid,'Q','D','3',bcl,bcr);
    splineColloc=new CollocGrid(splineSpace);
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
    for(int i=0;i<nMax;i++)
    { xi=splineColloc.mesh[i];
      for(int j=0;j<nMax;j++)
      { theMatrixArray[i][j]=-splineSpace.d2BSpline(xi,j)
                             +(dl/xi/xi - z)*splineSpace.fBSpline(xi,j);
        identityOperatorArray[i][j]=splineSpace.fBSpline(xi,j);
      }
    }
    Matrix tmpId=new Matrix(identityOperatorArray);
    theMatrix=new Matrix(theMatrixArray);
//    theMatrix=tmpId.inverse().times(theMatrix);
  }

  public AFunction times(AFunction arg)
  {
    AFunction res=arg.completeClone();
    int nM=arg.getArrayLength();
    //System.err.println("Method times called from Simple TwoBodyOperator");
    for(int i=0;i<nM;i++)
    { res.coef[i]=0.0;
      for(int j=0;j<nM;j++)
      { res.coef[i]+=theMatrix.getArray()[i][j]*arg.coef[j];
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
      { res.coef[i]+=invMatrix.getArray()[i][j]*rhs.coef[j];
      }
    }
    return res;
  }

  public void printSpectrum()
  {
    for (int i=0;i<nMax;i++)
    { System.out.println(theSpectrum.getEigenvaluesRe()[i]+" "+theSpectrum.getEigenvaluesIm()[i]+"I");
    }
  }

  public int getRank()
  { return nMax;
  }
}
