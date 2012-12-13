package threeBodyOperators;

import basicAbstractions.AnOperator;
import basicAbstractions.AFunction;
import basicAbstractions.Simple;
import basicAbstractions.APotential;
import Jama.Matrix;
import decompositions.MyEigenvalueDecomposition;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class MulticomponentThreeBodyClusterHamiltonian implements AnOperator, Simple
{
  public APotential[] vPot;
  public MulticomponentDiscretization3D md3d;
  public MulticomponentDiscretization3D md3dR;
  ClusterThreeBodyHamiltonian[] clustHamilt;
  int noc;
  double[] erelArray;

  public double z;

  public MulticomponentThreeBodyClusterHamiltonian(APotential[] vPot, MulticomponentDiscretization3D md3d, MulticomponentDiscretization3D md3dR, double erel)
  { this.md3d=md3d;
    this.md3dR=md3dR;
    this.vPot=vPot;

    noc=md3d.getNumberOfComponents();
    clustHamilt=new ClusterThreeBodyHamiltonian[noc];
    for (int i=0;i<noc;i++)
      clustHamilt[i]=new ClusterThreeBodyHamiltonian(vPot[i],md3d.d3d[i],md3dR.d3d[i]);
    erelArray=new double[noc];
    int ic0,n0,l0;
    ic0=InitState.initialChannel;
    n0=InitState.initialVibState;
    l0=InitState.initialAngState;
    System.out.println("Initial channel:"+"  "+ic0+"  "+n0+"  "+l0);
    this.z=erel+clustHamilt[ic0].xOperator[n0].getEigenvaluesRe()[l0];
    //System.out.println("Total energy (internal units) z="+z);
    //System.out.println("Thresholds in different channels (internal units)");
    System.out.println();
    System.out.println("Energies and lengths are in atomic units, the Shape Factor is dimensionless");
    for (int i=0;i<noc;i++)
    {
      erelArray[i]=z-clustHamilt[i].xOperator[0].getEigenvaluesRe()[0];
//      System.out.println("Erel["+i+"]="+erelArray[i]+"; "+"e2["+i+"]="+clustHamilt[i].xOperator[0].getEigenvaluesRe()[0]);
      System.out.println("Two-body subsystem interaction parameters in channel "+i);
      clustHamilt[i].printLowEnergyParameters(i);
    }
    md3d.resetYSplines(erelArray);
    // Renew clustHamilt for new boundary conditions
    for (int i=0;i<noc;i++)
      clustHamilt[i]=new ClusterThreeBodyHamiltonian(vPot[i],md3d.d3d[i],md3dR.d3d[i]);
  }
  public final AFunction times(AFunction u)
  { return this.times(u, this.z);
  }
  public final AFunction times(AFunction u, double z)
  {
    Matrix idXinv, idYinv, idZinv;
    MyEigenvalueDecomposition[] xOperator, yOperator;
    MyEigenvalueDecomposition zOperator;
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;

      // turning to spectral representation for the operator
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {   j=md3d.index[ic][ix][iy][jz];
            resRe.coef[i]+=zOperator.getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
            resIm.coef[i]+=zOperator.getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
        }
      }
    }
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
      //  operator multiplication in spectral representation

    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      {
        i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=
            (xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]-z)*resRe.coef[i]
              -
            (xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy])*resIm.coef[i];
        tresIm.coef[i]=
            (xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]-z)*resIm.coef[i]
              +
            (xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy])*resRe.coef[i];
      }
    }

    // recovering space representation
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
       { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {
          j=md3d.index[ic][ix][iy][jz];
          resRe.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresRe.coef[j]
                              -
                         zOperator.getEigenvectorsRightIm()[iz][jz]*tresIm.coef[j];
          resIm.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresIm.coef[j]
                              +
                         zOperator.getEigenvectorsRightIm()[iz][jz]*tresRe.coef[j];
        }
        if (Math.abs(resIm.coef[i])>5.0e-9) System.err.println("method times() incorrect: imCoef["+i+"]="+resIm.coef[i]);
      }
    }

