package threeBodyOperators;
import hermitSplines.SplineFunction1D;
import auxiliary.DFunction;
import auxiliary.InpReader;
import java.io.*;
import basicAbstractions.AFunction;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 0.5
 */
public class SolutionAnaliser
{ Function3D[] solution;
  Function3D[] tSolution;
  SplineFunction1D clusterWF;
  MulticomponentDiscretization3D d3D;
  int noc=3;
  double erel;
  double[] yscal;
  MulticomponentThreeBodyEquation theEq;

  public SolutionAnaliser(MulticomponentThreeBodyEquation eq, AFunction sol)
  {
    System.out.print("SolutionAnaliser initialization...");
    this.theEq=eq;
    this.erel=eq.theA.erel;
    clusterWF=eq.getClusterWF();
    d3D=eq.theA.v.md3dR;
    noc=d3D.getNumberOfComponents();
    tSolution=new Function3D[noc];
    for (int k=0;k<noc;k++)
    {
      int n=d3D.d3d[k].getN();
      int n0=eq.theA.h0.md3dR.index[k][0][0][0];
      double[] coef=new double[n];
      for (int i=0;i<n;i++) coef[i]= sol.coef[i+n0];
      tSolution[k]=new Function3D(eq.theA.h0.md3dR.d3d[k], new DFunction(coef));
    }
    d3D=eq.theA.h0.md3d;
    sol=eq.theA.h0.solve(sol);
    noc=d3D.getNumberOfComponents();
    solution=new Function3D[noc];
    for (int k=0;k<noc;k++)
    {
      int n=d3D.d3d[k].getN();
      int n0=eq.theA.h0.md3d.index[k][0][0][0];
      double[] coef=new double[n];
      for (int i=0;i<n;i++) coef[i]= sol.coef[i+n0];
      solution[k]=new Function3D(d3D.d3d[k], new DFunction(coef));
    }
    yscal=new double[noc];
    double msum=0.0;
    double m2s;
    for (int i=0;i<3;i++) msum+=eq.theA.v.mass[i];
    for (int i=0;i<noc;i++)
    {
      m2s=0.0;
      for (int j=0;j<3;j++) if (j!=i) m2s+=eq.theA.v.mass[j];
      yscal[i]=Math.sqrt(2*eq.theA.v.mass[i]*m2s/msum);
      //System.err.println("yScaling:"+yscal[i]);
    }
    System.out.println("Done");
  }

