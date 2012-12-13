package potentialCollection;

import basicAbstractions.APotential;

public class hfdb3fc11Potential extends APotential
{ // potential HFD-B(He), distance in a.u., Value in a.u.e.
  private static double d=1.438;
  private static double eps=10.956/3.1577464e5; //*3.1669e-6;//;
  private static double rm=2.9683;
  private static double a=186924.404;
  private static double alf=10.5717543;
  private static double bet=-2.07758779;
  private static double value;
  public final double valueAt(double r0)
  {
    double r=r0*0.529177249;//<- given by the authors  | current NIST recommended-> *0.52921778108;
    double f;
    double dzt=r/rm;
    double dzt2=dzt*dzt;
    double dzt6=1.0/Math.pow(dzt,6);
    double dzt8=dzt6/(dzt2);
    double dzt10=dzt8/(dzt2);
    double c6r=1.35186623*dzt6;
    double c8r=0.41495143*dzt8;
    double c10r=0.17151143*dzt10;
    if (dzt<=d) f=Math.exp(-(d/dzt-1)*(d/dzt-1));
    else f=1.0;
    value=eps*(a*Math.exp(-alf*dzt+bet*dzt2)-(c6r+c8r+c10r)*f);
    double shortRangeCut=7.0e-2;
    if (value> shortRangeCut) value=shortRangeCut;
    return value;
  }
  public String getDescription()
  { return "potential HFD-B3-FCI1(He) // Aziz et al, PRL 74, 1586 (1995)";
  }
}
/*    c Potential definition
      FUNCTION Vhfdb3fc11(R)
      IMPLICIT REAL*8(A-H,O-Z)
      D=1.438D0
      EPS=10.956D0/12.12D0
      RM=2.9683D0
      A=1.86924404D5
      ALF=10.5717543D0
      BET=-2.07758779d0
      DZT=R/RM
      DZT2=DZT*DZT
      DZT6=1.0D0/(DZT**6)
      DZT8=DZT6/(DZT2)
      DZT10=DZT8/(DZT2)
      C6R=1.35186623D0*DZT6
      C8R=0.41495143D0*DZT8
      C10R=0.17151143D0*DZT10
      IF (DZT.LE.D) THEN
        F=DEXP(-(D/DZT-1)*(D/DZT-1))
      ELSE
        F=1.0D0
      END IF
      res=EPS*(A*DEXP(-ALF*DZT+BET*DZT2)-(C6R+C8R+C10R)*F)
      IF (RES.GT.1000.0d0) RES=1000.0d0
      Vhfdb3fc11=RES
      RETURN
      END
*/