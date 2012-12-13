package linearEquationSolvers;
import basicAbstractions.*;
import decompositions.ArnoldiContraction;
import java.io.IOException;
import Jama.Matrix;
public class FOMLinearEquationSolver implements ALinearEquationSolver
{ double accuracy=1.0e-7;
  int dim=200;
  public AFunction calculateSolution(ALinearEquation eq)
  {
    try
    {
      AnOperator a=eq.a;
      AFunction x0=eq.rhs.completeClone();
      AFunction v0;
      AFunction[] w;
      double beta;
      Matrix h;
      Matrix sol;
      ArnoldiContraction ac;
      AFunction xm=x0.completeClone();
      int rank=Math.min(dim,x0.getArrayLength());
      double[] e0=new double[rank];
      for (int i=0;i<rank;i++) e0[i]=0.0;
      e0[0]=1.0;
      Matrix e1=new Matrix(e0,rank);
      // System.err.print("approx solution in GMRES... is");
      //for (int i=0;i<rank;i++) System.err.print(" "+x0.coef[i]);
      //
      //eq.rhs.normalize();
      //beta=1.0;
      ac=new ArnoldiContraction(rank);
      int niter=0;
      do
      { x0=xm.completeClone();
        eq.calculateResidue(x0);
        beta=eq.getResidueNorm();
        v0=eq.getResidue();
        v0=v0.times(1.0/beta);
        ac.init(a,v0,rank);
        h=new Matrix(ac.getMatrix());
        sol=h.inverse().times(e1.times(beta));
        w=ac.getBasis();
        xm.setZero();
        xm=xm.plus(x0);
        for(int i=0;i<rank;i++) xm=xm.plus(w[i].times(sol.getArray()[i][0]));
        eq.calculateResidue(xm);
        System.out.println("Residue ="+eq.getResidueNorm()+"   "+niter);
      }
      while ((eq.getResidueNorm()>accuracy)&&(niter++<1000));
      x0=xm;
      return x0;
    }
    catch(java.io.IOException ioExc)
    { System.err.println("IO error in FOMLinearEquationSolver");
      System.exit(1);
      return null;
    }
  }
  public void setAccuracy(double newAccuracy)
  { accuracy=newAccuracy;
  }
  public void setDimention(int dim)
  { this.dim=dim;
  }
}
