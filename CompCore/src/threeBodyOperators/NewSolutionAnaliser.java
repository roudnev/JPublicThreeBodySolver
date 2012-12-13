package threeBodyOperators;
import hermitSplines.*;
import auxiliary.DFunction;
import auxiliary.InpReader;
import java.io.*;
import basicAbstractions.AFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 0.5
 */
public class NewSolutionAnaliser
{ SplineFunction1D[] fy;
  SplineFunction1D[] wf2;
  MulticomponentDiscretization3D d3D;
  int noc=3;
  double[] erel;
  double[] yscal;
  MulticomponentThreeBodyEquation theEq;

  public NewSolutionAnaliser(MulticomponentThreeBodyEquation eq, AFunction solution)
  {
    AFunction sol;
    System.out.print("SolutionAnaliser initialization...");
    this.theEq=eq;
    this.erel=eq.theA.erelArray;

    d3D=eq.theA.h0.md3d;
    noc=d3D.getNumberOfComponents();

    sol=eq.theA.h0.solve(solution);
    sol=eq.theA.h0.toSpectralXZ(sol);
    noc=d3D.getNumberOfComponents();
    fy=new SplineFunction1D[noc];
    yscal=eq.config.getyReducedMass().clone();
    for (int k=0;k<noc;k++)
    {
      int ny=d3D.d3d[k].getNY();
      double[] coef=new double[ny];
      for (int iy=0;iy<ny;iy++) coef[iy]= sol.coef[eq.theA.h0.md3d.index[k][0][iy][0]];
      fy[k]=new SplineFunction1D(d3D.d3d[k].ySpline, ny, new DFunction(coef));
      //yscal[k]=yscal[k]*Math.sqrt(2.0);
    }
//        try {
//            checkBasis();
//        } catch (Exception ex) {
//            Logger.getLogger(NewSolutionAnaliser.class.getName()).log(Level.SEVERE, null, ex);
//        }
    System.out.println("Done");
  }