//    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
//    {
//      resRe=tensorTimes(ic, clustHamilt[ic].idX, clustHamilt[ic].idY, clustHamilt[ic].idZ, resRe);
//    }

    return resRe;
  }
  //
  public final int getRank()
  {
    return md3d.getDimension();
  }
  //
  public AFunction solve(AFunction rhs)
  {

    AFunction u=expand(rhs);
    Matrix idXinv, idYinv, idZinv;
    MyEigenvalueDecomposition[] xOperator, yOperator;
    MyEigenvalueDecomposition zOperator;
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      idXinv=clustHamilt[ic].idXinv;
      idYinv=clustHamilt[ic].idYinv;
      idZinv=clustHamilt[ic].idZinv;
      u=tensorTimes(ic,idXinv, idYinv, idZinv, u);
    }
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;

      // turning to spectral representation for the operator
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {   j=md3d.index[ic][ix][iy][jz];
            resRe.coef[i]+=zOperator.getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
            resIm.coef[i]+=zOperator.getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
        }
      }
    }
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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

      //  operator multiplication in spectral representation

    double evRe,evReInv,evIm,evImInv,r;
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
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

    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
       { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {
          j=md3d.index[ic][ix][iy][jz];
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
    return resRe;
  }
  public final AFunction expand(AFunction f)
  { AFunction res;
    if (f.coef.length==md3d.getDimension())
       res=f.completeClone();
    else
    {
      res=f.imprint(md3d.getDimension());
      res.setZero();
      int nx,ny,nz;
      int nc=md3dR.getNumberOfComponents();
      for (int ic=0;ic<nc;ic++)
      {
        nx=md3dR.d3d[ic].getNX();
        ny=md3dR.d3d[ic].getNY();
        nz=md3dR.d3d[ic].getNZ();
        for(int ix=0;ix<nx;ix++)
        for(int iy=0;iy<ny;iy++)
        for(int iz=0;iz<nz;iz++)
        {
          res.coef[md3d.index[ic][ix][iy][iz]]=f.coef[md3dR.index[ic][ix][iy][iz]];
        }
      }
    }
    return res;
  }
  static java.io.PrintStream out;
  static
  {
    try{
      out=new java.io.PrintStream(new java.io.FileOutputStream("MTBCH.log"));
    }
    catch(Exception e)
    { System.err.println(e.getMessage());
    }
  }
  static int wcounter=0;
  public AFunction tensorTimes(int ic,Matrix mx, Matrix my, Matrix mz, AFunction u)
  {
    Discretization3D t3D;
    MulticomponentDiscretization3D mt3D;
    int i,j;
    int nx=mx.getRowDimension();
    int ny=my.getRowDimension();
    int nz=mz.getRowDimension();
    t3D=md3d.d3d[ic];
    mt3D=md3d;
    AFunction res=u.imprint(u.getArrayLength());//u.completeClone();
    res.setZero();
    AFunction tres=u.imprint(u.getArrayLength());//u.completeClone();
    tres.setZero();

    if (u.getArrayLength()==md3d.getDimension())
    {
      t3D=md3d.d3d[ic];
      mt3D=md3d;
    }
    else if (u.getArrayLength()==md3dR.getDimension())
    {
      t3D=md3dR.d3d[ic];
      mt3D=md3dR;
    }
    else
      { System.err.println("Error calling tensorTimes: incorrect dimensions");
        System.err.println("nx:" + nx + " NX:" + md3d.d3d[ic].getNX()+ " rNX:" + md3dR.d3d[ic].getNX());
        System.err.println("ny:" + ny + " NY:" + md3d.d3d[ic].getNY()+ " rNY:" + md3dR.d3d[ic].getNY());
        System.err.println("nz:" + nz + " NZ:" + md3d.d3d[ic].getNZ()+ " rNZ:" + md3dR.d3d[ic].getNZ());
        System.exit(1);
      }
    for (int jc=0;jc<mt3D.getNumberOfComponents();jc++)
    if (jc==ic)
    {
      for(int ix=0;ix<t3D.getNX();ix++)
      for(int iy=0;iy<t3D.getNY();iy++)
      for(int iz=0;iz<t3D.getNZ();iz++)
      {
        i=mt3D.index[ic][ix][iy][iz];
        res.coef[i]=0.0;
        for(int jx=0;jx<t3D.getNX();jx++)
        {
          j=mt3D.index[ic][jx][iy][iz];
          res.coef[i]+= mx.getArray()[ix][jx]*u.coef[j];
        }
      }
      for(int ix=0;ix<t3D.getNX();ix++)
      for(int iy=0;iy<t3D.getNY();iy++)
      for(int iz=0;iz<t3D.getNZ();iz++)
      {
        i=mt3D.index[ic][ix][iy][iz];
        tres.coef[i]=0.0;
        for(int jy=0;jy<t3D.getNY();jy++)
        {
          j=mt3D.index[ic][ix][jy][iz];
          tres.coef[i]+= my.getArray()[iy][jy]*res.coef[j];
        }
      }
      for(int ix=0;ix<t3D.getNX();ix++)
      for(int iy=0;iy<t3D.getNY();iy++)
      for(int iz=0;iz<t3D.getNZ();iz++)
      {
        i=mt3D.index[ic][ix][iy][iz];
        res.coef[i]=0.0;
        for(int jz=0;jz<t3D.getNZ();jz++)
        {
          j=mt3D.index[ic][ix][iy][jz];
          res.coef[i]+= mz.getArray()[iz][jz]*tres.coef[j];
        }
      }
    }
    else
    {
      for(int ix=0;ix<mt3D.d3d[jc].getNX();ix++)
      for(int iy=0;iy<mt3D.d3d[jc].getNY();iy++)
      for(int iz=0;iz<mt3D.d3d[jc].getNZ();iz++)
      {
        i=mt3D.index[jc][ix][iy][iz];
        res.coef[i]=u.coef[i];
      }
    }
    return res;
  }
  //
  public AFunction expPadeTimes(AFunction u,double t)
  {
    double lr,li,vr,vi;
    Matrix idXinv, idYinv, idZinv;
    MyEigenvalueDecomposition[] xOperator, yOperator;
    MyEigenvalueDecomposition zOperator;
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;

      // turning to spectral representation for the operator
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {   j=md3d.index[ic][ix][iy][jz];
            resRe.coef[i]+=zOperator.getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
            resIm.coef[i]+=zOperator.getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
        }
      }
    }
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
      //  operator multiplication in spectral representation

    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      {
        lr=(xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy])*t;
        li=(xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy])*t;
