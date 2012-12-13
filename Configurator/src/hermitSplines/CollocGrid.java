package hermitSplines;
public class CollocGrid extends SimpleGrid
{ public CollocGrid(HSplineBC s)
  { double dt,sh1,sh2,sh3,sh4;
    maxPointNumber=s.getNMax();
    int n=maxPointNumber;
    mesh=new double[maxPointNumber+1];
    int NSPN=s.getSplinesPerNode();
    switch (s.getSplineType())
    { case 'C':
        sh1=(1.0-1.0/java.lang.Math.sqrt(3.0))*0.5;
	sh2=(1.0+1.0/java.lang.Math.sqrt(3.0))*0.5;
        for (int i=0; i<s.theGrid.mesh.length-1; i++)
	{ dt=s.theGrid.mesh[i+1]-s.theGrid.mesh[i];
	  mesh[i*2]=s.theGrid.mesh[i]+dt*sh1;
	  mesh[i*2+1]=s.theGrid.mesh[i]+dt*sh2;
	}
      break;
      case 'Q':
        sh1=(1.0-java.lang.Math.sqrt(0.6))*0.5;
	sh2=0.5;
	sh3=(1.0+java.lang.Math.sqrt(0.6))*0.5;
//        System.out.println("Generation of the grid:");
        for (int i=0; i<s.theGrid.mesh.length-1; i++)
	{ 
//          System.out.println(i+"  "+s.theGrid.mesh[i]+"  "+s.theGrid.mesh[i+1]);
          dt=s.theGrid.mesh[i+1]-s.theGrid.mesh[i];
	  mesh[i*3]=s.theGrid.mesh[i]+dt*sh1;
	  mesh[i*3+1]=s.theGrid.mesh[i]+dt*sh2;
	  mesh[i*3+2]=s.theGrid.mesh[i]+dt*sh3;
//          System.out.println(i*3+"  "+mesh[i*3]+"  "+mesh[i*3+1]+"  "+mesh[i*3+2]);
	}
        dt=s.theGrid.mesh[s.theGrid.mesh.length-1]-s.theGrid.mesh[s.theGrid.mesh.length-2];
        mesh[n-3] = dt*(1.0-0.861136311594053)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
        mesh[n-2]=dt*(1.0-0.339981043584856)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
        mesh[n-1]=dt*(1.0+0.339981043584856)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
        mesh[n]=dt*(1.0+0.861136311594053)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
      break;
      default:
        System.out.println("Unknown spline type in CollocGrid constructor, exiting...");
	System.exit(-1);
      break;
    }
    leftmostPoint=mesh[0];
    rightmostPoint=mesh[maxPointNumber];
//    name=s.theGrid.name+": collocations";
//    System.out.println(name);
//    for (int i=0;i<mesh.length;i++)
//    {
//      System.out.println(i+"   "+mesh[i]);
//    }
//    
  }
}
