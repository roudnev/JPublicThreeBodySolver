/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package potentialCollection;
import basicAbstractions.APotential;
/**
 *
 * @author roudnev
 */
public class TT2003GenericPotential extends APotential
{
  protected double[] c;

  protected double a;
  protected double b;
  
  public TT2003GenericPotential()
  { super();
    this.c=new double[]  { 0.0e0,     0.0e0,     0.0e0,     0.0e0,     0.0e0   };
    this.a=0.0;
    this.b=0.0;
  }
  private static final double[] fact=  // Factorials precalculated
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
  @Override
  public double valueAt(double r)
  { double xau=r;
    double vex=a*Math.exp(-b*xau);
    double vdisp=0.0;
    double br=xau*b;
    double f2i=brkExp(2,br);
    double f2ir=0.0;
    for (int i=3;i<=5;i++)
    {
      f2i=f2i-Math.exp(-br)*Math.pow(br,2*i-1)*(1.0+br/(i*2.0))/fact[2*i-2];
      f2ir=f2i;
      if (Math.abs(f2ir)<4.0e-14) f2ir=0.0;
      vdisp=vdisp-c[i-1]*f2ir/Math.pow(xau,2*i);
    }
    double res=(vdisp+vex);
    double shortRangeCut=1.0e-1;
    if (res>shortRangeCut) res=shortRangeCut;
    return res;
  }

  @Override
  public String getDescription()
  {
    return "Generic Rare Gas Potential, (to be extended for a specific system) // Tang, Toennies, J. Chem. Phys. 118, 4976 (2003)";
  }
//
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
