package potentialCollection;

import basicAbstractions.APotential;

public class hfdhe2Potential extends APotential
{ // potential HFDHe2, distance in a.u., Value in a.u.e.
  private static double d=1.241314;
  private static double eps=10.8/3.1577464e5;
  private static double rm=2.9673;
  private static double a=544850.4;
  private static double alf=13.353384;
  private static double bet=0.0;
  private static double value;
  public double valueAt(double r0)
  {
    double r=r0*0.52917781908;
    double f;
    double dzt=r/rm;
    double dzt2=dzt*dzt;
    double dzt6=1.0/Math.pow(dzt,6);
    double dzt8=dzt6/(dzt2);
    double dzt10=dzt8/(dzt2);
    double c6r=1.3732412*dzt6;
    double c8r=0.4253785*dzt8;
    double c10r=0.178100*dzt10;
    if (dzt<=d) f=Math.exp(-(d/dzt-1)*(d/dzt-1));
    else f=1.0;
    value=eps*(a*Math.exp(-alf*dzt+bet*dzt2)-(c6r+c8r+c10r)*f);
    if (value>1.0) value=1.0;
    return value;
  }
  public String getDescription()
  { return "Aziz et al., 1979 He-He potential HFDHE2";
  }
}
