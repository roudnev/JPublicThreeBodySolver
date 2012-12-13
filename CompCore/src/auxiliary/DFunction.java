package auxiliary;
import basicAbstractions.*;
import java.io.Serializable;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class DFunction extends AFunction implements Cloneable
{
  static int NTHREADS=new NCPUCongfigChecker().ncpu;
  static int scalParallelThreshold=350000;
  final Range[] slicesIndex=new Range[NTHREADS];
  public DFunction()
  { super();
    //initSlicing();
  }
  public DFunction(AFunction v)
  { this(v.coef);
  }
  public DFunction(double[] cf)
  {
    coef=new double[cf.length];
    for(int i=0;i<cf.length;i++) coef[i]=cf[i];
    //coef=(double[]) cf.clone();
    //initSlicing();
  }
  public DFunction(int n)
  { 
    coef=new double[n];
    //initSlicing();
  }
  //
  private final void initSlicing()
    {
      int slicelength=coef.length/(NTHREADS);
      for (int i=0; i<NTHREADS; i++)
      {
          int left=i*slicelength;
          int right=Math.min((i+1)*slicelength, coef.length);
          slicesIndex[i]=new Range(left, right);
      }
      slicesIndex[NTHREADS-1].right=coef.length;
//      for (int i=0; i<NTHREADS; i++)
//      {
//          System.err.println("Indexes #"+i+"   left="+slicesIndex[i].left+"   right="+slicesIndex[i].right);
//      }
    }
  //
  public final double scal(AFunction u0)
  { //double res=0.0;
    //if (this.coef.length<scalParallelThreshold)
    //  res=scalSerial(u0);
    //else
    //  res=scalParallel(u0);
    return scalSerial(u0);
  }
  public final double scalParallel(AFunction u0)
  {
    final AFunction u=u0;
    final double[] resarray=new double[NTHREADS];
    final ExecutorService exec=Executors.newFixedThreadPool(NTHREADS);
    //final ExecutorService exec=Executors.newSingleThreadExecutor();

    for (int iThread=0;iThread<NTHREADS; iThread++)
    {
        final int left=slicesIndex[iThread].left;
        final int right=slicesIndex[iThread].right;
        final int threadNumber=iThread;
        final Runnable runnable = new Runnable()
        {
             public final void run()
             {
               resarray[threadNumber]=0.0;
               for(int i=right-1;i>=left;i--) resarray[threadNumber]+=u.coef[i]*coef[i];
             }
        };
        exec.execute(runnable);
    }
    exec.shutdown();
    try
      {
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        //System.err.println("Tick...");
      }
    catch (InterruptedException ex)
      {
            Logger.getLogger(DFunction.class.getName()).log(Level.SEVERE, null, ex);
      }
    double res=0;
    for(int iThread=NTHREADS-1; iThread>=0;iThread--) res+=resarray[iThread];//coef[i]*u.coef[i];
    return res;
  }
 //
  public final AFunction timesParallel(final double x)
  { // Parallel implementation
    final DFunction res=new DFunction(coef.length);
    final ExecutorService exec=Executors.newSingleThreadExecutor();
    for (int iThread=0;iThread<NTHREADS; iThread++)
    {
        final int left=slicesIndex[iThread].left;
        final int right=slicesIndex[iThread].right;
        final Runnable runnable = new Runnable()
        {
             public final void run()
             {
               for(int i=right-1;i>=left;i--) res.coef[i]=coef[i]*x;
             }
        };
        exec.execute(runnable);
    }
    exec.shutdown();
    try
      {
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        //System.err.println("Tick...");
      }
    catch (InterruptedException ex)
      {
            Logger.getLogger(DFunction.class.getName()).log(Level.SEVERE, null, ex);
      }
    return res;
  }
//
  public final AFunction plusParallel(final AFunction u)
  { final DFunction res=new DFunction(coef.length);
    final ExecutorService exec=Executors.newSingleThreadExecutor();
    for (int iThread=0;iThread<NTHREADS; iThread++)
    {
        final int left=slicesIndex[iThread].left;
        final int right=slicesIndex[iThread].right;
        final Runnable runnable = new Runnable()
        {
             public final void run()
             {
               for(int i=left;i<right;i++) res.coef[i]=coef[i]+u.coef[i];
             }
        };
        exec.execute(runnable);
    }
    exec.shutdown();
    try
      {
        exec.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        //System.err.println("Tick...");
      }
    catch (InterruptedException ex)
      {
            Logger.getLogger(DFunction.class.getName()).log(Level.SEVERE, null, ex);
      }
    return res;
  }

//
  public final AFunction times(double x)
  { DFunction res=new DFunction(coef.length);
    for(int i=0;i<coef.length;i++) res.coef[i]=x*coef[i];
    return res;
  }
  //
    //
  public final AFunction plus(AFunction u)
  { DFunction res=new DFunction(coef.length);
    for(int i=0;i<coef.length;i++) res.coef[i]=coef[i]+u.coef[i];
    return res;
  }
    public final double scalSerial(AFunction u)
  { double res=0;
    for(int i=coef.length-1; i>=0;i--) res+=coef[i]*u.coef[i];
    return res;
  }
  //
  public final void normalize()
  { double scal=1.0/java.lang.Math.sqrt(norm2());
    for(int i=0;i<coef.length;i++) coef[i]=coef[i]*scal;
  }
  //
  public final void setZero()
  { for(int i=0;i<coef.length;i++) coef[i]=0.0;
  }
  //
  public final double norm2()
  { double res=this.scal(this);
    return res;
  }
 

  //
  public final AFunction add(AFunction u)
  { for(int i=0;i<coef.length;i++) coef[i]=coef[i]+u.coef[i];
    return this;
  }
  //
  public final AFunction minus(AFunction u)
  { DFunction res=new DFunction(coef.length);
    for(int i=0;i<coef.length;i++) res.coef[i]=coef[i]-u.coef[i];
    return res;
  }
  //
    @Override
  public final int getArrayLength()
  {  return coef.length;
  }
/***********************/
    private class Range implements Serializable
    {   int left=0;
        int right = 0;
        Range(int l, int r)
        { left=l;
          right=r;
        }
    }
/***********************/

  //
  private static class NCPUCongfigChecker
    { int ncpu=1;

      public NCPUCongfigChecker()
      {
        try
        {
          InpReader iniFile=new InpReader("NCPU.conf");
          ncpu=Integer.parseInt(iniFile.readLine());
          iniFile.close();
        }
        catch (Exception e)
        {
          ncpu=Math.max(Runtime.getRuntime().availableProcessors()-1,1);
        }
        //ncpu=7;
        System.out.println("Configuring the scalar product execution for "+ncpu+ "CPUs.");
      }
    }
  public static void main(String[] args)
  {
    int parThreshold;
    long startTime;
    long endTime;
    System.out.println("Testing  Scalar  Product");
    System.out.println("Threshold  Serial  Parallel");
    int sampleLength=1000;
    double res;
    for (int ithreshold=10000;ithreshold<2000000; ithreshold+=40000)
    { DFunction tst=new DFunction(ithreshold);
      tst.setRandom();
      double resS=0.0;
      final long startTimeS=System.nanoTime();
      for (int i=0;i<sampleLength;i++) resS+=tst.scalSerial(tst);
      final long endTimeS=System.nanoTime();
      System.out.print(ithreshold+"  "+(endTimeS-startTimeS)+" ");
      double resP=0.0;
      final long startTimeP=System.nanoTime();
      for (int i=0;i<sampleLength;i++) resP+=tst.scal(tst);
      final long endTimeP=System.nanoTime();
      System.out.println("  "+(endTimeP-startTimeP)+" "+(resS-resP)/resS);
    }

  }
}
