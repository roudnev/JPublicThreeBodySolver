package hermitSplines;
public class CollocGridFreeBC extends SimpleGrid
{ public CollocGridFreeBC(HSplineFree s)
  { double dt,sh1,sh2,sh3,sh4,sh5,sh6;
    maxPointNumber=s.getNMaxFree();
    mesh=new double[maxPointNumber+1];
    //System.out.println(" N points = "+maxPointNumber);
    int NSPN=s.getSplinesPerNode();
    switch (s.getSplineType())
    { case 'C':
        sh1=(1.0-1.0/java.lang.Math.sqrt(3.0))*0.5;
	sh2=(1.0+1.0/java.lang.Math.sqrt(3.0))*0.5;
        for (int i=0; i<s.theGrid.mesh.length-1; i++)
	{ dt=s.theGrid.mesh[i+1]-s.theGrid.mesh[i];
	  mesh[i*2+1]=s.theGrid.mesh[i]+dt*sh1;
	  mesh[i*2+2]=s.theGrid.mesh[i]+dt*sh2;
	}

      break;
      case 'Q':
        int npoints=s.theGrid.maxPointNumber;
        switch (npoints)
        { case 1:
            sh1=-0.932469514203152; sh1=(1.0+sh1)*0.5;
            sh2=-0.661209386466265; sh2=(1.0+sh2)*0.5;
            sh3=-0.238619186083197; sh3=(1.0+sh3)*0.5;
            sh4= 0.238619186083197; sh4=(1.0+sh4)*0.5;
            sh5= 0.661209386466265; sh5=(1.0+sh5)*0.5;
            sh6= 0.932469514203152; sh6=(1.0+sh6)*0.5;
            dt=s.theGrid.mesh[1]-s.theGrid.mesh[0];
            mesh[0]=dt*sh1+s.theGrid.mesh[0];
            mesh[1]=dt*sh2+s.theGrid.mesh[0];
            mesh[2]=dt*sh3+s.theGrid.mesh[0];
            mesh[3]=dt*sh4+s.theGrid.mesh[0];
            mesh[4]=dt*sh5+s.theGrid.mesh[0];
            mesh[5]=dt*sh6+s.theGrid.mesh[0];
          break;
          default:
            sh1=(1.0-java.lang.Math.sqrt(0.6))*0.5;
    	    sh2=0.5;
	    sh3=(1.0+java.lang.Math.sqrt(0.6))*0.5;
            for (int i=2; i<s.theGrid.mesh.length-1; i++)
	    { dt=s.theGrid.mesh[i]-s.theGrid.mesh[i-1];
              mesh[i*3-1]=s.theGrid.mesh[i-1]+dt*sh1;
	      mesh[i*3]=s.theGrid.mesh[i-1]+dt*sh2;
	      mesh[i*3+1]=s.theGrid.mesh[i-1]+dt*sh3;
            }
            dt=s.theGrid.mesh[1]-s.theGrid.mesh[0];
            mesh[0]=dt*(1.0-0.906179845938664)*0.5+s.theGrid.mesh[0];
            mesh[1]=dt*(1.0-0.538469310105683)*0.5+s.theGrid.mesh[0];
            mesh[2]=dt*0.5+s.theGrid.mesh[0];
            mesh[3]=dt*(1.0+0.538469310105683)*0.5+s.theGrid.mesh[0];
            mesh[4]=dt*(1.0+0.906179845938664)*0.5+s.theGrid.mesh[0];
            int n=s.theGrid.mesh.length*3;
            //System.out.println(" n points = "+(s.theGrid.mesh.length*3));
            dt=s.theGrid.mesh[s.theGrid.mesh.length-1]-s.theGrid.mesh[s.theGrid.mesh.length-2];
            mesh[n-4] = dt*(1.0-0.861136311594053)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
            mesh[n-3]=dt*(1.0-0.339981043584856)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
            mesh[n-2]=dt*(1.0+0.339981043584856)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
            mesh[n-1]=dt*(1.0+0.861136311594053)*0.5+s.theGrid.mesh[s.theGrid.mesh.length-2];
          break;
        }
	//
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
