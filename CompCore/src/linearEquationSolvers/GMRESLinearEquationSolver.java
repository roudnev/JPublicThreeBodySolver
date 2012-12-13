package linearEquationSolvers;
import basicAbstractions.*;
import decompositions.*;
import java.io.*;
import Jama.*;
public class GMRESLinearEquationSolver implements ALinearEquationSolver
{ double accuracy=1.0e-9;
  int dim=500;
  double[] si,ci;
  double[][] hBar;
  private boolean ifVerbose=true;
  public AFunction calculateSolution(ALinearEquation eq)
  {
      AnOperator a=eq.a;
      AFunction x0=initSolution(eq.rhs.completeClone());
      AFunction v0;
      AFunction[] w;
      double beta;
      Matrix h;
      Matrix sol;
      AFunction xm=x0.completeClone();
      int rank=Math.min(dim,x0.getArrayLength()+1);
      GrowableArnoldiContraction ac=new GrowableArnoldiContraction(rank);
      int irank=0;
      double[] e0=new double[rank];
      for (int i=0;i<rank;i++) e0[i]=0.0;
      e0[0]=1.0;
      Matrix el0=new Matrix(e0,rank);
      Matrix el;
      int niter=0;
      do
      {
        try
        {
          x0=xm.completeClone();
          eq.calculateResidue(x0);
          beta=eq.getResidueNorm();
//          System.out.println("### Residue="+beta);
          v0=eq.getResidue();
          v0=v0.times(1.0/beta);
//          System.out.println("### init begin");
          ac.ini(a,v0);
         // ac=new ArnoldiContraction(a,v0,rank);
//          System.out.println("### init end");
          irank=0;
          ci=new double[rank];
          si=new double[rank];
          ac.makeStep();
          irank=ac.getStepNumber();
          h=new Matrix(ac.getMatrix()).getMatrix(0,irank,0,irank-1);
          Matrix hi=new Matrix(rank,rank-1);
          double d=Math.sqrt(Math.pow(h.getArray()[irank-1][irank-1],2)+Math.pow(h.getArray()[irank][irank-1],2));
          si[0]=h.getArray()[irank][irank-1]/d;
          ci[0]=h.getArray()[irank-1][irank-1]/d;
          hi=makeHi(hi,h,0);
          el=calcRHS(el0.getMatrix(0,1,0,0),0).times(beta);
          //el.print(new java.text.DecimalFormat(),el0.getRowDimension());
          int j=1;
          do
          {
            ac.makeStep();
            irank=ac.getStepNumber();
            h=new Matrix(ac.getMatrix()).getMatrix(0,irank,0,irank-1);
            d=Math.sqrt(Math.pow(hi.getArray()[j-1][j-1],2)+Math.pow(h.getArray()[j][j-1],2));
            si[j]=h.getArray()[j][j-1]/d;    //System.out.print("si="+si[j]);
            ci[j]=hi.getArray()[j-1][j-1]/d; //System.out.print("ci="+ci[j]);
            hi=makeHi(hi,h,j);
            el=calcRHS(el,j);
            //h.print(new java.text.DecimalFormat(),rank);
            sol=hi.getMatrix(0,j,0,j).inverse().times(el.getMatrix(0,j,0,0));
            w=ac.getBasis();
            xm.setZero();
            xm=xm.plus(x0);
            for(int i=irank-1;i>=0;i--) xm=xm.plus(w[i].times(sol.getArray()[i][0]));
            //
            if (ifVerbose) System.out.println("Residue ="+Math.abs(el.get(irank,0))+"   "+niter+" "+j);
          }
          while ((Math.abs(el.get(irank,0))>accuracy*0.1)&&(j++<rank-1));
          eq.calculateResidue(xm);
        }
        catch (java.lang.OutOfMemoryError e)
        {  System.err.print(e.getMessage());
           System.gc();
           rank=Math.max(irank-5,1);
           ac.schrink(rank);
        }
      }
      while ((eq.getResidueNorm()>accuracy)&&(niter++<40));
      if (niter>40) System.out.println("The system is ill conditioned. The number of iterations exceeded");
      x0=xm;
      saveSolution(x0);
      return x0;
  }
  public void setAccuracy(double newAccuracy)
  { accuracy=newAccuracy;
  }
  //
  private Matrix makeHi(Matrix hi,Matrix h,int n)
  { hi=new Matrix(h.getArrayCopy());
//    h.print(new java.text.DecimalFormat(),n);
//    System.out.print("====================");
    for (int i=0;i<=n;i++)
    {
      double d=Math.sqrt(Math.pow(h.getArray()[i][i],2)+Math.pow(h.getArray()[i+1][i],2));
      si[i]=h.getArray()[i+1][i]/d;
      ci[i]=h.getArray()[i][i]/d;
      for (int j=i;j<=n;j++)
      { hi.set(i,j,ci[i]*h.get(i,j)+si[i]*h.get(i+1,j));
        hi.set(i+1,j,-si[i]*h.get(i,j)+ci[i]*h.get(i+1,j));
      }
     h=new Matrix(hi.getArrayCopy());
    }
//    h.print(new java.text.DecimalFormat(),n);
    return h;
  }

  //
  private Matrix calcRHS(Matrix e,int n)
  { Matrix res=new Matrix(n+2,1);
    for(int i=0;i<e.getRowDimension();i++) res.getArray()[i][0]=e.getArray()[i][0];
    res.getArray()[n][0]=ci[n]*e.getArray()[n][0];//+si[n]*e.getArray()[n+1][0];
    res.getArray()[n+1][0]=-si[n]*e.getArray()[n][0];//+ci[n]*e.getArray()[n+1][0];
    return res;
  }
  private void saveSolution(AFunction sol)
  { try{
      ObjectOutputStream out=new ObjectOutputStream(
                                   new FileOutputStream("solution.dat")
                                 );
      out.writeObject(sol);
      out.close();
    }
    catch(IOException e){ System.err.println(e.getMessage());}
  }
  private AFunction initSolution(AFunction ini)
  { AFunction res;
    try {
      File iniFile=new File("solution.dat");
      if(iniFile.exists())
      {
        ObjectInputStream in=new ObjectInputStream(
                                   new FileInputStream(iniFile)
                                 );
        res=(AFunction)in.readObject();
        in.close();
        if (res.getArrayLength()!=ini.getArrayLength())
        { res=ini.completeClone();
        System.out.println("The solution.dat does not match the configuration, using the default");

        }
      }
      else
      {
        res=ini.completeClone();
        System.out.println("File solution.dat does not exist, using the rhs as an initial guess");
      }
    }
    catch(ClassNotFoundException e)
    { System.err.println(e.getMessage());
      res=ini.completeClone();
    }
    catch(IOException e)
    { System.err.println(e.getMessage());
      res=ini.completeClone();
    }
    return res;
  }
  public void setIfVerbose(boolean x)
  {
    ifVerbose=x;
  }
}
