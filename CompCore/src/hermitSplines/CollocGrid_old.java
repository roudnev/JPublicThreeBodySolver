package hermitSplines;
public class CollocGrid_old extends SimpleGrid
{ public CollocGrid_old(HSplineBC s)
  { double DT,sh1,sh2,sh3;
    maxPointNumber=s.getNMax();
    mesh=new double[maxPointNumber+1];
    int NSPN=s.getSplinesPerNode();
    switch (s.getSplineType())
    { case 'C':
        sh1=(1.0-1.0/java.lang.Math.sqrt(3.0))*0.5;
	sh2=(1.0+1.0/java.lang.Math.sqrt(3.0))*0.5;
        for (int i=0; i<s.theGrid.mesh.length-1; i++)
	{ DT=s.theGrid.mesh[i+1]-s.theGrid.mesh[i];
	  mesh[i*2]=s.theGrid.mesh[i]+DT*sh1;
	  mesh[i*2+1]=s.theGrid.mesh[i]+DT*sh2;
	}
      break;
      case 'Q':
        sh1=(1.0-1.0/java.lang.Math.sqrt(3.0))*0.5;
	sh2=(1.0+1.0/java.lang.Math.sqrt(3.0))*0.5;
	mesh[0]=s.theGrid.mesh[0]+(s.theGrid.mesh[1]-s.theGrid.mesh[0])*sh1;
	mesh[1]=s.theGrid.mesh[0]+(s.theGrid.mesh[1]-s.theGrid.mesh[0])*sh2;
	//
        sh1=(1.0-java.lang.Math.sqrt(0.6))*0.5;
	sh2=0.5;
	sh3=(1.0+java.lang.Math.sqrt(0.6))*0.5;
        for (int i=1; i<s.theGrid.mesh.length-1; i++)
	{ DT=s.theGrid.mesh[i+1]-s.theGrid.mesh[i];
	  mesh[i*3-1]=s.theGrid.mesh[i]+DT*sh1;
	  mesh[i*3]=s.theGrid.mesh[i]+DT*sh2;
	  mesh[i*3+1]=s.theGrid.mesh[i]+DT*sh3;
	}
      break;
      default:
        System.out.println("Unknown spline type in CollocGrid constructor, exiting...");
	System.exit(-1);
      break;
    }
    leftmostPoint=mesh[0];
    rightmostPoint=mesh[maxPointNumber];
    name=s.theGrid.name+": collocations";
  }
}
