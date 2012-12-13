package threeBodyOperators;
import basicAbstractions.APotential;
import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import basicAbstractions.Solvable;
import basicAbstractions.Simple;
import hermitSplines.HSplineBC;
import hermitSplines.HSplineFree;
import hermitSplines.SummationLimits;
import Jama.Matrix;
import decompositions.MyEigenvalueDecomposition;
import hermitSplines.HSplineIntegrator;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.Format;
import potentialCollection.WeightedPotential;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 0.5
 */

public class ClusterThreeBodyHamiltonian extends FreeThreeBodyHamiltonian
{
  static int instanceCounter=0;
  APotential vPot;
  int[] nChannelBoundStates;
  double[][] zStateOrthogonalProjector;
  double[][][] xStateOrthogonalProjector;
  double[] zNorm;
  public ClusterThreeBodyHamiltonian(APotential vPot, Discretization3D d3D, Discretization3D rd3D)
  {
    super();
    this.d3D=d3D;
    this.rd3D=rd3D;
    this.vPot=vPot;
    zOperator=super.makeZoperator();
    normalizeAngularStates();
    nChannelBoundStates=new int[zOperator.rank];
    xOperator=this.makeXoperator();
    for (int i=0;i<nChannelBoundStates.length;i++) nChannelBoundStates[i]=xOperator[i].getNumberOfNegativeEigenvalues();
    normalizeBoundStates();
    yOperator=super.makeYoperator();
   // printLowEnergyParameters();
    initOrthogonalProjectors();
  }

  private void initOrthogonalProjectors()
  {
      // Not finished;
      zNorm=new double[zOperator.rank];
      zStateOrthogonalProjector=new double[6][zOperator.rank];
      xStateOrthogonalProjector=new double[6][][];
      for (int i=0; i<6; i++) xStateOrthogonalProjector[i]=new double[nChannelBoundStates[i]][xOperator[i].rank];
      d3D.getXSpline().fillOverlapMatrix();
      d3D.getZSpline().fillOverlapMatrix();
      Matrix omz=new Matrix(d3D.getZSpline().overlapMatrix);
      Matrix z0=new Matrix(zOperator.getEigenvectorsRightRe());
      Matrix z1=omz.times(z0);
      Matrix zn=z0.transpose().times(z1);
      for (int i=0; i<zNorm.length;i++) zNorm[i]=Math.sqrt(zn.get(i, i));
      //zn.print(new PrintWriter(System.out, true), zn.rank(), 10);
  }

  protected MyEigenvalueDecomposition[] makeXoperator()
  {
    instanceCounter++;
    //System.out.println("makeXoperator() in ClusterThreeBodyOperator is called");
    double zl;
    int nz=getzOperator().getEigenvaluesRe().length;
    int nx=d3D.getNX();
    int rnx=rd3D.getNX();
    double[][] dX=new double[nx][nx];
    double[][] iX=new double[nx][nx];
    double[][] riX=new double[nx][nx];
    Matrix dMatrix;
    Matrix iMatrix;
    Matrix tmpM;
    double xi;
    HSplineBC spl=d3D.getXSpline();
    MyEigenvalueDecomposition[] res=new MyEigenvalueDecomposition[nz];
    int iL=0,iR=0;
    SummationLimits limits=new SummationLimits(0,0);
    for (int lz=0;lz<nz;lz++)
    {
      zl=   getzOperator().getEigenvaluesRe()[lz];
      for (int i=0;i<nx;i++)
      {
        xi=d3D.getXCollocGrid().mesh[i];
        limits=spl.getLimits(xi,limits);
        iL=limits.LeftLimit; iR=limits.RightLimit;
       // System.out.println(""+xi+" "+iL+" "+iR);
        for (int j=iL;j<=iR;j++)
        {
          dX[i][j]=-spl.d2BSpline(xi,j)+spl.fBSpline(xi,j)*(zl/(xi*xi)+vPot.valueAt(xi));
          iX[i][j]=spl.fBSpline(xi,j);
        //  System.out.print("  "+iX[i][j]);
        }
      //  System.out.println();
      }
      dMatrix=new Matrix(dX);
      iMatrix=new Matrix(iX);
      idX=iMatrix;
      tmpM=idX.inverse().times(dMatrix);
      res[lz]=new MyEigenvalueDecomposition(tmpM);
      try
      {
        java.io.PrintStream
          dumpWriter=new java.io.PrintStream(
                          new java.io.FileOutputStream("XSpectrumForZl.eq."+Math.round(zl)+"."+instanceCounter+".dat")
                        );
        for (int i=0;i<res[lz].getEigenvaluesIm().length;i++)
        { dumpWriter.print(" "+res[lz].getEigenvaluesRe()[i]+" + "+res[lz].getEigenvaluesIm()[i]+"I");
          dumpWriter.println();
        }
        dumpWriter.close();
      }
      catch(Exception e)
      { System.err.println("Error initializing MultocomponentDiscretization3D:"+e.getMessage());
      }
      //System.out.println();
       /*
      tmpM=new Matrix( res[lz].getEigenvectorsLeftRe());
      tmpM=tmpM.times(iMatrix.inverse());
      res[lz].setEigenvectorsLeftRe(tmpM.getArrayCopy());
      tmpM=new Matrix( res[lz].getEigenvectorsLeftIm());
      tmpM=tmpM.times(iMatrix.inverse());
      res[lz].setEigenvectorsLeftIm(tmpM.getArrayCopy()); */
    }
    spl=d3D.getXSpline();
    for (int i=0;i<nx;i++)
    {
      xi=d3D.getXCollocGrid().mesh[i];
      for (int j=0;j<nx;j++)
      {
        riX[i][j]=spl.fBSpline(xi,j);
      }
    }
    idXinv=new Matrix(riX);
    idXinv=idXinv.inverse();
    return res;
  }

