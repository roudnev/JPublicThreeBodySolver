package potentialCollection;

import basicAbstractions.APotential;

public class saptPotential extends APotential
{ // potential SAPT, distance in a.u., Value in a.u.e.
   public static void main(String args[])
  {
    saptPotential sapt=new saptPotential();
    sapt.dumpPotential();
   }

  protected static final double[] fact=
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
  protected static final double[] c=
   { 0.0e0, 
     0.0e0,
     1.4609778e0,
     14.117855e0,
     183.69125e0, 
     3265.0e0, 
     76440.0e0, 
     2275000.0e0,0.0,0.0,0.0,0.0
   };
  protected static final double a=2.07436426e6; // (K)
  protected static final double alpha=1.88648251; // (bohr^-1)
  protected static final double beta=-6.20013490e-2; // (bohr^-2)
  protected static final double delta=1.94861295; // (bohr^-1)
  protected static final double d=7.449;
  
  public double valueAt(double r)
  { 
    double xau=r; ///0.529177;
    double vex=a*Math.exp((-alpha+beta*xau)*xau)/3.1577464e5; // K->a.u. conversion
    double dr=xau*delta;
    double vdisp=-brkExp(3,dr)*c[2]*fsapt(xau)/Math.pow(xau,6);
    double f2i=brkExp(3,dr);
    double f2r=f2i;
    for (int i=4;i<=12;i++)
    { 
      f2i=f2i-Math.exp(-dr)*Math.pow(dr,2*i-1)*(1.0+dr/(i*2.0))/fact[2*i-2];
      f2r=f2i;
      if (Math.abs(f2r)<1.0e-13) f2r=0.0;
      vdisp=vdisp-c[i-1]*f2r/Math.pow(xau,2*i);
    }
    double res=(vdisp+vex);
    double shortRangeCut=8.0e-2;
    if (res> shortRangeCut) res=shortRangeCut;
    return res;
  }
  public String getDescription()
  { return "He-He without dumping, Aziz, Slaman, J. Chem. Phys. 107, 914 (1997)";
  }
  protected double brkExp(int n, double br)
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
  
  protected double fsapt(double r)
  { return 1.0;
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

}
/*     
      REAL*8 FUNCTION TTY(X)
      IMPLICIT REAL*8(A-H,O-Z)
      DIMENSION C(12)    
      dimension fact(24)
      data fact//
      DATA 
      XAU=X/0.529177D0
c      xau=x
      BETA=1.3443d0
      D=7.449D0
      VEX=D*XAU**(3.5D0/BETA-1)*DEXP(-2*BETA*XAU)
      B=2*BETA-(3.5D0/BETA-1)/XAU
      VDISP=0.0D0
      BR=XAU*B
      F2I=BRKEXP(2,BR)
      DO I=3,12
        F2I=F2I-dexp(-br)*BR**(2*I-1)*(1.0D0+BR/(I*2.0D0))/FACT(2*I-1)
        VDISP=VDISP-C(I)*F2I/XAU**(2*I)
      END DO
      res=(VDISP+VEX)
     >      /3.1669d-6/12.12d0
      if (xau.lt.4.0d0) res=dabs(res)
      if (res.gt.1000.0d0) res=1000.0d0
      tty=res     
      RETURN
      END
c
      REAL*8 FUNCTION BRKEXP(N,BR)
      IMPLICIT REAL*8(A-H,O-Z)
      dimension fact(24)
      data fact/
     >     0.10000000000000000e+01,
     >     0.20000000000000000e+01,
     >     0.60000000000000000e+01,
     >     0.23999999999999999e+02,
     >     0.11999999999999999e+03,
     >     0.72000000000000011e+03,
     >     0.50400000000000018e+04,
     >     0.40320000000000022e+05,
     >     0.36288000000000047e+06,
     >     0.36288000000000084e+07,
     >     0.39916800000000030e+08,
     >     0.47900160000000101e+09,
     >     0.62270208000000105e+10,
     >     0.87178291200000168e+11,
     >     0.13076743680000029e+13,
     >     0.20922789888000055e+14,
     >     0.35568742809600069e+15,
     >     0.64023737057279940e+16,
     >     0.12164510040883208e+18,
     >     0.24329020081766400e+19,
     >     0.51090942171709424e+20,
     >     0.11240007277776115e+22,
     >     0.25852016738885062e+23,
     >     0.62044840173324357e+24/
      S=0.0d0
      DO I=1,2*N
        kk=2*N-I+1
        S=S+BR**KK*dexp(-br)/FACT(KK)
      END DO
      S=S+1.0d0*dexp(-br)
      brkexp=1.0d0-s
      RETURN
      END
*/
