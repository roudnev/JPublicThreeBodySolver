package twoBodyOperators;
import basicAbstractions.SimpleLinearEquation;
import basicAbstractions.Solvable;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import auxiliary.*;
public class SimpleTwoBodyEquation extends SimpleLinearEquation implements Solvable
{ // fields:
  public SimpleTwoBodyOperator a;
  //public DFunction rhs;
  // constructors:
  public SimpleTwoBodyEquation(basicAbstractions.APotential vpot)
  { a= new SimpleTwoBodyOperator(10,vpot);
    double[] tmp=new double[a.getRank()];
    for(int i=0;i<tmp.length;i++) tmp[i]=0.0;
    tmp[tmp.length-1]=1.0;
    rhs= new DFunction(tmp);
  }
  // methods:
  public AFunction solve()
  {
    return a.solve(rhs);
  }
  public AFunction solve(AFunction rh)
  {
    return a.solve(rh);
  }
  protected void rhsInit()
  {
  }
}
