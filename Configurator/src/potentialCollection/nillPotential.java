package potentialCollection;

import basicAbstractions.APotential;

public class nillPotential extends APotential
{ public double valueAt(double arg)
  {
    return 0.0;
  }
  public String getDescription()
  {
    return "Nill potential";
  }
}