  public void printLowEnergyParameters(int ic)
  { java.io.PrintStream output=System.out;
    int numberOfBoundStates=xOperator[0].getNumberOfNegativeEigenvalues();
    WeightedPotential potential=(WeightedPotential)vPot;
    output.println();
    for (int i=0;i<numberOfBoundStates;i++)
      {
         output.format("          E%1$-15d",i);
      }
      output.println(" Scattering Length   Effective Range    Shape factor");
    output.print("#2body#"+ic);
    for(int i=0;i<numberOfBoundStates;i++)
    { output.format("  %2$ 15e  ",i,xOperator[0].getEigenvaluesRe()[i]/potential.getCoupling(),xOperator[0].getEigenvaluesIm()[i]/potential.getCoupling());
    }
    int nContStates=3;
    double[] spectrum=xOperator[0].getEigenvaluesRe();
    double[] contStateEnergyArray=new double[nContStates];
    double[] deltaArray=new double[nContStates];
    double[] kctgArray=new double[nContStates];
    Matrix rhs=new Matrix(nContStates,1);
    Matrix matr=new Matrix(nContStates,3);
    Matrix res=new Matrix(nContStates,1);
    double k;
    double rMax=d3D.getXBaseGrid().mesh[d3D.getXBaseGrid().mesh.length-1];
    //    System.out.println("rMax="+rMax);
    double skn2=0.0;
    double skn4=0.0;
    double skctg=0.0;
    double sk2kctg=0.0;
    for(int i=0;i<contStateEnergyArray.length;i++)
    {
        contStateEnergyArray[i]=spectrum[numberOfBoundStates+i];
    //        System.out.println("Energy="+contStateEnergyArray[i]);
        k=Math.sqrt(contStateEnergyArray[i]);
        deltaArray[i]=(i+0.0)*Math.PI-k*rMax;
    //        System.out.println("Delta="+deltaArray[i]);
        kctgArray[i]=k/Math.tan(deltaArray[i]);
        matr.set(i, 0, -1.0);
        matr.set(i, 1, 0.5*k*k);
        matr.set(i, 2, k*k*k*k);
        rhs.set(i, 0, kctgArray[i]);
    //        skn2+=k*k;
    //        skn4+=k*k*k*k;
    //        skctg+=kctgArray[i];
    //        sk2kctg+=k*k*kctgArray[i];
      }
    //    matr.set(0, 0, 1.0*nContStates);
    //    matr.set(0, 1, -0.5*skn2);
    //    matr.set(1, 0, -0.5*skn2);
    //    matr.set(0, 0, 0.25*skn4);
    //    rhs.set(0, 0, -skctg);
    //    rhs.set(1, 0, 0.5*sk2kctg);
      res=matr.solve(rhs);
      double a=1.0/res.getArray()[0][0]/potential.getScale();
      double r0=res.getArray()[1][0]/potential.getScale();
      double x=res.getArray()[2][0]/potential.getScale()/(r0*r0*r0);
    //    for (int i=0;i<nContStates;i++)
    //        System.out.print((-1.0/a+0.5*r0*contStateEnergyArray[i]+contStateEnergyArray[i]*contStateEnergyArray[i]*x)+"  ");
    //    System.out.println();
    //    for (int i=0;i<nContStates;i++)
    //        System.out.print(kctgArray[i]+"  ");
    //    System.out.println();

      output.format("  %1$ 15e  %2$ 15e    %3$ 15e  ",a,r0,x);
      output.println();
  }

