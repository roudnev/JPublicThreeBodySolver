/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hermitSplines;

/**
 *
 * @author roudnev
 */
public abstract class AbstractHermitSpline implements Cloneable{
    protected int nMax;
    protected char splineType;
    protected int splinesPerNode;
    protected SimpleGrid theGrid;
    public double[][] overlapMatrix;

    public AbstractHermitSpline() {
    }
    public abstract void fillOverlapMatrix();

    protected final double chi(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 0.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    System.out.println("Error in spline: Cubic Chi called");
                    System.exit(-1);
                    break;
                case 'Q':
                    res = DL * DL * DL * DR * DR * 0.5 / (HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    System.out.println("Error in spline: Cubic Chi called");
                    System.exit(-1);
                    break;
                case 'Q':
                    res = -DR * DR * DR * DL * DL * 0.5 / (HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double chi1(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 0.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    System.out.println("Error in spline: Cubic Chi called");
                    System.exit(-1);
                    break;
                case 'Q':
                    res = (3 * DL * DL * DR * DR + 2 * DL * DL * DL * DR) * 0.5 / (HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    System.out.println("Error in spline: Cubic Chi called");
                    System.exit(-1);
                    break;
                case 'Q':
                    res = -(3 * DR * DR * DL * DL + 2 * DR * DR * DR * DL) * 0.5 / (HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double chi2(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 1.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    System.out.println("Error in spline: Cubic Chi called");
                    System.exit(-1);
                    break;
                case 'Q':
                    res = DL * (DL * DL + 3 * DR * (DR + 2 * DL)) / (HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    System.out.println("Error in spline: Cubic Chi called");
                    System.exit(-1);
                    break;
                case 'Q':
                    res = -DR * (DR * DR + 3 * DL * (DL + 2 * DR)) / (HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    public abstract double fBSpline(double t, int I);

    public final SimpleGrid getGrid() {
        return theGrid;
    }

    public SummationLimits getLimits(double x, SummationLimits limits) {
        int i = 1;
        int iLeft;
        int iRight;
        if (x < theGrid.rightmostPoint & x > theGrid.leftmostPoint) {
            while (!(x > theGrid.mesh[i - 1] & x < theGrid.mesh[i])) {
                i++;
            }
            iRight = getSplinesPerNode() * (i + 1) - 1;
            iLeft = Math.max(0, iRight - getSplinesPerNode() * 2 + 1);
            iRight = Math.min(iRight, getNMax());
            limits.LeftLimit = iLeft;
            limits.RightLimit = iRight;
        } else {
            limits.LeftLimit = 0;
            limits.RightLimit = 0;
        }
        return limits;
    }
    public abstract int getNMax();

    public char getSplineType() {
        return splineType;
    }

    public final int getSplinesPerNode() {
        return splinesPerNode;
    }

    protected final double phi(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 1.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    res = -2 * DL * DL * (DR - 0.5 * HK) / (HK * HK * HK);
                    break;
                case 'Q':
                    res = DL * DL * DL * (6 * DR * DR - 3 * DR * HK + HK * HK) / (HK * HK * HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    res = 2 * (DL + 0.5 * HK) * DR * DR / (HK * HK * HK);
                    break;
                case 'Q':
                    res = DR * DR * DR * (-6 * DL * DL - 3 * DL * HK - HK * HK) / (HK * HK * HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double phi1(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 0.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    res = -2 * (DL * (DR - 0.5 * HK) + DL * (DR - 0.5 * HK) + DL * DL) / (HK * HK * HK);
                    break;
                case 'Q':
                    res = (3 * DL * DL * (6 * DR * DR - 3 * DR * HK + HK * HK) + DL * DL * DL * (12 * DR - 3 * HK)) / (HK * HK * HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    res = 2 * (DR * DR + (DL + 0.5 * HK) * DR + (DL + 0.5 * HK) * DR) / (HK * HK * HK);
                    break;
                case 'Q':
                    res = (3 * DR * DR * (-6 * DL * DL - 3 * DL * HK - HK * HK) + DR * DR * DR * (-12 * DL - 3 * HK)) / (HK * HK * HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double phi2(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 0.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    res = -4 * (DR - 0.5 * HK + 2 * DL) / (HK * HK * HK);
                    break;
                case 'Q':
                    res = (6 * DL * (6 * DR * DR - 3 * DR * HK + HK * HK) + 6 * DL * DL * (12 * DR - 3 * HK) + DL * DL * DL * 12) / (HK * HK * HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    res = 4 * (DL + 0.5 * HK + 2 * DR) / (HK * HK * HK);
                    break;
                case 'Q':
                    res = (6 * DR * (-6 * DL * DL - 3 * DL * HK - HK * HK) + 6 * DR * DR * (-12 * DL - 3 * HK) + DR * DR * DR * (-12)) / (HK * HK * HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double psi(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 0.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    res = DL * DL * DR / (HK * HK);
                    break;
                case 'Q':
                    res = DL * DL * DL * (DL - 4 * DR) * DR / (HK * HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    res = DL * DR * DR / (HK * HK);
                    break;
                case 'Q':
                    res = DR * DR * DR * (DR - 4 * DL) * DL / (HK * HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double psi1(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 1.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    res = (DL * DR + DL * DR + DL * DL) / (HK * HK);
                    break;
                case 'Q':
                    res = (3 * DL * DL * (DL - 4 * DR) * DR - 3 * DL * DL * DL * DR + DL * DL * DL * (DL - 4 * DR)) / (HK * HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    res = (DR * DR + DL * DR + DL * DR) / (HK * HK);
                    break;
                case 'Q':
                    res = (3 * DR * DR * (DR - 4 * DL) * DL - 3 * DR * DR * DR * DL + DR * DR * DR * (DR - 4 * DL)) / (HK * HK * HK * HK);
                    break;
            }
        }
        return res;
    }

    protected final double psi2(double x, double x1i, double xi, double xi1) {
        double res = 0.0;
        double HK;
        double DL;
        double DR;
        if (x < x1i) {
            res = 0.0;
        } else if (x > xi1) {
            res = 0.0;
        } else if (x == xi) {
            res = 0.0;
        } else if (x < xi) {
            HK = xi - x1i;
            DL = x - x1i;
            DR = x - xi;
            switch (splineType) {
                case 'C':
                    res = 2 * (2 * DL + DR) / (HK * HK);
                    break;
                case 'Q':
                    res = -6 * DL * DR * (4 * DR + 6 * DL) / (HK * HK * HK * HK);
                    break;
            }
        } else {
            HK = xi1 - xi;
            DL = x - xi;
            DR = x - xi1;
            switch (splineType) {
                case 'C':
                    res = 2 * (DL + 2 * DR) / (HK * HK);
                    break;
                case 'Q':
                    res = -6 * DL * DR * (4 * DL + 6 * DR) / (HK * HK * HK * HK);
                    break;
            }
        }
        return res;
    }
  //
    @Override
  public Object clone()
  { try
    { AbstractHermitSpline res=(AbstractHermitSpline) super.clone();
      return res;
    }
    catch (CloneNotSupportedException e)
    { System.err.println(e.toString());
      throw new InternalError(e.toString());
    }
  }
  //
}
