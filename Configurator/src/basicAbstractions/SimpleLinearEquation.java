package basicAbstractions;
public abstract class SimpleLinearEquation extends ALinearEquation implements Solvable
{ 
  public abstract AFunction solve();
  public abstract AFunction solve(AFunction rhs);
  
}
