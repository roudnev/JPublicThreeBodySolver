package threeBodyOperators;

import java.io.*;

import hermitSplines.SplineFunction1D;
import basicAbstractions.ALinearEquation;
import basicAbstractions.AFunction;
import auxiliary.*;
import configReader.ConfiguratorEssentials;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class MulticomponentThreeBodyEquation extends ALinearEquation
{
  public IterativeMulticomponentThreeBodyOperator theA;
  MulticomponentDiscretization3D md3d, md3dR;
  int main2BQuantumNumber=InitState.initialVibState;
  private double p;
  ConfiguratorEssentials config;
  InitState initState;
  public MulticomponentThreeBodyEquation(double erelau,ConfiguratorEssentials config) throws Exception
  {
    this.config=config;
    a=new IterativeMulticomponentThreeBodyOperator(erelau, config);
    theA=(IterativeMulticomponentThreeBodyOperator)a;
    // Hack to simplify initialization for the bound state calculations
    double erel=theA.erelArray[initState.initialChannel];
    if (erel<0)
      p=-Math.sqrt(-erel);
    else
      p=Math.sqrt(erel);
    md3dR=theA.h0.md3dR;
    md3d=theA.h0.md3d;
    rhsInit();
    //rhs.dump("RHS.DAT");
    //System.out.println("Checking operators implementation...");
    //checkOperators();
  }

  protected void rhsInit()
  {
    int dim=md3d.getDimension();
    rhs=new DFunction(dim);
    rhs.setZero();
    SplineFunction1D clusterWF=getClusterWF(InitState.initialAngState, InitState.initialVibState, InitState.initialChannel);
    SplineFunction1D angularWF=getClusterWFAngular(InitState.initialAngState, InitState.initialChannel );
    int k;
    double xi,yi,zi;
    //md3d.check();
    //for (int ic=0;ic<md3dR.getNumberOfComponents();ic++)
    int ic0,n0,l0;
    ic0=InitState.initialChannel;
    {
      int nx=md3d.d3d[ic0].getNX();
      int ny=md3d.d3d[ic0].getNY();
      int nz=md3d.d3d[ic0].getNZ();
      for (int ix=0;ix<nx;ix++)
      for (int iy=0;iy<ny;iy++)
      for (int iz=0;iz<nz;iz++)
      {
        k=md3d.index[ic0][ix][iy][iz];
        xi=md3d.d3d[ic0].getXCollocGrid().mesh[ix];
        yi=md3d.d3d[ic0].getYCollocGrid().mesh[iy];
        zi=md3d.d3d[ic0].getZCollocGrid().mesh[iz];
        rhs.coef[k]=-clusterWF.valueAt(xi)*sinpxDp(yi)*angularWF.valueAt(zi);
      }
    }
    //rhs.dump("rhsMC0.dat");
    //clusterWF.dump("cWF_MC.dump");
    //rhs=theA.h0.expand(rhs);
    //rhs.dump("rhs-1.dat");
    rhs=theA.h0.recover(rhs);
    //rhs.dump("rhs-2.dat");
    rhs=theA.v.times(rhs);
    //rhs.dump("rhsMC.dat");
    //theA.v.dump();
  }
  public SplineFunction1D getClusterWF()
  { int ic0,n0,l0;
    ic0=InitState.initialChannel;
    n0=InitState.initialVibState;
    l0=InitState.initialAngState;
    Discretization3D d3D=theA.h0.md3d.d3d[ic0];
    double[] tmp=new double[ d3D.getNX()];
    for(int ix=0;ix<d3D.getNX();ix++) tmp[ix]=theA.h0.clustHamilt[ic0].xOperator[n0].getEigenvectorsRightRe()[ix][main2BQuantumNumber];
    return new SplineFunction1D(d3D.getXSpline(), d3D.getNX(),new DFunction(tmp));
  }

  public SplineFunction1D getClusterWF(int lz, int n, int icomp)
  { // lz - orbital, n - main, icom - Faddeev component
    Discretization3D d3D=theA.h0.md3d.d3d[icomp];
    double[] tmp=new double[ d3D.getNX()];
    for(int ix=0;ix<d3D.getNX();ix++) tmp[ix]=theA.h0.clustHamilt[icomp].xOperator[lz].getEigenvectorsRightRe()[ix][n];
    return new SplineFunction1D(d3D.getXSpline(), d3D.getNX(),new DFunction(tmp));
  }

   public SplineFunction1D getClusterWFAngular(int lz, int icomp)
  { // lz - orbital, n - main, icom - Faddeev component
    Discretization3D d3D=theA.h0.md3d.d3d[icomp];
    double[] tmp=new double[ d3D.getNZ()];
    for(int iz=0;iz<d3D.getNZ();iz++) tmp[iz]=theA.h0.clustHamilt[icomp].zOperator.getEigenvectorsRightRe()[iz][lz];
    return new SplineFunction1D(d3D.getZSpline(), d3D.getNZ(),new DFunction(tmp));
  }

  public SplineFunction1D getClusterWFProjector(int lz, int n, int icomp)
  { // lz - orbital, n - main, icom - Faddeev component
    Discretization3D d3D=theA.h0.md3d.d3d[icomp];
    double[] tmp=new double[ d3D.getNX()];
    for(int ix=0;ix<d3D.getNX();ix++) tmp[ix]=theA.h0.clustHamilt[icomp].xOperator[lz].getEigenvectorsLeftRe()[ix][n];
    return new SplineFunction1D(d3D.getXSpline(), d3D.getNX(),new DFunction(tmp));
  }


  private double sinpxDp(double x)
  { double res=0.0;
    if (p>0.0)
        res=Math.sin(p*x)/p;
    else if (p==0)
        res=x;
    else
        res=Math.atan(x)*Math.exp(p*x); // For bound state initialization
    return res;
  }
  public void checkOperators()
  {
    AFunction tst=rhs.completeClone();
    tst.setZero();
    for (int k=0;k<md3dR.getNumberOfComponents();k++) tst.coef[md3dR.index[k][0][0][0]]=1.0;
    tst=theA.h0.expand(tst);
    Jama.Matrix x,xi,y,yi,z,zi;
    for (int k=0;k<md3dR.getNumberOfComponents();k++)
    {
      x=theA.h0.clustHamilt[k].idX;
      y=theA.h0.clustHamilt[k].idX;
      z=theA.h0.clustHamilt[k].idX;
      tst=theA.h0.tensorTimes(k,x,y,z,tst);
    }
    for (int k=0;k<md3dR.getNumberOfComponents();k++)
    {
      xi=theA.h0.clustHamilt[k].idXinv;
      yi=theA.h0.clustHamilt[k].idXinv;
      zi=theA.h0.clustHamilt[k].idXinv;
      tst=theA.h0.tensorTimes(k,xi,yi,zi,tst);
    }
    //tst.dump("tensorTimesCheck.dat");
    AFunction tst2=theA.h0.times(tst);
    tst2=theA.h0.solve(tst2);
    System.out.println("tst2.coef.length="+tst2.coef.length+"; tst.coef.length="+tst.coef.length);
    AFunction chk=tst2.minus(tst);
    //tst.dump("tst.dump");
    //chk.dump("chk.dump");
    //tst2.dump("tst2.dump");
    //for (int i=0;i<theA.vp.a.length; i++) out.println(i+" "+theA.vp.a[i]);
    System.out.println("H0 check: "+chk.norm2()+" "+tst2.norm2()+" "+tst.norm2());
  }
 
  /////////////////////////////////////////////////
  static java.io.PrintStream out;
  static
  {
    try{
      out=new java.io.PrintStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream("Mc3BEq.log"),131072));
    }
    catch(Exception e)
    { System.err.println(e.getMessage());
    }
  }
}