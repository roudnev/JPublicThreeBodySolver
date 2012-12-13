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
public class TT2003HeHePotential extends TT2003GenericPotential
{
  public static void main(String args[])
  {
    TT2003HeHePotential tt=new TT2003HeHePotential();
    tt.dumpPotential();
  }

  public TT2003HeHePotential()
  { super();
     this.c=new double[]  { 0.0e0,     0.0e0,     1.461e0,     14.11e0,     183.6e0   };
     this.a=41.96;
     this.b=2.523;
  }

  @Override
  public String getDescription()
  {
    return "Generic Rare Gas Potential, (He-He) // Tang, Toennies, J. Chem. Phys. 118, 4976 (2003)";
  }
//

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
