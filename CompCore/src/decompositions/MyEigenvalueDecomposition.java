package decompositions;
import Jama.Matrix;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
public class MyEigenvalueDecomposition extends decompositions.NativeEigenvalueDecomposition implements AnOperator
{
  private double[] spectralRe;
  private double[] spectralIm;
  private double[] transformedEVRe;
  private double[] transformedEVIm;
  private int numberOfNegativeEigenvalues;
  public MyEigenvalueDecomposition(Matrix aMatrix)
  { 
    super(aMatrix);

    numberOfNegativeEigenvalues=0;
    for (int i=0;i<getEigenvaluesRe().length;i++)
    {
      if (getEigenvaluesRe()[i]<0.0) numberOfNegativeEigenvalues++;
    }
  }

  public void setSpectral(AFunction u) 
  { 
  }
    public AFunction times(AFunction u) {
        setSpectral(u);
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRank() {
        return rank;
    }

    public int getNumberOfNegativeEigenvalues()
    {
        return numberOfNegativeEigenvalues;
    }
}