package potentialCollection;

import basicAbstractions.APotential;

public class sech2Potential extends APotential
{ // potential Manfliet Tjon Vb, distance in Fm, Value in Fm^(-2)
  private static double d=-1.31e-6;
  private static double r0=15.0;
  private static double res;
  public final double valueAt(double arg)
  { res=arg/r0;
    res=0.5*(Math.exp(res)+Math.exp(-res));
    res=d/(res*res);
    return res;
  }
  public final String getDescription()
  { return "potential D*sech^2(r/r0)";
  }
}
