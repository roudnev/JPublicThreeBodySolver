package decompositions;
import java.io.*;
import basicAbstractions.*;
public class GrowableArnoldiContraction
{
// fields:
  protected int dimK;
  protected double[][] theMatrix;
  protected AFunction[] theBasis;
  protected AnOperator a;
  private int stepNumber;
  private AFunction w,u;
// constructors:
  public GrowableArnoldiContraction(int dim)
  {
//    System.out.println("### GrowableArnoldiContraction constructor called with dim="+dim);
    dimK=dim;
    theMatrix=new double[dimK+1][dimK];
    theBasis=new AFunction[dimK+1];
    stepNumber=0;
  }

  public GrowableArnoldiContraction(AnOperator a, AFunction v0,int dim)
  {
    this(dim);
    ini(a,v0);
    for (int j=0;j<dimK;j++)
    { makeStep();
    }
  }

//
// Methods:
  public void ini(AnOperator a, AFunction v0)
  {
    this.a=a;
    w=v0.completeClone();
    u=v0.completeClone();
    double resid=0;
    theBasis[0]= v0.completeClone();
    theBasis[0].normalize();
    stepNumber=0;
  }
  public void makeStep()
  { int j=stepNumber;
//    System.out.println("### makeStep called");
    double scal=0.0;
  // the step:
    if (j<dimK)
    {
      w=a.times(theBasis[j]);
      for(int i=0;i<=j;i++) theMatrix[i][j]=w.scal(theBasis[i]);
      u.setZero();
      for(int i=0;i<=j;i++) u=u.plus(theBasis[i].times(theMatrix[i][j]));
      w=w.minus(u);
      // ensure orthogonality:
      for (int i=0; i<=j; i++)
      { scal=w.scal(theBasis[i]);
	w=w.minus(theBasis[i].times(scal));
      }
      double resid=java.lang.Math.sqrt(w.norm2());
      theMatrix[j+1][j]=resid;
      w.normalize();
      theBasis[j+1]=w.completeClone();
      }
      else
      { System.err.println("Error: Krylov subspace dimension shoud not exceed "+dimK);
      }
    // end step
//    System.out.println(stepNumber);
    stepNumber++;
    //return stepNumber;
  }

  public void schrink(int dim)
  {
      theBasis=null;
      theMatrix=null;
      System.gc();
      dimK=dim;
      theMatrix=new double[dimK+1][dimK];
      theBasis=new AFunction[dimK+1];
      stepNumber=0;
  }

  public int getStepNumber()
  { return stepNumber;
  }

  public AFunction[] getBasis()
  { AFunction[] res=theBasis;
    return res;
  }
  //
  public double[][] getMatrix()
  { double[][] res=theMatrix;
    return res;
  }

}
