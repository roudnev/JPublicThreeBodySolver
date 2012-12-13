package twoBodyOperators;
import basicAbstractions.SimpleLinearEquation;
import basicAbstractions.Solvable;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import basicAbstractions.ALinearEquation;
import auxiliary.*;
public class TwoBodyEquation extends ALinearEquation
{ // fields:
  //public SimpleTwoBodyOperator a;
  //public AFunction rhs;
  // constructors:
  public TwoBodyEquation(basicAbstractions.APotential vpot)
  { a= new SimpleTwoBodyOperator(10,vpot);
    double[] tmp=new double[a.getRank()];
    for(int i=0;i<tmp.length;i++) tmp[i]=0.0;
    tmp[tmp.length-1]=1.0;
    rhs= new DFunction(tmp);
  }
  // methods:
  protected void rhsInit()
  {
  }
}