//        vr = -(-4.0 + Math.pow(t, 2) * Math.pow(lr, 2) + Math.pow(t, 2) * Math.pow(li, 2))
//                /
//              (4.0 + 4.0 * t * lr + Math.pow(t, 2) * Math.pow(lr, 2) + Math.pow(t, 2) * Math.pow(li, 2));
//        vi = -4 * t * li
//                /
//                (4.0 + (4 * t * lr) + (Math.pow(t,2) * Math.pow(lr, 2)) + Math.pow(t, 2)*Math.pow(li, 2));
////
//        vr = -(-4.0 + Math.pow(lr, 2) + Math.pow(li, 2))/(4.0 + 4.0*lr + Math.pow(lr, 2) + Math.pow(li, 2));
//        vi = -4*li/(4.0 + 4*lr + Math.pow(lr, 2) + Math.pow(li, 2));
////
        vr=Math.exp(-lr)*Math.cos(li);
        vi=-Math.exp(-lr)*Math.sin(li);
        i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=
            (vr)*resRe.coef[i]
              -
            (vi)*resIm.coef[i];
        tresIm.coef[i]=
            (vr)*resIm.coef[i]
              +
            (vi)*resRe.coef[i];
      }
    }

    // recovering space representation
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
       { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {
          j=md3d.index[ic][ix][iy][jz];
          resRe.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresRe.coef[j]
                              -
                         zOperator.getEigenvectorsRightIm()[iz][jz]*tresIm.coef[j];
          resIm.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresIm.coef[j]
                              +
                         zOperator.getEigenvectorsRightIm()[iz][jz]*tresRe.coef[j];
        }
        if (Math.abs(resIm.coef[i])>5.0e-9) System.err.println("method times() incorrect: imCoef["+i+"]="+resIm.coef[i]);
      }
    }
//
//    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
//    {
//      resRe=tensorTimes(ic, clustHamilt[ic].idX, clustHamilt[ic].idY, clustHamilt[ic].idZ, resRe);
//    }

    return resRe;
  }
  //
   //
  public AFunction resolvent(AFunction u,double z)
  {
    double lr,li,vr,vi;
    Matrix idXinv, idYinv, idZinv;
    MyEigenvalueDecomposition[] xOperator, yOperator;
    MyEigenvalueDecomposition zOperator;
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;

      // turning to spectral representation for the operator
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {   j=md3d.index[ic][ix][iy][jz];
            resRe.coef[i]+=zOperator.getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
            resIm.coef[i]+=zOperator.getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
        }
      }
    }
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
      //  operator multiplication in spectral representation

    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      {
        lr=(xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]);
        li=(xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy]);
