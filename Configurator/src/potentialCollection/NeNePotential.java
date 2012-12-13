package potentialCollection;

import basicAbstractions.APotential;

public class NeNePotential extends APotential
{ // potential HFD-B(He), distance in a.u., Value in a.u.e.
  private static final double[] fact=
   {
          0.10000000000000000e+01,
          0.20000000000000000e+01,
          0.60000000000000000e+01,
          0.24000000000000000e+02,
          0.12000000000000000e+03,
          0.72000000000000000e+03,
          0.50400000000000000e+04,
          0.40320000000000000e+05,
          0.36288000000000000e+06,
          0.36288000000000000e+07,
          0.39916800000000000e+08,
          0.47900160000000000e+09,
          0.62270208000000000e+10,
          0.87178291200000000e+11,
          0.13076743680000000e+13,
          0.20922789888000000e+14,
          0.35568742809600000e+15,
          0.64023737057279940e+16,
          0.12164510040883208e+18,
          0.24329020081766400e+19,
          0.51090942171709424e+20,
          0.11240007277776115e+22,
          0.25852016738885062e+23,
          0.62044840173324357e+24
   };
  private static final double[] c=
   { 0.0e0,
     0.0e0,
     6.28174e0,
     90.0503e0,
     1679.45e0, 
     4.18967e4, 
     1.36298e6, 
     5.62906e7
   };

  private static final double a=88.5513e0;
  private static final double alpha=2.20626e0;
  private static final double beta=-0.0249851e0;
  private static final double b=1.85166e0;

  public double valueAt(double r)
  { 
    double xau=r;// /0.529177;
//    double vex=d*Math.pow(xau,3.5/beta-1.0)*Math.exp(-2*beta*xau);
    double vex=a*Math.exp(-(alpha-beta*xau)*xau);
    double vdisp=0.0;
    double br=xau*b;
    double f2i=brkExp(2,br);
    double f2ir=0.0;
    for (int i=3;i<=8;i++)
    { 
      f2i=f2i-Math.exp(-br)*Math.pow(br,2*i-1)*(1.0+br/(i*2.0))/fact[2*i-2];
      f2ir=f2i;
      if (Math.abs(f2i)<1.0e-14) f2ir=0.0;
      vdisp=vdisp-c[i-1]*f2ir/Math.pow(xau,2*i);
    }
    
    double res=(vdisp+vex);///3.1669e-6/12.11928e0;
    double shortRangeCut=1.0e0;
    if (res>shortRangeCut) res=shortRangeCut;
    return res;//*19.99244018e0/4.00260325e0;    // ! 20Ne
  }
  public String getDescription()
  { return "Ne-Ne Potential av5z+(3321) // Sławomir M. Cybulski and Rafał R. Toczyłowski J.Chem.Phys. 111, 10520 (1999)";
  }

  private double brkExp(int n, double br)
  {
    double s=0.0;
    int kk;
    for (int i=1;i<=2*n;i++)
    {
        kk=2*n-i+1;
        s+=Math.pow(br,kk)*Math.exp(-br)/fact[kk-1];
    }
    s+=Math.exp(-br);
    return 1.0-s;
  }
}
/*     
      REAL*8 FUNCTION ANeNe(X)
      IMPLICIT REAL*8(A-H,O-Z)
      DIMENSION C(8)    
      dimension fact(24)
      data fact/
     >     0.10000000000000000D+01,
     >     0.20000000000000000D+01,
     >     0.60000000000000000D+01,
     >     0.24000000000000000D+02,
     >     0.12000000000000000D+03,
     >     0.72000000000000011D+03,
     >     0.50400000000000018D+04,
     >     0.40320000000000022D+05,
     >     0.36288000000000047D+06,
     >     0.36288000000000084D+07,
     >     0.39916800000000030D+08,
     >     0.47900160000000101D+09,
     >     0.62270208000000105D+10,
     >     0.87178291200000168D+11,
     >     0.13076743680000029D+13,
     >     0.20922789888000055D+14,
     >     0.35568742809600069D+15,
     >     0.64023737057279940D+16,
     >     0.12164510040883208D+18,
     >     0.24329020081766400D+19,
     >     0.51090942171709424D+20,
     >     0.11240007277776115D+22,
     >     0.25852016738885062D+23,
     >     0.62044840173324357D+24/
      DATA C/0.0D0,0.0D0,
     >       6.28174D0,
     >       90.0503D0,
     >       1679.45D0, 
     >       4.18967D4, 
     >       1.36298D6, 
     >       5.62906D7/
      XAU=X/0.529177D0
c      xau=x
      A=88.5513D0
      ALPHA=2.20626D0
      BETA=-0.0249851D0
      B=1.85166D0
      VEX=A*DEXP(-(ALPHA-BETA*XAU)*XAU)
      VDISP=0.0D0
      BR=XAU*B
      F2I=BRKEXP(2,BR)
      DO I=3,8
        F2I=F2I-dexp(-br)*BR**(2*I-1)*(1.0D0+BR/(I*2.0D0))/FACT(2*I-1)
        VDISP=VDISP-C(I)*F2I/XAU**(2*I)
      END DO
      res=(VDISP+VEX)
     >      /3.1669d-6/12.1197d0
      if (xau.lt.4.0d0) res=dabs(res)
      if (res.gt.2000.0d0) res=2000.0d0
      ANeNe=res*19.99244018d0/4.00260325d0     ! 20Ne
      RETURN
      END
*/
