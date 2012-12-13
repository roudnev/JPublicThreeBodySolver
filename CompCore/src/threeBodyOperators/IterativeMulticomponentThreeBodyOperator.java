package threeBodyOperators;

import basicAbstractions.*;
import auxiliary.*;
import configReader.ConfiguratorEssentials;
import hermitSplines.*;
import java.io.FileOutputStream;
import java.io.PrintStream;
import potentialCollection.WeightedPotential;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class IterativeMulticomponentThreeBodyOperator implements AnOperator
{
  public int rank;
  public int main2BQuantumNumber=InitState.initialVibState;
  public MulticomponentThreeBodyClusterHamiltonian h0;
  public MulticomponentRHSOperator v;
  //SparceOperator v;
  double z=0.0;
  double erel=0.0;
  double[] erelArray;
  MulticomponentDiscretization3D md3d;
  MulticomponentDiscretization3D md3dR;
  WeightedPotential[] vPot;
  double[] mass;
  double[] rmass;
  double massNorm;
  private double energyConversionFactor;
  ConfiguratorEssentials config;

  public IterativeMulticomponentThreeBodyOperator(double erelau,ConfiguratorEssentials config) throws Exception
  { this.config=config;
    System.out.println("Initializing a three-body operator for Erel="+erelau+"a.u.");
    md3d=new MulticomponentDiscretization3D();
    md3dR=new MulticomponentDiscretization3D();
    initMasses();
    this.erel=erelau*massNorm;
    initPotentials();
    restrictGrid();
    this.energyConversionFactor=vPot[0].getCoupling();
    h0=new ParallelMulticomponentThreeBodyClusterHamiltonian(vPot, md3d, md3dR,erel);
    erelArray=h0.erelArray;
    //h0=new MulticomponentThreeBodyClusterHamiltonian(vPot, md3d, md3dR,erel);
    if (erel<0.0)
    {
        try
        {
            v=new ParallelMulticomponentRHSOperator(mass, vPot, md3d, md3d);
        }
        catch(OutOfMemoryError e)
        {
           System.out.println("Not enough memory for parallel execution, running in slow mode");
           v=new MulticomponentRHSOperator(mass, vPot, md3d, md3d);

        }
    }
    else
    {
        try
        {
            v=new ParallelMulticomponentRHSOperator(mass, vPot, md3d, md3dR);
        }
        catch(OutOfMemoryError e)
        {
            System.out.println("Not enough memory for parallel execution, running in slow mode");
            v=new MulticomponentRHSOperator(mass, vPot, md3d, md3dR);
            
        }
    }
// MulticomponentRHSOperator
//    this.erel=erel;
    int ic0,n0,l0;
    ic0=InitState.initialChannel;
    n0=InitState.initialVibState;
    l0=InitState.initialAngState;
    z=h0.z;
    System.out.println("Total Energy Z="+z/massNorm+"a.u.");
    System.out.println("erel="+erelau);
    System.out.println("e2="+h0.clustHamilt[ic0].xOperator[l0].getEigenvaluesRe()[n0]/massNorm);
    rank=v.getRank();

  }
  //
  //
  protected void restrictGrid()
  {
    SimpleGrid tmpGrid;
    char splineType;
    for (int i=0;i<md3dR.getNumberOfComponents();i++)
    { tmpGrid=md3dR.d3d[i].getXBaseGrid();
      tmpGrid=restrictGrid(tmpGrid, vPot[i]);
      splineType=md3dR.d3d[i].xSpline.getSplineType();
      md3dR.d3d[i].xSpline=new HSplineBC(tmpGrid, splineType, 'D', 'D');
      md3dR.d3d[i].ySpline.setBoundaryConditionsTypeLeft('D');
      md3dR.d3d[i].ySpline.setBoundaryConditionsTypeRight('D');
      md3dR.d3d[i].reset();
    }
    md3dR.reinitIndex();
  }
  //
  private SimpleGrid restrictGrid(SimpleGrid grid, APotential vPotI)
  {
    double level=1.0e-12;
    // count points:
    int n1=0;
    double minValue=1.0;
    for (int i=0;i<grid.maxPointNumber;i++)
        if (vPotI.valueAt(grid.mesh[i]+1.0e-5)<minValue) minValue=vPotI.valueAt(grid.mesh[i]+1.0e-5);
    for (int i=0;i<grid.maxPointNumber;i++)
    { if (Math.abs(vPotI.valueAt(grid.mesh[i]+1.0e-5)/minValue)>level) n1++;
        //System.out.println("Vpot("+grid.mesh[i]+")="+vPotI.valueAt(grid.mesh[i]+1.0e-5));
    }
    //System.out.println("old number of points="+grid.maxPointNumber+" new one="+n1);
    double[] mesh1=new double[n1];
    for (int i=0;i<n1;i++) mesh1[i]=grid.mesh[i];
    SimpleGrid res=new SimpleGrid(mesh1,grid.getName()+ vPotI.getDescription());
    return res;
  }
  //
  //
  private final void initPotentials() throws Exception
  {//try
    { vPot=new WeightedPotential[3];
      for (int i=0; i<3; i++)
      {
        vPot[i]=new potentialCollection.WeightedPotential(config.getInteractions()[i],rmass[i],massNorm);
        System.out.println("Potential in channel "+i+" is initialized as\n"+vPot[i].getDescription());
      }
    }
//    catch(Exception e)
//    {
//      System.err.println("Error initializing potentials:"+ e.getMessage());
//    }
  }
  //
  private final void initMasses()
  { double[] yrmass=config.getyReducedMass();
    mass=config.getMass();
    massNorm=config.getMassNorm();
    rmass=config.getReducedMass();
    System.out.println("Energy (au) is scaled by "+massNorm);
    for (int  iComp=0;iComp<3;iComp++)
      { rmass[iComp]=Math.sqrt(2*rmass[iComp]/massNorm);
        yrmass[iComp]=Math.sqrt(2*yrmass[iComp]/massNorm);
        System.out.println("X scaling for pair "+iComp+"="+rmass[iComp]);
        System.out.println("Y scaling for pair "+iComp+"="+yrmass[iComp]);
      }
    for (int  iComp=0;iComp<3;iComp++) mass[iComp]=mass[iComp]/massNorm;
  }
  //
  //
  public AFunction times(AFunction u)
  {  AFunction tmp=v.times(h0.solve(u));
    //System.err.println("u.getArrayLength()"+"="+u.getArrayLength() );
    return u.plus(tmp);
  }
  public int getRank()
  {
    return rank;
  }
  public void setZ(double z)
  { this.z=z;
  }
  public double getZ()
  { return z;
  }


  /**
   * @return the energyConversionFactor
   */
  public double getEnergyConversionFactor()
  {
    return energyConversionFactor;
  }
}