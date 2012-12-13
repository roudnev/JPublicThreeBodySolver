package decompositions;
import Jama.*;
import java.io.*;
/**
 * Title:        Few-Body Solver
 * Description:  A software package for bound state and scattering calculations in  2- 3- and 4-body systems.
 * Copyright:    Copyright (c) 2001
 * Company:      St-Petersburg State University, Dept. Comp. Phys.
 * @author Vladimir Roudnev
 * @version 1.5
 */

public class NativeEigenvalueDecomposition
{
// fields:
  private static int counter=0;
  private Matrix theMatrix;
  private double[]
           eigenvaluesRe,
           eigenvaluesIm;
  private double[]
           eigenvaluesInvRe,
           eigenvaluesInvIm;
  private double[][]
           eigenvectorsRightRe,
	   eigenvectorsRightIm,
	   eigenvectorsLeftRe,
	   eigenvectorsLeftIm;
  private boolean ifSortAbs=false;
  private EigenvalueDecomposition edM;

// constructors:
  public NativeEigenvalueDecomposition(Matrix aMatrix)
  { counter++;
    theMatrix=new Matrix(aMatrix.getArrayCopy());
    int cdim=theMatrix.getColumnDimension();
    int rdim=theMatrix.getRowDimension();
    //
    long startTime,endTime;
    //
    if (cdim!=rdim)
    { System.err.println(
      "NativeEigenvalueDecomposition constructor: the matrix is not square");
    }
    else
    { eigenvaluesRe=new double[rdim];
      eigenvaluesIm=new double[rdim];
      eigenvaluesInvRe=new double[rdim];
      eigenvaluesInvIm=new double[rdim];
      eigenvectorsRightRe=new double[rdim][rdim];
      eigenvectorsRightIm=new double[rdim][rdim];
      eigenvectorsLeftRe=new double[rdim][rdim];
      eigenvectorsLeftIm=new double[rdim][rdim];
      double[] v0=new double[rdim];
      double[] v1=new double[rdim];
      double[] v2=new double[rdim];
      startTime=System.currentTimeMillis();
      edM=new EigenvalueDecomposition(theMatrix);
      endTime=System.currentTimeMillis();
      //System.out.println("finished in "+(0.001*(endTime-startTime))+"seconds");
      setEigenvectorsAndEigenvalues();
      //

      //dump();
/*
       for (int i=0;i<eigenvaluesRe.length;i++)
      { System.out.print(eigenvaluesRe[i]+" ");
        System.out.println(eigenvaluesIm[i]);
      }
      double[][] matrU=multiply(eigenvectorsRightRe,eigenvectorsLeftRe);
      for (int i=0;i<rdim;i++)
      {
        for (int j=0;j<rdim;j++)
        { System.out.print((float)matrU[i][j]+"   ");
        }
	System.out.println();
      }

      matrU=multiply(theMatrix.getArray(),eigenvectorsRightRe);
      matrU=multiply(eigenvectorsLeftRe,matrU);
      for (int i=0;i<rdim;i++)
      {
        for (int j=0;j<rdim;j++)
        { System.out.print((float)matrU[i][j]+"  ");
        }
	System.out.println();
      } */
    }
  }
// methods:
  public void dump()
  { // for debuggin purposes
    try
    {
      java.io.BufferedWriter
        dumpWriter=new java.io.BufferedWriter(
                        new java.io.FileWriter("specrum2b-"+counter+".dat")
                      );
      for (int i=0;i<eigenvaluesRe.length;i++)
      { dumpWriter.write(eigenvaluesRe[i]+" ");
        dumpWriter.write(eigenvaluesIm[i]+" ");
        dumpWriter.newLine();
      }
      dumpWriter.close();
    }
    catch(Exception e)
    { System.err.println("Error initializing MultocomponentDiscretization3D:"+e.getMessage());
    }
  }
  private void setEigenvectorsAndEigenvalues()
      //(EigenvalueDecomposition edM)
  { eigenvaluesRe=edM.getRealEigenvalues();
    eigenvaluesIm=edM.getImagEigenvalues();
    int rank=eigenvaluesRe.length;
    Matrix v=edM.getV();
    sort(v);
    Matrix w=v.inverse();
    Matrix d=edM.getD();
    Matrix lRe=new Matrix(rank,rank);
    Matrix lIm=new Matrix(rank,rank);
    Matrix rRe=new Matrix(rank,rank);
    Matrix rIm=new Matrix(rank,rank);
    for (int i=0; i<rank; i++)
    { lRe.set(i,i,1.0);
      rRe.set(i,i,1.0);
    }
    for (int i=0; i<rank-1; i++)
    if ((Math.abs(d.get(i,i)-d.get(i+1,i+1))<1.0e-12)
         &
        (Math.abs(d.get(i,i+1)+d.get(i+1,i))<1.0e-12))
    {
      lRe.set(i  ,i+1, 1.0/Math.sqrt(2.0));
      lRe.set(i+1,i+1, 1.0/Math.sqrt(2.0));
      lRe.set(i  ,i  , 0.0);
      lRe.set(i+1,i  , 0.0);

      lIm.set(i  ,i  , 1.0/Math.sqrt(2.0));
      lIm.set(i+1,i  ,-1.0/Math.sqrt(2.0));
      lIm.set(i  ,i+1, 0.0);
      lIm.set(i+1,i+1, 0.0);

      rRe.set(i+1,i  , 1.0/Math.sqrt(2.0));
      rRe.set(i+1,i+1, 1.0/Math.sqrt(2.0));
      rRe.set(i  ,i  , 0.0);
      rRe.set(i  ,i+1, 0.0);

      rIm.set(i  ,i  ,-1.0/Math.sqrt(2.0));
      rIm.set(i  ,i+1, 1.0/Math.sqrt(2.0));
      rIm.set(i+1,i  , 0.0);
      rIm.set(i+1,i+1, 0.0);
    }
    Matrix vIm=v.times(rIm);
    Matrix vRe=v.times(rRe);
    Matrix wIm=lIm.times(w);
    Matrix wRe=lRe.times(w);
    eigenvectorsRightRe=vRe.getArray();
    eigenvectorsRightIm=vIm.getArray();
    eigenvectorsLeftRe=wRe.getArray();
    eigenvectorsLeftIm=wIm.getArray();
    double r,re,im;
    for (int i=0;i<rank;i++)
    {
      r=Math.pow(eigenvaluesRe[i],2)+Math.pow(eigenvaluesIm[i],2);
      re=eigenvaluesRe[i]/r;
      im=-eigenvaluesIm[i]/r;
      eigenvaluesInvRe[i]=re;
      eigenvaluesInvIm[i]=im;
    }
  }
  //
  public double[][] getEigenvectorsRightRe()
  {
    return eigenvectorsRightRe;
  }
  //
  public double[][] getEigenvectorsLeftRe()
  {
    return eigenvectorsLeftRe;
  }
  //
  public void setEigenvectorsLeftRe(double[][] evl)
  {
    eigenvectorsLeftRe=evl;
  }
  //
  public double[][] getEigenvectorsRightIm()
  {
    return eigenvectorsRightIm;
  }
  //
  public double[][] getEigenvectorsLeftIm()
  {
    return eigenvectorsLeftIm;
  }
  //
  public void setEigenvectorsLeftIm(double[][] evl)
  {
    eigenvectorsLeftIm=evl;
  }
  //
  public double[] getEigenvaluesRe()
  {
    return eigenvaluesRe;
  }
  //
  public double[] getEigenvaluesIm()
  {
    return eigenvaluesIm;
  }
  public double[] getEigenvaluesInvRe()
  {
    return eigenvaluesInvRe;
  }
  //
  public double[] getEigenvaluesInvIm()
  {
    return eigenvaluesInvIm;
  }
  //
  public final void setIfSortAbs(boolean ifsa)
  { this.ifSortAbs=ifsa;
    this.setEigenvectorsAndEigenvalues();
  }
  //
  private void sort(Matrix w)
  {
      //IMPLICIT REAL*8(A-H,O-Z)
      //DIMENSION E(N),EI(N),W(LD,N),WI(LD,N),V(N)
    int n=theMatrix.getRowDimension();
    double[] v=new double[n];
    double vmx,t;
    int kmx;
    for (int i=0;i<n;i++)
    {
      if (ifSortAbs)
        v[i]=Math.abs(eigenvaluesRe[i]);
      else
        v[i]=eigenvaluesRe[i];
    }
    for (int i=1;i<=n;i++)
    { vmx=v[n-i];
      kmx=n-i;
      for (int j=0; j<n-i;j++)
      {
        if (v[j]>vmx)
        {
          vmx=v[j];
          kmx=j;
        }
      }
      v[kmx]=v[n-i];
      v[n-i]=vmx;
//
      t=eigenvaluesRe[kmx];
      eigenvaluesRe[kmx]=eigenvaluesRe[n-i];
      eigenvaluesRe[n-i]=t;
//
      t=eigenvaluesIm[kmx];
      eigenvaluesIm[kmx]=eigenvaluesIm[n-i];
      eigenvaluesIm[n-i]=t;
      for (int j=0; j<n;j++)
      {
        t=w.get(j,kmx);
        w.set(j,kmx,w.get(j,n-i));
        w.set(j,n-i,t);
//
//          T=WI(J,KMX)
//          WI(J,KMX)=WI(J,N-I+1)
//          WI(J,N-I+1)=T
      }
     }
   }
// debugging methods
  private double[][] multiply(double[][] a,double[][] b)
  { double[][] res=new double[a.length][b.length];
    int n=a.length;
    for (int i=0;i<n;i++)
    { for (int j=0;j<n;j++)
      { res[i][j]=0.0;
        for (int k=0;k<n;k++)
        { res[i][j]+=a[i][k]*b[k][j];
        }
      }
    }
    return res;
  }
  //
  private double[][] multiply(double[] a,double[][] b)
  { double[][] res=new double[b.length][b.length];
    int n=b.length;
    for (int i=0;i<n;i++)
    { for (int j=0;j<n;j++)
      { res[i][j]=0.0;
        for (int k=0;k<n;k++)
        { res[i][j]+=a[i*n+k]*b[k][j];
        }
      }
    }
    return res;
  }
}