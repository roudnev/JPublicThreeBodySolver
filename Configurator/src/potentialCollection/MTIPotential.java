package potentialCollection;

import basicAbstractions.APotential;

public class MTIPotential extends mtVPotential
{ // potential Manfliet Tjon Vb, distance in Fm, Value in Fm^(-2)
  private static double c1=-513.968;
  private static double c2=1438.720;
  private static double k1=1.55;
  private static double k2=3.11;
  private static double value;
  @Override
  public double valueAt(double arg)
  { value=(this.c1*java.lang.Math.exp(-this.k1*arg)+this.c2*java.lang.Math.exp(-this.k2*arg))/arg/41.47;
    return value;
  }
  @Override
  public String getDescription()
  { return "potential Manfliet Tjon I, singlet unbound";
  }
}
