package twoBodyOperators;
import basicAbstractions.APotential;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import hermitSplines.*;
import auxiliary.SparceOperator;
import auxiliary.SparceRow;
class PotentialOperator extends SparceOperator implements AnOperator
{
//fields:
  APotential vPot;
  SimpleGrid vGrid;
  CollocGrid cGrid;
  HSplineBC spline;
//constructors:
  public PotentialOperator(SimpleGrid theGrid, APotential vPot)
  { double xi;
    this.vPot=vPot;
    vGrid=restrictGrid(theGrid);
    spline=new HSplineBC(vGrid,'Q','D','D');
    cGrid=new CollocGrid(spline);
    rank=cGrid.mesh.length; /*
    double[][] h=new double[rank][rank];
    for (int i=0;i<rank;i++)
    { xi=cGrid.mesh[i];
      for (int j=0;j<rank;j++)
        h[i][j]=spline.BSpline(xi,j)*vPot.valueAt(xi);
    } */
    super.init();
  }
//methods:
  public double[] getRow(int i)
  {
    double[] row=new double[rank];
    double xi=cGrid.mesh[i];
    for (int j=0;j<rank;j++) row[j]=0.0;
    for (int j=0;j<rank;j++)
      row[j]=spline.fBSpline(xi,j)*vPot.valueAt(xi);
    return row;
  }
  //
  public SparceRow getSparceRow(int i)
  { return new SparceRow();
  }

  //
  private SimpleGrid restrictGrid(SimpleGrid grid0)
  {
    double level=1.0e-10;
    // count points:
    int n1=0;
    for (int i=0;i<grid0.maxPointNumber;i++)
    { if (Math.abs(vPot.valueAt(grid0.mesh[i]))>level) n1++;
      //System.out.println("Vpot("+grid0.mesh[i]+")="+vPot.valueAt(grid0.mesh[i]));
    }
    double[] mesh1=new double[n1];
    for (int i=0;i<n1;i++) mesh1[i]=grid0.mesh[i];
    SimpleGrid res=new SimpleGrid(mesh1,grid0.getName()+vPot.getDescription());
    return res;
  }
  //
  public SimpleGrid getVGrid()
  { return vGrid;
  }
  //
  public CollocGrid getCGrid()
  { return cGrid;
  }
}