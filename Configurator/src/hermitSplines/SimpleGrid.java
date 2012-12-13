package hermitSplines;
import java.io.*;
public class SimpleGrid
{ //fields:
  public int maxPointNumber;
  protected double rightmostPoint;
  protected double leftmostPoint;
  public double mesh[];
  protected String name;
  // constructors:
  public SimpleGrid()
  { maxPointNumber=1;
    mesh=new double[maxPointNumber+1];
    leftmostPoint=0.0;
    rightmostPoint=1.0;
    name="Such a grid";
    mesh[0]=leftmostPoint;
    mesh[maxPointNumber-1]=rightmostPoint;
  }
  public SimpleGrid(String name)
  { this();
    this.name=name;
  }
  public SimpleGrid(double[] mesh,String name)
  { this.mesh=mesh;
    this.name=name;
    maxPointNumber=mesh.length-1;
    leftmostPoint=mesh[0];
    rightmostPoint=mesh[maxPointNumber];
  }
  public SimpleGrid(int N)
  { maxPointNumber=N;
    name="Such a grid";
    mesh=new double[maxPointNumber+1];
    leftmostPoint=0.0;
    rightmostPoint=1.0;
    double d=(rightmostPoint-leftmostPoint)/(maxPointNumber);
    for(int i=0; i<=maxPointNumber; i++)
      { mesh[i]=d*i+leftmostPoint;}
  }
  public SimpleGrid(double[] msh)
  { this();
    maxPointNumber=msh.length-1;
    rightmostPoint=msh[maxPointNumber];
    leftmostPoint=msh[0];
    mesh=msh;
  }
  public SimpleGrid(int N, String name)
  { try
    {
      File gridFile=new File(name);
      BufferedReader inp=new BufferedReader(new FileReader(gridFile));

      int cpLength=Integer.parseInt(inp.readLine());
      double[] x=new double[cpLength];
      double[] y=new double[cpLength];
      for (int i=0;i<cpLength;i++)
      {
        x[i]=Double.parseDouble(inp.readLine());
        y[i]=Double.parseDouble(inp.readLine());
      }
      inp.close();
      maxPointNumber=N;
      leftmostPoint=x[0];
      rightmostPoint=x[x.length-1];
      double dx=1.0/(N);
      mesh=new double[N+1];
      MonotoneSplineMap map=new MonotoneSplineMap(y,x);
      for(int i=0;i<mesh.length;i++)
      {
          mesh[i]=map.valueAt(dx*i);
//          System.out.println("Base Grid"+i+"   "+mesh[i]);
      }
    }
    catch(IOException e1)
    {
      System.err.println("Error reading file "+name);
    }
    this.name=name;
  }
  //
  public SimpleGrid(int N, double R)
  { maxPointNumber=N;
    name="Such a grid";
    mesh=new double[maxPointNumber+1];
    leftmostPoint=0.0;
    rightmostPoint=R;
    double d=(rightmostPoint-leftmostPoint)/(maxPointNumber);
    for(int i=0; i<=maxPointNumber; i++)
      { mesh[i]=d*i+leftmostPoint;}
  }
  public SimpleGrid(int N, double R, String name)
  { this(N,R);
    this.name=name;
  }
  public SimpleGrid(int N, double L, double R)
  { maxPointNumber=N;
    name="Such a grid";
    mesh=new double[maxPointNumber+1];
    leftmostPoint=L;
    rightmostPoint=R;
    double d=(rightmostPoint-leftmostPoint)/(maxPointNumber);
    for(int i=0; i<=maxPointNumber; i++)
      { mesh[i]=d*i+leftmostPoint;}
  }
  public SimpleGrid(int N, double L, double R, String name)
  { this(N,L,R);
    this.name=name;
  }

  public SimpleGrid(int[] N, double[] B)
  { int NP=0, NPP=1;
    for(int i=1; i<B.length; i++)
      { if (i<=N.length) NPP=N[i-1];
        NP+=NPP;
	// System.out.println(i+" "+NP+" "+NPP);
      };
    maxPointNumber=NP;
    mesh=new double[maxPointNumber+1];
    leftmostPoint=B[0];
    rightmostPoint=B[B.length-1];
    mesh[0]=leftmostPoint;
    int k=0;
    for(int i=1; i<B.length; i++)
      { if (i<=N.length) NPP=N[i-1];
        double d=(B[i]-B[i-1])/NPP;
        for(int j=1;j<=NPP;j++)
	  { k++;
	    mesh[k]=mesh[k-1]+d;
            // System.out.println(i+" "+k+" "+Mesh[k]+" ");
	  }
      };
    this.name="Such a grid";
  }

  public SimpleGrid(int[] N, double[] B, String name)
  { this(N,B);
    this.name=name;
  }
  //methods:
  public void DisplayGrid()
  { try
    { System.out.println("Grid   : "+this.name);
      System.out.println("Points : "+this.maxPointNumber);
      System.out.println("Bounds : "+this.leftmostPoint+" -:- "+this.rightmostPoint);
      System.out.println("N Point ");
      for(int i=0; i<=maxPointNumber; i++)
      { System.out.println(i+" "+this.mesh[i]);
      }
      System.out.println();
    }
    catch(ArrayIndexOutOfBoundsException e)
    { System.out.println("Wrong index in displayGrid:"+e);
    }
  }
  public void DisplayGrid(java.io.PrintStream out)
  { try
    { out.println("Grid   : "+this.name);
      out.println("Points : "+this.mesh.length);
      out.println("Bounds : "+this.leftmostPoint+" -:- "+this.rightmostPoint);
      out.println("N Point ");
      for(int i=0; i<=maxPointNumber; i++)
      { out.println(i+" "+this.mesh[i]);
      }
      out.println();
    }
    catch(ArrayIndexOutOfBoundsException e)
    { System.out.println("Wrong index in displayGrid:"+e);
    }
  }

  public String getName()
  { return name;
  }
  
  public double getRightmostPoint()
  {
	  return mesh[mesh.length-1];
  }
}
