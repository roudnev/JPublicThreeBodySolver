package potentialCollection;

import basicAbstractions.APotential;
import java.io.*;

public class WeightedPotential extends APotential
{ public APotential vPot;
  private double scale;
  private double coupling;
  private double couplingModifier=1.0;
  public WeightedPotential(String potname, double scale, double coupling) throws Exception
  {
    this.vPot=(APotential)Class.forName("potentialCollection."+potname).newInstance();
    this.scale=scale;
    this.coupling=coupling;
    if (potname.equals("bargmanPotential"))
    {
      //this.scale=1.0;
      //this.coupling=1.0;
      this.vPot=new bargmanPotential(this.coupling,this.scale);
    }
    //if (potname.equalsIgnoreCase("mtVpotential"))
    if (this.vPot instanceof mtVPotential)
    { //this.scale=1.0;
      //this.coupling=1.0;
      System.out.print("Special case: scaling and coupling are reset to "+this.scale);
    }
    this.couplingModifier=readCouplingModifier();
  }
  private double readCouplingModifier()
  {
    double res=1.0;
    try
    {
      BufferedReader configReader=new BufferedReader(new FileReader("./CouplingConstant.conf"));
      String inps=configReader.readLine();
      res=Double.parseDouble(inps);
      configReader.close();
      if (res!=1.0)
        System.out.println("The coupling constant is modified by a factor of "+res);
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      res=1.0;
    }
    return res;
  }
  @Override
  public double valueAt(double arg)
  {
    return vPot.valueAt(arg/scale)*coupling*couplingModifier;
  }
  @Override
  public String getDescription()
  {
    return vPot.getDescription()+" scaled by "+getScale()+" with coupling "+getCoupling();
  }

  /**
   * @return the scale
   */
  public double getScale()
  {
    return scale;
  }

  /**
   * @return the coupling
   */
  public double getCoupling()
  {
    return coupling;
  }
}