  public void printKMatrix()
  { double a,y;
    double k;
    System.out.println();
    System.out.println("K-Matrix, a.u. (by channel):");
    double kInitState=Math.sqrt(erel[InitState.initialChannel]);
    for (int icomp=0; icomp<noc;icomp++)
    {
        System.out.print("#3body#"+icomp);
        for(int iy=d3D.d3d[icomp].getNY()-2;iy<d3D.d3d[icomp].getNY();iy++)
        { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
         // System.out.print("FOR Y="+y);
          if (erel[icomp]>=0)
          {
            k=Math.sqrt(erel[icomp]);
            a=fy[icomp].valueAt(y)/(Math.cos(k*y));
            if (kInitState!=0)
              a=a*kInitState*Math.sqrt(k/kInitState);
            else
               a=a/yscal[icomp]; // for E=0 printing the scattering length
            System.out.print(" "+a+" ");
          }
          else
          {
            System.out.print(" VIRTUAL CHANNEL!!! ");
            k=Math.sqrt(-erel[icomp]);
            a=fy[icomp].valueAt(y)/(Math.exp(-k*y));
            a=a/yscal[icomp];
            System.out.print(" "+a+" ");
          }
        }
        System.out.println();
    }
  }
  public void writeKmatrix() throws Exception
  {
    double a,y;
    double k;
    double kInitState=Math.sqrt(erel[InitState.initialChannel]);
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("Kmatr-"+icomp+".DAT"), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      {
          y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
          if (erel[icomp]>=0)
          {
            k=Math.sqrt(erel[icomp]);
            a=fy[icomp].valueAt(y)/(Math.cos(k*y));
            if (kInitState!=0)
              a=a*kInitState*Math.sqrt(k/kInitState);
            else
               a=a/yscal[icomp]; // for E=0 printing the scattering length
            wrt.println(y+" "+a+"  ");
          }
          else
          {
            System.out.print(" VIRTUAL CHANNEL!!! ");
            k=Math.sqrt(-erel[icomp]);
            a=fy[icomp].valueAt(y)/(Math.exp(-k*y));
            a=a/yscal[icomp];
            wrt.println(y+" "+a);
          }
      }
      wrt.close();
    }
  }
  //

  public void printPhaseShift()
  { double a,y;
    double k;
    for (int icomp=0;icomp<noc;icomp++)
    for (int iy=d3D.d3d[icomp].getNY()-4;iy<d3D.d3d[icomp].getNY();iy++)
    { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
      System.out.print("FOR Y="+y);
      k=Math.sqrt(erel[icomp]);
      a=fy[icomp].valueAt(y)/(Math.cos(k*y));
      //a=a/yscal[icomp];
      a=Math.atan(k*a);
      //if(a<0.0) a+=Math.PI;
      System.out.print(" "+a+" ");
      
    }
  }
  public void writeComponents(String fName) throws Exception
  {
    double a,y;
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("n"+icomp+fName), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
        a=fy[icomp].valueAt(y);
        wrt.println(y+" "+a);
      }
      wrt.close();
    }
  }

  
  public void writePhase() throws Exception
  {
    double a,y,delta;
    double k;
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("Phase-"+icomp+".DAT"), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
        k=Math.sqrt(erel[icomp]);
        a=fy[icomp].valueAt(y)/(Math.cos(k*y));
        delta=Math.atan(k*a);
        //if(a<0.0) delta+=2*Math.PI;
        wrt.println(y+" "+delta+" "+(delta*180/Math.PI));
      }
      wrt.close();
    }
  }
  //
  private void checkBasis() 
  {
      try
      {
          int l=0;
          PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("XBasis"+l+".DAT"), 131072));
          int nx=theEq.theA.h0.clustHamilt[0].xOperator[l].rank;
          double[] coef=new double[nx];
          SplineFunction1D[] fx=new SplineFunction1D[nx];
          for (int ix=0;ix<nx;ix++)
          {
              for (int jx=0; jx<nx; jx++)
                coef[jx]=theEq.theA.h0.clustHamilt[0].xOperator[l].getEigenvectorsRightRe()[jx][ix];
              fx[ix]=new SplineFunction1D(theEq.theA.h0.md3d.d3d[l].xSpline, nx, new DFunction(coef));
          }
          double[] colloc=theEq.theA.h0.md3d.d3d[0].xCGrid.mesh;
          for (int ix=0; ix<nx; ix++)
          {
              wrt.print(colloc[ix]+"  ");
              for (int jx=0; jx<nx; jx++)
              {
                  wrt.print(fx[jx].valueAt(colloc[ix])+"  ");
              }
              wrt.println();
          }
          wrt.close();
          //
          wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("XBasisProjector"+l+".DAT"), 131072));
          nx=theEq.theA.h0.clustHamilt[0].xOperator[l].rank;
             for (int ix=0;ix<nx;ix++)
          {
              for (int jx=0; jx<nx; jx++)
                coef[jx]=theEq.theA.h0.clustHamilt[0].xOperator[l].getEigenvectorsLeftRe()[ix][jx];
              fx[ix]=new SplineFunction1D(theEq.theA.h0.md3d.d3d[l].xSpline, nx, new DFunction(coef));
          }
          colloc=theEq.theA.h0.md3d.d3d[0].xCGrid.mesh;
          for (int ix=0; ix<nx; ix++)
          {
              wrt.print(colloc[ix]+"  ");
              for (int jx=0; jx<nx; jx++)
              {
                  wrt.print(fx[jx].valueAt(colloc[ix])+"  ");
              }
              wrt.println();
          }
          wrt.close();
          //
          int nz=theEq.theA.h0.clustHamilt[0].zOperator.rank;
          coef=new double[nz];
          wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("ZBasis"+nz+".DAT"), 131072));
          for (int ix=0;ix<nz;ix++)
          {
              for (int jx=0; jx<nz; jx++)
                coef[jx]=theEq.theA.h0.clustHamilt[0].zOperator.getEigenvectorsRightRe()[jx][ix];
              fx[ix]=new SplineFunction1D(theEq.theA.h0.md3d.d3d[l].zSpline, nz, new DFunction(coef));
          }
          SimpleGrid tmp=new SimpleGrid(50,-1.0, 1.0);
          colloc=tmp.mesh;
          for (int ix=0; ix<colloc.length; ix++)
          {
              wrt.print(colloc[ix]+"  ");
              for (int jx=0; jx<nz; jx++)
              {
                  wrt.print(fx[jx].valueAt(colloc[ix])+"  ");
              }
              wrt.println();
          }
          wrt.close();
          //
          wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("ZBasisPojector"+nz+".DAT"), 131072));
          for (int ix=0;ix<nz;ix++)
          {
              for (int jx=0; jx<nz; jx++)
                coef[jx]=theEq.theA.h0.clustHamilt[0].zOperator.getEigenvectorsLeftRe()[ix][jx];
              fx[ix]=new SplineFunction1D(theEq.theA.h0.md3d.d3d[l].zSpline, nz, new DFunction(coef));
          }
          for (int ix=0; ix<colloc.length; ix++)
          {
              wrt.print(colloc[ix]+"  ");
              for (int jx=0; jx<nz; jx++)
              {
                  wrt.print(fx[jx].valueAt(colloc[ix])+"  ");
              }
              wrt.println();
          }
          wrt.close();
      }
      catch(Exception e)
      {
          System.err.println(e);
      }
  }
}
