/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hermitSplines;

/**
 *
 * @author vitaly
 */
public class HSplineIntegrator {
    
    public double[] splIntegral;
    public double[][] splProdIntegral;
    
    public void calcSplineIntegrals(AbstractHermitSpline s)
    {
      
      
      int n=s.getNMax(); //max index
      int NSPN=s.getSplinesPerNode();
      SimpleGrid theGrid = s.getGrid();
      
      SummationLimits limits = new SummationLimits(0,0);
      double a,b,midp,half;
      double dx0,dx1,dx2;
      double[] Weight = new double[3];
      double[] gaussPoints={0.2386191860831969086305017,0.6612093864662645136613996,0.9324695142031520278123016};
      double[] gaussWeights={0.4679139345726910473898703,0.3607615730481386075698335,0.1713244923791703450402961};
      
      splIntegral = new double[n+1];
      splProdIntegral = new double[n+1][n+1];
      java.util.Arrays.fill(splIntegral, 0);
      for (int ifu = 0; ifu <= n; ifu++ )
        java.util.Arrays.fill(splProdIntegral[ifu], 0);
      
      switch(s.getSplineType())
      {
        case 'Q':
          for (int i = 0; i<(theGrid.mesh.length - 1); i++)
          {
            a = theGrid.mesh[i];
            b = theGrid.mesh[i+1];
            midp = (a+b)/2;
            half = (b-a)/2;
            
            dx0 = half*gaussPoints[0];
            dx1 = half*gaussPoints[1];
            dx2 = half*gaussPoints[2];
            
            Weight[0] = half*gaussWeights[0];
            Weight[1] = half*gaussWeights[1];
            Weight[2] = half*gaussWeights[2];
            
            s.getLimits( midp , limits );
            for (int ifu = limits.LeftLimit; ifu <= limits.RightLimit; ifu++)
            {

              splIntegral[ifu] += Weight[0]*(s.fBSpline(midp - dx0, ifu) + s.fBSpline(midp + dx0, ifu));
              splIntegral[ifu] += Weight[1]*(s.fBSpline(midp - dx1, ifu) + s.fBSpline(midp + dx1, ifu));
              splIntegral[ifu] += Weight[2]*(s.fBSpline(midp - dx2, ifu) + s.fBSpline(midp + dx2, ifu));
              
              for (int jfu = ifu; jfu <= limits.RightLimit; jfu++)
              {
                splProdIntegral[ifu][jfu] += Weight[0]*(s.fBSpline(midp - dx0, ifu)*s.fBSpline(midp - dx0, jfu) + s.fBSpline(midp + dx0, ifu)*s.fBSpline(midp + dx0, jfu));
                splProdIntegral[ifu][jfu] += Weight[1]*(s.fBSpline(midp - dx1, ifu)*s.fBSpline(midp - dx1, jfu) + s.fBSpline(midp + dx1, ifu)*s.fBSpline(midp + dx1, jfu));
                splProdIntegral[ifu][jfu] += Weight[2]*(s.fBSpline(midp - dx2, ifu)*s.fBSpline(midp - dx2, jfu) + s.fBSpline(midp + dx2, ifu)*s.fBSpline(midp + dx2, jfu));
              }
              
            }
          }
          
          for (int ifu = 0; ifu <= n; ifu++)
            for (int jfu = ifu; jfu <= Math.min(ifu+2*NSPN,n); jfu++)
              splProdIntegral[jfu][ifu] = splProdIntegral[ifu][jfu];
          break;
          
        case 'C':
          System.out.println("Integration for cubic splines not supported, exiting ...");
          System.exit(-1);
          break;
        default:
          System.out.println("Unknown spline type in calcSplineIntegrals, exiting...");
          System.exit(-1);
      }
      
    }
    
}
