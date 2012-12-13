package twoBodyOperators;
import basicAbstractions.*;
public abstract class SimpleTwoBodyLinearEquation extends SimpleLinearEquation
{ SimpleTwoBodyOperator a;
  AFunction rhs;
  public AFunction solve()
  { return a.solve(rhs);
  }

}
