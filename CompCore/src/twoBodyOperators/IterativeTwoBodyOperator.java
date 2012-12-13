package twoBodyOperators;
import basicAbstractions.AnOperator;
import basicAbstractions.AFunction;
import basicAbstractions.APotential;
import hermitSplines.SimpleGrid;
class IterativeTwoBodyOperator implements AnOperator
{ // fields:
  FreeTwoBodyHamiltonian h0;
  PotentialOperator v;
  double z;
  int l=0;
  int rank=0;
  //constructors:
  IterativeTwoBodyOperator(SimpleGrid theGrid, APotential vPot,double z)
  { this.z=z;
    h0=new FreeTwoBodyHamiltonian(theGrid,z,l);
    v=new PotentialOperator(theGrid,vPot);
    rank=v.getRank();
  }
  //methods:
  public AFunction times(AFunction u)
  {
    return u.plus(v.times(h0.solve(u)));
  }
  public int getRank()
  {
    return rank;
  }
  public double getZ()
  {
    return z;
  }
  //
  public PotentialOperator getV()
  {
    return v;
  }
}
