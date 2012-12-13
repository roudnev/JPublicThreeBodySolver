package linearEquationSolvers;
import basicAbstractions.*;
public class SimpleLinearEquationSolver implements ALinearEquationSolver
{ public AFunction calculateSolution(SimpleLinearEquation eq)
  {
    return eq.solve();
  }

  public AFunction calculateSolution(ALinearEquation eq)
  { System.err.println
    ("Only SimpleLinearEquation should be supplied to SimpleLinearEquationSolver!");
    // throw new ComplicatedEquationIsSubmittedException();
    return null;
  }

  public void setAccuracy(double ac)
  {
    ac=0.0;
  }

  class ComplicatedEquationIsSubmittedException extends Exception
  { ComplicatedEquationIsSubmittedException(){}
    ComplicatedEquationIsSubmittedException(String msg)
    { super(msg);
    }
  }
}