//        vr = -(-4.0 + Math.pow(t, 2) * Math.pow(lr, 2) + Math.pow(t, 2) * Math.pow(li, 2))
//                /
//              (4.0 + 4.0 * t * lr + Math.pow(t, 2) * Math.pow(lr, 2) + Math.pow(t, 2) * Math.pow(li, 2));
//        vi = -4 * t * li
//                /
//                (4.0 + (4 * t * lr) + (Math.pow(t,2) * Math.pow(lr, 2)) + Math.pow(t, 2)*Math.pow(li, 2));
////
//        vr = -(-4.0 + Math.pow(lr, 2) + Math.pow(li, 2))/(4.0 + 4.0*lr + Math.pow(lr, 2) + Math.pow(li, 2));
//        vi = -4*li/(4.0 + 4*lr + Math.pow(lr, 2) + Math.pow(li, 2));
////
        vr=(lr-z)/((lr-z)*(lr-z)+li*li);
        vi=-li/((lr-z)*(lr-z)+li*li);
        i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=
            (vr)*resRe.coef[i]
              -
            (vi)*resIm.coef[i];
        tresIm.coef[i]=
            (vr)*resIm.coef[i]
              +
            (vi)*resRe.coef[i];
      }
    }

    // recovering space representation
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jy=0;jy<md3d.d3d[ic].getNY();jy++)
        {
          j=md3d.index[ic][ix][jy][iz];
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
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].getzOperator();
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
       { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {
          j=md3d.index[ic][ix][iy][jz];
          resRe.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresRe.coef[j]
                              -
                         zOperator.getEigenvectorsRightIm()[iz][jz]*tresIm.coef[j];
          resIm.coef[i]+=zOperator.getEigenvectorsRightRe()[iz][jz]*tresIm.coef[j]
                              +
                         zOperator.getEigenvectorsRightIm()[iz][jz]*tresRe.coef[j];
        }
        if (Math.abs(resIm.coef[i])>5.0e-9) System.err.println("method times() incorrect: imCoef["+i+"]="+resIm.coef[i]);
      }
    }
//
//    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
//    {
//      resRe=tensorTimes(ic, clustHamilt[ic].idX, clustHamilt[ic].idY, clustHamilt[ic].idZ, resRe);
//    }

    return resRe;
  }

  //
  public AFunction recover(AFunction u)
  {
    Matrix idXinv,idYinv,idZinv;
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      idXinv=clustHamilt[ic].idXinv;
      idYinv=clustHamilt[ic].idYinv;
      idZinv=clustHamilt[ic].idZinv;
      u=tensorTimes(ic,idXinv, idYinv, idZinv, u);
    }
    return u;
  }
  //
  public final AFunction cover(AFunction u)
  {
    Matrix idXinv,idYinv,idZinv;
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      idXinv=clustHamilt[ic].idX;
      idYinv=clustHamilt[ic].idY;
      idZinv=clustHamilt[ic].idZ;
      u=tensorTimes(ic,idXinv, idYinv, idZinv, u);
    }
    return u;
  }

  public final AFunction toSpectralXZ(AFunction u)
  {
    Matrix idXinv, idYinv, idZinv;
    MyEigenvalueDecomposition[] xOperator, yOperator;
    MyEigenvalueDecomposition zOperator;
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].zOperator;
      int i,j;

      // turning to spectral representation for the operator
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        resRe.coef[i]=0.0;
        resIm.coef[i]=0.0;
        for(int jz=0;jz<md3d.d3d[ic].getNZ();jz++)
        {   j=md3d.index[ic][ix][iy][jz];
            resRe.coef[i]+=zOperator.getEigenvectorsLeftRe()[iz][jz]*u.coef[j];//*clustHamilt[ic].zNorm[iz];
            resIm.coef[i]+=zOperator.getEigenvectorsLeftIm()[iz][jz]*u.coef[j];//*clustHamilt[ic].zNorm[iz];
        }
      }
    }
    for (int ic=0;ic<md3d.getNumberOfComponents();ic++)
    {
      xOperator=clustHamilt[ic].xOperator;
      yOperator=clustHamilt[ic].yOperator;
      zOperator=clustHamilt[ic].zOperator;
      int i,j;
      for(int ix=0;ix<md3d.d3d[ic].getNX();ix++)
      for(int iy=0;iy<md3d.d3d[ic].getNY();iy++)
      for(int iz=0;iz<md3d.d3d[ic].getNZ();iz++)
      { i=md3d.index[ic][ix][iy][iz];
        tresRe.coef[i]=0.0;
        tresIm.coef[i]=0.0;
        for(int jx=0;jx<md3d.d3d[ic].getNX();jx++)
        {
          j=md3d.index[ic][jx][iy][iz];
          tresRe.coef[i]+=
            xOperator[iz].getEigenvectorsLeftRe()[ix][jx]*resRe.coef[j]
              -
            xOperator[iz].getEigenvectorsLeftIm()[ix][jx]*resIm.coef[j];
          tresIm.coef[i]+=
            xOperator[iz].getEigenvectorsLeftIm()[ix][jx]*resRe.coef[j]
              +
            xOperator[iz].getEigenvectorsLeftRe()[ix][jx]*resIm.coef[j];
        }
      }
    }
    System.out.println(resIm.norm2());
    return tresRe;
  }
}