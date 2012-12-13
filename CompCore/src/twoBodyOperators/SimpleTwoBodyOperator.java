package twoBodyOperators;
import basicAbstractions.*;
import hermitSplines.*;
import Jama.Matrix;
import decompositions.MyEigenvalueDecomposition;
public class SimpleTwoBodyOperator implements AnOperator, Simple
{
  APotential vPot;
  SimpleGrid theGrid;
  HSplineBC splineSpace;
  CollocGrid splineColloc;
  int nMax;
  double[][] theMatrixArray;
  double[][] identityOperatorArray;
  Matrix theMatrix;
  MyEigenvalueDecomposition theSpectrum;

  public SimpleTwoBodyOperator(int nBas, APotential pot)
  {
    theGrid=new SimpleGrid(nBas,0.0,50.0,"X equidistant grid");
    vPot=pot;

    splineSpace=new HSplineBC(theGrid,'Q','D','D');
    splineColloc=new CollocGrid(splineSpace);
    this.nMax=splineColloc.mesh.length;
    theMatrixArray=new double[splineColloc.mesh.length][splineColloc.mesh.length];
    identityOperatorArray=new double[splineColloc.mesh.length][splineColloc.mesh.length];
    fillTheMatrix();
    theSpectrum=new MyEigenvalueDecomposition(theMatrix);
    //printSpectrum();
  }

  void fillTheMatrix()
  {
    double xi;
    for(int i=0;i<nMax;i++)
    { xi=splineColloc.mesh[i];
      for(int j=0;j<nMax;j++)
      { theMatrixArray[i][j]=-splineSpace.d2BSpline(xi,j)
                             +vPot.valueAt(xi)*splineSpace.fBSpline(xi,j);
        identityOperatorArray[i][j]=splineSpace.fBSpline(xi,j);
      }
    }
    Matrix tmpId=new Matrix(identityOperatorArray);
    theMatrix=new Matrix(theMatrixArray);
    theMatrix=tmpId.inverse().times(theMatrix);
  }

  public AFunction times(AFunction arg)
  {
    AFunction res=arg.completeClone();
    //System.err.println("Method times called from Simple TwoBodyOperator");
    for(int i=0;i<nMax;i++)
    { res.coef[i]=0.0;
      for(int j=0;j<nMax;j++)
      { res.coef[i]+=theMatrix.getArray()[i][j]*arg.coef[j];
      }
    }
    return res;
  }

  public AFunction solve(AFunction rhs)
  {
    Matrix tmpMatr=theMatrix.inverse();
    AFunction res=rhs.completeClone();
    for(int i=0;i<nMax;i++)
    { res.coef[i]=0.0;
      for(int j=0;j<nMax;j++)
      { res.coef[i]+=tmpMatr.getArray()[i][j]*rhs.coef[j];
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
