/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threeBodyOperators;
import basicAbstractions.APotential;
import basicAbstractions.AFunction;
import auxiliary.DFunction;
//import basicAbstractions.AnOperator;
import hermitSplines.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import auxiliary.InpReader;
/**
 *
 * @author roudnev
 */
public class ParallelMulticomponentRHSOperator extends MulticomponentRHSOperator
{
    final int NTHREADS;
    final Range[] slicesIndex;
    private int slicelength;
    private int nslices;
    private static int calls=0;
    AFunction res;
    boolean isMatrixCalculated;
    double[][] theMatrix;
    int[][] kIndex;
    int[] counterIndex;
    //final SlicedOperatorMultiplyer[] operatorSlices;
    //Thread[] threads;
    
    public ParallelMulticomponentRHSOperator(double[] mass, APotential[] vPot,
                                   MulticomponentDiscretization3D md3d,
                                   MulticomponentDiscretization3D md3dR)
    { super(mass,vPot,md3d,md3dR);
      NCPUCongfigChecker cpuconfig=new NCPUCongfigChecker();
      NTHREADS=cpuconfig.ncpu;
      nslices=NTHREADS;
      slicesIndex=new Range[nslices];
      initSlicing();
      isMatrixCalculated=false;
      int nNonzeroElementsEstimate=3*md3dR.d3d[0].getXSpline().getSplinesPerNode()
                                    *md3dR.d3d[0].getYSpline().getSplinesPerNode()
                                    *md3dR.d3d[0].getZSpline().getSplinesPerNode()*8;
      //System.err.println("nNonzeroElementsEstimate="+nNonzeroElementsEstimate);
      theMatrix=new double[rank][nNonzeroElementsEstimate];
      kIndex=new int[rank][nNonzeroElementsEstimate];
      counterIndex=new int[rank];
    }

    private final void initSlicing()
    { 
      slicelength=rank/(nslices);
      for (int i=0; i<nslices; i++)
      { 
          int left=i*slicelength;
          int right=Math.min((i+1)*slicelength, rank);
          slicesIndex[i]=new Range(left, right);
      }
      slicesIndex[nslices-1].right=rank;
//      for (int i=0; i<NTHREADS; i++)
//      {
//          System.err.println("Indexes #"+i+"   left="+slicesIndex[i].left+"   right="+slicesIndex[i].right);
//      }
    }

