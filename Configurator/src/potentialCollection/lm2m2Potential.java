package potentialCollection;

import basicAbstractions.APotential;

public final class lm2m2Potential extends APotential
{ // potential LM2M2 distance a.u., value a.u.e.
  private static final double d=1.4088;
  private static final double eps=10.97;
  private static final double rm=2.9695;
  private static final double a=189635.35353;
  private static final double alf=10.70203539;
  private static final double bet=-1.90740649;
  private static final double x0=1.003535949;
  private static final double x1=1.454790369;
  private static double value;
  public static void main(String args[])
  {
    lm2m2Potential lm2m2=new lm2m2Potential();
    lm2m2.dumpPotential();
  }
//  public lm2m2Potential()
//  {
//    dumpPotential();
//  }
  public final  double valueAt(double r0)
  {
    double r=r0*0.5291772108;
    if (r==0.0) r=r+1.0e-7;
    double f,va;
    double dzt=r/rm;
    double dzt2=dzt*dzt;
    double dzt6=1.0/Math.pow(dzt,6);
    double dzt8=dzt6/(dzt2);
    double dzt10=dzt8/(dzt2);
    double c6r=1.34687065*dzt6;
    double c8r=0.41308398*dzt8;
    double c10r=0.17060159*dzt10;
    if (dzt<=d) f=Math.exp(-(d/dzt-1)*(d/dzt-1));
    else f=1.0;
    va=0.0;
    if ((x0<dzt) & (dzt<x1))
      va=0.0026*(-Math.cos((dzt-x0)*2*Math.PI/(x1-x0))+1);
    value=eps*(a*Math.exp(-alf*dzt+bet*dzt2)-(c6r+c8r+c10r)*f+va);
    value=value/3.1577464e5;
    double shortRangeCut=8.0e-2;
    if (value> shortRangeCut) value=shortRangeCut;
    return value;
  }

  private final void dumpPotential()
  { double[] rset=new double[]{4.0 ,4.8, 5.1, 5.6, 5.8, 6.0, 6.5, 8.0, 10.0};
    double[] tabulated=new double[]
      {293.1*(100.0-0.057)/100.0,
       13.52*(100.0-0.884)/100.0,
       -4.43*(100.0+0.965)/100.0,
       -10.94*(100.0+0.258)/100.0,
       -10.56*(100.0+0.285)/100.0,
       -9.64*(100.0+0.335)/100.0,
       -6.868*(100.0+0.078)/100.0,
       -2.062*(100.0+0.238)/100.0,
       -0.512*(100.0-0.065)/100.0
      };
    for (int i=0;i<rset.length;i++) System.out.println(rset[i]+"   "+tabulated[i]+"    "+(valueAt(rset[i])*3.1577464e5));
  }

  @Override
  public final String getDescription()
  { return "potential LM2M2(He) (J. Chem. Phys. 94, 8047 (1994))";
  }
}
/*
 DOUBLE PRECISION FUNCTION VLM2M2(R0)
!  IBM POTENTIAL LM2M2B (FULL)
      IMPLICIT Double Precision(A-H,O-Z)
      PI=4*DATAN(1.0D0)
      R=R0*0.5291772108d0
      IF (R0.EQ.0.0D0) R=R+1.0D-7
      D=1.4088D0
      EPS=10.97D0/3.1577464e5!/12.12D0
      RM=2.9695D0
      A=1.89635353D5
      ALF=10.70203539D0
      BET=-1.90740649D0
      DZT=R/RM
      DZT2=DZT*DZT
      DZT6=1.0D0/(DZT**6)
      DZT8=DZT6/(DZT2)
      DZT10=DZT8/(DZT2)
      C6R=1.34687065D0*DZT6
      C8R=0.41308398D0*DZT8
      C10R=0.17060159D0*DZT10
      IF (DZT.LE.D) THEN
        F=DEXP(-(D/DZT-1)*(D/DZT-1))
      ELSE
        F=1.0D0
      END IF
      X0=1.003535949D0
      X1=1.454790369D0
      IF (X0.LT.DZT.AND.DZT.LT.X1) THEN
        VA=0.0026D0*(-DCOS((DZT-X0)*2*PI/(X1-X0))+1.0D0)
      ELSE
        VA=0.0D0
      END IF
      RES=EPS*(A*DEXP(-ALF*DZT+BET*DZT2)-(C6R+C8R+C10R)*F+VA)
      if (RES.gt.1000.0d0) RES=1000.0d0
      VLM2M2=RES
      RETURN
      END FUNCTION VLM2M2
*/
