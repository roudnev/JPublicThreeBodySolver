/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package threeBodyOperators;
import basicAbstractions.AFunction;
import hermitSplines.HSplineBC;
import hermitSplines.HSplineFree;
import hermitSplines.SummationLimits;
/**
 *
 * @author roudnev
 */
public class UnrestrictingMulticomponentRHSOperator extends MulticomponentRHSOperator
{ MulticomponentThreeBodyClusterHamiltonian h0;
  double t=1.0;
  public UnrestrictingMulticomponentRHSOperator(MulticomponentRHSOperator v, MulticomponentThreeBodyClusterHamiltonian h0)
  { super(v.mass, v.vPot, v.md3d, v.md3dR);
    this.h0=h0;
    this.md3dR=this.md3d;
  }

  public UnrestrictingMulticomponentRHSOperator(MulticomponentRHSOperator v, MulticomponentThreeBodyClusterHamiltonian h0, double t)
  { super(v.mass, v.vPot, v.md3d, v.md3dR);
    this.t=t;
    this.h0=h0;
    this.md3dR=this.md3d;
  }
  @Override
  public int getRank()
  {
      return h0.getRank();
  }
  @Override
  public AFunction times(AFunction u)
  {
  AFunction res=u.completeClone();
  res.setZero();
    for (int j=0;j<res.getArrayLength();j++)
    {
      int ix=md3d.indexX[j];
      int iy=md3d.indexY[j];
      int iz=md3d.indexZ[j];
      int ic=md3d.indexComp[j];
      HSplineBC xspl,yspl;
      HSplineFree zspl;
      if (md3d.index[ic][ix][iy][iz]!=j)
      { System.err.println("Error in row index calculation, exiting!!!!");
        System.exit(-1);
      }
    //System.out.println("1 ix, iy, iz: " + ix+" "+iy+" "+iz);
//    HSplineBC xspl=d3D.getXSpline();
//    HSplineBC yspl=d3D.getYSpline();
//    HSplineFree zspl=d3D.getZSpline();
      double xit=md3d.d3d[ic].getXCollocGrid().mesh[ix];
      double yit=md3d.d3d[ic].getYCollocGrid().mesh[iy];
      double zit=md3d.d3d[ic].getZCollocGrid().mesh[iz];
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
          double xp,yp,zp;
          coordSet xyzSet=rotcoord(xit,yit,zit,ic,jc0);
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
            res.coef[j]+=parityTable[jc0]*potentialValue*xsplValues[jx-limitsX.LeftLimit]
                                  *ysplValues[jy-limitsY.LeftLimit]
                                  *zsplValues[jz-limitsZ.LeftLimit]/(xp*yp)
                                  *
                                  u.coef[k];
          }
        }
      }
    }
    res=res.times(t);
   // res=h0.recover(res);
    return res;
  }
  //
}
