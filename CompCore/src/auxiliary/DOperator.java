package auxiliary;
import basicAbstractions.*;
public class DOperator implements AnOperator
{ int rank;
  double[][] a;
  public DOperator(double[][] h)
  { rank=h.length;
    a=new double[rank][rank];
    for(int i=0;i<rank;i++)
    { for(int j=0;j<rank;j++) a[i][j]=h[i][j];
    }
  }
  public AFunction times(AFunction u)
  { AFunction res=new DFunction(rank);
    double s=0.0;
    for(int i=0;i<rank;i++)
    { s=0.0;
      for(int k=0;k<rank;k++) s+=a[i][k]*u.coef[k];
      res.coef[i]=s;
    }
    return res;
  }
  public int getRank()
  { return rank;
  }
}
