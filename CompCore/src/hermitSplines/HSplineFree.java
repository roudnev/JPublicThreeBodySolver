
//////////////////////////////////////////////////////////////////////////////////
// HSplineFree HSplineFree HSplineFree HSplineFree HSplineFree HSplineFree HSplineFree
//////////////////////////////////////////////////////////////////////////////////
package hermitSplines;
public class HSplineFree extends AbstractHermitSpline implements Cloneable
{  
  protected int nMaxFree;
  private double[] leftPoints, midPoints,rightPoints;
    ////////////////////////////////
    // Constructors:              //
    ////////////////////////////////
  public HSplineFree(SimpleGrid GR, char SType)
  { theGrid=GR;
    splineType=SType;
    switch (splineType)
    { case 'C': splinesPerNode=2;
        break;
      case 'Q': splinesPerNode=3;
        break;
      default: System.out.println("Unknown spline type");
               System.exit(-1);
        break;
    }
    nMaxFree=theGrid.mesh.length*splinesPerNode-1;
    nMax=nMaxFree;
    leftPoints=new double[nMaxFree+1];
    midPoints=new double[nMaxFree+1];
    rightPoints=new double[nMaxFree+1];
    initRefPoints();
    
    //fillOverlapMatrix();
  }
    private void initRefPoints()
    {
        int RangeNumber, SplineNumber;
        double x1i,xi,xi1,res=0.0;
        for (int i=0;i<=nMaxFree; i++)
        {
          RangeNumber=i/splinesPerNode;
          SplineNumber=i%splinesPerNode;
          xi=theGrid.mesh[RangeNumber];
          if (RangeNumber !=0)
            x1i=theGrid.mesh[RangeNumber-1];
          else
            x1i=xi;
          if (RangeNumber < theGrid.mesh.length-1)
            xi1=theGrid.mesh[RangeNumber+1];
          else
            xi1=xi;
          leftPoints[i]=x1i;
          midPoints[i]=xi;
          rightPoints[i]=xi1;
        }
    }

    public final double bSplineFree(double t, int I)
  { int RangeNumber, SplineNumber;
    double x1i,xi,xi1,res=0.0;
    RangeNumber=I/splinesPerNode;
    SplineNumber=I%splinesPerNode;
//    xi=theGrid.mesh[RangeNumber];
//    if (RangeNumber !=0)
//      x1i=theGrid.mesh[RangeNumber-1];
//    else
//      x1i=xi;
//    if (RangeNumber < theGrid.mesh.length-1)
//      xi1=theGrid.mesh[RangeNumber+1];
//    else
//      xi1=xi;
    x1i=leftPoints[I];
    xi=midPoints[I];
    xi1=rightPoints[I];
    switch (SplineNumber)
    { case 0: res=phi(t,x1i,xi,xi1); break;
      case 1: res=psi(t,x1i,xi,xi1); break;
      case 2: res=chi(t,x1i,xi,xi1); break;
    }
//    if (SplineNumber==0)
//      res=phi(t,x1i,xi,xi1);
//    else if (SplineNumber==1)
//      res=psi(t,x1i,xi,xi1);
//    else if(SplineNumber== 2)
//      res=chi(t,x1i,xi,xi1);
    return res;
  }
public double fBSpline(double t, int I)
  { 
    return this.bSplineFree(t, I);
  }
  public final double d1BSplineFree(double t, int I)
  { int RangeNumber, SplineNumber;
    double x1i,xi,xi1,res=0.0;
    RangeNumber=I/splinesPerNode;
    SplineNumber=I%splinesPerNode;
//    xi=theGrid.mesh[RangeNumber];
//    if (RangeNumber !=0) x1i=theGrid.mesh[RangeNumber-1];
//    else x1i=xi;
//    if (RangeNumber < theGrid.mesh.length-1) xi1=theGrid.mesh[RangeNumber+1];
//    else xi1=xi;

    x1i=leftPoints[I];
    xi=midPoints[I];
    xi1=rightPoints[I];
    switch (SplineNumber)
    { case 0: res=phi1(t,x1i,xi,xi1); break;
      case 1: res=psi1(t,x1i,xi,xi1); break;
      case 2: res=chi1(t,x1i,xi,xi1); break;
    }
    return res;
  }