    @Override
  public AFunction times(AFunction u0)
  {
      calls++;
      final AFunction u=u0;
      final AFunction resf=u.imprint(rank);//new DFunction(rank);
      final AFunction resRef=u.imprint(rank);//new DFunction(rank);
      resf.setZero();
      resRef.setZero();
      
//      System.err.println("call="+calls+"  isMatrixCalculated="+isMatrixCalculated);
// reference scalar code, invoked only to precalculate the matrix/////////////////////////
      if (! isMatrixCalculated)
      {
          for (int iThread=0;iThread<nslices;iThread++)
          {
             final int left=slicesIndex[iThread].left;
             final int right=slicesIndex[iThread].right;
             final Runnable runnable = new Runnable() {
                 public final void run()
                 {
                //System.err.println("Running left="+left+"  right="+right);
                /*****************/
                for (int j=left;j<right;j++)
                {
                  final int ix=md3dR.indexX[j];
                  final int iy=md3dR.indexY[j];
                  final int iz=md3dR.indexZ[j];
                  final int ic=md3dR.indexComp[j];
                  //HSplineBC xspl,yspl;
                  //HSplineFree zspl;
                  if (md3dR.index[ic][ix][iy][iz]!=j)
                  { System.err.println("Error in row index calculation, exiting!!!!");
                    System.exit(-1);
                  }
                //System.out.println("1 ix, iy, iz: " + ix+" "+iy+" "+iz);
            //    HSplineBC xspl=d3D.getXSpline();
            //    HSplineBC yspl=d3D.getYSpline();
            //    HSplineFree zspl=d3D.getZSpline();
                  final double xit=md3dR.d3d[ic].getXCollocGrid().mesh[ix];
                  final double yit=md3dR.d3d[ic].getYCollocGrid().mesh[iy];
                  final double zit=md3dR.d3d[ic].getZCollocGrid().mesh[iz];
            //  potential from the left-hand side of the equation:
                  //final int length=md3d.getDimension();
                  final double potentialValue=vPot[ic].valueAt(xit)*xit*yit;
                  final SummationLimits limitsX0=new SummationLimits(0,0);
                  final SummationLimits limitsY0=new SummationLimits(0,0);
                  final SummationLimits limitsZ0=new SummationLimits(0,0);
                  int counter=0;
                  for (int jc0=0;jc0<3; jc0++)
                  {
                    final int jc=idTable[jc0];
                    if (jc0!=ic)
                    {
                      final double xp,yp,zp;
                      final coordSet xyzSet=rotcoord(xit,yit,zit,ic,jc0);
                      xp=xyzSet.x;  yp=xyzSet.y; zp=xyzSet.z;
                      //
                      final HSplineBC xspl=(HSplineBC)md3d.d3d[jc].getXSpline().clone();
                      final HSplineBC yspl=(HSplineBC)md3d.d3d[jc].getYSpline().clone();
                      final HSplineFree zspl=(HSplineFree)md3d.d3d[jc].getZSpline().clone();
//                      final double rho=xit*xit+yit*yit;
//                      final double rhop=xp*xp+yp*yp;
//                      if (Math.abs(rho-rhop)/rho>1.0e-15)
//                        System.err.println(rho+" "+rhop+" "+" ");
                      final double[] xsplValues=new double[xspl.getSplinesPerNode()*2];
                      final double[] ysplValues=new double[yspl.getSplinesPerNode()*2];
                      final double[] zsplValues=new double[zspl.getSplinesPerNode()*2];

                //    "right-hand side" of the equation, short-range part of the interaction:
                      final SummationLimits limitsX=xspl.getLimits(xp,limitsX0);
                      for (int i=limitsX.LeftLimit;i<=limitsX.RightLimit;i++)
                        xsplValues[i-limitsX.LeftLimit]=xspl.fBSpline(xp,i);
                      final SummationLimits limitsY=yspl.getLimits(yp,limitsY0);
                      for (int i=limitsY.LeftLimit;i<=limitsY.RightLimit;i++)
                        ysplValues[i-limitsY.LeftLimit]=yspl.fBSpline(yp,i);
                      final SummationLimits limitsZ=zspl.getLimits(zp*zTable[jc0],limitsZ0);
                      for (int i=limitsZ.LeftLimit;i<=limitsZ.RightLimit;i++)
                        zsplValues[i-limitsZ.LeftLimit]=zspl.bSplineFree(zp*zTable[jc0],i);
                      for (int jx=limitsX.LeftLimit;jx<=limitsX.RightLimit;jx++)
                      for (int jy=limitsY.LeftLimit;jy<=limitsY.RightLimit;jy++)
                      for (int jz=limitsZ.LeftLimit;jz<=limitsZ.RightLimit;jz++)
                      { final int k;
                        k=md3d.index[jc][jx][jy][jz];
                        resRef.coef[j]+=parityTable[jc0]*potentialValue*xsplValues[jx-limitsX.LeftLimit]
                                              *ysplValues[jy-limitsY.LeftLimit]
                                              *zsplValues[jz-limitsZ.LeftLimit]/(xp*yp)
                                              *
                                              u.coef[k];

                            theMatrix[j][counter]=parityTable[jc0]*potentialValue*xsplValues[jx-limitsX.LeftLimit]
                                              *ysplValues[jy-limitsY.LeftLimit]
                                              *zsplValues[jz-limitsZ.LeftLimit]/(xp*yp);
                            kIndex[j][counter]=k;
                            counter++;

                      }
                    }
                  }
                  counterIndex[j]=counter;
                }
                /*****************/
    //            System.err.println("Finished left="+left+"  right="+right);
               }
             };
             runnable.run();
          }
      }
      if (!isMatrixCalculated)
      {
          for (int j=0;j<rank;j++)
          {
              double[] tmp=new double[counterIndex[j]];
              for (int k=0;k<tmp.length;k++) tmp[k]=theMatrix[j][k];
              theMatrix[j]=tmp;
              //System.err.println(theMatrix[j].length);
          }
      }
      isMatrixCalculated=true;
/////////////////////////////////////////////
      final ExecutorService executor=Executors.newFixedThreadPool(NTHREADS);//Executors.newSingleThreadExecutor();//
      for (int iThread=0;iThread<nslices;iThread++)
      {
         final int left=slicesIndex[iThread].left;
         final int right=slicesIndex[iThread].right;
         final Runnable runnable = new Runnable() {
           public final void run()
           {
             for (int j=left;j<right;j++)
             {
              // System.err.println("Tick "+j);
               for (int kcount=0;kcount<theMatrix[j].length;kcount++)
                  { final int k=kIndex[j][kcount];
                    resf.coef[j]+=theMatrix[j][kcount]*u.coef[k];
                    //if (Math.abs(resf.coef[j])<1.0e-15 && calls >1) System.err.println(j+"  "+k+"  "+theMatrix[j][kcount]+"  "+calls);
                   // System.err.println(" "+k+" ("+theMatrix[j][kcount]*u.coef[k]+")  ");
                  }
               
             }
//            
            }
            /*****************/
//            System.err.println("Finished left="+left+"  right="+right);
         };
         executor.execute(runnable);
         //runnable.run();
      }
      executor.shutdown();
      try 
      {
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        //System.err.println("Tick...");
      } catch (InterruptedException ex)
      {
            Logger.getLogger(ParallelMulticomponentRHSOperator.class.getName()).log(Level.SEVERE, null, ex);
      }
      res=resf;
//      for (int i=0;i<rank;i++)
//      {
//          if (Math.abs(res.coef[i]-resRef.coef[i])/Math.abs(resRef.coef[i])>1.0e-14)
//          {
//              System.err.println(i+" comp. at "+calls+" call problem:"+res.coef[i]+" != "+resRef.coef[i]);
////              for (int k=0;k<theMatrix[i].length; k++)
////                System.err.print(theMatrix[i][k]+" ");
////              System.err.println(theMatrix[i].length);
//          }
//      }
//      System.err.println("======"+count+"=======");
//      out.println();
      //System.err.println("*****************");
  return res;
  }


/***********************/
    private class Range
    {   int left=0;
        int right = 0;
        Range(int l, int r)
        { left=l;
          right=r;
        }
    }
/***********************/
    private class NCPUCongfigChecker
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
        //ncpu=1;
        System.out.println("Configuring the coupling operator execution for "+ncpu+ "CPUs.");
      }
    }
}
