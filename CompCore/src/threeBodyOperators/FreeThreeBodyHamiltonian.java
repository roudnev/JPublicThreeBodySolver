package threeBodyOperators;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import basicAbstractions.Solvable;
import basicAbstractions.Simple;
import hermitSplines.HSplineBC;
import hermitSplines.HSplineFree;
import hermitSplines.SummationLimits;
import Jama.Matrix;
import decompositions.MyEigenvalueDecomposition;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 1.0
 */

public class FreeThreeBodyHamiltonian implements AnOperator, Simple
{
// fields:
  double z=0.0;
  MyEigenvalueDecomposition[] xOperator;
  MyEigenvalueDecomposition[] yOperator;
  MyEigenvalueDecomposition zOperator;
  Matrix idX,idY,idZ;
  Matrix idXinv,idYinv,idZinv;
  Matrix idNormX;
  Discretization3D d3D;
  Discretization3D rd3D;
// constructors:
  FreeThreeBodyHamiltonian()
  { super();
  }
  public FreeThreeBodyHamiltonian(Discretization3D d3D, Discretization3D rd3D)
  { this.d3D=d3D;
    this.rd3D=rd3D;
    zOperator=makeZoperator();
    xOperator=makeXoperator();
    yOperator=makeYoperator();
  //  fillIdNormXMatrix();
  }
// methods:
 /*  private void fillIdNormXMatrix()
   {
     int nx=d3D.getNX();
     double[][] idNX=new double[nx][nx];
     HSplineBC spl=d3D.getXSpline();
     for (int i=0;i<nx;i++)
      {
        xi=d3D.getXCollocGrid().mesh[i];
        limits=spl.getLimits(xi,limits);
        iL=limits.LeftLimit; iR=limits.RightLimit;
        // System.out.println(""+xi+" "+iL+" "+iR);
        for (int j=iL;j<iR+1;j++)
        {
          iX[i][j]=spl.fBSpline(xi,j);
        }
      }
   }
  */

