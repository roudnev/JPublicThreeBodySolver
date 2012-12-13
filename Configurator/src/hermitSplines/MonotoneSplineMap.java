package hermitSplines;
import basicAbstractions.Function1D;

/**
 * Title:        MkGrid2<p>
 * Description:  Visual design of meshes for differential equations solving<p>
 * Copyright:    Copyright (c) V. Roudnev<p>
 * Company:      I4PhaH<p>
 * @author V. Roudnev
 * @version 1.0
 */

public class MonotoneSplineMap implements Function1D
{ //fields:
  private SimpleGrid gridPoints;
  HSplineFree basicSpline;
  private double[] controlPoints;
  private double[] derivatives;
  double[] basePoints;
  int gpNumber;
  //constructor:
  public MonotoneSplineMap(double[] tb, double[] tc)
  {
    basePoints=tb;
    controlPoints=tc;
    gridPoints=new SimpleGrid(tb);
    basicSpline=new HSplineFree(getGridPoints(),'C');
    derivatives=prepDerivArray();
  }
  //methods:
  private double[] prepDerivArray()
  { double[] res=basePoints.clone();
    double s=0.0;
    double d,d1,d2,a1,a2,w;
    int i10,i0,i01;
    for(int i=0;i<basePoints.length;i++)
    { 
      i10=Math.max(0,i-1);
      i01=Math.min(i+1,controlPoints.length-1);
      d1=0.0;
      a1=(basePoints[i]-basePoints[i10]);
      if(i!=i10 & a1!=0.0 ) d1=(controlPoints[i]-controlPoints[i10])/a1;
      d2=0.0;
      a2=(basePoints[i01]-basePoints[i]);
      if(i!=i01 & a2!=0.0) d2=(controlPoints[i01]-controlPoints[i])/a2;
      w=0.0;
      if(a1!=0.0) w+=d1;
      if(a2!=0.0) w+=d2;
      d=(d1*d2+d2*d1);
      if(w!=0.0)  d=d/w;
      if(i==0) d=d2;
      if(i==basePoints.length-1) d=d1;
      //d=Math.min(d,1.0/d);
      res[i]=d;
    }
    return res;
  }
  //
  public double valueAtOld(double t)
  {
    double s=0.0;
    double d,d1,d2,a1,a2,w;
    int i10,i0,i01;
    for(int i=0;i<basePoints.length;i++)
    { s+=controlPoints[i]*basicSpline.bSplineFree(t,i*3);
      i10=Math.max(0,i-1);
      i01=Math.min(i+1,controlPoints.length-1);
      d1=0.0;
      a1=(basePoints[i]-basePoints[i10]);
      if(i!=i10) d1=(controlPoints[i]-controlPoints[i10])/a1;
      d2=0.0;
      a2=(basePoints[i01]-basePoints[i]);
      if(i!=i01) d2=(controlPoints[i01]-controlPoints[i])/a2;
      w=0.0;
      if(a1!=0.0) w+=d1;
      if(a2!=0.0) w+=d2;
      d=(d1*d2+d2*d1)/w;
      if(i==0) d=d2;
      if(i==basePoints.length-1) d=d1;
      //d=Math.min(d,1.0/d);
      s+=d*basicSpline.bSplineFree(t,i*3+1);
    }
    return s;
  }
  public double valueAt(double t)
  {
    double s=0.0;
    for(int i=0;i<basePoints.length;i++)
    { s+=controlPoints[i]*basicSpline.bSplineFree(t,i*2);
      s+=derivatives[i]*basicSpline.bSplineFree(t,i*2+1);
    }
    return s;
  }

  /**
   * @return the gridPoints
   */
  public //fields:
  SimpleGrid getGridPoints()
  {
    return gridPoints;
  }

  /**
   * @param gridPoints the gridPoints to set
   */
  public void setGridPoints(SimpleGrid gridPoints)
  {
    this.gridPoints = gridPoints;
  }

  /**
   * @return the controlPoints
   */
  public double[] getControlPoints()
  {
    return controlPoints;
  }

  /**
   * @return the derivatives
   */
  public double[] getDerivatives()
  {
    return derivatives;
  }
}