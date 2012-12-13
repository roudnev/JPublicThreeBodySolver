package twoBodyOperators;
import hermitSplines.SplineFunction1D;
import basicAbstractions.AFunction;
import hermitSplines.HSplineBC;
import hermitSplines.CollocGrid;
public class TwoBodySolutionAnaliser
{ //
  // fields:
  // IterativeTwoBodyEquation eq;
  AFunction solution;
  SplineFunction1D splineSol;
  HSplineBC spline;
  CollocGrid cGrid;
  double k;
  double z;
  // constructors:
  public TwoBodySolutionAnaliser(IterativeTwoBodyEquation eq, AFunction solution)
  {
    this.solution=solution;
    spline=eq.getA().h0.splineSpace;
    z=eq.z;
    k=Math.sqrt(z);
    int dim=spline.getNMax()+1;
    cGrid=eq.getA().h0.splineColloc;
    splineSol=new SplineFunction1D(spline,dim,solution);
    splineSol=(SplineFunction1D)eq.getA().h0.solve(splineSol);
  }
  // methods:
  public void printAmplitude()
  {
    double a;
    double x;
    System.out.println("Amplitude results:");
    for (int i=0;i<cGrid.mesh.length;i++)
    { x=cGrid.mesh[i];
      a=splineSol.valueAt(x)/Math.cos(k*x);
      System.out.println("a("+x+")="+a);
    }
  }
}