package decompositions;
import java.io.*;
import basicAbstractions.*;
public class ArnoldiContraction
{
// fields:
  protected int dimK;
  protected double[][] theMatrix;
  protected AFunction[] theBasis;
// constructors:
  public ArnoldiContraction(AnOperator a, AFunction v0, int dim) //throws IOException
  { // initialization
    //System.out.println("Dimension="+dim+"="+v0.getArrayLength()+"?");
    dimK=dim;
//    AFunction w=new AFunction(v0);
//    AFunction u=new AFunction(v0);
    AFunction w=v0.completeClone();
    AFunction u=v0.completeClone();
    theMatrix=new double[dimK][dimK];
    theBasis=new AFunction[dimK];
    double resid=0;
    theBasis[0]= v0.completeClone();
    theBasis[0].normalize();
    //
    // the main loop follows:
    //
    double scal=0.0;
    for(int j=0; j<dimK; j++)
    { w=a.times(theBasis[j]);
      for(int i=0;i<=j;i++) theMatrix[i][j]=w.scal(theBasis[i]);
      u.setZero();
      for(int i=0;i<=j;i++) u=u.plus(theBasis[i].times(theMatrix[i][j]));
      w=w.minus(u);
      // ensure orthogonality:
      for (int i=0; i<=j; i++)
      { scal=w.scal(theBasis[i]);
	w=w.minus(theBasis[i].times(scal));
      }
      resid=java.lang.Math.sqrt(w.norm2());
      if (j<dimK-1)
      { theMatrix[j+1][j]=resid;
        w.normalize();
        theBasis[j+1]=w.completeClone();;
      }
      //System.out.println(j+" | "+resid);
    }
// Debugging fragment
/*
    FileOutputStream MyOutputFile=new FileOutputStream("MyCheckFile.txt");
    PrintStream MyOutput=new PrintStream(MyOutputFile,true);
    System.out.print("Checking orthogonality...");
    double s=0.0;
    for(int i=0; i<dimK; i++)
    { for(int j=0; j<dimK; j++)
      { scal=theBasis[i].scal(theBasis[j]);
        s+=scal*scal;
        if (i==j) s=s-1.0;
	MyOutput.print("["+i+"]"+"["+j+"]"+"="+scal+"      ");
      }
      MyOutput.println();
    }
    System.out.println(java.lang.Math.sqrt(s));
*/
  }
  public ArnoldiContraction(int dim)
  {
    dimK=dim;
    theMatrix=new double[dimK][dimK];
    theBasis=new AFunction[dimK];
  }
  //
  public void init(AnOperator a, AFunction v0, int dim) throws IOException
  { // initialization
    //    System.out.println("Dimension="+dim+"="+v0.getArrayLength()+"?");
//    AFunction w=new AFunction(v0);
//    AFunction u=new AFunction(v0);
    AFunction w=v0.completeClone();
    AFunction u=v0.completeClone();
    double resid=0;
    theBasis[0]= v0.completeClone();
    theBasis[0].normalize();
    //
    // the main loop follows:
    //
    double scal=0.0;
    for(int j=0; j<dimK; j++)
    { w=a.times(theBasis[j]);
      for(int i=0;i<=j;i++) theMatrix[i][j]=w.scal(theBasis[i]);
      u.setZero();
      for(int i=0;i<=j;i++) u=u.plus(theBasis[i].times(theMatrix[i][j]));
      w=w.minus(u);
      // ensure orthogonality:
      for (int i=0; i<=j; i++)
      { scal=w.scal(theBasis[i]);
	w=w.minus(theBasis[i].times(scal));
      }
      resid=java.lang.Math.sqrt(w.norm2());
      if (j<dimK-1)
      { theMatrix[j+1][j]=resid;
        w.normalize();
        theBasis[j+1]=w.completeClone();
      }
      //System.out.println(j+" | "+resid);
    }
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