   public void setZ(double z)
   {
     this.z=z;
   }
//
  protected MyEigenvalueDecomposition[] makeXoperator()
  {
    double zl;
    int nz=getzOperator().getEigenvaluesRe().length;
    int nx=d3D.getNX();
    int rnx=rd3D.getNX();
    double[][] dX=new double[nx][nx];
    double[][] iX=new double[nx][nx];
    double[][] riX=new double[nx][nx];
    Matrix dMatrix;
    Matrix iMatrix;
    Matrix tmpM;
    double xi;
    HSplineBC spl=d3D.getXSpline();
    MyEigenvalueDecomposition[] res=new MyEigenvalueDecomposition[nz];
    int iL=0,iR=0;
    SummationLimits limits=new SummationLimits(0,0);
    for (int lz=0;lz<nz;lz++)
    {
      zl=   getzOperator().getEigenvaluesRe()[lz];
      for (int i=0;i<nx;i++)
      {
        xi=d3D.getXCollocGrid().mesh[i];
        limits=spl.getLimits(xi,limits);
        iL=limits.LeftLimit; iR=limits.RightLimit;
        // System.out.println(""+xi+" "+iL+" "+iR);
        for (int j=iL;j<iR+1;j++)
        {
          dX[i][j]=-spl.d2BSpline(xi,j)+zl*spl.fBSpline(xi,j)/(xi*xi);
          iX[i][j]=spl.fBSpline(xi,j);
        }
      }
      dMatrix=new Matrix(dX);
      iMatrix=new Matrix(iX);
      idX=iMatrix;
      tmpM=idX.inverse().times(dMatrix);
      res[lz]=new MyEigenvalueDecomposition(tmpM);
      //System.out.println(" X Spectrum for zl="+zl);
      //for (int i=0;i<res[lz].getEigenvaluesIm().length;i++)
      //  System.out.println(" "+res[lz].getEigenvaluesRe()[i]+" + "+res[lz].getEigenvaluesIm()[i]+"I");
       /*
      tmpM=new Matrix( res[lz].getEigenvectorsLeftRe());
      tmpM=tmpM.times(iMatrix.inverse());
      res[lz].setEigenvectorsLeftRe(tmpM.getArrayCopy());
      tmpM=new Matrix( res[lz].getEigenvectorsLeftIm());
      tmpM=tmpM.times(iMatrix.inverse());
      res[lz].setEigenvectorsLeftIm(tmpM.getArrayCopy()); */
    }
    spl=d3D.getXSpline();
    for (int i=0;i<nx;i++)
    {
      xi=d3D.getXCollocGrid().mesh[i];
      for (int j=0;j<nx;j++)
      {
        riX[i][j]=spl.fBSpline(xi,j);
      }
    }
    idXinv=new Matrix(riX);
    idXinv=idXinv.inverse();
    return res;
  }
  //
  protected MyEigenvalueDecomposition[] makeYoperator()
  {
    double zl;
    int nz=getzOperator().getEigenvaluesRe().length;
    int ny=d3D.getNY();
    int rny=rd3D.getNY();
    double[][] dY=new double[ny][ny];
    double[][] iY=new double[ny][ny];
    double[][] riY=new double[ny][ny];
    Matrix dMatrix;
    Matrix iMatrix;
    Matrix tmpM;
    double yi;
    HSplineBC spl=d3D.getYSpline();
    MyEigenvalueDecomposition[] res=new MyEigenvalueDecomposition[nz];
    for (int lz=0;lz<nz;lz++)
    {
      zl=   getzOperator().getEigenvaluesRe()[lz];
      for (int i=0;i<ny;i++)
      {
        yi=d3D.getYCollocGrid().mesh[i];
        for (int j=0;j<ny;j++)
        {
          dY[i][j]=-spl.d2BSpline(yi,j)+zl*spl.fBSpline(yi,j)/(yi*yi);
          iY[i][j]=spl.fBSpline(yi,j);
        }
      }
      dMatrix=new Matrix(dY);
      iMatrix=new Matrix(iY);
      idY=iMatrix;
      idYinv=idY.inverse();
      tmpM=idYinv.times(dMatrix);
      res[lz]=new MyEigenvalueDecomposition(tmpM);
      try
      {
        java.io.PrintStream
          dumpWriter=new java.io.PrintStream(
                          new java.io.FileOutputStream("YSpectrumForZl.eq."+Math.round(zl)+".dat")
                        );
        for (int i=0;i<res[lz].getEigenvaluesIm().length;i++)
        { dumpWriter.print(" "+res[lz].getEigenvaluesRe()[i]+" + "+res[lz].getEigenvaluesIm()[i]+"I");
          dumpWriter.println();
        }
        dumpWriter.close();
      }
      catch(Exception e)
      { System.err.println("Error initializing MultocomponentDiscretization3D:"+e.getMessage());
      }
        /*
      tmpM=new Matrix( res[lz].getEigenvectorsLeftRe());
      tmpM=tmpM.times(iMatrix.inverse());
      res[lz].setEigenvectorsLeftRe(tmpM.getArrayCopy());
      tmpM=new Matrix( res[lz].getEigenvectorsLeftIm());
      tmpM=tmpM.times(iMatrix.inverse());
      res[lz].setEigenvectorsLeftIm(tmpM.getArrayCopy()); */
    }
    spl=d3D.getYSpline();
    for (int i=0;i<ny;i++)
    {
      yi=d3D.getYCollocGrid().mesh[i];
      for (int j=0;j<ny;j++)
      {
        riY[i][j]=spl.fBSpline(yi,j);
      }
    }
    idYinv=new Matrix(riY);
    idYinv=idYinv.inverse();
    return res;
  }
  //
  protected MyEigenvalueDecomposition makeZoperator()
  {
    int nz=d3D.getNZ();
    double[][] dZ=new double[nz][nz];
    double[][] iZ=new double[nz][nz];
    double zi;
    SummationLimits limits=new SummationLimits(0,0);
    HSplineFree spl=d3D.getZSpline();
    for (int i=0;i<nz;i++)
    {
      zi=d3D.getZCollocGrid().mesh[i];
      limits=spl.getLimits(zi,limits);
      for (int j=limits.LeftLimit;j<=limits.RightLimit;j++)
      { dZ[i][j]=-(1.0-zi*zi)*spl.d2BSplineFree(zi,j)+2*zi*spl.d1BSplineFree(zi,j);
        iZ[i][j]=spl.bSplineFree(zi,j);
      }
    }
    Matrix dzMatrix=new Matrix(dZ);
    Matrix izMatrix=new Matrix(iZ);
    idZ=izMatrix;
    idZinv=izMatrix.inverse();
    Matrix tmpZ=idZinv.times(dzMatrix);
    MyEigenvalueDecomposition res=new MyEigenvalueDecomposition(tmpZ);
    //for (int i=0;i<res.getEigenvaluesIm().length;i++)
    //  System.out.println(" "+res.getEigenvaluesRe()[i]+" + "+res.getEigenvaluesIm()[i]+"I");
    for (int i=0;i<res.getEigenvaluesIm().length;i++)
    if (Math.abs(res.getEigenvaluesIm()[i])>1.0e-9)
      {
        System.out.println(" "+res.getEigenvaluesRe()[i]+" + "+res.getEigenvaluesIm()[i]+"I");
        System.err.println("Error: imaginary angular momentum");
        System.exit(1);
      } /*
    tmpZ=new Matrix(res.getEigenvectorsLeftIm());
    tmpZ=tmpM.times(iMatrix.inverse());
    res.setEigenvectorsLeftIm(tmpZ.getArrayCopy());
    tmpZ=new Matrix(res.getEigenvectorsLeftRe());
    tmpZ=tmpM.times(iMatrix.inverse());
    res.setEigenvectorsLeftRe(tmpZ.getArrayCopy()); */
    //for (int i=0;i<nz;i++) System.out.println("L="+i+" ~ "+res.getEigenvaluesRe()[i]+"+"+res.getEigenvaluesIm()[i]+"I");
    try
      {
        java.io.PrintStream
          dumpWriter=new java.io.PrintStream(
                          new java.io.FileOutputStream("ZSpectrum.dat")
                        );
        for (int i=0;i<res.getEigenvaluesIm().length;i++)
        { dumpWriter.print(" "+res.getEigenvaluesRe()[i]+" + "+res.getEigenvaluesIm()[i]+"I");
          dumpWriter.println();
        }
        dumpWriter.close();
      }
      catch(Exception e)
      { System.err.println("Error initializing MultocomponentDiscretization3D:"+e.getMessage());
      }
    return res;
  }
  //
  public final AFunction tensorTimes(Matrix mx, Matrix my, Matrix mz, AFunction u)
  {
    Discretization3D t3D;
    int i,j;
    int nx=mx.getRowDimension();
    int ny=my.getRowDimension();
    int nz=mz.getRowDimension();
    t3D=d3D;
    AFunction res=u.completeClone();
    res.setZero();
    AFunction tres=u.completeClone();
    tres.setZero();
//    if (nx*ny*nz!=u.getArrayLength())
//      throw new java.lang.UnsupportedOperationException("Method tensorTimes not yet implemented.");
    if (nx==d3D.getNX() & ny==d3D.getNY() & nz==d3D.getNZ())
    {
      t3D=d3D;
    }
    else if (nx==rd3D.getNX() & ny==rd3D.getNY() & nz==rd3D.getNZ())
    {
      t3D=rd3D;
    }
    else
      { System.err.println("Error calling tensorTimes: incorrect dimensions");
        System.err.println("nx:" + nx + " NX:" + d3D.getNX()+ " rNX:" + rd3D.getNX());
        System.err.println("ny:" + ny + " NY:" + d3D.getNY()+ " rNY:" + rd3D.getNY());
        System.err.println("nz:" + nz + " NZ:" + d3D.getNZ()+ " rNZ:" + rd3D.getNZ());
        System.exit(1);
      }
    for(int ix=0;ix<t3D.getNX();ix++)
    for(int iy=0;iy<t3D.getNY();iy++)
    for(int iz=0;iz<t3D.getNZ();iz++)
    {
      i=t3D.getIndex(ix,iy,iz);
      res.coef[i]=0.0;
      for(int jx=0;jx<t3D.getNX();jx++)
      {
        j=t3D.getIndex(jx,iy,iz);
        res.coef[i]+= mx.getArray()[ix][jx]*u.coef[j];
      }
    }
    for(int ix=0;ix<t3D.getNX();ix++)
    for(int iy=0;iy<t3D.getNY();iy++)
    for(int iz=0;iz<t3D.getNZ();iz++)
    {
      i=t3D.getIndex(ix,iy,iz);
      tres.coef[i]=0.0;
      for(int jy=0;jy<t3D.getNY();jy++)
      {
        j=t3D.getIndex(ix,jy,iz);
        tres.coef[i]+= my.getArray()[iy][jy]*res.coef[j];
      }
    }
    for(int ix=0;ix<t3D.getNX();ix++)
    for(int iy=0;iy<t3D.getNY();iy++)
    for(int iz=0;iz<t3D.getNZ();iz++)
    {
      i=t3D.getIndex(ix,iy,iz);
      res.coef[i]=0.0;
      for(int jz=0;jz<t3D.getNZ();jz++)
      {
        j=t3D.getIndex(ix,iy,jz);
        res.coef[i]+= mz.getArray()[iz][jz]*tres.coef[j];
      }
    }
    return res;
  }
  //
  public final AFunction times(AFunction u)
  { int i,j;
    int n=d3D.getN();
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    // turning to spectral representation for the operator
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jz=0;jz<d3D.getNZ();jz++)
      {   j=d3D.getIndex(ix,iy,jz);
          resRe.coef[i]+=getzOperator().getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
          resIm.coef[i]+=getzOperator().getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
      }
    }
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      tresRe.coef[i]=0.0;
      tresIm.coef[i]=0.0;
      for(int jy=0;jy<d3D.getNY();jy++)
      {
        j=d3D.getIndex(ix,jy,iz);
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
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jx=0;jx<d3D.getNX();jx++)
      {
        j=d3D.getIndex(jx,iy,iz);
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
    //  operator multiplication in spectral representation
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    {
      i=d3D.getIndex(ix,iy,iz);
      tresRe.coef[i]=
          (xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]-z)*resRe.coef[i]
            -
          (xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy])*resIm.coef[i];
      tresIm.coef[i]=
          (xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]-z)*resIm.coef[i]
            +
          (xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy])*resRe.coef[i];
    }
    // recovering space representation
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jx=0;jx<d3D.getNX();jx++)
      {
        j=d3D.getIndex(jx,iy,iz);
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
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      tresRe.coef[i]=0.0;
      tresIm.coef[i]=0.0;
      for(int jy=0;jy<d3D.getNY();jy++)
      {
        j=d3D.getIndex(ix,jy,iz);
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

    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jz=0;jz<d3D.getNZ();jz++)
      {
        j=d3D.getIndex(ix,iy,jz);
        resRe.coef[i]+=getzOperator().getEigenvectorsRightRe()[iz][jz]*tresRe.coef[j];
        resIm.coef[i]+=getzOperator().getEigenvectorsRightRe()[iz][jz]*tresIm.coef[j];
      }
      if (Math.abs(resIm.coef[i])>1.0e-10) System.err.println("method times() incorrect: "+resIm.coef[i]);
    }
    resRe=tensorTimes(idX, idY, idZ, resRe);
    resRe=shrink(resRe);
    return resRe;
  }
  /////////////////////////////////////////////////////////////////////
  public final AFunction solve(AFunction rhs)
  {
    AFunction u=expand(rhs);
    u=tensorTimes(idXinv, idYinv, idZinv, u);
    int i,j;
    int n=d3D.getN();
    AFunction resRe=u.completeClone();
    AFunction resIm=u.completeClone();
    resRe.setZero();
    resIm.setZero();
    AFunction tresRe=resRe.completeClone();
    AFunction tresIm=resIm.completeClone();
    // turning to spectral representation for the operator
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jz=0;jz<d3D.getNZ();jz++)
      {   j=d3D.getIndex(ix,iy,jz);
          resRe.coef[i]+=getzOperator().getEigenvectorsLeftRe()[iz][jz]*u.coef[j];
          resIm.coef[i]+=getzOperator().getEigenvectorsLeftIm()[iz][jz]*u.coef[j];
      }
    }

    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      tresRe.coef[i]=0.0;
      tresIm.coef[i]=0.0;
      for(int jy=0;jy<d3D.getNY();jy++)
      {
        j=d3D.getIndex(ix,jy,iz);
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

    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jx=0;jx<d3D.getNX();jx++)
      {
        j=d3D.getIndex(jx,iy,iz);
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

    //  operator multiplication in spectral representation

    double evRe,evReInv,evIm,evImInv,r;
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    {
      i=d3D.getIndex(ix,iy,iz);
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

    // recovering space representation

    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jx=0;jx<d3D.getNX();jx++)
      {
        j=d3D.getIndex(jx,iy,iz);
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

    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      tresRe.coef[i]=0.0;
      tresIm.coef[i]=0.0;
      for(int jy=0;jy<d3D.getNY();jy++)
      {
        j=d3D.getIndex(ix,jy,iz);
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

    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    { i=d3D.getIndex(ix,iy,iz);
      resRe.coef[i]=0.0;
      resIm.coef[i]=0.0;
      for(int jz=0;jz<d3D.getNZ();jz++)
      {
        j=d3D.getIndex(ix,iy,jz);
        resRe.coef[i]+=getzOperator().getEigenvectorsRightRe()[iz][jz]*tresRe.coef[j]
                            -
                       getzOperator().getEigenvectorsRightIm()[iz][jz]*tresIm.coef[j];
        resIm.coef[i]+=getzOperator().getEigenvectorsRightRe()[iz][jz]*tresIm.coef[j]
                            +
                       getzOperator().getEigenvectorsRightIm()[iz][jz]*tresRe.coef[j];
      }
      if (Math.abs(resIm.coef[i])>5.0e-7) System.err.println("method solve() incorrect: "+resIm.coef[i]);
    }
    return resRe;
  }
  //
  public int getRank()
  {
    return d3D.getN();
  }
  //
  public AFunction shrink(AFunction f)
  { AFunction res=f.imprint(rd3D.getN());
    int nx=rd3D.getNX();
    int ny=rd3D.getNY();
    int nz=rd3D.getNZ();
    for(int ix=0;ix<nx;ix++)
    for(int iy=0;iy<ny;iy++)
    for(int iz=0;iz<nz;iz++)
      res.coef[rd3D.getIndex(ix,iy,iz)]=f.coef[d3D.getIndex(ix,iy,iz)];
    return res;
  }
  //
  public AFunction expand(AFunction f)
  { AFunction res=f.imprint(d3D.getN());
    res.setZero();
    int nx=rd3D.getNX();
    int ny=rd3D.getNY();
    int nz=rd3D.getNZ();
    for(int ix=0;ix<nx;ix++)
    for(int iy=0;iy<ny;iy++)
    for(int iz=0;iz<nz;iz++)
      res.coef[d3D.getIndex(ix,iy,iz)]=f.coef[rd3D.getIndex(ix,iy,iz)];
    return res;
  }

    /**
     * @return the zOperator
     */
    public MyEigenvalueDecomposition getzOperator() {
        return zOperator;
    }
}
/*
   double evRe,evReInv,evIm,evImInv,r;
    for(int ix=0;ix<d3D.getNX();ix++)
    for(int iy=0;iy<d3D.getNY();iy++)
    for(int iz=0;iz<d3D.getNZ();iz++)
    {
      i=d3D.getIndex(ix,iy,iz);
      evRe=xOperator[iz].getEigenvaluesRe()[ix]+yOperator[iz].getEigenvaluesRe()[iy]-z;
      evIm=xOperator[iz].getEigenvaluesIm()[ix]+yOperator[iz].getEigenvaluesIm()[iy];
      r=Math.pow(evRe,2.0)+Math.pow(evIm,2.0);
      evReInv=evRe/r;
      evImInv=-evIm/r;
      tresRe.coef[i]=
          (evReInv)*resRe.coef[i]
            -
          (evImInv)*resIm.coef[i];
      tresIm.coef[i]=
          (evReInv)*resIm.coef[i]
            +
          (evImInv)*resRe.coef[i];
    }
*/