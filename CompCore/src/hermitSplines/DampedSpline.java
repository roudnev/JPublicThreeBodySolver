package hermitSplines;

/**
 * <p>Title: Coulomb 3B</p>
 * <p>Description: Quantum Coulomb 3b with 2 identical particles</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Vladimir A. Roudnev
 * @version 1.0
 */

public class DampedSpline extends HSplineBC
{
  private double k=0.0;
  private boolean ifCos=true;

  public DampedSpline(double erel, HSplineBC spl)
  { super(spl.getGrid(), spl.splineType, 'D', 'N');
    if (erel>=0.0)
    { k=Math.sqrt(erel);
      ifCos=true;
    }
    else
    { k=Math.sqrt(-erel);
      ifCos=false;
      super.setBoundaryConditionsTypeRight('D');
    }

  }

  public final double fBSpline(double t, int i)
  { return super.fBSpline(t, i)*fDampingFunction(t);
  }

  public final double d1BSpline(double t, int i)
  { return super.d1BSpline(t, i)*fDampingFunction(t)+super.fBSpline(t,i)*d1DampingFunction(t);
  }

  public final double d2BSpline(double t, int i)
  { return super.d2BSpline(t, i)*fDampingFunction(t)
           +2*super.d1BSpline(t,i)*d1DampingFunction(t)
           +  super.fBSpline(t,i)*d2DampingFunction(t);
  }

  private final double fDampingFunction(double x)
  { double res=1.0;
    if (ifCos)
      res=Math.cos(k*x);
//    else
//      res=Math.exp(-k*x);
    return res;
  }
  private final double d1DampingFunction(double x)
  { double res=0.0;
    if (ifCos)
        res=-k*Math.sin(k*x);
//      else
//        res=-k*Math.exp(-k*x);
    return res;
  }
  private final double d2DampingFunction(double x)
  { double res=0.0;
    if (ifCos)
      res=-k*k*Math.cos(k*x);
//    else
//      res=k*k*Math.exp(-k*x);
    return res;
  }
}