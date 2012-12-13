package auxiliary;
import basicAbstractions.*;
public abstract class SparceOperator implements AnOperator
{ // fields:
  public int rank;
  public double[] a;
  public int[] iA;
  public int[] jA;
  protected int nz;
  // constructors:
  public SparceOperator()
  { //init();
  }
  //
  public SparceOperator(double[][] h)
  { init(h);
  }
  // methods:
  public void init(double[][] h)
  { rank=h.length;
    nz=0;
    double level=1.0e-15;
    for(int i=0;i<rank;i++)
    for(int j=0;j<rank;j++)
      if (Math.abs(h[i][j])>level) nz++;
    a=new double[nz];
    iA=new int[nz];
    jA=new int[nz];
    int kz=0;
    for(int i=0;i<rank;i++)
    for(int j=0;j<rank;j++)
      if (Math.abs(h[i][j])>level)
      { a[kz]=h[i][j];
        iA[kz]=i;
        jA[kz]=j;
        kz++;
      }
  }
  // working version

  public void init()
  { nz=0;
    double level=1.0e-15;
    double[] row=getRow(0);
    System.out.print("nonzero elements counting...");
    for(int i=0;i<rank;i++)
    {
      row=getRow(i);
      for(int j=0;j<row.length;j++)
//        if (Math.abs(row[j])>level) nz++;
        if (row[j]!=0.0) nz++;
    }
    System.out.println("row.length"+"="+row.length+"; nz="+nz+"; rank="+rank+"...");
    System.gc();
    a=new double[nz];
    iA=new int[nz];
    jA=new int[nz];
    System.out.print("filling in...");
    int kz=0;
    for(int i=0;i<rank;i++)
    {
      row=getRow(i);
      for(int j=0;j<row.length;j++)
        if (Math.abs(row[j])>level)
        { a[kz]=row[j];
          iA[kz]=i;
          jA[kz]=j;
          kz++;
        }
    }
  }
//
  public void initSparce()
{ nz=0;
  double level=1.0e-15;
  SparceRow row=getSparceRow(0);
  System.out.print("nonzero elements counting...");
  for(int i=0;i<rank;i++)
  {
    row=getSparceRow(i);
    for(int j=0;j<row.nz;j++)
//        if (Math.abs(row[j])>level) nz++;
      if (row.a[j]!=0.0) nz++;
  }
  System.out.print(" nz="+nz+"; rank="+rank+"...");
  a=new double[nz];
  iA=new int[nz];
  jA=new int[nz];
  System.out.print("filling in...");
  int kz=0;
  for(int i=0;i<rank;i++)
  {
    row=getSparceRow(i);
    for(int j=0;j<row.nz;j++)
      if (Math.abs(row.a[j])>level)
      { a[kz]=row.a[j];
        iA[kz]=i;
        jA[kz]=row.j[j];
        kz++;
      }
  }
}

  // new version
/*
  public void init()
  { int nz0=650*rank;
    System.out.print("Sparce operator initialization starting...");
    double level=1.0e-15;
    double[] at=new double[nz0];
    int[] iAt=new int[nz0];
    int[] jAt=new int[nz0];
    int kz=0;
    double[] row;
    for(int i=0;i<rank;i++)
    {
      row=getRow(i);
      for(int j=0;j<row.length;j++)
        if (Math.abs(row[j])>level)
        { at[kz]=row[j];
          iAt[kz]=i;
          jAt[kz]=j;
          kz++;
        }
    }
    nz=kz;
    a=new double[nz];
    iA=new int[nz];
    jA=new int[nz];
    for (int i=0;i<nz;i++)
    {
      a[i]=at[i];
      iA[i]=iAt[i];
      jA[i]=jAt[i];
    }
    System.out.println(" done. rank="+rank+" nz="+nz+" nz0="+nz0);
  }
*/
  //
  public abstract double[] getRow(int j);
  public abstract SparceRow getSparceRow(int j);
  //
  static int ncall=0;
  public final AFunction times(AFunction u)
  { AFunction res=u.imprint(rank);//u.completeClone();
    res.setZero();
    //u.dump("SparceOpTimes"+ncall+"U.log");
    //System.out.println("in SparceOperator times() res.coef.length= "+res.coef.length);
    //System.out.println("in SparceOperator times() nz="+nz);
    for(int i=0;i<nz;i++)
    {
      res.coef[iA[i]]+=a[i]*u.coef[jA[i]];
    }
    //res.dump("SparceOpTimes"+ncall+"res.log"); ncall++;
    return res;
  }
  public int getRank()
  { return rank;
  }
  /////////////////

  public void dump()
  {
    try
    {
      java.io.PrintStream out=new java.io.PrintStream(
                 new java.io.BufferedOutputStream(
                                     new java.io.FileOutputStream("SparceOperator.dump"),131072
                                                 )
                                 );
      out.println("nz="+nz);
      for (int i=0;i<nz;i++)
        out.println(iA[i]+" "+jA[i]+" "+a[i]);
      out.close();
    }
    catch(Exception e)
    { System.err.println(e.getMessage());
    }
  }
}
