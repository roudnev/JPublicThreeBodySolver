package main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import hermitSplines.SimpleGrid;
import java.io.*;
import potentialCollection.*;
import basicAbstractions.APotential;
/**
 *
 * @author roudnev
 */
public class ConfiguratorEssentials
{
  protected ParticleDescriptor[] particles;
  boolean[] ifIDMarked;
  protected String[] interactions;
    protected String[] gridNames;
    protected double[] reducedMass;
    private double[] yReducedMass;
    protected double massNorm;
    int[] interactionIndex;
    protected  int[] gridsNx;
    protected int[] gridsNy;
    protected int[] gridsNz;
    protected double[] yMax;
    protected double[] xMax;
    protected int nComponents;
    private double aum=1822.888485;
  PotentialsRegister potentials;
  private double[] mass;
  APotential[] vpot;
  public ConfiguratorEssentials()
  {
    potentials=new PotentialsRegister();
    particles = new ParticleDescriptor[3];
    ifIDMarked = new boolean[3];
    interactions=new String[] {"","",""};
    interactionIndex=new int[] {0,0,0};
    gridNames=new String[]  {"","",""};
    gridsNx=new int[] {20,20,20};
    gridsNy=new int[] {20,20,20};
    gridsNz=new int[] {2,2,2};
    yMax=new double[] {1000.0,1000.0,1000.0};
    xMax=new double[] {1000.0,1000.0,1000.0};
    mass=new double[3];
    reducedMass=new double[3];
    yReducedMass=new double[3];
    massNorm=1.0;
    nComponents=3;
    for (int i = 0; i < 3; i++)
    {
      particles[i] = new ParticleDescriptor();
      ifIDMarked[i] = false;
//      System.out.println(particles[i].getDescription());
//      System.out.println("Mass[" + i + "]=" + particles[i].getM());
//      System.out.println("Is it a Boson? " + particles[i].isIfBoson());
//      System.out.println("Is it marked identical? " + ifIDMarked[i]);
    }
      particles[0].setM(4.0026032497); //3.01603
      particles[1].setM(4.0026032497);
      particles[2].setM(4.0026032497);
      try
      {
        readInteractionsConfigurationFile();
        boolean ifMTV=false;
        for (int iComp=0;iComp<3;iComp++)
        {
           String potname=interactions[iComp];
           ifMTV=ifMTV | (APotential)Class.forName("potentialCollection."+potname).newInstance() instanceof mtVPotential;
        }
//        for (int iComp=0;iComp<3;iComp++)
//           ifMTV=ifMTV | (interactionIndex[iComp]==2);
        if (ifMTV)
        {
            aum=1.0;
            System.out.println("!!!!!!!!!!!ifMTV="+ifMTV+"  aum="+aum);
        }
        readMassesConfigurationFile();
        recalculateReducedMasses();
        readIDConfigurationFile();
        readDiscretizationConfigurationFile();
      }
      catch (Exception e)
      {
        System.err.println(e+"  "+e.getMessage());
      }
  }

  public void recalculateReducedMasses()
  {
    double m1,m2;
    m1=particles[1].getM();
    m2=particles[2].getM();
    reducedMass[0]=aum*m1*m2/(m1+m2);
    //
    m1=particles[0].getM();
    m2=particles[2].getM();
    reducedMass[1]=aum*m1*m2/(m1+m2);
    //
    m1=particles[0].getM();
    m2=particles[1].getM();
    reducedMass[2]=aum*m1*m2/(m1+m2);
    massNorm=Math.max(reducedMass[0], reducedMass[1]);
    massNorm=Math.max(massNorm, reducedMass[2]);
    massNorm=2*massNorm;
     for (int iComp=0;iComp<3;iComp++)
      {
        double sum=0.0;
        double prod=1.0;
        for(int jComp=0;jComp<3;jComp++)
          if(iComp!=jComp)
          { sum+=mass[jComp];
            prod=prod*mass[jComp];
          }
        yReducedMass[iComp]=sum*mass[iComp]/(sum+mass[iComp]);
      }
  }