  public void printAmplitude()
  { double a,x,y,z;
    double k=Math.sqrt(erel);
    for (int icomp=0; icomp<noc;icomp++)
    for(int iy=d3D.d3d[icomp].getNY()-2;iy<d3D.d3d[icomp].getNY();iy++)
    { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
      System.out.println("FOR Y="+y);
      for(int ix=d3D.d3d[icomp].getNX()-1;ix>=0;ix--)
      { x=d3D.d3d[icomp].getXCollocGrid().mesh[ix];
        System.out.print("component#"+icomp+"FOR X="+x+" ");
        for(int iz=0;iz<d3D.d3d[icomp].getNZ();iz++)
        { z=d3D.d3d[icomp].getZCollocGrid().mesh[iz];
          a=solution[icomp].valueAt(x,y,z)/clusterWF.valueAt(x)/(Math.cos(k*y));
          a=a/yscal[icomp];
          System.out.print(" "+a+" ");
        }
        System.out.println();
      }
      System.out.println();
    }
  }
  public void printPhaseShift()
  { double a,x,y,z;
    double k=Math.sqrt(erel);
    for (int icomp=0;icomp<noc;icomp++)
    for (int iy=d3D.d3d[icomp].getNY()-4;iy<d3D.d3d[icomp].getNY();iy++)
    { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
      System.out.println("FOR Y="+y);
      for(int ix=d3D.d3d[icomp].getNX()-1;ix>=0;ix--)
      { x=d3D.d3d[icomp].getXCollocGrid().mesh[ix];
        System.out.print("component#"+icomp+"FOR X="+x+" ");
        for(int iz=0;iz<d3D.d3d[icomp].getNZ();iz++)
        { z=d3D.d3d[icomp].getZCollocGrid().mesh[iz];
          a=solution[icomp].valueAt(x,y,z)/clusterWF.valueAt(x)/(Math.cos(k*y));
          // a=a/yscal[icomp];
          a=Math.atan(k*a);
          //if(a<0.0) a+=Math.PI;
          System.out.print(" "+a+" ");
        }
        System.out.println();
      }
      System.out.println();
    }
  }
  public void writeComponents(String fName) throws Exception
  {
    double a,x,y,z;
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("n"+icomp+fName), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
        for(int ix=0;ix<d3D.d3d[icomp].getNX();ix++)
        { x=d3D.d3d[icomp].getXCollocGrid().mesh[ix];
          for(int iz=0;iz<d3D.d3d[icomp].getNZ();iz++)
          { z=d3D.d3d[icomp].getZCollocGrid().mesh[iz];
            a=solution[icomp].valueAt(x,y,z);
            wrt.println(x+" "+y+" "+z+" "+a);
          }
        }
      }
      wrt.close();
    }
  }
  public void writeTComponent(String fName) throws Exception
  {
    double a,x,y,z;
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("n"+icomp+fName), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
        for(int ix=0;ix<d3D.d3d[icomp].getNX();ix++)
        { x=d3D.d3d[icomp].getXCollocGrid().mesh[ix];
          for(int iz=0;iz<d3D.d3d[icomp].getNZ();iz++)
          { z=d3D.d3d[icomp].getZCollocGrid().mesh[iz];
            a=solution[icomp].f.coef[d3D.d3d[icomp].getIndex(ix,iy,iz)];
            wrt.println(x+" "+y+" "+z+" "+a);
          }
        }
      }
      wrt.close();
    }
  }
  public void writeAmplitude() throws Exception
  {
    double a,x,y,z;
    double k=Math.sqrt(erel);
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("AMP-"+icomp+".DAT"), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
        for(int ix=d3D.d3d[icomp].getNX()-1;ix>=0;ix--)
        { x=d3D.d3d[icomp].getXCollocGrid().mesh[ix];
          for(int iz=0;iz<d3D.d3d[icomp].getNZ();iz++)
          { z=d3D.d3d[icomp].getZCollocGrid().mesh[iz];
            a=solution[icomp].valueAt(x,y,z)/clusterWF.valueAt(x)/(Math.cos(k*y));
            a=a/yscal[icomp];
            wrt.println(x+" "+y+" "+z+" "+a);
          }
        }
      }
      wrt.close();
    }
  }
  //
  public void writePhase() throws Exception
  {
    double a,x,y,z;
    double k=Math.sqrt(erel);
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("Phase-"+icomp+".DAT"), 131072));
      for(int iy=0;iy<d3D.d3d[icomp].getNY();iy++)
      { y=d3D.d3d[icomp].getYCollocGrid().mesh[iy];
        for(int ix=d3D.d3d[icomp].getNX()-1;ix>=0;ix--)
        { x=d3D.d3d[icomp].getXCollocGrid().mesh[ix];
          for(int iz=0;iz<d3D.d3d[icomp].getNZ();iz++)
          { z=d3D.d3d[icomp].getZCollocGrid().mesh[iz];
            a=solution[icomp].valueAt(x,y,z)/clusterWF.valueAt(x)/(Math.cos(k*y));
            a=a/yscal[icomp];
            a=Math.atan(k*a);
            if(a<0.0) a+=2*Math.PI;
            wrt.println(x+" "+y+" "+z+" "+a*180/Math.PI);
          }
        }
      }
      wrt.close();
    }
  }
  //
  public void ifritDumpComponent() throws Exception
  {
    String fName="IfritData.txt";
    InpReader reader=new InpReader(fName);
    int[] dimensions=reader.readIntArray();
    double[] scales=reader.readDoubleArray();
    double a,x,y,z;
    double dx,dy,dz;
    dx=scales[0]/dimensions[0];
    dy=scales[1]/dimensions[1];
    dz=2*scales[2]/dimensions[2];
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("n"+icomp+fName), 131072));
      wrt.println((dimensions[2]-1)+"  "+(dimensions[0]-1)+"  "+(dimensions[1]-1));
      for(int iy=1;iy<dimensions[1];iy++)
      { y=dy*iy;
        for(int ix=1;ix<dimensions[0];ix++)
        { x=dx*ix;
          for(int iz=1;iz<dimensions[2];iz++)
          { z=-scales[2]+iz*dz;
            //System.out.println(" "+x+" "+y+" "+z);
            a=solution[icomp].valueAt(x,y,z);
            wrt.println(a);
          }
        }
      }
      wrt.close();
    }
  }
  //
  public void ifritDumpWaveFunction() throws Exception
  {
    String fName="IfritData.txt";
    InpReader reader=new InpReader(fName);
    int[] dimensions=reader.readIntArray();
    double[] scales=reader.readDoubleArray();
    double a,x,y,z;
    double xt,yt,zt;
    double dx,dy,dz;
    dx=scales[0]/dimensions[0];
    dy=scales[1]/dimensions[1];
    dz=2*scales[2]/dimensions[2];
    PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("wf"+fName), 131072));
    wrt.println((dimensions[2]-1)+"  "+(dimensions[0]-1)+"  "+(dimensions[1]-1));
    for(int iy=1;iy<dimensions[1];iy++)
    { y=dy*iy;
      for(int ix=1;ix<dimensions[0];ix++)
      { 
    	x=dx*ix;
        for(int iz=1;iz<dimensions[2];iz++)
        { 
          z=-scales[2]+iz*dz;
          a=solution[0].valueAt(x,y,z);
          //System.out.println(x+" "+y+" "+z);
          for (int icomp=1;icomp<3;icomp++)
          {
           	int jcomp=theEq.theA.v.idTable[icomp];
          	coordSet xyzSet=theEq.theA.v.rotcoord(x,y,z,0,icomp);
          	xt=xyzSet.x;
          	yt=xyzSet.y;
           	zt=xyzSet.z;
           	//System.out.println("    "+xt+"=?="+theEq.theA.v.xp);
           	//System.out.println("    "+yt+"=?="+theEq.theA.v.yp);
           	//System.out.println("    "+zt+"=?="+theEq.theA.v.zp);
           	a+=x*y*theEq.theA.v.parityTable[icomp]*solution[jcomp].valueAt(xt,yt,zt)/(xt*yt);
           	
          }
          wrt.println(a);
        }
      }
    }
    wrt.close();
  }
  //
  public void ifritDumpTComponent() throws Exception
  {
    String fName="IfritData.txt";
    InpReader reader=new InpReader(fName);
    int[] dimensions=reader.readIntArray();
    double[] scales=reader.readDoubleArray();
    double a,x,y,z;
    double dx,dy,dz;
    double xmax=tSolution[0].d3D.getXBaseGrid().getRightmostPoint();
    double ymax=tSolution[0].d3D.getYBaseGrid().getRightmostPoint();
    if (scales[0]>xmax) 
    {
    	scales[0]=xmax;
    	System.out.println("Ifrit output warning: xmax is scaled down to "+scales[0]);
    }
    if (scales[1]>ymax) 
    {
    	scales[1]=ymax;
    	System.out.println("Ifrit output warning: ymax is scaled down to "+scales[1]);
    }
    dx=scales[0]/dimensions[0];
    dy=scales[1]/dimensions[1];
    dz=2*scales[2]/dimensions[2];
    for (int icomp=0;icomp<noc;icomp++)
    {
      PrintStream wrt=new PrintStream(new BufferedOutputStream( new FileOutputStream("Tn"+icomp+fName), 131072));
      wrt.println((dimensions[2])+"  "+(dimensions[0])+"  "+(dimensions[1]));
      //tSolution[icomp].dump("tSolDump"+icomp+".txt");
      //System.out.println("The center="+scales[0]/2+" "+"  "+scales[1]/2);
      //System.out.println("Sample value at the center="+tSolution[icomp].dvalueAt(xmax/2,ymax/2,0.0));
      for(int iy=0;iy<dimensions[1];iy++)
      { y=dy*iy;
        for(int ix=0;ix<dimensions[0];ix++)
        { x=dx*ix;
          for(int iz=0;iz<dimensions[2];iz++)
          { z=-scales[2]+iz*dz;
            //System.out.println(" "+x+" "+y+" "+z);
            a=tSolution[icomp].valueAt(x,y,z);
            wrt.println(a);
          }
        }
      }
      wrt.close();
    }
  }
}
