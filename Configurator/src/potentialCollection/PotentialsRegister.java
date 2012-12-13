/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package potentialCollection;

/**
 *
 * @author roudnev
 */
public class PotentialsRegister {
    private static String[] potentials=
      {"",
       "ttyPotential",
       "mtVPotential",
       "bargmanPotential",
       "lm2m2Potential",
       "hfdbPotential",
       "hfdhe2Potential",
       "hfdb3fc11Potential",
       "sapt1Potentilal",
       "sapt2Potential",
       "TT2003HeHePotential",
       "NeNePotential",
       "TT2003NeNePotential",
       "TT2003HeNePotential",
       "SAPT2007",
       "LennardJonesPotential",
       "MTIPotential",
       "MTIIIPotential"
    };
public static void main(String[] args)
{
  PotentialsRegister pr=new PotentialsRegister();
}
  public PotentialsRegister()
  { 
    check();
  }
public void check()
{   ttyPotential tty=new ttyPotential();
    mtVPotential mtv=new mtVPotential();
    bargmanPotential bargm=new bargmanPotential();
    lm2m2Potential lm2m2p=new lm2m2Potential();
    hfdbPotential hfdb=new hfdbPotential();
    hfdb3fc11Potential hfdb3fc11=new hfdb3fc11Potential();
}

    /**
     * @return the potentials
     */
    public static String[] getPotentials() {
        return potentials;
    }
    public int getIndexForName(String pName)
    {
        int ind=0;
        for (int i=0;i<potentials.length;i++)
        {
            if (pName.matches(potentials[i])) ind=i;
        }
        return ind;
    }

}
