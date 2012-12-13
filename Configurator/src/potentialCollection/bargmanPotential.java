package potentialCollection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.NumberFormat;
import basicAbstractions.APotential;
import java.io.*;
public class bargmanPotential extends APotential
{ double beta, gm, b, mu;
  private double a0,r0,a;
  private double e0;
  private double coupling,scaling;
  private String description;
  public bargmanPotential()
  { readConfig();
    a=-Math.sqrt(-e0);
    gm=-a;
    b=2.0/r0-gm;
    beta=(b+gm)/(b-gm);
    this.coupling=1.0;
    this.scaling=1.0;

//    System.out.println("Bargman potential parameters:"+gm+"  "+b+"  "+beta);
  }
  public bargmanPotential(double coupling, double scaling)
  { readConfig();
    b=(Math.sqrt(a0*a0-2*a0*r0)+a0)/(a0*r0*scaling);
    gm=-(Math.sqrt(a0*a0-2*a0*r0)-a0)/(a0*r0*scaling);
    a=-gm;
//    gm=Math.sqrt(-e0*coupling);
//    b=(2.0/r0/scaling-gm);
    beta=((b-a)/(b+a));
    this.coupling=coupling;
    this.scaling=scaling;
    double lambda=(2.0/mu-1.0)*8*gm*(b+gm)/(b-gm);
    description="Bargman potential (K.Chadan and P.C.Sabatier, Inverse Problems in Quantum Scattering Theory),\n"
            +
            "Bound state energy="+(-gm*gm)/coupling
            +
            "\n,Scat.Length="+((b+gm)/b/gm)/scaling
            +
            "\nEff.Range="+(2.0/(b+gm)/scaling)
            +
           "\nAsympt.Normalizing.Constant="+(2.0*Math.sqrt((b+gm)/(b-gm)/mu))+"\n"
           +
           "Parameters   b="+b+"   beta="+beta+"  gamma="+gm+"\n";
    //System.err.println("   b="+b+"   beta="+beta+"  gamma="+gm+"  mu="+mu+"   lambda="+lambda);
    //dump();
  }

