/*
This is a backup class, somewhat working
 */
package gridGenerator;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
//import javax.swing.JFrame;
//import javax.swing.JTextArea;
//import basicAbstractions.APotential;
import basicAbstractions.AnOperator;
import hermitSplines.HSplineBC;
import hermitSplines.MonotoneSplineMap;
import hermitSplines.SimpleGrid;
import hermitSplines.SplineFunction1D;
import potentialCollection.WeightedPotential;
import twoBodyOperators.TwoBodyOperator;
//import threebodysolverconfigurator.potentialCollection.*;
import twoBodyOperators.FreeTwoBodyHamiltonian;
/**
 *
 * @author roudnev
 */
public class AutoGridGenerator_exp
{
  public static void main(String[] argv) throws Exception
  {
   double  m1=4.0026032497;
   //m1=19.99244018e0;//19.9924
   double m2=m1;
    double rMass=1822.888485*m1*m2/(m1+m2);
    double cutOff=1000.0*Math.sqrt(2.0);
    double massNorm=rMass;
    MyTextOutputJFrame outputFrame =new MyTextOutputJFrame();
    //AutoGridGenerator generator=new AutoGridGenerator("NeNePotential",rMass,cutOff,massNorm);
    AutoGridGenerator_exp generator=new AutoGridGenerator_exp("ttyPotential",rMass,cutOff,massNorm,outputFrame);
  }
// Fields:
  public WeightedPotential potential;
  double reducedMass;
  MonotoneSplineMap grading;
  MonotoneSplineMap theMapping;
  TwoBodyOperator h2;
  PrintWriter out;
  MyTextOutputJFrame outputFrame;
  private String gridName;
  SimpleGrid theGrid;
  //SimpleGrid mapGrid;
  // Constructors
//  public AutoGridGenerator(String potentialName,double reducedMass, double cutOff, double massNorm) throws Exception
//  {
//    double massFactor=2*reducedMass/massNorm;
//    double scaling=Math.sqrt(massFactor);
//    potential=new WeightedPotential(potentialName, scaling, massNorm);
//    System.out.print(potential.getDescription());
//    outputFrame =new MyTextOutputJFrame();
//    outputFrame.setVisible(true);
//    out=outputFrame.out;
////    outputFrame.notify();
//    this.reducedMass=reducedMass;
//    int n=150;
//    double t,res;
//    double rmax=cutOff;
//    this.gridName=potentialName+"_"+rmax+"_"+String.format("%1$6f", reducedMass)+".grd";
//    out.println("Configuring "+gridName);
//    SimpleGrid seed=seedSquareGrid(0, rmax, n);
////    for (int i=0;i<n;i++) out.println(seed.mesh[i]+"  "+potential.valueAt(seed.mesh[i]));
//    double z=0.0;
//    int l=0;
//    int nGradingElements=0;
//    for (int k=0;k<3;k++)
//    {
//      h2=new TwoBodyOperator(seed, z, l, potential,reducedMass,massNorm);
//      h2.printSpectrum(5,out);
//      nGradingElements=h2.getNumberOfBoundStates()+1;
//      MonotoneSplineMap gradfun= grading(nGradingElements,h2);
//      theMapping=getTheMap(gradfun);
//      seed=getTheGrid(theMapping);
//      //dumpTheMapping(gradfun);
//    }
//    h2=new TwoBodyOperator(seed, z, l, potential,reducedMass,massNorm);
//    h2.printSpectrum(5,out);
//    theGrid=seed;
//    out.println(gridName+" is configured.");
//    writeGridFile();
//    out.println("Checking convergence:");
//    for (int k=15;k<n;k++)
//    { out.print("n="+k+"  ");
//      seed=new SimpleGrid(k,getGridName());
//      h2=new TwoBodyOperator(seed, z, l, potential,reducedMass,massNorm);
//      h2.printSpectrum(5,out);
//    }
//  }
  //
  public AutoGridGenerator_exp(String potentialName,double reducedMass, double cutOff, double massNorm, MyTextOutputJFrame outputFrame) throws Exception
  {
    double massFactor=2*reducedMass/massNorm;
    double scaling=Math.sqrt(massFactor);
    potential=new WeightedPotential(potentialName, scaling, massNorm);
    System.out.print(potential.getDescription());
    this.outputFrame=outputFrame;
    outputFrame.setVisible(true);
    this.out=this.outputFrame.out;
//    outputFrame.notify();
    this.reducedMass=reducedMass;
    int n=150;
    double t,res;
    double rmax=cutOff;
    this.gridName=potentialName+"_"+rmax+"_"+String.format("%1$6f", reducedMass)+".grd";
    //this.mapGrid=seedTheGrid(0, rmax, n*2);
    out.println("Configuring "+gridName);
    SimpleGrid seed=seedTheGrid(0, rmax, n);
//    for (int i=0;i<n;i++) out.println(seed.mesh[i]+"  "+potential.valueAt(seed.mesh[i]));
    double z=0.0;
    int l=0;
    int nGradingElements=0;
    for (int k=0; k<3; k++)
    {
      System.out.println(k);
      h2=new TwoBodyOperator(seed, z, l, potential,reducedMass,massNorm);
      h2.printSpectrum(5,out);
      nGradingElements=h2.getNumberOfBoundStates()+2;
      SplineFunction1D gradfun= grading(nGradingElements,h2);
      theMapping=getTheMap(gradfun);
      seed=generateTheGrid(theMapping, n);
      seed.DisplayGrid();
      dumpTheGrading(gradfun);
      dumpTheMapping(theMapping);
    }
    h2=new TwoBodyOperator(seed, z, l, potential,reducedMass,massNorm);
    h2.printSpectrum(5,out);
    theGrid=seed;
    out.println(gridName+" is configured.");
    writeGridFile();
    out.println("Checking convergence:");
    for (int k=15;k<n;k++)
    { out.print("n="+k+"  ");
      //seed=new SimpleGrid(k,getGridName());
      //seed=seedTheGrid(0, rmax, k);
      seed=generateTheGrid(theMapping, k);
      h2=new TwoBodyOperator(seed, z, l, potential,reducedMass,massNorm);
      h2.printSpectrum(5,out);
    }

  }
  // Methods:
  static int calls=0;
  void dumpTheMapping(MonotoneSplineMap map)  throws Exception
  { PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("/home/roudnev/tmp/gnuplot/mapping"+calls+".dat"), 131072));
    calls++;
    double[] mesh0=map.getGridPoints().mesh;
    double[] mesh=seedTheGrid(mesh0[0], mesh0[mesh0.length-1], mesh0.length).mesh;
    double[] points=map.getControlPoints();
    //for (int i=0;i<mesh.length;i++) points[i]=map.valueAt(mesh[i]);
    for (int i=0;i<mesh0.length;i++)
      wrt.println(mesh0[i]+"   "+points[i]);
    wrt.close();
  }
    void dumpTheGrading(SplineFunction1D map)  throws Exception
  { PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("/home/roudnev/tmp/gnuplot/grading"+calls+".dat"), 131072));
    double[] mesh0=map.getColloc().mesh;
   // double[] mesh=seedTheGrid(mesh0[0], mesh0[mesh0.length-1], mesh0.length).mesh;
    double[] points=new double[mesh0.length+1];//map.getControlPoints();
    for (int i=0;i<mesh0.length;i++) points[i]=map.valueAt(mesh0[i]);
    for (int i=0;i<mesh0.length;i++)
      wrt.println(mesh0[i]+"   "+points[i]);
    wrt.close();
  }

  MonotoneSplineMap getTheMap(SplineFunction1D gradfun)
  { double t,res=0.0;
    int n=gradfun.getColloc().mesh.length+2;
    double[] colloc=gradfun.getColloc().mesh;
    double[] mapMesh=new double[n];
    double rmin=gradfun.getSpline().getGrid().mesh[0];
    double rmax=gradfun.getSpline().getGrid().mesh[gradfun.getSpline().getGrid().mesh.length-1];
    double[] c0=new double[n];
    c0[0]=0.0;
    mapMesh[0]=0.0;
    for (int i=0; i<colloc.length; i++) mapMesh[i+1]=colloc[i];
    mapMesh[mapMesh.length-1]=rmax-rmin;
    for (int i=1;i<c0.length;i++)
    {
      c0[i]=c0[i-1]+0.5*(mapMesh[i]-mapMesh[i-1])*(gradfun.valueAt(mapMesh[i])+gradfun.valueAt(mapMesh[i-1]));
    }
    for (int i=0;i<c0.length;i++)
      c0[i]=c0[i]*mapMesh[c0.length-1]/c0[c0.length-1];
    for (int i=0;i<mapMesh.length;i++)
      mapMesh[i]=mapMesh[i]-rmin;
    return new MonotoneSplineMap(c0,mapMesh);
  }
  private SplineFunction1D grading(int n, TwoBodyOperator h2)
  {
    HSplineBC spline=h2.getSplineSpace();
    char splineType=h2.getSplineSpace().getSplineType();
    int gradPower=13;
//    out.println("gp="+gradPower);
    if (splineType=='C') gradPower=9;
    int npoints=h2.getRank();
    double[] mesh=h2.getSplineColloc().mesh;
    double[] points=mesh.clone();
    SplineFunction1D u=new SplineFunction1D(spline);
    AnOperator minusD2=new FreeTwoBodyHamiltonian(spline, 0.0, 0);
    SplineFunction1D d2u;
    for (int i=0; i<points.length;i++) points[i]=0.0;
    for (int j=0; j<n; j++)
    {
      for (int i=0; i<u.coef.length;i++) u.coef[i]=h2.getTheSpectrum().getEigenvectorsRightRe()[i][j];
        d2u=(SplineFunction1D)minusD2.times(u);
        d2u=(SplineFunction1D)minusD2.times(d2u);
        if (gradPower==13) d2u=(SplineFunction1D)minusD2.times(d2u);
        for (int i=0;i<mesh.length;i++)
           points[i]+=Math.pow(Math.abs(d2u.valueAt(mesh[i])),2.0);
    }
    //points=smoothenArrayMedian(points);
    for (int i=0;i<mesh.length;i++)
    {
        points[i]=Math.pow(points[i],1.0/gradPower);
       // System.out.print("     "+points[i]);
    }
    SplineFunction1D res=new SplineFunction1D(spline);
    //points=smoothenArrayMedian(points);
    //points=smoothenArrayAverages(points);
    res.setCoefFromValues(points);
    // System.out.println();
    return res;
  }
  public SimpleGrid seedTheGrid(double rmin,double rmax, int n)
  {
    double[] seed=new double[n];
    double dt=1.0/(seed.length-1);
    double dy0=Math.min(1.0, 25.0/n);
    double c=dy0*n/(rmax-rmin);
    double k=1.0;
     double dk=1.0;
     while(Math.abs(dk)>1.0e-5)
     {
       dk=Math.log(k/c+1.0)-k;
       k=k+dk;
     }
    for (int i=0;i<n;i++) seed[i]= gMap(dt*i,k)/gMap(1.0,k);//Math.pow(dt*i,3);
    for (int i=0;i<n;i++) seed[i]=seed[i]*(rmax-rmin)+rmin;
    return new SimpleGrid(seed);
  }

  public SimpleGrid generateTheGrid(MonotoneSplineMap map, int n)
  {
      double[] mesh=new double[n];
      double umin=map.getGridPoints().mesh[0];
      double umax=map.getGridPoints().mesh[map.getGridPoints().mesh.length-1];
      double du=(umax-umin)/(n-1);
      double u;
      for (int i=0;i<n;i++)
      {
          u=du*i+umin;
          u=Math.min(u, umax-umin-1.0e-14);
          mesh[i]=map.valueAt(u);
      }

      return new SimpleGrid(mesh);
  }

  double gMap(double t, double k)
  {
       return Math.exp(k*t)-1;
  }


  public SimpleGrid getTheGrid(MonotoneSplineMap map)
  {
    int l=map.getGridPoints().mesh.length;
    double[] mesh=new double[l];
    double dx=map.getGridPoints().mesh[l-1]*(1.0-1e-15)/(l-1);
    for(int i=0;i<l;i++) mesh[i]=map.valueAt(dx*i);
    return new SimpleGrid(mesh,getGridName());
  }
  public void writeGridFile() throws Exception
  { DataOutputStream output=new DataOutputStream(new FileOutputStream(getGridName()));
    int setlength=theMapping.getGridPoints().mesh.length;
    output.writeInt(setlength);
    double[] mesh=theMapping.getGridPoints().mesh;
    double t,v;
    for (int i=0;i<setlength;i++)
    {
      t=mesh[i];
      v=theMapping.valueAt(t);
      output.writeDouble(v);
      output.writeDouble(t/mesh[setlength-1]);
    }
    output.close();
  }

  /**
   * @return the gridName
   */
  public String getGridName()
  {
    return gridName;
  }

  /**
   * @param gridName the gridName to set
   */
  public void setGridName(String gridName)
  {
    this.gridName = gridName;
  }

  private double[] smoothenArrayMedian(double[] a)
  { // median smoothing function
    double[] res=new double[a.length];
    int sampleLength=2;
    res[0]=a[0];
    double smin=0.0;
    double smax=0.0;
    for (int i=1; i<a.length; i++)
    {  smin=a[i];
       smax=a[i];
       for (int j=Math.max(0, i-sampleLength); j<Math.min(a.length, i+sampleLength); j++) //=i;j++)
       { smax=Math.max(smax, a[j]);
         smin=Math.min(smin, a[j]);
       }
       res[i]=0.5*(smin+smax);
    }
    return res;
  }


  private double[] smoothenArrayAverages(double[] a)
  { // median smoothing function
    double[] res=new double[a.length];
    int sampleLength=4;
    res[0]=a[0];
    double s=0.0;
    int jmin,jmax,ns;
    for (int i=1; i<a.length; i++)
    {  s=0;
       jmin=Math.max(0, i-sampleLength);
       jmax=Math.min(a.length-1, i+sampleLength);
       ns=jmax-jmin+1;
       if (ns>0)
       {
         for (int j=jmin; j<jmax+1; j++) s+=a[j];
         res[i]=s/ns;
       }
       else
       {
         res[i]=a[i];
       }
    }
    return res;
  }
}
