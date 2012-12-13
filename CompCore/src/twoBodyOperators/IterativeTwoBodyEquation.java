package twoBodyOperators;
import basicAbstractions.SimpleLinearEquation;
import basicAbstractions.Solvable;
import basicAbstractions.AFunction;
import basicAbstractions.ALinearEquation;
import auxiliary.*;
import hermitSplines.SimpleGrid;
import hermitSplines.HSplineBC;
import basicAbstractions.APotential;
public class IterativeTwoBodyEquation extends ALinearEquation
{ // fields:
  double z;
  APotential vPot;
  AFunction solution;
  IterativeTwoBodyOperator theA;
  int innerRank;
  int outerRank;
  // constructors:
  public IterativeTwoBodyEquation(APotential vpot, double z)
  { this.vPot=vpot;
    double xi;
    this.z=z;
    a= new IterativeTwoBodyOperator(
            new SimpleGrid(60,"../y_2500.grd"),
            vpot,z);
    theA=(IterativeTwoBodyOperator)a;
    innerRank=theA.v.getRank();
    outerRank=theA.h0.getRank();
    double[] tmp=new double[a.getRank()];
    double k=Math.sqrt(z);
    for(int i=0;i<tmp.length;i++)
    { xi=theA.getV().getCGrid().mesh[i];
      tmp[i]=-Math.sin(k*xi)*vpot.valueAt(xi)/k;
    }
    rhs= new DFunction(tmp);
    System.err.println("dimension="+rhs.coef.length);
  }
  //
  public IterativeTwoBodyEquation(SimpleGrid grid, APotential vpot,double z)
  { a= new IterativeTwoBodyOperator(grid,vpot,z);
    this.vPot=vpot;
    this.z=z;
    rhsInit();
  }
  // methods:
  public AFunction getSolution(AFunction sol)
  { solution=theA.h0.solve(sol);
    return solution;
  }
  //
  public IterativeTwoBodyOperator getA()
  {
    return theA;
  }
  //
  public void rhsInit()
  { double xi;
    double[] tmp=new double[a.getRank()];
    double k=Math.sqrt(z);
    for(int i=0;i<tmp.length;i++)
    { xi=theA.getV().getCGrid().mesh[i];
      tmp[i]=-Math.sin(k*xi)*vPot.valueAt(xi)/k;
    }
    rhs= new DFunction(tmp);
  }
}