  public void performIDCheck()
  { nComponents=4;
    for (int i = 0; i < 3; i++)
    {
      if (this.ifIDMarked[i])
      {
        nComponents--;
        for (int j = i+1; j < 3; j++)
        {
          if (this.ifIDMarked[j])
          {
            this.particles[j].setDescription(this.particles[i].getDescription());
            this.particles[j].setM(this.particles[i].getM());
            this.particles[j].setIfBoson(this.particles[i].isIfBoson());
            this.interactionIndex[j]=this.interactionIndex[i];
            this.interactions[j]=this.interactions[i];
            gridsNx[j]=gridsNx[i];
            gridsNy[j]=gridsNy[i];
            gridsNz[j]=gridsNz[i];
          }
        }
      }
    }
    if (nComponents==4) nComponents--;
    recalculateReducedMasses();
  }

  public void writeConfigurations()
  {
    performIDCheck();
    writeMassesConfigurationFile();
    writeIDConfigurationFile();
    writeInteractionsConfigurationFile();
    writeDiscretizationConfigurationFile();
  }

  public void writeMassesConfigurationFile()
  {
    try
    {
      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./Masses.conf"), 131072));
      for (int i = 0; i < particles.length; i++)
      {
        wrt.println(particles[i].getM());
      }
      wrt.close();
    }
    catch (Exception e)
    {
      System.out.println("Error initializing masses:" + e.getMessage());
    }
  }

    public void readMassesConfigurationFile() throws Exception
  {

      BufferedReader in =new BufferedReader(new FileReader("./Masses.conf"));
      for (int i = 0; i < particles.length; i++)
      {
        String s=in.readLine();
        double m=Double.parseDouble(s);
        particles[i].setM(m);
      }
      in.close();
      for (int i=0;i<3;i++)
        mass[i]=particles[i].getM()*aum;
  }


  public void writeInteractionsConfigurationFile()
  {
    try
    {
      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./Potentials.conf"), 131072));
      wrt.println(interactions[0]);
      wrt.println(interactions[1]);
      wrt.println(interactions[2]);
      wrt.close();
    }
    catch(Exception e)
    {
      System.out.println("Error initializing interactions:" + e.getMessage());
    }
  }

  public void readInteractionsConfigurationFile() throws Exception
  {
      BufferedReader in = new BufferedReader(new FileReader("./Potentials.conf"));
      interactions[0]=in.readLine();
      interactions[1]=in.readLine();
      interactions[2]=in.readLine();
      in.close();
      interactionIndex[0]=potentials.getIndexForName(interactions[0]);
      interactionIndex[1]=potentials.getIndexForName(interactions[1]);
      interactionIndex[2]=potentials.getIndexForName(interactions[2]);
      //System.err.println(interactions[0]+"  "+interactionIndex[0]);
      //System.err.println(interactions[1]+"  "+interactionIndex[1]);
      //System.err.println(interactions[2]+"  "+interactionIndex[2]);
  }

  public void writeDiscretizationConfigurationFile()
  {
     try
    {
      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./Discretization.conf"), 131072));
      wrt.println(nComponents);
      for (int i = 0; i < nComponents; i++)
      {
        wrt.println(gridNames[i]);
        wrt.println(gridsNx[i]);
        wrt.println(yMax[i]);
        wrt.println(gridsNy[i]);
        wrt.println(gridsNz[i]);
      }
      wrt.close();
    }
    catch (Exception e)
    {
      System.out.println("Error initializing Discretization:" + e);
    }
  }

