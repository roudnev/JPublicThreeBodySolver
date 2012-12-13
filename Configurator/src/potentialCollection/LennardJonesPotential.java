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
public class LennardJonesPotential extends APotential
{ public static double r0=1.0; // equilibrium distance
  public static double muinv=0.01418; // half-neon inverse mass parameter
  @Override
  public double valueAt(double r)
  {
    double res=Math.pow(r0/r,6);
    res=(res*res-2*res);///muinv;
    if (res>800.0) res=800.0;
    return res;
  }

    @Override
    public String getDescription()
    {
      return "Lennard-Jones scaled model (1/r^12-2/r^6)";
    }

    public static void main(String[] args)
    {
        LennardJonesPotential v=new LennardJonesPotential();
        for (int i=1; i<200; i++)
            System.out.println(i*0.01+"  "+v.valueAt(i*0.01));
    }
}