  private void readConfig()
  { 
//    mu=2.01;
    try
    {
    BufferedReader configReader=new BufferedReader(new	 FileReader("./Bargman.conf"));
    String inps=configReader.readLine();
    inps=configReader.readLine();
    a0=Double.parseDouble(inps);
    inps=configReader.readLine();
    inps=configReader.readLine();
    r0=Double.parseDouble(inps);
    configReader.readLine();
    mu=Double.parseDouble(configReader.readLine());
    configReader.close();
    if (a0<2*r0)
      {
        System.err.println("Error in configuration file Bargman.conf: a0 should be > 2*r0, exiting.");
        System.exit(1);
      }
    }
    catch (Exception e)
    { System.err.println("something wrong reading ./Bargman.conf, resetting ./Bargman.conf");
      a0=375.0;
      r0=4.0/0.529177;
      mu=2.0;
      try
      {
        PrintStream out = new PrintStream(new FileOutputStream("./Bargman.conf"));
        out.println("Scattering length (a.u.)");
        out.println(a0);
        out.println("Effective Range (a.u.)");
        out.println(r0);
        out.println("Normalizing constant");
        out.println(mu);
      }
      catch (FileNotFoundException ex)
      {
        Logger.getLogger(bargmanPotential.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
@Override
  public double valueAt(double x)
  { double r=x*this.scaling;
    double res=0.0;
// Special case:
//    res=-8*b*b*beta*Math.exp(-2*b*r)/Math.pow((1+beta*Math.exp(-2*b*r)),2);

//    res=-4*gm*(b*Math.cosh(b*r)*g(mu,gm,r)
//              +Math.sinh(b*r)*(dg(mu,gm,r)
//              -g(mu,gm,r)*(dg(mu,gm+b,r)-dg(mu,gm-b,r))/(g(mu,gm+b,r)-g(mu,gm-b,r))))/(g(mu,gm+b,r)-g(mu,gm-b,r));

//    res=-4*gm*(b*Math.cosh(b*r)*g(mu,gm,r)
//              +Math.sinh(b*r)*(dg(mu,gm,r)
//              -g(mu,gm,r)*(dg(mu,gm+b,r)-dg(mu,gm-b,r))/(g(mu,gm+b,r)-g(mu,gm-b,r))))/(g(mu,gm+b,r)-g(mu,gm-b,r));


        double t2 = b * b;
        double t3 = t2 * gm;
        double t6 = mexp(0.2e1 * b * r);
        double t7 = mu * mu;
        double t13 = mexp(0.2e1 * (gm + b) * r);
        double t17 = 0.2e1 * gm;
        double t21 = mexp(0.2e1 * r * (b + t17));
        double t24 = b * t7;
        double t25 = gm * gm;
        double t28 = mexp(0.2e1 * gm * r);
        double t34 = 0.2e1 * b;
        double t38 = mexp(0.2e1 * r * (gm + t34));
        double t39 = b * t38;
        double t45 = t13 * mu;
        double t46 = t25 * b;
        double t51 = mu * t28;
        double t54 = gm * t6;
        double t58 = t2 * b;
        double t61 = -t3 * t6 * t7 + 0.2e1 * t3 * t13 * t7 - t3 * t21 * t7 + t24 * t25 * t28 - 0.2e1 * t24 * t25 * t13 + t39 * t7 * t25 - 0.2e1 * t39 * mu * t25 + 0.4e1 * t45 * t46 - 0.4e1 * t45 * t3 - 0.2e1 * t51 * t46 + 0.4e1 * t54 * mu * t2 - 0.4e1 * t6 * t58;
        double t62 = t25 * gm;
        double t69 = t6 * mu;
        double t72 = t7 * t62;
        double t81 = t58 * t7;
        double t89 = 0.4e1 * t45 * t62 - 0.4e1 * t45 * t58 - 0.2e1 * t51 * t62 + 0.4e1 * t69 * t58 + t72 * t28 - 0.2e1 * t72 * t13 + t38 * t7 * t62 - 0.2e1 * t38 * mu * t62 - t81 * t6 + 0.2e1 * t81 * t13 - t58 * t21 * t7 - 0.4e1 * t54 * t2;
        double t103 = -t17 + t34 - t45 * gm + t45 * b + mu * gm - mu * b + 0.2e1 * t54 + 0.2e1 * t6 * b + t51 * gm + t51 * b - t54 * mu - t69 * b;
        double t104 = t103 * t103;
        res = 0.8e1 * (-gm + b) * (t61 + t89) / t104;
        // System.err.println(r+"  "+res+"  "+t61+"  "+t89+"  "+t104);
    //}
//  
    return res/this.coupling;
  }
private double valueAtTst(double x)
  { double r=x*this.scaling;
    double res=0.0;
    res=-8*b*b*beta*Math.exp(-2*b*r)/Math.pow((1+beta*Math.exp(-2*b*r)),2);
    return res/this.coupling;
  }
    @Override
  public String getDescription()
  {
    return description;
  }
  
  private double sinh(double x)
  {double res=0.0;
//   if (Math.abs(x)<30.0)
         res=(mexp(x)-mexp(-x))/2.0;
//   else
//         res=(Math.exp(30.0*Math.signum(x))-Math.exp(-30.0*Math.signum(x)))/2.0;
   return res;
  }

  private double cosh(double x)
  { double res=0.0;
//    if (Math.abs(x)<30.0)
      res=(mexp(x)+mexp(-x))/2.0;
//    else
//      res=(mexp(30.0)+mexp(-30.0))/2.0;
    return res;
  }
  private double mexp(double x)
  {
    double res=0.0;
    if (Math.abs(x)<400.0)
      res=Math.exp(x);
    else if (x>0)
      res=Math.exp(400.0);
    return res;
  }
  private double dg(double mu, double x, double r)
  { return (-mexp(-x*r)+mu*cosh(x*r));
  }
  private double g(double mu, double x, double r)
  { return (mexp(-x*r)+mu*sinh(x*r))/x;
  }
  public void dump()
  {double dx=0.5;
   double a1,a2;
   for (int i=2;i<2500;i++ )
   {   
       a1=Math.log(Math.abs(valueAt(i*dx)/valueAt((i-1)*dx)))/dx;
       a2=Math.log(Math.abs(valueAtTst(i*dx)/valueAtTst((i-1)*dx)))/dx;
       System.out.println(i*dx+"  "+valueAt(i*dx)+"  "+valueAtTst(i*dx)+"  "+a1+"  "+a2);
   }

  }

  public void dump(double[] points)
  {
   for (int i=0;i<points.length;i++ )
   {
       System.out.println(points[i]+"  "+valueAt(points[i]));
   }

  }
}