  public final double d2BSplineFree(double t, int I)
  { int RangeNumber, SplineNumber;
    double x1i,xi,xi1,res=0.0;
    RangeNumber=I/splinesPerNode;
    SplineNumber=I%splinesPerNode;
//    xi=theGrid.mesh[RangeNumber];
//    if (RangeNumber !=0) x1i=theGrid.mesh[RangeNumber-1];
//    else x1i=xi;
//    if (RangeNumber < theGrid.mesh.length-1) xi1=theGrid.mesh[RangeNumber+1];
//    else xi1=xi;

    x1i=leftPoints[I];
    xi=midPoints[I];
    xi1=rightPoints[I];
    switch (SplineNumber)
    { case 0: res=phi2(t,x1i,xi,xi1); break;
      case 1: res=psi2(t,x1i,xi,xi1); break;
      case 2: res=chi2(t,x1i,xi,xi1); break;
    }
    return res;
  }

  public int getNMaxFree()
  { int res=nMaxFree;//(theGrid.mesh.length)*splinesPerNode-1;
    return res;
  }

    ////////////////
    // Methods:   //
    ////////////////
    public int getNMax() {
        //int res=nMax;//getNMaxFree()-2*(splinesPerNode-1);
        return nMax;
    }

    @Override
    public void fillOverlapMatrix()
    {
        //System.out.println(this.theGrid.getName()+"  nMax="+nMax+" nMaxFree="+nMaxFree);
        overlapMatrix=new double[nMax+1][nMax+1];
        //
        double[] gaussPoints={0.2386191860831969086305017,0.6612093864662645136613996,0.9324695142031520278123016};
        double[] gaussWeights={0.4679139345726910473898703,0.3607615730481386075698335,0.1713244923791703450402961};
        double leftI,leftJ,rightI,rightJ,midI,midJ;
        double left, right,middle;
        double a,b,A,B,t;
        for (int i=0;i<overlapMatrix.length; i++)
        {
            leftI=leftPoints[i];
            midI=midPoints[i];
            rightI=rightPoints[i];
            for (int j=0;j<overlapMatrix.length; j++)
            {
                leftJ=leftPoints[j];
                midJ=midPoints[j];
                rightJ=rightPoints[j];
                overlapMatrix[i][j]=0.0;
                if ((leftI>rightJ) | (leftJ>rightI))
                  overlapMatrix[i][j]=0.0;
                else
                {
                  left=Math.max(leftI,leftJ);
                  right=Math.min(rightI,rightJ);
                  if ((leftI==leftJ) & (rightI==rightJ))
                  {
                      left=leftI;
                      middle=midPoints[i];
                      right=rightI;
                      overlapMatrix[i][j]=0.0;
                      a=left;
                      b=middle;
                      A=0.5*(b-a);
                      B=0.5*(b+a);
                      for (int k=0; k< gaussPoints.length;k++)
                      {
                          t=B+A*gaussPoints[k];
                          overlapMatrix[i][j]+=A*gaussWeights[k]*this.fBSpline(t, i)*this.fBSpline(t, j);
                          t=B-A*gaussPoints[k];
                          overlapMatrix[i][j]+=A*gaussWeights[k]*this.fBSpline(t, i)*this.fBSpline(t, j);
                      }
                      a=middle;
                      b=right;
                      A=0.5*(b-a);
                      B=0.5*(b+a);
                      for (int k=0; k< gaussPoints.length;k++)
                      {
                          t=B+A*gaussPoints[k];
                          overlapMatrix[i][j]+=A*gaussWeights[k]*this.fBSpline(t, i)*this.fBSpline(t, j);
                          t=B-A*gaussPoints[k];
                          overlapMatrix[i][j]+=A*gaussWeights[k]*this.fBSpline(t, i)*this.fBSpline(t, j);
                      }
                  }
                  else
                  {
                      a=left;
                      b=right;
                      A=0.5*(b-a);
                      B=0.5*(b+a);
                      for (int k=0; k< gaussPoints.length;k++)
                      {
                          t=B+A*gaussPoints[k];
                          overlapMatrix[i][j]+=A*gaussWeights[k]*this.fBSpline(t, i)*this.fBSpline(t, j);
                          t=B-A*gaussPoints[k];
                          overlapMatrix[i][j]+=A*gaussWeights[k]*this.fBSpline(t, i)*this.fBSpline(t, j);
                      }
                  }
              }
             //if (i<7) System.out.format("  %1$ 13g" ,overlapMatrix[i][j]);
            }
            //if (i<7) System.out.println();
        }
    }
     @Override
  public Object clone()
  {
      HSplineFree res=(HSplineFree) super.clone();
      return res;
  }
}
