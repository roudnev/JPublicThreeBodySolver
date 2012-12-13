package hermitSplines;

/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 0.5
 */

public class SummationLimits
{ //fields:
  public int LeftLimit;
  public int RightLimit;
  public SummationLimits(int l, int r)
  { LeftLimit=l;
    RightLimit=r;
  }
}