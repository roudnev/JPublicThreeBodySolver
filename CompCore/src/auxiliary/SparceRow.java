package auxiliary;

/**
 * <p>Title: Coulomb 3B</p>
 * <p>Description: Quantum Coulomb 3b with 2 identical particles</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author Vladimir A. Roudnev
 * @version 1.0
 */

public class SparceRow
{
  //fields:
  public double[] a;
  public int[] j;
  public int nz;
  public SparceRow()
  { nz=650;
    a=new double[nz];
    j=new int[nz];
  }
  public void compress()
  { for (int i=0; i<nz; i++)
      for (int k=i+1; k<nz; k++)
        if (j[k]==j[i])
        { a[i]+=a[k];
          a[k]=0.0;
          //System.err.println(i+" and "+k+" are matching, compressed");
        }
  }
}