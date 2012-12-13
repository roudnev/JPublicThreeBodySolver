package threeBodyOperators;

/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 1.0
 */

import hermitSplines.HSplineBC;
import hermitSplines.HSplineFree;
import hermitSplines.CollocGrid;
import hermitSplines.CollocGridFreeBC;
import hermitSplines.SimpleGrid;
public class Discretization3D
{
// fields:
  HSplineBC xSpline;
  HSplineBC ySpline;
  HSplineFree zSpline;
  CollocGrid xCGrid;
  CollocGrid yCGrid;
  CollocGridFreeBC zCGrid;
  int nX, nY, nZ, n;
  int[][][] index3;
  int[] indexX;
  int[] indexY;
  int[] indexZ;
// constructors:
  public Discretization3D(HSplineBC xSpline, HSplineBC ySpline,HSplineFree zSpline)
  {
    this.xSpline=xSpline;
    this.ySpline=ySpline;
    this.zSpline=zSpline;
    xCGrid=new CollocGrid(xSpline);
    yCGrid=new CollocGrid(ySpline);
    zCGrid=new CollocGridFreeBC(zSpline);
    nX=xCGrid.mesh.length;
    nY=yCGrid.mesh.length;
    nZ=zCGrid.mesh.length;
    n=nX*nY*nZ;
    indexX=new int[n];
    indexY=new int[n];
    indexZ=new int[n];
    index3=new int[nX][nY][nZ];
    int k;
    for(int iz=0;iz<nZ;iz++)
    for(int iy=0;iy<nY;iy++)
    for(int ix=0;ix<nX;ix++)
    {
      k=(iz*nY+iy)*nX+ix;
//      System.out.println("kxyz: "+k+" "+ix+" "+iy+" "+iz);
      index3[ix][iy][iz]=k;
      indexX[k]=ix;
      indexY[k]=iy;
      indexZ[k]=iz;
    }
  }
// methods:
  public final int getIndexX(int i)
  { return indexX[i];
  }
  public final int getIndexY(int i)
  { return indexY[i];
  }
  public final int getIndexZ(int i)
  { return indexZ[i];
  }
  public final int getIndex(int i, int j, int k)
  { return index3[i][j][k];
  }
  public final int getNX()
  { return nX;
  }
  public final int getNY()
  { return nY;
  }
  public final int getNZ()
  { return nZ;
  }
  public final int getN()
  { return n;
  }
  public final HSplineBC getXSpline()
  { return xSpline;
  }
  public final HSplineBC getYSpline()
  { return ySpline;
  }
  public final HSplineFree getZSpline()
  { return zSpline;
  }
  public final CollocGrid getXCollocGrid()
  { return xCGrid;
  }
  public final SimpleGrid getXBaseGrid()
  { return xSpline.getGrid();
  }
  public final CollocGrid getYCollocGrid()
  { return yCGrid;
  }
  public final SimpleGrid getYBaseGrid()
  { return ySpline.getGrid();
  }
  public final CollocGridFreeBC getZCollocGrid()
  { return zCGrid;
  }
  public final SimpleGrid getZBaseGrid()
  { return zSpline.getGrid();
  }
  public void reset()
  {    xCGrid=new CollocGrid(xSpline);
    yCGrid=new CollocGrid(ySpline);
    zCGrid=new CollocGridFreeBC(zSpline);
    nX=xCGrid.mesh.length;
    nY=yCGrid.mesh.length;
    nZ=zCGrid.mesh.length;
    n=nX*nY*nZ;
    indexX=new int[n];
    indexY=new int[n];
    indexZ=new int[n];
    index3=new int[nX][nY][nZ];
    int k;
    for(int iz=0;iz<nZ;iz++)
    for(int iy=0;iy<nY;iy++)
    for(int ix=0;ix<nX;ix++)
    {
      k=(iz*nY+iy)*nX+ix;
//      System.out.println("kxyz: "+k+" "+ix+" "+iy+" "+iz);
      index3[ix][iy][iz]=k;
      indexX[k]=ix;
      indexY[k]=iy;
      indexZ[k]=iz;
    }
  }
}