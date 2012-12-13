/////////////////////////////////////////////////////////////////////////////////////
// HSplineBC HSplineBC HSplineBC HSplineBC HSplineBC HSplineBC HSplineBC HSplineBC //
/////////////////////////////////////////////////////////////////////////////////////
//
// revision 2.0 (2002)
//
//
package hermitSplines;
public class HSplineBC extends HSplineFree
{
////////////////
// Fields:    //
////////////////
  private char boundaryConditionsTypeLeft;
  private char boundaryConditionsTypeRight;
  private double cFLeft,  cDFLeft;
  private double cFRight, cDFRight;
  private boolean isLeftBC3Set=false,
                  isRightBC3Set=false;
////////////////////////////////
// Constructors:              //
////////////////////////////////

  public HSplineBC(SimpleGrid gr, char sType, char bcLeft, char bcRight)
  { super(gr,sType);
    boundaryConditionsTypeLeft=bcLeft;
    boundaryConditionsTypeRight=bcRight;
    switch (boundaryConditionsTypeLeft)
    { case 'D':
        cFLeft=0.0;
        cDFLeft=1.0;
      break;
      case 'N':
        cFLeft=1.0;
        cDFLeft=0.0;
      break;
      case '3':
         System.err.println("error: Incompatible BC initialization, exiting...");
         System.exit(-1);
      break;
    }
    switch (boundaryConditionsTypeRight)
    { case 'D':
        cFRight=0.0;
        cDFRight=1.0;
      break;
      case 'N':
        cFRight=1.0;
        cDFRight=0.0;
      break;
      case '3':
         System.out.println("Incompatible BC initialization, exiting...");
         System.exit(-1);
      break;
    }
    nMax=getNMaxFree()-2;//*(splinesPerNode-1);
    //fillOverlapMatrix();
  }

  public HSplineBC(SimpleGrid gr, char sType, char bcLeft, char bcRight, double[] bccl, double[] bccr)
  { super(gr,sType);
    boundaryConditionsTypeLeft=bcLeft;
    boundaryConditionsTypeRight=bcRight;
    switch (boundaryConditionsTypeLeft)
    { case 'D':
        cFLeft=0.0;
        cDFLeft=1.0;
      break;
      case 'N':
        cFLeft=1.0;
        cDFLeft=0.0;
      break;
      case '3':
        cFLeft=1.0;
        cDFLeft=bccl[0];
      break;
    }
    switch (boundaryConditionsTypeRight)
    { case 'D':
        cFRight=0.0;
        cDFRight=1.0;
      break;
      case 'N':
        cFRight=1.0;
        cDFRight=0.0;
      break;
      case '3':
        cFRight=1.0;
        cDFRight=bccr[0];
      break;
    }
    nMax=getNMaxFree()-2;//*(splinesPerNode-1);
    //fillOverlapMatrix();
//    System.err.println();
//    System.err.println(gr.getName()+" nMax="+nMax);
//    System.err.println();
  }

    @Override
  public double fBSpline(double t, int i)
  { int n=nMax;//getNMax();
    int iDumb;
    double res=13.0;
    switch (splineType)
    { case 'Q':
          if (i==0)
            { res=cFLeft*bSplineFree(t,0)+
                  cDFLeft*bSplineFree(t,1);
            }
          else if (i==n-1)
            {  res=bSplineFree(t,nMaxFree);
            }
          else if (i==n)
          {  res=cFRight*bSplineFree(t,nMaxFree-2)+
                 cDFRight*bSplineFree(t,nMaxFree-1);
          }
          else
            { iDumb=i+1;
              res=bSplineFree(t,iDumb);
            }
      break;
      //
      case 'C':
          if (i==0)
            { res=cFLeft*bSplineFree(t,0)+
                  cDFLeft*bSplineFree(t,1);
            }
          else if (i==n)
            { res=cFRight*bSplineFree(t,nMaxFree-1)+
                      cDFRight*bSplineFree(t,nMaxFree);
            }
          else
          { iDumb=i+1;
            res=bSplineFree(t,iDumb);
          }
      break;
      //
       default:
        System.out.println("Unknown type of spline, exiting...");
        System.exit(-1);
      break;
    }
    return res;
  }

