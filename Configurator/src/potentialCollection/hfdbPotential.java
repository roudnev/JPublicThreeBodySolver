package potentialCollection;

import basicAbstractions.APotential;

public class hfdbPotential extends APotential
{ // potential HFD-B(He), distance in a.u., Value in a.u.e.
  private static double d=1.4826;
  private static double eps=10.948/3.1577464e5;
  private static double rm=2.963;
  private static double a=184431.01;
  private static double alf=10.43329537;
  private static double bet=-2.27965105;
  private static double value;
  public double valueAt(double r0)
  { double r=r0*0.52917781908;
    double f;
    double dzt=r/rm;
    double dzt2=dzt*dzt;
    double dzt6=1.0/Math.pow(dzt,6);
    double dzt8=dzt6/(dzt2);
    double dzt10=dzt8/(dzt2);
    double c6r=1.36745214*dzt6;
    double c8r=0.42123807*dzt8;
    double c10r=0.17473318*dzt10;
    if (dzt<=d)
        f=Math.exp(-(d/dzt-1)*(d/dzt-1));
    else
        f=1.0;
    value=eps*(a*Math.exp(-alf*dzt+bet*dzt2)-(c6r+c8r+c10r)*f);
    double shortRangeCut=1.0e-1;
    if (value> shortRangeCut) value=shortRangeCut;
    return value;
  }
  public String getDescription()
  { return "potential HFD-B(He)";
  }
}
/*     FUNCTION VAZIZ(R)
      IMPLICIT REAL*8(A-H,O-Z)
      D=1.4826D0
      EPS=10.948D0/12.12D0
      RM=2.963D0
      A=184431.01D0
      ALF=10.43329537D0
      BET=-2.27965105
      DZT=R/RM
      DZT2=DZT*DZT
      DZT6=1.0D0/(DZT**6)
      DZT8=DZT6/(DZT2)
      DZT10=DZT8/(DZT2)
      C6R=1.36745214D0*DZT6
      C8R=0.42123807D0*DZT8
      C10R=0.17473318D0*DZT10
      IF (DZT.LE.D) THEN
        F=DEXP(-(D/DZT-1)*(D/DZT-1))
      ELSE
        F=1.0D0
      END IF
      IF (R.LT.1.0D0) THEN
        RES=EPS*A*0.1D0
      ELSE
        RES=EPS*(A*DEXP(-ALF*DZT+BET*DZT2)-(C6R+C8R+C10R)*F)
      END IF
      IF (RES.GT.800.0d0) RES=800.0d0
      VAZIZ=RES

      RETURN
      END
*/