package threeBodyOperators;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */
import hermitSplines.*;
public class MulticomponentDiscretization3D
{
////////////////
// Fields:
////////////////
  Discretization3D[] d3d;
  private int numberOfComponents;
  private int theComponentLength[];
  private  int dimension;
  public  int index[][][][];
  public  int indexX[];
  public  int indexY[];
  public  int indexZ[];
  public  int indexComp[];
////////////////
// Constructors:
////////////////

  public MulticomponentDiscretization3D() throws Exception
  {
    HSplineBC xSpline;
    HSplineBC ySpline;
    HSplineFree zSpline;
    MDDConfigReader configReader=new MDDConfigReader();
    numberOfComponents=configReader.readNumberOfComponents();
    theComponentLength=new int[numberOfComponents];
    d3d=new Discretization3D[numberOfComponents];
    dimension=0;
    for (int i=0;i<numberOfComponents; i++)
    { xSpline=configReader.readSplineConfigX();
      ySpline=configReader.readSplineConfigY();
      zSpline=configReader.readSplineConfigZ();
      d3d[i]=new Discretization3D(xSpline, ySpline, zSpline);
      theComponentLength[i]=d3d[i].getN();
      dimension+=d3d[i].getN();
//      ySpline.getGrid().DisplayGrid();
//      xSpline.getGrid().DisplayGrid();
    }
    configReader.close();
    indexX=new int[dimension];
    indexY=new int[dimension];
    indexZ=new int[dimension];
    indexComp=new int[dimension];
    index=new int[numberOfComponents][][][];

    int base=0;
    int indexTemp=0;
    for (int i=0;i<numberOfComponents; i++)
    {
      index[i]=new int[d3d[i].getNX()][d3d[i].getNY()][d3d[i].getNZ()];
      for (int iz=0;iz<d3d[i].getNZ(); iz++)
      for (int iy=0;iy<d3d[i].getNY(); iy++)
      for (int ix=0;ix<d3d[i].getNX(); ix++)
      {
        indexTemp=base+d3d[i].getIndex(ix,iy,iz);
        index[i][ix][iy][iz]=indexTemp;
        indexX[indexTemp]=ix;
        indexY[indexTemp]=iy;
        indexZ[indexTemp]=iz;
        indexComp[indexTemp]=i;

      }
      base+=d3d[i].getN();
    }
  }
////////////////
// Methods:
////////////////
  public final void reinitIndex()
  {
    dimension=0;
    for (int i=0;i<numberOfComponents; i++)
    {
      theComponentLength[i]=d3d[i].getN();
      dimension+=d3d[i].getN();
    }
    indexX=new int[dimension];
    indexY=new int[dimension];
    indexZ=new int[dimension];
    indexComp=new int[dimension];
    index=new int[numberOfComponents][][][];
    int base=0;
    int indexTemp=0;
    for (int i=0;i<numberOfComponents; i++)
    {
      index[i]=new int[d3d[i].getNX()][d3d[i].getNY()][d3d[i].getNZ()];
      for (int ix=0;ix<d3d[i].getNX(); ix++)
      for (int iy=0;iy<d3d[i].getNY(); iy++)
      for (int iz=0;iz<d3d[i].getNZ(); iz++)
      {
        indexTemp=base+d3d[i].getIndex(ix,iy,iz);
        index[i][ix][iy][iz]=indexTemp;
        indexX[indexTemp]=ix;
        indexY[indexTemp]=iy;
        indexZ[indexTemp]=iz;
        indexComp[indexTemp]=i;
      }
      base+=d3d[i].getN();
    }
  }
  //
  public final int getNumberOfComponents()
  { return numberOfComponents;
  }

  public final int getDimension()
  { return dimension;
  }
  public final void resetYSplines(double[] erel)
  { double[] bcr=new double[2];
    for (int ic=0; ic<numberOfComponents; ic++)
    {
      if (erel[ic]>=0)
      {
        double k=Math.sqrt(erel[ic]);
        double ky=k*d3d[ic].ySpline.getGrid().mesh[d3d[ic].ySpline.getGrid().mesh.length-1];
        double tky=k*Math.tan(ky);
        bcr[0]=-tky;
        bcr[1]=-erel[ic];
        d3d[ic].ySpline.setBoundaryConditionsTypeRight('3',bcr);
      }
      else
      {
//        double k=Math.sqrt(-erel[ic]);
///        bcr[0]=-k;
//        bcr[1]=-erel[ic];
//        d3d[ic].ySpline.setBoundaryConditionsTypeRight('3',bcr);
        d3d[ic].ySpline.setBoundaryConditionsTypeRight('D');
      }
    }
  }
////////////////////////////////////////////////////////////////////////////////
// internal classes:
////////////////////////////////////////////////////////////////////////////////

}