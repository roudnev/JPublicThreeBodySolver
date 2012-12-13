package hermitSplines;
import basicAbstractions.*;
import Jama.Matrix;
public class SplineFunction1D extends AFunction implements Cloneable,Function1D
{ //
  // fields:
  int cNumber;
  private HSplineBC spline;
  private CollocGrid colloc;
  private double[][] idMatr;
  private Matrix idMatrInv;
  //
  // constructors:
  public SplineFunction1D(SplineFunction1D v)
  {
    this.cNumber=v.getCNumber();
    this.coef=new double[cNumber];
    for(int i=0;i<cNumber;i++) this.coef[i]=v.coef[i];
    this.spline=v.spline;
    this.colloc=new CollocGrid(this.spline);
    this.idMatr=new double[cNumber][cNumber];
    for (int i=0;i<cNumber;i++)
    for (int j=0;j<cNumber;j++)
        idMatr[i][j]=spline.fBSpline(colloc.mesh[i], j);
    idMatrInv=new Matrix(idMatr).inverse();
  }
  public SplineFunction1D(HSplineBC spline)
  { cNumber=spline.getNMax()+1;
    coef=new double[cNumber];
    this.spline=spline;
    this.colloc=new CollocGrid(this.spline);
    this.idMatr=new double[cNumber][cNumber];
    for (int i=0;i<cNumber;i++)
    for (int j=0;j<cNumber;j++)
        idMatr[i][j]=spline.fBSpline(colloc.mesh[i], j);
    idMatrInv=new Matrix(idMatr).inverse();
  }
//  public SplineFunction1D(HSplineBC spline, int dim, AFunction f)
//  {
//    this(spline,dim);
// //   if (cNumber!=f.coef.length)
// //     throw new java.lang.ExceptionInInitializerError("Splines and coefficients do not correspond to each other");
//    for(int i=0;i<cNumber;i++) this.coef[i]=0.0;
//    for(int i=0;i<f.coef.length;i++) this.coef[i]=f.coef[i];
//  }
  //
  //methods:
  public double valueAt(double x)
  {
    double res=0.0;
    for (int i=0; i<cNumber;i++)
      res+=coef[i]*getSpline().fBSpline(x,i);
    return res;
  }
  public final double scal(AFunction u)
  {
    double res=0;
    for(int i=0; i<cNumber;i++) res+=coef[i]*u.coef[i];
    return res;
  }
  //
  public final void normalize()
  {
    double scal=1.0/java.lang.Math.sqrt(norm2());
    for(int i=0;i<cNumber;i++) coef[i]=coef[i]*scal;
  }
  //
  public final void setZero()
  { for(int i=0;i<cNumber;i++) coef[i]=0.0;
  }
  //
  public final double norm2()
  {
    double res=this.scal(this);
    return res;
  }
  //
  public final AFunction times(double x)
  {
    AFunction res=this.completeClone();
    for(int i=0;i<cNumber;i++) res.coef[i]=x*coef[i];
    return res;
  }
  //
  public final AFunction plus(AFunction u)
  {
    AFunction res=this.completeClone();
    for(int i=0;i<cNumber;i++) res.coef[i]=coef[i]+u.coef[i];
    return res;
  }
  //
  public final AFunction add(AFunction u)
  {
    for(int i=0;i<cNumber;i++) coef[i]=coef[i]+u.coef[i];
    return this;
  }
  //
  public final AFunction minus(AFunction u)
  {
    AFunction res=this.completeClone();
    for(int i=0;i<cNumber;i++) res.coef[i]=coef[i]-u.coef[i];
    return res;
  }
  //
  public final int getArrayLength()
  {  return cNumber;
  }
  //
  public final int getCNumber()
  {  return cNumber;
  }

  public final void setCoefFromValues(double[] coef)
  {
    double[][] idInv=idMatrInv.getArray();
    this.coef=coef.clone();
    for (int i=0; i<coef.length;i++)
    {
      this.coef[i]=0.0;
      for (int j=0; j<coef.length;j++) this.coef[i]+=idInv[i][j]*coef[j];
    }
  }
    /**
     * @return the spline
     */
    public HSplineBC getSpline() {
        return spline;
    }

    /**
     * @return the colloc
     */
    public CollocGrid getColloc() {
        return colloc;
    }

    /**
     * @param colloc the colloc to set
     */
    public void setColloc(CollocGrid colloc) {
        this.colloc = colloc;
    }
}
