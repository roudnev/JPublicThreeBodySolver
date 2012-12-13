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
import java.io.*;
import auxiliary.InpReader;

class MDDConfigReader
  {// Fields:
   InpReader reader;
   // Constructors:
   MDDConfigReader() throws IOException
   {
          reader=new InpReader("./Discretization.conf");
   }
   // Methods:

   void close()
   {
     try
     {
       reader.close();
     }
     catch(Exception e)
     { System.err.println("Error when closing MDDConfigReader: "+e.getMessage());
     }
   }

   int readNumberOfComponents() throws IOException
   { int res=0;
     res=Integer.parseInt(reader.readLine());
       //res=reader.readIntArray()[0];
     return res;
   }

   HSplineBC readSplineConfigX() throws IOException
   {
     HSplineBC spline=null;
     String xGridConfFileName=reader.readLine();
     int nx=Integer.parseInt(reader.readLine());
     SimpleGrid xGrid=new SimpleGrid(nx,xGridConfFileName);
// !!!!!!! To comment out !!!!!! The following is for testing with nonoptimal grids only
//     double rmax=xGrid.mesh[xGrid.mesh.length-1];
//     xGrid=new SimpleGrid(nx, rmax, "Exp 8 X grid");
//     for (int i=0;i<xGrid.mesh.length; i++) xGrid.mesh[i]=xMap(xGrid.mesh[i]/rmax)*rmax/xMap(1.0);

     spline=new HSplineBC(xGrid,'Q','D','D');
     return spline;
   }

   double xMap(double t)
   { return Math.exp(8*t)-1;
   }

   double yMap(double t, double k)
   {
       return Math.exp(k*t)-1;
//       return -Math.log(1.0-(1.0-1.0/k)*t);
   }

   HSplineBC readSplineConfigYExp() throws IOException
   {
     HSplineBC spline=null;
     double rmax=reader.readDouble();
     int nx=Integer.parseInt(reader.readLine());
// estimating a reasonable minimal step
     double dy0=2.0;//Math.min(1.0, 100.0/nx);
// estimating the appropriate grid parameter k
     double c=dy0*nx/rmax;
     double k=1.0;
     double dk=1.0;
     while(Math.abs(dk)>1.0e-5)
     {
       dk=Math.log(k/c+1.0)-k;
       k=k+dk;
     }
     System.out.println("k="+k);
     SimpleGrid yGrid=new SimpleGrid(nx, rmax, "Quadratic Y grid");
     for (int i=0;i<yGrid.mesh.length; i++)
     {
//       yGrid.mesh[i]=Math.pow(yGrid.mesh[i]/rmax,2.5)*rmax;
       yGrid.mesh[i]=yMap(yGrid.mesh[i]/rmax,k)*rmax/yMap(1.0,k);
     }
     spline=new HSplineBC(yGrid,'Q','D','D');
     return spline;
   }

   HSplineBC readSplineConfigY() throws IOException
   {
     HSplineBC spline=null;
     double alpha=0.67;
     double[] tmp=reader.readDoubleArray();
     double rmax=tmp[0];
     if (tmp.length>1) alpha=tmp[1];
     if (Math.abs(alpha)<1.0e-10) alpha=0.67;
     System.err.println("Ygrid alpha="+alpha);
     int nx=Integer.parseInt(reader.readLine());
     SimpleGrid yGrid=new SimpleGrid(nx, rmax, "Quadratic Y grid");
     for (int i=0;i<yGrid.mesh.length; i++)
     {
//       yGrid.mesh[i]=Math.pow(yGrid.mesh[i]/rmax,2.5)*rmax;
//       yGrid.mesh[i]=Math.pow(rmax+1, yGrid.mesh[i]/rmax)-1;
         double u=yGrid.mesh[i]/rmax;
         yGrid.mesh[i]=Math.pow( ((Math.pow(rmax+1,1-alpha)-1)*u+1), 1.0/(1.0-alpha))-1;
     }
     spline=new HSplineBC(yGrid,'Q','D','D');
     return spline;
   }

    HSplineBC readSplineConfigYLog() throws IOException
   {
     HSplineBC spline=null;
     double rmax=reader.readDouble();
     int nx=Integer.parseInt(reader.readLine());
// estimating a reasonable minimal step
     double ac=500.0;//Math.min(1.0, 100.0/nx);
// estimating the appropriate grid parameter k

     SimpleGrid yGrid=new SimpleGrid(nx, rmax, "Quadratic Y grid");
     for (int i=0;i<yGrid.mesh.length; i++)
     {
//       yGrid.mesh[i]=Math.pow(yGrid.mesh[i]/rmax,2.5)*rmax;
       yGrid.mesh[i]=yMap(yGrid.mesh[i]/rmax,ac)*rmax/yMap(1.0,ac);
     }
     spline=new HSplineBC(yGrid,'Q','D','D');
     return spline;
   }

   HSplineFree readSplineConfigZ() throws IOException
   { HSplineFree spline=null;
     int nx=Integer.parseInt(reader.readLine());
     SimpleGrid xGrid=new SimpleGrid(nx,-1.0,1.0,"Z grid");
     for (int i=0; i<nx;i++) xGrid.mesh[i]=zmap(xGrid.mesh[i]);
     spline=new HSplineFree(xGrid,'Q');
     return spline;
   }

   double zmap(double t)
   { double alpha=2.3;
     return (t+alpha*t*t*t)/(1.0+alpha);  //Math.tanh(alpha*t)/Math.tanh(alpha);
   }
  }

/*
class MDDConfigReader
  {// Fields:
   InpReader reader;
   // Constructors:
   MDDConfigReader()
   {
     try
     {
       reader=new InpReader("./Discretization.conf");
     }
     catch(java.io.IOException e)
     { System.err.println("Error in MDDConfigReader: "+e.getMessage());
     }
   }
   // Methods:

   void close()
   {
     try
     {
       reader.close();
     }
     catch(Exception e)
     { System.err.println("Error when closing MDDConfigReader: "+e.getMessage());
     }
   }

   int readNumberOfComponents()
   { int res=0;
     try
     { res=reader.readIntArray()[0];
     }
     catch(java.io.IOException e)
     { System.err.println("I/O shit happend:" + e.getMessage() );
     }
     return res;
   }

   HSplineBC readSplineConfigXY()
   {
    HSplineBC spline=null;
    try
    {
     String xGridConfFileName=reader.readLine();
     int nx=reader.readIntArray()[0];
     SimpleGrid xGrid=new SimpleGrid(nx,xGridConfFileName);
     spline=new HSplineBC(xGrid,'Q','D','D');
     }
     catch(java.io.IOException e)
     { System.err.println("I/O shit happend:" + e.getMessage() );
     }
     return spline;
   }

   HSplineFree readSplineConfigZ()
   { HSplineFree spline=null;
    try
    {
     String xGridConfFileName=reader.readLine();
     int nx=reader.readIntArray()[0];
     //SimpleGrid xGrid=new SimpleGrid(nx,xGridConfFileName);
     SimpleGrid xGrid=new SimpleGrid(nx,-1.0,1.0,"Z grid");;
     spline=new HSplineFree(xGrid,'Q');
     System.out.print(reader.readLine());
     }
     catch(java.io.IOException e)
     { System.err.println("I/O shit happend:" + e.getMessage() );
     }
     return spline;
   }
  }

*/