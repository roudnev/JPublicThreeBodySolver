package threeBodyOperators;
import basicAbstractions.AFunction;
import hermitSplines.SplineFunction1D;
import auxiliary.DFunction;
import hermitSplines.SummationLimits;
import hermitSplines.HSplineBC;
import hermitSplines.HSplineFree;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 0.5
 */

public class Function3D
{
// fields:
  Discretization3D d3D;
  AFunction f;
  private SummationLimits lx,ly,lz;
  private HSplineBC splx,sply;
  private HSplineFree splz;
// constructors:
  public Function3D()
  {
  }
  public Function3D(Discretization3D d3D, AFunction f)
  {
    lx =new SummationLimits(0,0);
    ly =new SummationLimits(0,0);
    lz =new SummationLimits(0,0);
    this.d3D=d3D;
    this.f=f;
    splx=d3D.getXSpline();
    sply=d3D.getYSpline();
    splz=d3D.getZSpline();
  }
// methods:
  public double valueAt(double x, double y, double z)
  {
    lx=splx.getLimits(x,lx);
    ly=sply.getLimits(y,ly);
    lz=splz.getLimits(z,lz);
    double res=0;
    for (int ix=lx.LeftLimit;ix<=lx.RightLimit;ix++)
    for (int iy=ly.LeftLimit;iy<=ly.RightLimit;iy++)
    for (int iz=lz.LeftLimit;iz<=lz.RightLimit;iz++)
    { res+=splx.fBSpline(x,ix)*sply.fBSpline(y,iy)*splz.bSplineFree(z,iz)
           *f.coef[d3D.getIndex(ix,iy,iz)];
    }
    return res;
  }
  //
  public void dump(String fname)
  {
  try
    {
      java.io.BufferedWriter
        dumpWriter=new java.io.BufferedWriter(
                        new java.io.FileWriter(fname)
                      );
      for (int i=0;i<f.coef.length;i++)
      { dumpWriter.write(i+" "+f.coef[i]);
        dumpWriter.newLine();
      }
      dumpWriter.close();
    }
    catch(Exception e)
    { System.err.println("Error initializing MultocomponentDiscretization3D:"+e.getMessage());
    }
  }
  //
  public double dvalueAt(double x, double y, double z)
  {
    lx=splx.getLimits(x,lx);
    ly=sply.getLimits(y,ly);
    lz=splz.getLimits(z,lz);
    System.err.println("x="+x+" :"+lx.LeftLimit+" "+lx.RightLimit);
    System.err.println("y="+y+" :"+ly.LeftLimit+" "+ly.RightLimit);
    System.err.println("y="+z+" :"+lz.LeftLimit+" "+lz.RightLimit);
    double res=0;
    for (int ix=lx.LeftLimit;ix<=lx.RightLimit;ix++)
    for (int iy=ly.LeftLimit;iy<=ly.RightLimit;iy++)
    for (int iz=lz.LeftLimit;iz<=lz.RightLimit;iz++)
    { res+=splx.fBSpline(x,ix)*sply.fBSpline(y,iy)*splz.bSplineFree(z,iz)
           *f.coef[d3D.getIndex(ix,iy,iz)];
    }
    return res;
  }
}