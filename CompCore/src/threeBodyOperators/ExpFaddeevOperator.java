/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threeBodyOperators;

import basicAbstractions.AFunction;
import basicAbstractions.AnOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import decompositions.ArnoldiContraction;
import configReader.ConfiguratorEssentials;

/**
 *
 * @author roudnev
 */
public class ExpFaddeevOperator implements AnOperator
{
    FaddeevOperatorResolvent rF;
    double t;
    private double dt=1.0;
    private long niter=1;
    MulticomponentRHSOperator v0;
    public UnrestrictingMulticomponentRHSOperator v;
    public MulticomponentThreeBodyClusterHamiltonian h0;
    public ExpFaddeevOperator(double t,ConfiguratorEssentials config)
    {
        this.t=t;
        niter=Math.round(t/dt);
        dt=Math.max(t, dt);
        if (niter>0) dt=t/niter;

        try
        {
             rF = new FaddeevOperatorResolvent(-2.0/dt, config);
             v0=rF.theA.v;
             h0=rF.theA.h0;
             v=new UnrestrictingMulticomponentRHSOperator(v0,h0,t);
        }
        catch (Exception ex)
        {
            Logger.getLogger(ExpFaddeevOperator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

      public AFunction times(AFunction u)
    {
       AFunction res=u.completeClone();
       AFunction tmp;
       for (long i=0;i<niter; i++)
       {
//           res=h0.expPadeTimes(res, dt);
//           res=res.minus(h0.times(res,0.0).times(dt/2));
//           res=h0.resolvent(res,-2.0/dt).times(2.0/dt);
           tmp=h0.times(res,0.0).plus(v.times(res));
           tmp=tmp.times(dt/2);
           res=res.minus(tmp);
           tmp=h0.expand(rF.times(res)).times(2.0/dt);
           res=h0.solve(tmp);
       }
       // res=h0.expPadeTimes(u, t/2);
        return res;
    }



      private AFunction timesExpV(AFunction u)
      {   Jama.Matrix h;
          ArnoldiContraction ac=new ArnoldiContraction(v, u, 100);
          h=new Jama.Matrix(ac.getMatrix());

          return u;
      }

    public int getRank()
    {
        return h0.getRank();
    }
    public double getConversion()
    { return rF.theA.getEnergyConversionFactor();
    }
}
