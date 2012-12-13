
//////////////////////////////////////////////////////////////////////////////////
// HSplineDumb HSplineDumb HSplineDumb HSplineDumb HSplineDumb HSplineDumb HSplineDumb
//////////////////////////////////////////////////////////////////////////////////
package hermitSplines;
public class HSplineFree
{ protected SimpleGrid theGrid;
  protected char splineType;
  protected int splinesPerNode;
  protected int nMaxFree;
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
  }
    public final double bSplineFree(double t, int I)
  { int RangeNumber, SplineNumber;
    double x1i,xi,xi1,res=0.0;
    RangeNumber=I/splinesPerNode;
    SplineNumber=I%splinesPerNode;
    xi=theGrid.mesh[RangeNumber];
    if (RangeNumber !=0) x1i=theGrid.mesh[RangeNumber-1];
    else x1i=xi;
    if (RangeNumber < theGrid.mesh.length-1) xi1=theGrid.mesh[RangeNumber+1];
    else xi1=xi;
    switch (SplineNumber)
    { case 0: res=phi(t,x1i,xi,xi1); break;
      case 1: res=psi(t,x1i,xi,xi1); break;
      case 2: res=chi(t,x1i,xi,xi1); break;
    }
    return res;
  }

  public final double d1BSplineFree(double t, int I)
  { int RangeNumber, SplineNumber;
    double x1i,xi,xi1,res=0.0;
    RangeNumber=I/splinesPerNode;
    SplineNumber=I%splinesPerNode;
    xi=theGrid.mesh[RangeNumber];
    if (RangeNumber !=0) x1i=theGrid.mesh[RangeNumber-1];
    else x1i=xi;
    if (RangeNumber < theGrid.mesh.length-1) xi1=theGrid.mesh[RangeNumber+1];
    else xi1=xi;
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
    xi=theGrid.mesh[RangeNumber];
    if (RangeNumber !=0) x1i=theGrid.mesh[RangeNumber-1];
    else x1i=xi;
    if (RangeNumber < theGrid.mesh.length-1) xi1=theGrid.mesh[RangeNumber+1];
    else xi1=xi;
    switch (SplineNumber)
    { case 0: res=phi2(t,x1i,xi,xi1); break;
      case 1: res=psi2(t,x1i,xi,xi1); break;
      case 2: res=chi2(t,x1i,xi,xi1); break;
    }
    return res;
  }

    public final double d5BSplineFree(double t, int I)
  { int RangeNumber, SplineNumber;
    double x1i,xi,xi1,res=0.0;
    RangeNumber=I/splinesPerNode;
    SplineNumber=I%splinesPerNode;
    xi=theGrid.mesh[RangeNumber];
    if (RangeNumber !=0) x1i=theGrid.mesh[RangeNumber-1];
    else x1i=xi;
    if (RangeNumber < theGrid.mesh.length-1) xi1=theGrid.mesh[RangeNumber+1];
    else xi1=xi;
    switch (SplineNumber)
    { case 0: res=phi5(t,x1i,xi,xi1); break;
      case 1: res=psi5(t,x1i,xi,xi1); break;
      case 2: res=chi5(t,x1i,xi,xi1); break;
    }
    return res;
  }

  public int getNMaxFree()
  { //int res=(theGrid.mesh.length)*splinesPerNode-1;
    return nMaxFree;
  }

  protected double phi(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=1.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       {
         case 'C': res=-2*DL*DL*(DR-0.5*HK)/(HK*HK*HK);
        break;
	 case 'Q': res=DL*DL*DL*(6*DR*DR-3*DR*HK+HK*HK)/(HK*HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': res=2*(DL+0.5*HK)*DR*DR/(HK*HK*HK);
        break;
	 case 'Q': res=DR*DR*DR*(-6*DL*DL-3*DL*HK-HK*HK)/(HK*HK*HK*HK*HK);
        break;
       }
     }
    return res;
  }

  protected double psi(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': res=DL*DL*DR/(HK*HK);
        break;
	 case 'Q': res=DL*DL*DL*(DL-4*DR)*DR/(HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': res=DL*DR*DR/(HK*HK);
        break;
	 case 'Q': res=DR*DR*DR*(DR-4*DL)*DL/(HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double chi(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=DL*DL*DL*DR*DR*0.5/(HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=-DR*DR*DR*DL*DL*0.5/(HK*HK*HK);
        break;
       }
     }
    return res;

  }
  protected double phi1(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C':
           res=-2*(DL*(DR-0.5*HK)+DL*(DR-0.5*HK)+DL*DL)/(HK*HK*HK);
        break;
	 case 'Q':
	   res=(3*DL*DL*(6*DR*DR-3*DR*HK+HK*HK)+DL*DL*DL*(12*DR-3*HK))/(HK*HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C':
         res=2*(DR*DR+(DL+0.5*HK)*DR+(DL+0.5*HK)*DR)/(HK*HK*HK);
        break;
	 case 'Q':
	   res=(3*DR*DR*(-6*DL*DL-3*DL*HK-HK*HK)+DR*DR*DR*(-12*DL-3*HK))/(HK*HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double psi1(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=1.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C':
           res=(DL*DR+DL*DR+DL*DL)/(HK*HK);
        break;
	 case 'Q':
	   res=(3*DL*DL*(DL-4*DR)*DR-3*DL*DL*DL*DR+DL*DL*DL*(DL-4*DR))/(HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C':
           res=(DR*DR+DL*DR+DL*DR)/(HK*HK);
        break;
	 case 'Q':
	   res=(3*DR*DR*(DR-4*DL)*DL-3*DR*DR*DR*DL+DR*DR*DR*(DR-4*DL))/(HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double chi1(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=(3*DL*DL*DR*DR+2*DL*DL*DL*DR)*0.5/(HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C':  System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=-(3*DR*DR*DL*DL+2*DR*DR*DR*DL)*0.5/(HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double phi2(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C':
           res=-4*(DR-0.5*HK+2*DL)/(HK*HK*HK);
        break;
	 case 'Q':
	   res=(6*DL*(6*DR*DR-3*DR*HK+HK*HK)+
                6*DL*DL*(12*DR-3*HK)+
                DL*DL*DL*12)/(HK*HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C':
           res=4*(DL+0.5*HK+2*DR)/(HK*HK*HK);
        break;
	 case 'Q':
	   res=(6*DR*(-6*DL*DL-3*DL*HK-HK*HK)+
                6*DR*DR*(-12*DL-3*HK)+
                DR*DR*DR*(-12))/(HK*HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double psi2(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': res=2*(2*DL+DR)/(HK*HK);
        break;
	 case 'Q': res=-6*DL*DR*(4*DR+6*DL)/(HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': res=2*(DL+2*DR)/(HK*HK);
        break;
	 case 'Q': res=-6*DL*DR*(4*DL+6*DR)/(HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double chi2(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=1.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=DL*(DL*DL+3*DR*(DR+2*DL))/(HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=-DR*(DR*DR+3*DL*(DL+2*DR))/(HK*HK*HK);
        break;
       }
     }
    return res;
  }

   protected double phi5(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C':
           res=0;
        break;
	 case 'Q':
	   res=720.0/(HK*HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C':
           res=0;
        break;
	 case 'Q':
	   res=-720.0/(HK*HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double psi5(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=0.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': res=0;
        break;
	 case 'Q': res=-360.0/(HK*HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': res=0;
        break;
	 case 'Q': res=-360.0/(HK*HK*HK*HK);
        break;
       }
     }
    return res;

  }

  protected double chi5(double x, double x1i, double xi, double xi1)
  { double res=0.0, HK, DL, DR;
    if (x<x1i) res=0.0;
    else if (x>xi1) res=0.0;
    else if (x==xi ) res=1.0;
    else if (x<xi)
     { HK=xi-x1i;
       DL=x-x1i;
       DR=x-xi;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=60.0/(HK*HK*HK);
        break;
       }
      }
    else
     { HK=xi1-xi;
       DL=x-xi;
       DR=x-xi1;
       switch (splineType)
       { case 'C': System.out.println("Error in spline: Cubic Chi called");
               System.exit(-1);
        break;
	 case 'Q': res=-60.0/(HK*HK*HK);
        break;
       }
     }
    return res;
  }

  public char getSplineType()
  {
    return splineType;
  }

  public int getSplinesPerNode()
  {
    return splinesPerNode;
  }

  public SimpleGrid getGrid()
  {
    return theGrid;
  }
  //
  public SummationLimits getLimits(double x, SummationLimits limits)
  { int i=1;
    int iLeft, iRight;
    while (!(x>theGrid.mesh[i-1] & x<theGrid.mesh[i])) i++;
    iRight=getSplinesPerNode()*(i+1)-1;
    iLeft=Math.max(0,iRight-getSplinesPerNode()*2+1);
    iRight=Math.min(iRight,getNMaxFree());
    limits.LeftLimit=iLeft;
    limits.RightLimit=iRight;
    return limits;
  }
}
