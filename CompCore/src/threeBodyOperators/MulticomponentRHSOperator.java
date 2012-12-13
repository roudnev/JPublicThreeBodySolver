package threeBodyOperators;

import auxiliary.*;
import hermitSplines.*;
import basicAbstractions.APotential;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import java.io.*;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class MulticomponentRHSOperator implements AnOperator
{
  //
  int rank=0;
  //
  //int nz=0;
  MulticomponentDiscretization3D md3d;
  MulticomponentDiscretization3D md3dR;
  APotential[] vPot;
  double[] mass;
  double[][][][] cpm;
  //
  int[] idTable={0,1,2};
  double[] parityTable={1.0,1.0,1.0};
  double[] zTable={1.0,1.0,-1.0};
  //
  public MulticomponentRHSOperator(double[] mass, APotential[] vPot,
                                   MulticomponentDiscretization3D md3d,
                                   MulticomponentDiscretization3D md3dR)
  {
    this.mass=mass;
    this.vPot=vPot;
    this.md3d=md3d;
    this.md3dR=md3dR;

    // System.out.println("md3dR.getDimension()"+"="+md3dR.getDimension());
    System.out.print("Initializing ID tables...");
    initId();
    System.out.println("done.");
    //System.err.println("Cutoff OK");
    // System.out.println("md3dR.getDimension()"+"="+md3dR.getDimension());

    cpm=new double[3][3][2][2];
    System.out.print("Initializing Jacobi coordinate transformations...");
    cpmFill();
    System.out.println("done.");
    rank=md3dR.getDimension();
//    System.out.print("Initializing the coupling matrix...");
//    System.out.println("done.");
    //this.dump();
  }
  //
  protected void cpmFill()
  {
    double cij=0.0,
           sij=0.0;
    double totalMass=0.0;
    double pwr=1.0;
    for (int i=0; i<mass.length;i++) totalMass+=mass[i];
    for (int ic=0;ic<3;ic++)
    for (int jc=0;jc<3;jc++)
    {
      pwr=Math.pow(-1, ic-jc);
      if(ic!=jc)
      {
        cij=-Math.sqrt(mass[ic]*mass[jc]/((totalMass-mass[ic])*(totalMass-mass[jc])));
        sij=pwr*(ic-jc)*Math.sqrt(1.0-cij*cij)/Math.abs(ic-jc);
      }
      else
      { cij=1.0;
        sij=0.0;
      }
      cpm[ic][jc][0][0]=cij;
      cpm[ic][jc][0][1]=sij;
      cpm[ic][jc][1][0]=-sij;
      cpm[ic][jc][1][1]=cij;
      out.println();
      out.println(ic+" "+jc+" "+cpm[ic][jc][0][0]+" "+cpm[ic][jc][0][1]);
      out.println(ic+" "+jc+" "+cpm[ic][jc][1][0]+" "+cpm[ic][jc][1][1]);
      out.println();
    }
    out.flush();
  }
  //
  public int getRank()
  { return md3dR.getDimension();
  }
  //
  public SparceRow getSparceRow(int j)
  { SparceRow res=new SparceRow();
    res.nz=0;
    double xp,yp,zp;
    coordSet xyzSet;
    int ix=md3dR.indexX[j];
    int iy=md3dR.indexY[j];
    int iz=md3dR.indexZ[j];
    int ic=md3dR.indexComp[j];
    HSplineBC xspl,yspl;
    HSplineFree zspl;
    if (md3dR.index[ic][ix][iy][iz]!=j)
    { System.err.println("Error when calculation row index, exiting!!!!");
      System.exit(-1);
    }
        //System.out.println("1 ix, iy, iz: " + ix+" "+iy+" "+iz);
//    HSplineBC xspl=d3D.getXSpline();
//    HSplineBC yspl=d3D.getYSpline();
//    HSplineFree zspl=d3D.getZSpline();
    double xit=md3dR.d3d[ic].getXCollocGrid().mesh[ix];
    double yit=md3dR.d3d[ic].getYCollocGrid().mesh[iy];
    double zit=md3dR.d3d[ic].getZCollocGrid().mesh[iz];
//  potential from the left-hand side of the equation:
    int length=md3d.getDimension();
    for (int i=0;i<res.a.length;i++) res.a[i]=0.0;
    double potentialValue=vPot[ic].valueAt(xit)*xit*yit;
    SummationLimits limitsX=new SummationLimits(0,0);
    SummationLimits limitsY=new SummationLimits(0,0);
    SummationLimits limitsZ=new SummationLimits(0,0);
    for (int jc0=0;jc0<3; jc0++)
    {
      int jc=idTable[jc0];
      if (jc0!=ic)
      {
        xyzSet=rotcoord(xit,yit,zit,ic,jc0);
        xp=xyzSet.x; yp=xyzSet.y; zp=xyzSet.z;
        xspl=md3d.d3d[jc].getXSpline();
        yspl=md3d.d3d[jc].getYSpline();
        zspl=md3d.d3d[jc].getZSpline();
        //double rho=xit*xit+yit*yit;
        //double rhop=xp*xp+yp*yp;
        //System.err.println(rho+" "+rhop+" "+rhom+" "+zi+" "+zp+" "+zm);
        double[] xsplValues=new double[xspl.getSplinesPerNode()*2];
        double[] ysplValues=new double[yspl.getSplinesPerNode()*2];
        double[] zsplValues=new double[zspl.getSplinesPerNode()*2];
        int k;
        //    "right-hand side" of the equation, short-range part of the interaction:
        limitsX=xspl.getLimits(xp,limitsX);
        for (int i=limitsX.LeftLimit;i<=limitsX.RightLimit;i++)
              xsplValues[i-limitsX.LeftLimit]=xspl.fBSpline(xp,i);
        limitsY=yspl.getLimits(yp,limitsY);
        for (int i=limitsY.LeftLimit;i<=limitsY.RightLimit;i++)
              ysplValues[i-limitsY.LeftLimit]=yspl.fBSpline(yp,i);
        limitsZ=zspl.getLimits(zp*zTable[jc0],limitsZ);
        for (int i=limitsZ.LeftLimit;i<=limitsZ.RightLimit;i++)
              zsplValues[i-limitsZ.LeftLimit]=zspl.bSplineFree(zp*zTable[jc0],i);
        for (int jx=limitsX.LeftLimit;jx<=limitsX.RightLimit;jx++)
        for (int jy=limitsY.LeftLimit;jy<=limitsY.RightLimit;jy++)
        for (int jz=limitsZ.LeftLimit;jz<=limitsZ.RightLimit;jz++)
        {
          k=md3d.index[jc][jx][jy][jz];
          //k=md3d.index[ic][jx][jy][jz]; //!!!!!!!!!!!!!
          res.a[res.nz]+=parityTable[jc0]*potentialValue*xsplValues[jx-limitsX.LeftLimit]
                                    *ysplValues[jy-limitsY.LeftLimit]
                                    *zsplValues[jz-limitsZ.LeftLimit]/(xp*yp);
          res.j[res.nz]=k;
          res.nz++;
          // out.print("["+k+"] "+res[k]+"   ");
          // out.println();
        }
        //out.println();
      }
    }
    res.compress();
    return res;
  }
  //
  public AFunction times(AFunction u)
  { //System.out.println("Calculating row: " + j +" of " + rank);
    AFunction res=u.imprint(rank);//u.completeClone();
    res.setZero();
    double xp,yp,zp;
    coordSet xyzSet;
    for (int j=0;j<res.getArrayLength();j++)
    {
      int ix=md3dR.indexX[j];
      int iy=md3dR.indexY[j];
      int iz=md3dR.indexZ[j];
      int ic=md3dR.indexComp[j];
      HSplineBC xspl,yspl;
      HSplineFree zspl;
      if (md3dR.index[ic][ix][iy][iz]!=j)
      { System.err.println("Error in row index calculation, exiting!!!!");
        System.exit(-1);
      }
    //System.out.println("1 ix, iy, iz: " + ix+" "+iy+" "+iz);
//    HSplineBC xspl=d3D.getXSpline();
//    HSplineBC yspl=d3D.getYSpline();
//    HSplineFree zspl=d3D.getZSpline();
      double xit=md3dR.d3d[ic].getXCollocGrid().mesh[ix];
      double yit=md3dR.d3d[ic].getYCollocGrid().mesh[iy];
      double zit=md3dR.d3d[ic].getZCollocGrid().mesh[iz];
//  potential from the left-hand side of the equation:
      int length=md3d.getDimension();
      double potentialValue=vPot[ic].valueAt(xit)*xit*yit;
      SummationLimits limitsX=new SummationLimits(0,0);
      SummationLimits limitsY=new SummationLimits(0,0);
      SummationLimits limitsZ=new SummationLimits(0,0);
      for (int jc0=0;jc0<3; jc0++)
      {
        int jc=idTable[jc0];
        if (jc0!=ic)
        {
          xyzSet=rotcoord(xit,yit,zit,ic,jc0);
          xp=xyzSet.x;  yp=xyzSet.y; zp=xyzSet.z;
          xspl=md3d.d3d[jc].getXSpline();
          yspl=md3d.d3d[jc].getYSpline();
          zspl=md3d.d3d[jc].getZSpline();
          //double rho=xit*xit+yit*yit;
          //double rhop=xp*xp+yp*yp;
          //System.err.println(rho+" "+rhop+" "+rhom+" "+zi+" "+zp+" "+zm);
          double[] xsplValues=new double[xspl.getSplinesPerNode()*2];
          double[] ysplValues=new double[yspl.getSplinesPerNode()*2];
          double[] zsplValues=new double[zspl.getSplinesPerNode()*2];
          int k;
    //    "right-hand side" of the equation, short-range part of the interaction:
          limitsX=xspl.getLimits(xp,limitsX);
          for (int i=limitsX.LeftLimit;i<=limitsX.RightLimit;i++)
            xsplValues[i-limitsX.LeftLimit]=xspl.fBSpline(xp,i);
          limitsY=yspl.getLimits(yp,limitsY);
          for (int i=limitsY.LeftLimit;i<=limitsY.RightLimit;i++)
            ysplValues[i-limitsY.LeftLimit]=yspl.fBSpline(yp,i);
          limitsZ=zspl.getLimits(zp*zTable[jc0],limitsZ);
          for (int i=limitsZ.LeftLimit;i<=limitsZ.RightLimit;i++)
            zsplValues[i-limitsZ.LeftLimit]=zspl.bSplineFree(zp*zTable[jc0],i);
          for (int jx=limitsX.LeftLimit;jx<=limitsX.RightLimit;jx++)
          for (int jy=limitsY.LeftLimit;jy<=limitsY.RightLimit;jy++)
          for (int jz=limitsZ.LeftLimit;jz<=limitsZ.RightLimit;jz++)
          {
            k=md3d.index[jc][jx][jy][jz];
            //k=md3d.index[ic][jx][jy][jz]; //!!!!!!!!!!!!!
            res.coef[j]+=parityTable[jc0]*potentialValue*xsplValues[jx-limitsX.LeftLimit]
                                  *ysplValues[jy-limitsY.LeftLimit]
                                  *zsplValues[jz-limitsZ.LeftLimit]/(xp*yp)
                                  *
                                  u.coef[k];
            // out.print("["+k+"] "+res[k]+"   ");
            // out.println();
          }
          //out.println();
        }
      }
    }
    //System.out.println("5 ix, iy, iz: " + ix+" "+iy+" "+iz);
    // out.println();
    return res;
  }
  //
  final synchronized coordSet rotcoord(double x, double y, double z, int ic, int jc)
  {
    double xp,yp,zp;
    double xx=x*x;
    double yy=y*y;
    double xyz=x*y*z;
    xp=Math.sqrt(
        xx*cpm[ic][jc][0][0]*cpm[ic][jc][0][0]
      + yy*cpm[ic][jc][0][1]*cpm[ic][jc][0][1]
       + 2*cpm[ic][jc][0][0]*cpm[ic][jc][0][1]*xyz);
    yp=Math.sqrt(
        xx*cpm[ic][jc][1][0]*cpm[ic][jc][1][0]
      + yy*cpm[ic][jc][1][1]*cpm[ic][jc][1][1]
       + 2*cpm[ic][jc][1][0]*cpm[ic][jc][1][1]*xyz);
    zp=(
          cpm[ic][jc][0][0]*cpm[ic][jc][1][0]*xx
        + cpm[ic][jc][0][1]*cpm[ic][jc][1][1]*yy
        + xyz*(cpm[ic][jc][0][0]*cpm[ic][jc][1][1]+cpm[ic][jc][0][1]*cpm[ic][jc][1][0])
        )/(xp*yp);

    // out.println(x+" "+y+" "+z+" "+xp+" "+yp+" "+zp+" "+(xx+yy)+"=?"+(xp*xp+yp*yp));
    //
    return new coordSet(xp, yp, zp);
  }
  //
  private void initId()
  { try
    {
      InpReader reader=new InpReader("./ID.conf");
      idTable=reader.readIntArray();
      //System.err.println("idTable read as:"+ idTable);
      parityTable=reader.readDoubleArray();
      //System.err.println("parityTable read as:"+ parityTable);
      zTable=reader.readDoubleArray();
      //System.err.println("zTable read as:"+ zTable);
      reader.close();
    }
    catch(Exception e)
    {
      System.err.println("Error initializing ID tables from file: "+ e.getMessage());
    }
  }

//

  static java.io.PrintStream out;
  static
  {
    try{
      out=new java.io.PrintStream(new java.io.FileOutputStream("RHSOperator.log"));
    }
    catch(Exception e)
    { System.err.println(e.getMessage());
    }
  }
    @Override
  public void finalize()
  { out.close();
  }

////////////////////////////////////////////
  public AFunction shrink(AFunction u)
  { AFunction res=u.imprint(md3dR.getDimension());
    int k=0;
    for (int i=0; i<md3dR.getDimension(); i++)
    {
      k=md3d.index[md3dR.indexComp[i]][md3dR.indexX[i]][md3dR.indexY[i]][md3dR.indexZ[i]];
      res.coef[i]=u.coef[k];
    }
    return res;
  }
}
