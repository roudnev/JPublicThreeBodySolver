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
public class TT2003NeNePotential extends TT2003GenericPotential
{
  public static void main(String args[])
  {
    TT2003NeNePotential tt=new TT2003NeNePotential();
    tt.dumpPotential();
  }
  public TT2003NeNePotential()
  { super();
    this.c= new double[]   { 0.0e0,     0.0e0,     6.383e0,     90.34e0,     1536.0e0   };
    this.a=199.5;
    this.b=2.458;
}
  @Override
  public String getDescription()
  {
    return "Generic Rare Gas Potential, (Ne-Ne) // Tang, Toennies, J. Chem. Phys. 118, 4976 (2003)";
  }
    private final void dumpPotential()
  { double[] rset=new double[]{4.0 ,4.8, 5.1, 5.6, 5.8, 6.0, 6.5, 8.0, 10.0};
    NeNePotential neAz=new NeNePotential();
    for (int i=0;i<rset.length;i++) System.out.println(rset[i]+"   "+neAz.valueAt(rset[i])*3.1577464e5+"    "+(valueAt(rset[i])*3.1577464e5));
  }
}