  public void readDiscretizationConfigurationFile() throws Exception
  {
      BufferedReader in = new BufferedReader(new FileReader("./Discretization.conf"), 131072);
      nComponents=Integer.parseInt(in.readLine().trim());
      for (int i = 0; i < nComponents; i++)
      {
        gridNames[i]=in.readLine().trim();
        gridsNx[i]=Integer.parseInt(in.readLine().trim());
        yMax[i]=Double.parseDouble(in.readLine().trim());
        gridsNy[i]=Integer.parseInt(in.readLine().trim());
        gridsNz[i]=Integer.parseInt(in.readLine().trim());
        SimpleGrid xGrid= new SimpleGrid(10, gridNames[i]);
        xMax[i]=xGrid.mesh[xGrid.mesh.length-1]/Math.sqrt(reducedMass[i]*2/massNorm);

//        System.err.println(gridNames[i]);
//        System.err.println(gridsNx[i]);
//        System.err.println(yMax[i]);
//        System.err.println(gridsNy[i]);
//        System.err.println(gridsNz[i]);

      }

      in.close();
  }

  public void writeIDConfigurationFile()
  {
    try
    {
      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./ID.conf"), 131072));
      int j = 0;
      for (int i = 0; i < 3; i++)
      {
        wrt.print(j + "  ");
        if (!ifIDMarked[i])
        {
          j += 1;
        }
      }
      wrt.println();
      if ((j == 0) || (j == 2))
      {
        wrt.print("1.0   1.0   1.0");
      }
      else
      {
        wrt.print("1.0  1.0  ");
          if (particles[2].isIfBoson())
          {
            wrt.print("1.0 ");
          }
          else
          {
            wrt.print("-1.0 ");
          }
      }
      wrt.println();
      if (j == 3)
      {
        wrt.println("1.0  1.0  1.0");
      }
      else
      {
        wrt.println("1.0  1.0  -1.0");
      }
      wrt.close();
    }
    catch (Exception e)
    {
      System.out.println("Error initializing identical particles:" + e.getMessage());
    }
  }

  public void readIDConfigurationFile() throws Exception
  {
      BufferedReader in = new BufferedReader(new FileReader("./ID.conf"));
      int j = 0;
      String idTable=in.readLine().trim();
      // => ifIDMarked[i]
      String parityTable=in.readLine().trim();
      // => ifBoson
      String zTable=in.readLine();
      in.close();
      String[] idTableArray=idTable.split("\\s+");
      int[] idTabInt=new int[idTableArray.length];
      for (int i=0; i<idTabInt.length; i++) idTabInt[i]=Integer.parseInt(idTableArray[i]);
      if (idTabInt.length!=3) System.err.println("IdTable length="+idTabInt.length+"  ;  "+idTabInt);
      if (idTabInt[0]==idTabInt[1])
      {
          ifIDMarked[0]=true;
          ifIDMarked[1]=true;
          ifIDMarked[2]=true;
      }
      else if (idTabInt[1]==idTabInt[2])
      {   ifIDMarked[0]=false;
          ifIDMarked[1]=true;
          ifIDMarked[2]=true;
      }
      else
      {   ifIDMarked[0]=false;
          ifIDMarked[1]=false;
          ifIDMarked[2]=false;
      }
      //for (int i=0; i<3; i++) System.err.println(ifIDMarked[i]);
      String[] parityTableArray=parityTable.split("\\s+");
      double[] parityTabDouble=new double[3];
      for (int i=0; i<3; i++) parityTabDouble[i]=Double.parseDouble(parityTableArray[i]);
      for (int i=0; i<3; i++) particles[i].setIfBoson(true);
      if (!ifIDMarked[0] && (ifIDMarked[1] && ifIDMarked[2]))
      {
          if (parityTabDouble[2]<0 )
          {
              particles[1].setIfBoson(false);
              particles[2].setIfBoson(false);
          }
      }
  }

    /**
     * @return the particles
     */
    public ParticleDescriptor[] getParticles() {
        return particles;
    }

    /**
     * @return the interactions
     */
    public String[] getInteractions() {
        return interactions;
    }

    /**
     * @return the gridNames
     */
    public String[] getGridNames() {
        return gridNames;
    }

    /**
     * @return the reducedMass
     */
    public double[] getReducedMass() {
        return reducedMass;
    }

