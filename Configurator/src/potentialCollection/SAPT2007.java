/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package potentialCollection;
import basicAbstractions.APotential;

/**
 * @author roudnev
 */

public class SAPT2007 extends APotential
{
    double aa=-1349832.70006;
    double bb=-10213793.3548;
    double cc=-11866035.0059;
    double dd=2183527.27389;
    double alpha=5.72036885319;
    double beta=2.80857770752;
    double b=2.41324077320;
    double[] c={0.0,0.0,0.0,0.0,0.0,
                1.460977837725,
                0.0,
                14.11785737,
                0.0,
                183.691075,
                -76.70,
                3372.0,
                -3806.0,
                85340.0,
                -171000.0,
                2860000.0
               };
    double ap= 5777488.65278;
    double bp=-1947169.38329;
    double cp=4.0;
    //double ap=-8.8599067+4*alpha-aa;
    double as=42.88;
    double bs=2.178;
    double cs=0.323;
    double ds=0.8074;

    private static final double[] fact=
   {      1.0,
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
    public double valueAt(double x)
    {   // x, a.u. ; res, a.u
        double r=Math.max(x,0.8);
        double res=0.0;
        double resSR=(aa+bb*r+cc*r*r+cp/r)*Math.exp(-alpha*r)+
                     (ap+bp*r+dd*r*r)*Math.exp(-beta*r);
        double resLR=0.0;
        double br=b*r;
        for(int i=0;i<16;i++)
        {
            resLR += brkExp(i+2, br)*c[i]/Math.pow(r,i+1);
        }
        res=resSR/3.1577464e5-resLR;
        double shortRangeCut=8.0e-2;
        if (res>shortRangeCut) res=shortRangeCut;
        return res;
    }

    @Override
    public String getDescription()
    {
        return "W. Cencek et al., J.Phys.Chem, 111, 11311-11319, (2007)";
    }


  private double brkExp(int n, double br)
  {
    double s=0.0;
    for (int i=n;i>0;i--)
    {
        s+=Math.pow(br,i)/fact[i];
    }
    s=Math.exp(-br)*(1+s);
    return 1.0-s;
  }
  public static void main(String[] agrv)
  {
      SAPT2007 pot=new SAPT2007();
      double[] rr={0.001,
                   0.01,
                   0.1,
                   0.2,0.4,0.8,1.0,
                   3.0,
                   3.5,
                   4.0,
                   4.5,
                   5.0,
                   5.3,
                   5.6,
                   6.0,
                   6.5,
                   7.0,
                   8.0,
                   9.0,
                   10.0,
                   12.0
                   };
      System.out.println("c.length="+pot.c.length);
      for(int i=0;i<rr.length;i++)
      {
          System.out.println(rr[i]+"  "+pot.valueAt(rr[i])*3.1577464e5);
      }
  }

}
