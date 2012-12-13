/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threeBodyOperators;
import basicAbstractions.AFunction;
import basicAbstractions.APotential;
import Jama.Matrix;
import decompositions.MyEigenvalueDecomposition;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import auxiliary.InpReader;
/**
 *
 * @author roudnev
 */
public class ParallelMulticomponentThreeBodyClusterHamiltonian 
       extends MulticomponentThreeBodyClusterHamiltonian
{
    final int NTHREADS;
    public ParallelMulticomponentThreeBodyClusterHamiltonian(APotential[] vPot, MulticomponentDiscretization3D md3d, MulticomponentDiscretization3D md3dR, double erel)
    { super(vPot, md3d, md3dR, erel);
      NCPUCongfigChecker cpuconfig=new NCPUCongfigChecker();
      NTHREADS=cpuconfig.ncpu;
    }

    @Override
    public final AFunction solve(AFunction rhs)
  {
    AFunction u0=expand(rhs);
    Matrix idXinv, idYinv, idZinv;
    //MyEigenvalueDecomposition[] xOperator, yOperator;
    //MyEigenvalueDecomposition zOperator;
    final AFunction resRe=u0.completeClone();
    final AFunction resIm=u0.completeClone();
    resRe.setZero();
    resIm.setZero();
    final AFunction tresRe=resRe.completeClone();
    final AFunction tresIm=resIm.completeClone();
    final AFunction u;
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      idXinv=clustHamilt[ic].idXinv;
      idYinv=clustHamilt[ic].idYinv;
      idZinv=clustHamilt[ic].idZinv;
      u0=tensorTimes(ic,idXinv, idYinv, idZinv, u0);
    }
    u=u0;
    for (int  ic0=0;ic0<md3d.getNumberOfComponents();ic0++)
    {
      final int ic=ic0;
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      

      ExecutorService execZ=Executors.newFixedThreadPool(NTHREADS);
      // turning to spectral representation for the operator
      for(int ix0=0;ix0<md3d.d3d[ic].getNX();ix0++)
      { final int ix=ix0;
        final Runnable runnableZ = new Runnable()
        {
          public final void run()
          {
            for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
            for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
            { final int i=md3d.index[ic][ix][iy][iz];
              resRe.coef[i]=0.0;
              resIm.coef[i]=0.0;
              for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
              {   final int  j=md3d.index[ic][ix][iy][jz];
                  resRe.coef[i]+=zOperator.getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
                  resIm.coef[i]+=zOperator.getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
              }
            }
          }
        };
        execZ.execute(runnableZ);
      }
      execZ.shutdown();
            try {
                execZ.awaitTermination(10, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    for (int ic0=0;ic0<md3d.getNumberOfComponents();ic0++)
    {
      final int ic=ic0;
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      ExecutorService execY=Executors.newFixedThreadPool(NTHREADS);
      for(int ix0=0;ix0<md3d.d3d[ic].getNX();ix0++)
      {   final int ix=ix0;
          final Runnable runnableY =new Runnable()
          {
            public void run()
            {
              for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
              for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
              { final int i=md3d.index[ic][ix][iy][iz];
                tresRe.coef[i]=0.0;
                tresIm.coef[i]=0.0;
                for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
                {
                  final int j=md3d.index[ic][ix][jy][iz];
                  tresRe.coef[i]+=
                    yOperator[iz].getEigenvectorsLeftRe()[iy][jy]*resRe.coef[j]
                      -
                    yOperator[iz].getEigenvectorsLeftIm()[iy][jy]*resIm.coef[j];
                  tresIm.coef[i]+=
                     yOperator[iz].getEigenvectorsLeftIm()[iy][jy]*resRe.coef[j]
                      +
                     yOperator[iz].getEigenvectorsLeftRe()[iy][jy]*resIm.coef[j];
                }
              }
            }
          };
          execY.execute(runnableY);
       }
       execY.shutdown();
            try {
                execY.awaitTermination(10, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    for (int ic0=0;ic0<md3d.getNumberOfComponents();ic0++)
    { final int ic=ic0;
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      ExecutorService execX=Executors.newFixedThreadPool(NTHREADS);
      for(int ix0=0;ix0<md3d.d3d[ic].getNX();ix0++)
      { final int ix=ix0;

        final Runnable runnableX =new Runnable()
          {
            public void run()
            {
              for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
              for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
              { final int i=md3d.index[ic][ix][iy][iz];
                resRe.coef[i]=0.0;
                resIm.coef[i]=0.0;
                for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
                {
                  final int j=md3d.index[ic][jx][iy][iz];
                  resRe.coef[i]+=
                    xOperator[iz].getEigenvectorsLeftRe()[ix][jx]*tresRe.coef[j]
                      -
                    xOperator[iz].getEigenvectorsLeftIm()[ix][jx]*tresIm.coef[j];
                  resIm.coef[i]+=
                    xOperator[iz].getEigenvectorsLeftIm()[ix][jx]*tresRe.coef[j]
                      +
                    xOperator[iz].getEigenvectorsLeftRe()[ix][jx]*tresIm.coef[j];
                }
              }
            }
          };
          execX.execute(runnableX);
      }
      execX.shutdown();
            try {
                execX.awaitTermination(2, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

      //  operator multiplication in spectral representation

    double evRe,evReInv,evIm,evImInv,r;
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      {
        i=md3d.index[ic][ix][iy][iz];
        evRe=xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]-z;
        evIm=xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy];
        r=Math.pow(evRe,2.0)+Math.pow(evIm,2.0);
        evReInv=evRe/r;  // evReInv=1.0;
        evImInv=-evIm/r; // evImInv=0.0;
        tresRe.coef[i]=
            (evReInv)*resRe.coef[i]
              -
            (evImInv)*resIm.coef[i];
        tresIm.coef[i]=
            (evReInv)*resIm.coef[i]
              +
            (evImInv)*resRe.coef[i];
      }
    }

      // recovering space representation

    for (int ic0=0;ic0<md3d.getNumberOfComponents();ic0++)
    { final int ic=ic0;
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      ExecutorService execX=Executors.newFixedThreadPool(NTHREADS);
      for(int ix0=0;ix0<md3d.d3d[ic].getNX();ix0++)
      { final int ix=ix0;
        final Runnable runnableX =new Runnable()
          {
            public void run()
            {
              for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
              for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
              { final int i=md3d.index[ic][ix][iy][iz];
                resRe.coef[i]=0.0;
                resIm.coef[i]=0.0;
                for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
                {
                  final int j=md3d.index[ic][jx][iy][iz];
                  resRe.coef[i]+=
                    xOperator[iz].getEigenvectorsRightRe()[ix][jx]*tresRe.coef[j]
                      -
                    xOperator[iz].getEigenvectorsRightIm()[ix][jx]*tresIm.coef[j];
                  resIm.coef[i]+=
                    xOperator[iz].getEigenvectorsRightIm()[ix][jx]*tresRe.coef[j]
                      +
                    xOperator[iz].getEigenvectorsRightRe()[ix][jx]*tresIm.coef[j];
                }
              }
            }
        };
        execX.execute(runnableX);
      }
      execX.shutdown();
            try {
                execX.awaitTermination(2, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    for (int ic0=0;ic0<md3d.getNumberOfComponents();ic0++)
    { final int ic=ic0;
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      ExecutorService execY=Executors.newFixedThreadPool(NTHREADS);
      for(int ix0=0;ix0<md3d.d3d[ic].getNX();ix0++)
      { final int ix=ix0;
        final Runnable runnableY =new Runnable()
          {
            public void run()
            {
              for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
              for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
              { final int i=md3d.index[ic][ix][iy][iz];
                tresRe.coef[i]=0.0;
                tresIm.coef[i]=0.0;
                for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
                {
                  final int j=md3d.index[ic][ix][jy][iz];
                  tresRe.coef[i]+=
                    yOperator[iz].getEigenvectorsRightRe()[iy][jy]*resRe.coef[j]
                      -
                    yOperator[iz].getEigenvectorsRightIm()[iy][jy]*resIm.coef[j];
                  tresIm.coef[i]+=
                    yOperator[iz].getEigenvectorsRightIm()[iy][jy]*resRe.coef[j]
                      +
                    yOperator[iz].getEigenvectorsRightRe()[iy][jy]*resIm.coef[j];
                }
              }
            }
         };
         execY.execute(runnableY);
      }
      execY.shutdown();
            try {
                execY.awaitTermination(2, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    for (int ic0=0;ic0<md3d.getNumberOfComponents();ic0++)
    { final int ic=ic0;
      final MyEigenvalueDecomposition[] xOperator=clustHamilt[ic].xOperator;
      final MyEigenvalueDecomposition[] yOperator=clustHamilt[ic].yOperator;
      final MyEigenvalueDecomposition zOperator=clustHamilt[ic].getzOperator();
      ExecutorService execZ=Executors.newFixedThreadPool(NTHREADS);
      for(int ix0=0;ix0<md3d.d3d[ic].getNX();ix0++)
      { final int ix=ix0;
        final Runnable runnableZ =new Runnable()
          {
            public void run()
            {
              for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
              for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
               { final int i=md3d.index[ic][ix][iy][iz];
                resRe.coef[i]=0.0;
                resIm.coef[i]=0.0;
                for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
                {
                  final int j=md3d.index[ic][ix][iy][jz];
                  resRe.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresRe.coef[j]
                                      -
                                 zOperator.getEigenvectorsRightIm()[iz][jz]*tresIm.coef[j];
                  resIm.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresIm.coef[j]
                                      +
                                 zOperator.getEigenvectorsRightIm()[iz][jz]*tresRe.coef[j];
                }
                if (Math.abs(resIm.coef[i])>5.0e-5) System.err.println("method solve() incorrect: "+resIm.coef[i]);
              }
            }
          };
          execZ.execute(runnableZ);
      }
      execZ.shutdown();
            try {
                execZ.awaitTermination(2, TimeUnit.DAYS);
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    return resRe;
  }


    @Override
  public final AFunction tensorTimes(int ic,final Matrix mx, final Matrix my, final Matrix mz, final AFunction u)
  {
    Discretization3D t3D0;
    MulticomponentDiscretization3D mt3D0;
    final Discretization3D t3D;
    final MulticomponentDiscretization3D mt3D;
    int nx=mx.getRowDimension();
    int ny=my.getRowDimension();
    int nz=mz.getRowDimension();
    t3D0=md3d.d3d[ic];
    mt3D0=md3d;
    final AFunction res=u.imprint(u.getArrayLength());//u.completeClone();
    res.setZero();
    final AFunction tres=u.imprint(u.getArrayLength());//u.completeClone();
    tres.setZero();

    if (u.getArrayLength()==md3d.getDimension())
    {
      t3D0=md3d.d3d[ic];
      mt3D0=md3d;
    }
    else if (u.getArrayLength()==md3dR.getDimension())
    {
      t3D0=md3dR.d3d[ic];
      mt3D0=md3dR;
    }
    else
      { System.err.println("Error calling tensorTimes: incorrect dimensions");
        System.err.println("nx:" + nx + " NX:" + md3d.d3d[ic].getNX()+ " rNX:" + md3dR.d3d[ic].getNX());
        System.err.println("ny:" + ny + " NY:" + md3d.d3d[ic].getNY()+ " rNY:" + md3dR.d3d[ic].getNY());
        System.err.println("nz:" + nz + " NZ:" + md3d.d3d[ic].getNZ()+ " rNZ:" + md3dR.d3d[ic].getNZ());
        System.exit(1);
      }
    mt3D=mt3D0;
    t3D=t3D0;
    for (int jc=0;jc<mt3D.getNumberOfComponents();jc++)
    if (jc==ic)
    {
      final int nX=t3D.getNX();
      final int nY=t3D.getNY();
      final int nZ=t3D.getNZ();
      ExecutorService execX=Executors.newFixedThreadPool(NTHREADS);
      for(int ix=0;ix<t3D.getNX();ix++)
      {
          
          final int ixf=ix;
          final int icf=ic;
          final Runnable runnableX =new Runnable()
          {
              public void run()
              {
                  for(int iy=0;iy<nY;iy++)
                  for(int iz=0;iz<nZ;iz++)
                  {
                    final int i=mt3D.index[icf][ixf][iy][iz];
                    res.coef[i]=0.0;
                    for(int jx=0;jx<t3D.getNX();jx++)
                    {
                      final int j=mt3D.index[icf][jx][iy][iz];
                      res.coef[i]+= mx.getArray()[ixf][jx]*u.coef[j];
                    }
                  }
              }
          };
          //runnableX.run();
          execX.execute(runnableX);
      }
      execX.shutdown();
      try {
          execX.awaitTermination(2, TimeUnit.DAYS);
      } catch (InterruptedException ex) {
          Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
      }


      ExecutorService execY=Executors.newFixedThreadPool(NTHREADS);
      for(int ix=0;ix<t3D.getNX();ix++)
      {
          final int ixf=ix;
          final int icf=ic;
          final Runnable runnableY =new Runnable()
          {
              public void run()
              {
                  for(int iy=0;iy<t3D.getNY();iy++)
                  for(int iz=0;iz<t3D.getNZ();iz++)
                  {
                    int i=mt3D.index[icf][ixf][iy][iz];
                    tres.coef[i]=0.0;
                    for(int jy=0;jy<t3D.getNY();jy++)
                    {
                      int j=mt3D.index[icf][ixf][jy][iz];
                      tres.coef[i]+= my.getArray()[iy][jy]*res.coef[j];
                    }
                  }
              }
          };
          //runnableY.run();
          execY.execute(runnableY);
      }
      execY.shutdown();
      try {
          execY.awaitTermination(2, TimeUnit.DAYS);
      } catch (InterruptedException ex) {
          Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
      }


      ExecutorService execZ=Executors.newFixedThreadPool(NTHREADS);
      for(int ix=0;ix<t3D.getNX();ix++)
      {
          final int ixf=ix;
          final int icf=ic;
          final Runnable runnableZ =new Runnable()
          {
              public void run()
              {
                  for(int iy=0;iy<t3D.getNY();iy++)
                  for(int iz=0;iz<t3D.getNZ();iz++)
                  {
                    int i=mt3D.index[icf][ixf][iy][iz];
                    res.coef[i]=0.0;
                    for(int jz=0;jz<t3D.getNZ();jz++)
                    {
                      int j=mt3D.index[icf][ixf][iy][jz];
                      res.coef[i]+= mz.getArray()[iz][jz]*tres.coef[j];
                    }
                  }
              }
          };
          //runnableZ.run();
          execZ.execute(runnableZ);
      }
      execZ.shutdown();
      try {
          execZ.awaitTermination(2, TimeUnit.DAYS);
      } catch (InterruptedException ex) {
          Logger.getLogger(ParallelMulticomponentThreeBodyClusterHamiltonian.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    else
    {
      for(int ix=0;ix<mt3D.d3d[jc].getNX();ix++)
      for(int iy=0;iy<mt3D.d3d[jc].getNY();iy++)
      for(int iz=0;iz<mt3D.d3d[jc].getNZ();iz++)
      {
        int i=mt3D.index[jc][ix][iy][iz];
        res.coef[i]=u.coef[i];
      }
    }
    return res;
  }


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
        System.out.println("Configuring the cluster Hamiltonian operator execution for "+ncpu+ "CPUs.");
      }
    }

}