  protected void normalizeAngularStates()
  {
      double[][] evecRightRe,evecLeftRe;

      HSplineIntegrator splInt = new HSplineIntegrator();
      HSplineFree spl=d3D.getZSpline();
      int NSPN=spl.getSplinesPerNode();
      splInt.calcSplineIntegrals(spl);
      double[][] ovlpMat = splInt.splProdIntegral;

      evecRightRe=zOperator.getEigenvectorsRightRe();
      evecLeftRe=zOperator.getEigenvectorsLeftRe();
      int n=evecLeftRe.length;
      double norm;
      for (int i=0;i<n;i++)
      {
        norm = 0;
        for (int ifu=0;ifu<n;ifu++)
        {
          for (int jfu = Math.max(ifu-2*NSPN, 0); jfu < Math.min(ifu+2*NSPN,n); jfu++)
            norm += ovlpMat[ifu][jfu]*(evecRightRe[ifu][i]*evecRightRe[jfu][i]);
        }
        norm=Math.sqrt(norm);
        //System.out.println(i+"  "+l+"  "+norm);

        for (int ifu=0;ifu<n;ifu++)
        {
          evecRightRe[ifu][i] = evecRightRe[ifu][i]/norm;
          evecLeftRe[i][ifu] = evecLeftRe[i][ifu]*norm;
        }
      }
  }

    //
  protected void normalizeBoundStates()
  {
  //    double normDistortion=2.0;
      double[][] evecRightRe,evecLeftRe;
      double[][] evecRightIm,evecLeftIm;

      HSplineIntegrator splInt = new HSplineIntegrator();
      HSplineBC spl=d3D.getXSpline();
      int NSPN=spl.getSplinesPerNode();
      splInt.calcSplineIntegrals(spl);
      double[][] ovlpMat = splInt.splProdIntegral;
      //  java.io.PrintStream wrt=new java.io.PrintStream(new java.io.BufferedOutputStream( new java.io.FileOutputStream("tmp1.txt"), 131072));
      for (int l=0;l<xOperator.length;l++)
      {
          evecRightRe=xOperator[l].getEigenvectorsRightRe();
          evecRightIm=xOperator[l].getEigenvectorsRightIm();
          evecLeftRe=xOperator[l].getEigenvectorsLeftRe();
          evecLeftIm=xOperator[l].getEigenvectorsLeftIm();
          int n=evecLeftIm.length;
          int nBoundStates=nChannelBoundStates[l];
          double norm;
          for (int i=0;i<n;i++)
          {
            norm = 0;
            for (int ifu=0;ifu<n;ifu++)
            {
              for (int jfu = Math.max(ifu-2*NSPN, 0); jfu < Math.min(ifu+2*NSPN,n); jfu++)
               // for (int jfu = 0; jfu < n; jfu++)
                norm += ovlpMat[ifu][jfu]*(evecRightRe[ifu][i]*evecRightRe[jfu][i] + evecRightIm[ifu][i]*evecRightIm[jfu][i]);
            }
            norm=Math.sqrt(norm);
            //System.out.println(i+"  "+l+"  "+norm);

            for (int ifu=0;ifu<n;ifu++)
            {
              evecRightRe[ifu][i] = evecRightRe[ifu][i]/norm;
              evecRightIm[ifu][i] = evecRightIm[ifu][i]/norm;
              evecLeftRe[i][ifu] = evecLeftRe[i][ifu]*norm;
              evecLeftIm[i][ifu] = evecLeftIm[i][ifu]*norm;
            }
          }
      }

 //     wrt.close();
      //System.exit(-1);
  }

}