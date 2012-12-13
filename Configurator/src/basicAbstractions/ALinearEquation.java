package basicAbstractions;
public abstract class ALinearEquation
{
  //fields:
  public AnOperator a;
  public AFunction rhs;
  public AFunction residue;
  double residueNorm;

  // constructors:
  public ALinearEquation(AnOperator a, AFunction rhs)
  {
    super();
    this.a=a;
    this.rhs=rhs;
  }

  public ALinearEquation()
  {
    super();
  }
  // methods:
  public final AFunction getResidue(AFunction approxSolution)
  {
    calculateResidue(approxSolution);
    return residue;
  }

  public final AFunction getResidue()
  {
    return residue;
  }

  public final void calculateResidue(AFunction approxSolution)
  {
 //  System.err.print("approx solution in ALinearEquation:");
  //  for (int i=0;i<approxSolution.coef.length;i++) System.err.print(" "+approxSolution.coef[i]);
    residue=a.times(approxSolution);
    residue=rhs.minus(residue);
    residueNorm=Math.sqrt(residue.norm2());
  }

  public final double getResidueNorm()
  {
    return residueNorm;
  }
  //
  protected abstract void rhsInit();
  public final void setRhs(AFunction u)
  { rhs=u;
  }
}