    /**
     * @return the massNorm
     */
    public double getMassNorm() {
        return massNorm;
    }

    /**
     * @return the gridsNx
     */
    public int[] getGridsNx() {
        return gridsNx;
    }

    /**
     * @return the gridsNy
     */
    public int[] getGridsNy() {
        return gridsNy;
    }

    /**
     * @return the gridsNz
     */
    public int[] getGridsNz() {
        return gridsNz;
    }

    /**
     * @return the yMax
     */
    public double[] getyMax() {
        return yMax;
    }

    /**
     * @return the xMax
     */
    public double[] getxMax() {
        return xMax;
    }

    /**
     * @return the nComponents
     */
    public int getnComponents() {
        return nComponents;
    }

    /**
     * @return the mass
     */
    public double[] getMass() {
        return mass;
    }

    /**
     * @return the yReducedMass
     */
    public double[] getyReducedMass() {
        return yReducedMass;
    }
}




///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//
//
//import hermitSplines.SimpleGrid;
//import java.io.*;
//import potentialCollection.*;
//
///**
// *
// * @author roudnev
// */
//public class ConfiguratorEssentials
//{
//  ParticleDescriptor[] particles;
//  boolean[] ifIDMarked;
//  String[] interactions;
//  String[] gridNames;
//  double[] reducedMass;
//  double massNorm;
//  int[] interactionIndex;
//  int[] gridsNx;
//  int[] gridsNy;
//  int[] gridsNz;
//  double[] yMax;
//  double[] xMax;
//  int nComponents;
//  PotentialsRegister potentials;
//  public ConfiguratorEssentials()
//  {
//    potentials=new PotentialsRegister();
//    particles = new ParticleDescriptor[3];
//    ifIDMarked = new boolean[3];
//    interactions=new String[] {"","",""};
//    interactionIndex=new int[3];
//    gridNames=new String[]  {"","",""};
//    gridsNx=new int[] {20,20,20};
//    gridsNy=new int[] {20,20,20};
//    gridsNz=new int[] {2,2,2};
//    yMax=new double[] {1000.0,1000.0,1000.0};
//    xMax=new double[] {1000.0,1000.0,1000.0};
//    reducedMass=new double[3];
//    massNorm=1.0;
//    nComponents=3;
//    for (int i = 0; i < 3; i++)
//    {
//      particles[i] = new ParticleDescriptor();
//      ifIDMarked[i] = false;
////      System.out.println(particles[i].getDescription());
////      System.out.println("Mass[" + i + "]=" + particles[i].getM());
////      System.out.println("Is it a Boson? " + particles[i].isIfBoson());
////      System.out.println("Is it marked identical? " + ifIDMarked[i]);
//    }
//      particles[0].setM(4.0026032497); //3.01603
//      particles[1].setM(4.0026032497);
//      particles[2].setM(4.0026032497);
//      try
//      {
//        readMassesConfigurationFile();
//        recalculateReducedMasses();
//        readIDConfigurationFile();
//        readDiscretizationConfigurationFile();
//        readInteractionsConfigurationFile();
//      }
//      catch (Exception e)
//      {
//        System.err.println(e+"  "+e.getMessage());
//      }
//  }
//
//  public void recalculateReducedMasses()
//  {
//    double m1,m2;
//    m1=particles[1].getM();
//    m2=particles[2].getM();
//    reducedMass[0]=1822.888485*m1*m2/(m1+m2);
//    //
//    m1=particles[0].getM();
//    m2=particles[2].getM();
//    reducedMass[1]=1822.888485*m1*m2/(m1+m2);
//    //
//    m1=particles[0].getM();
//    m2=particles[1].getM();
//    reducedMass[2]=1822.888485*m1*m2/(m1+m2);
//    massNorm=Math.max(reducedMass[0], reducedMass[1]);
//    massNorm=Math.max(massNorm, reducedMass[2]);
//  }
//
//  public void performIDCheck()
//  { nComponents=4;
//    for (int i = 0; i < 3; i++)
//    {
//      if (this.ifIDMarked[i])
//      {
//        nComponents--;
//        for (int j = i+1; j < 3; j++)
//        {
//          if (this.ifIDMarked[j])
//          {
//            this.particles[j].setDescription(this.particles[i].getDescription());
//            this.particles[j].setM(this.particles[i].getM());
//            this.particles[j].setIfBoson(this.particles[i].isIfBoson());
//            this.interactionIndex[j]=this.interactionIndex[i];
//            this.interactions[j]=this.interactions[i];
//            gridsNx[j]=gridsNx[i];
//            gridsNy[j]=gridsNy[i];
//            gridsNz[j]=gridsNz[i];
//          }
//        }
//      }
//    }
//    if (nComponents==4) nComponents--;
//    recalculateReducedMasses();
//  }
//
//  public void writeConfigurations()
//  {
//    performIDCheck();
//    writeMassesConfigurationFile();
//    writeIDConfigurationFile();
//    writeInteractionsConfigurationFile();
//    writeDiscretizationConfigurationFile();
//  }
//
//  public void writeMassesConfigurationFile()
//  {
//    try
//    {
//      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./Masses.conf"), 131072));
//      for (int i = 0; i < particles.length; i++)
//      {
//        wrt.println(particles[i].getM());
//      }
//      wrt.close();
//    }
//    catch (Exception e)
//    {
//      System.out.println("Error initializing masses:" + e.getMessage());
//    }
//  }
//
//    public void readMassesConfigurationFile() throws Exception
//  {
//
//      BufferedReader in =new BufferedReader(new FileReader("./Masses.conf"));
//      for (int i = 0; i < particles.length; i++)
//      {
//        String s=in.readLine();
//        double m=Double.parseDouble(s);
//        particles[i].setM(m);
//      }
//      in.close();
//  }
//
//
//  public void writeInteractionsConfigurationFile()
//  {
//    try
//    {
//      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./Potentials.conf"), 131072));
//      wrt.println(interactions[0]);
//      wrt.println(interactions[1]);
//      wrt.println(interactions[2]);
//      wrt.close();
//    }
//    catch(Exception e)
//    {
//      System.out.println("Error initializing interactions:" + e.getMessage());
//    }
//  }
//
//  public void readInteractionsConfigurationFile() throws Exception
//  {
//      BufferedReader in = new BufferedReader(new FileReader("./Potentials.conf"));
//      interactions[0]=in.readLine();
//      interactions[1]=in.readLine();
//      interactions[2]=in.readLine();
//      in.close();
//      interactionIndex[0]=potentials.getIndexForName(interactions[0]);
//      interactionIndex[1]=potentials.getIndexForName(interactions[1]);
//      interactionIndex[2]=potentials.getIndexForName(interactions[2]);
//      //System.err.println(interactions[0]+"  "+interactionIndex[0]);
//      //System.err.println(interactions[1]+"  "+interactionIndex[1]);
//      //System.err.println(interactions[2]+"  "+interactionIndex[2]);
//  }
//
//  public void writeDiscretizationConfigurationFile()
//  {
//     try
//    {
//      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./Discretization.conf"), 131072));
//      wrt.println(nComponents);
//      for (int i = 0; i < nComponents; i++)
//      {
//        wrt.println(gridNames[i]);
//        wrt.println(gridsNx[i]);
//        wrt.println(yMax[i]);
//        wrt.println(gridsNy[i]);
//        wrt.println(gridsNz[i]);
//      }
//      wrt.close();
//    }
//    catch (Exception e)
//    {
//      System.out.println("Error initializing Discretization:" + e);
//    }
//  }
//
//  public void readDiscretizationConfigurationFile() throws Exception
//  {
//      BufferedReader in = new BufferedReader(new FileReader("./Discretization.conf"), 131072);
//      nComponents=Integer.parseInt(in.readLine().trim());
//      for (int i = 0; i < nComponents; i++)
//      {
//        gridNames[i]=in.readLine().trim();
//        gridsNx[i]=Integer.parseInt(in.readLine().trim());
//        yMax[i]=Double.parseDouble(in.readLine().trim());
//        gridsNy[i]=Integer.parseInt(in.readLine().trim());
//        gridsNz[i]=Integer.parseInt(in.readLine().trim());
//        SimpleGrid xGrid= new SimpleGrid(10, gridNames[i]);
//        xMax[i]=xGrid.mesh[xGrid.mesh.length-1]/Math.sqrt(reducedMass[i]*2/massNorm);
//
////        System.err.println(gridNames[i]);
////        System.err.println(gridsNx[i]);
////        System.err.println(yMax[i]);
////        System.err.println(gridsNy[i]);
////        System.err.println(gridsNz[i]);
//
//      }
//
//      in.close();
//  }
//
//  public void writeIDConfigurationFile()
//  {
//    try
//    {
//      PrintStream wrt = new PrintStream(new BufferedOutputStream(new FileOutputStream("./ID.conf"), 131072));
//      int j = 0;
//      for (int i = 0; i < 3; i++)
//      {
//        wrt.print(j + "  ");
//        if (!ifIDMarked[i])
//        {
//          j += 1;
//        }
//      }
//      wrt.println();
//      if ((j == 0) || (j == 2))
//      {
//        wrt.print("1.0   1.0   1.0");
//      }
//      else
//      {
//        wrt.print("1.0  1.0  ");
//          if (particles[2].isIfBoson())
//          {
//            wrt.print("1.0 ");
//          }
//          else
//          {
//            wrt.print("-1.0 ");
//          }
//      }
//      wrt.println();
//      if (j == 3)
//      {
//        wrt.println("1.0  1.0  1.0");
//      }
//      else
//      {
//        wrt.println("1.0  1.0  -1.0");
//      }
//      wrt.close();
//    }
//    catch (Exception e)
//    {
//      System.out.println("Error initializing identical particles:" + e.getMessage());
//    }
//  }
//
//  public void readIDConfigurationFile() throws Exception
//  {
//      BufferedReader in = new BufferedReader(new FileReader("./ID.conf"));
//      int j = 0;
//      String idTable=in.readLine().trim();
//      // => ifIDMarked[i]
//      String parityTable=in.readLine().trim();
//      // => ifBoson
//      String zTable=in.readLine();
//      in.close();
//      String[] idTableArray=idTable.split("\\s+");
//      int[] idTabInt=new int[idTableArray.length];
//      for (int i=0; i<idTabInt.length; i++) idTabInt[i]=Integer.parseInt(idTableArray[i]);
//      if (idTabInt.length!=3) System.err.println("IdTable length="+idTabInt.length+"  ;  "+idTabInt);
//      if (idTabInt[0]==idTabInt[1])
//      {
//          ifIDMarked[0]=true;
//          ifIDMarked[1]=true;
//          ifIDMarked[2]=true;
//      }
//      else if (idTabInt[1]==idTabInt[2])
//      {   ifIDMarked[0]=false;
//          ifIDMarked[1]=true;
//          ifIDMarked[2]=true;
//      }
//      else
//      {   ifIDMarked[0]=false;
//          ifIDMarked[1]=false;
//          ifIDMarked[2]=false;
//      }
//      //for (int i=0; i<3; i++) System.err.println(ifIDMarked[i]);
//      String[] parityTableArray=parityTable.split("\\s+");
//      double[] parityTabDouble=new double[3];
//      for (int i=0; i<3; i++) parityTabDouble[i]=Double.parseDouble(parityTableArray[i]);
//      for (int i=0; i<3; i++) particles[i].setIfBoson(true);
//      if (!ifIDMarked[0] && (ifIDMarked[1] && ifIDMarked[2]))
//      {
//          if (parityTabDouble[2]<0 )
//          {
//              particles[1].setIfBoson(false);
//              particles[2].setIfBoson(false);
//          }
//      }
//  }
//}
