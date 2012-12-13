package threeBodyOperators;

import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import configReader.ConfiguratorEssentials;
//import basicAbstractions.APotential;
//import basicAbstractions.ALinearEquationSolver;
//import basicAbstractions.ALinearEquation;
//import auxiliary.DFunction;
//import linearEquationSolvers.GMRESLinearEquationSolver;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 0.5
 */

public class FaddeevOperatorResolvent extends MulticomponentThreeBodyEquation implements AnOperator
{
// Fields:
  //ALinearEquationSolver solver;
  linearEquationSolvers.GMRESLinearEquationSolver solver;
// Constructors:
  public FaddeevOperatorResolvent(double erelau, ConfiguratorEssentials config) throws Exception
  { super(erelau,config);
    solver = new linearEquationSolvers.GMRESLinearEquationSolver();
    solver.setAccuracy(1.0e-6);
    solver.setIfVerbose(false);
  }
  //
// Methods:

  public AFunction times(AFunction u)
  {
    AFunction res=theA.h0.cover(theA.h0.solve(u));
    setRhs(theA.v.shrink(res));
    res=solver.calculateSolution(this);
    //res=theA.h0.solve(res);
    return res;
  }
  public int getRank()
  {
    //return d3D.getN();
    return rhs.coef.length;
  }
}