  public double d1BSpline(double t, int i)
  { int n=nMax;
    int iDumb;
    double res=13.0;
    switch (splineType)
    { case 'Q':
	  if (i==0)
            { res=cFLeft*d1BSplineFree(t,0)+
                  cDFLeft*d1BSplineFree(t,1);
            }
          else if (i==n-1)
            {  res=d1BSplineFree(t,nMaxFree);
            }
          else if (i==n)
          {  res=cFRight*d1BSplineFree(t,nMaxFree-2)+
                 cDFRight*d1BSplineFree(t,nMaxFree-1);
          }
          else
            { iDumb=i+1;
              res=d1BSplineFree(t,iDumb);
            }
      break;
      //
      case 'C':
	if (i==0)
	  { res=cFLeft*d1BSplineFree(t,0)+
	        cDFLeft*d1BSplineFree(t,1);
	  }
	else if (i==n)
	  { res=cFRight*d1BSplineFree(t,getNMaxFree()-1)+
	        cDFRight*d1BSplineFree(t,getNMaxFree());
	  }
	else
	{ iDumb=i+1;
	  res=d1BSplineFree(t,iDumb);
	}
      break;
      //
       default:
        System.out.println("Unknown type of spline, exiting...");
        System.exit(-1);
      break;
    }
    return res;
  }

  public double d2BSpline(double t, int i)
  { int n=getNMax();
    int iDumb;
    double res=13.0;
    switch (splineType)
    { case 'Q':
	if (i==0)
            { res=cFLeft*d2BSplineFree(t,0)+
                  cDFLeft*d2BSplineFree(t,1);
            }
          else if (i==n-1)
            {  res=d2BSplineFree(t,nMaxFree);
            }
          else if (i==n)
          {  res=cFRight*d2BSplineFree(t,nMaxFree-2)+
                 cDFRight*d2BSplineFree(t,nMaxFree-1);
          }
          else
            { iDumb=i+1;
              res=d2BSplineFree(t,iDumb);
            }
      break;
      //
      case 'C':
	if (i==0)
	  { res=cFLeft*d2BSplineFree(t,0)+
	        cDFLeft*d2BSplineFree(t,1);
	  }
	else if (i==n)
	  { res=cFRight*d2BSplineFree(t,getNMaxFree()-1)+
	        cDFRight*d2BSplineFree(t,getNMaxFree());
	  }
	else
	{ iDumb=i+1;
	  res=d2BSplineFree(t,iDumb);
	}
      break;
      //
       default:
        System.out.println("Unknown type of spline, exiting...");
        System.exit(-1);
      break;
    }
    return res;
  }
  //
    @Override
  public final SummationLimits getLimits(double x, SummationLimits limits)
  {
    int i=1;
    int iRight,iLeft;
    if (x<theGrid.rightmostPoint & x>theGrid.leftmostPoint)
    {
      while (!(x>theGrid.mesh[i-1] & x<theGrid.mesh[i])) i++;
      iRight=getSplinesPerNode()*i;
      if (splineType=='Q') iRight+=1;
      iLeft=Math.max(0,iRight-getSplinesPerNode()*2+1);
      iRight=Math.min(iRight,getNMax());
      limits.LeftLimit=iLeft;
      limits.RightLimit=iRight;
    }
    else
    { limits.LeftLimit=0;
      limits.RightLimit=0;
    }
    return limits;
  }
  //
  public void setBoundaryConditionsTypeLeft(char type)
  {
    boundaryConditionsTypeLeft=type;
    switch (boundaryConditionsTypeLeft)
    { case 'D':
        cFLeft=0.0;
        cDFLeft=1.0;
      break;
      case 'N':
        cFLeft=1.0;
        cDFLeft=0.0;
      break;
      case '3':
         System.err.println("error: Incompatible BC initialization, exiting...");
         System.exit(-1);
      break;
    }
  }
  //
  public void setBoundaryConditionsTypeRight(char type)
  {
    boundaryConditionsTypeRight=type;
    switch (boundaryConditionsTypeRight)
    { case 'D':
        cFRight=0.0;
        cDFRight=1.0;
      break;
      case 'N':
        cFRight=1.0;
        cDFRight=0.0;
      break;
      case '3':
         System.out.println("Incompatible BC initialization, exiting...");
         System.exit(-1);
      break;
    }
  }
  //
  public void setBoundaryConditionsTypeLeft(char type,double bcc[])
  {
    boundaryConditionsTypeLeft=type;
    switch (boundaryConditionsTypeLeft)
    { case 'D':
        cFLeft=0.0;
        cDFLeft=1.0;
      break;
      case 'N':
        cFLeft=1.0;
        cDFLeft=0.0;
      break;
      case '3':
        cFLeft=1.0;
        cDFLeft=bcc[0];
      break;
    }
  }
  //
  public void setBoundaryConditionsTypeRight(char type,double bcc[])
  {
    boundaryConditionsTypeRight=type;
    switch (boundaryConditionsTypeRight)
    { case 'D':
        cFRight=0.0;
        cDFRight=1.0;
      break;
      case 'N':
        cFRight=1.0;
        cDFRight=0.0;
      break;
      case '3':
        cFRight=1.0;
        cDFRight=bcc[0];
      break;
    }
  }
    @Override
  public final Object clone()
  {
      HSplineBC res=(HSplineBC) super.clone();
      return res;
  }
}
