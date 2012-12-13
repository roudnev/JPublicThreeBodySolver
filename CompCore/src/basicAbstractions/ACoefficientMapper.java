package basicAbstractions;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public abstract class ACoefficientMapper implements Cloneable, java.io.Serializable
{
  public double[] coefArray;

  public ACoefficientMapper(){ super(); }

  public ACoefficientMapper(int dim)
  { super();
    coefArray=new double[dim];
  }

  public abstract ACoefficientMapper getCoefficientsCopy();

  public final Object clone()
  { try
    { ACoefficientMapper res=(ACoefficientMapper) super.clone();
      return res;
    }
    catch (CloneNotSupportedException e)
    { System.err.println(e.toString());
      throw new InternalError(e.toString());
    }
  }

//  public abstract double getCoef(int i);

//  public abstract void setCoef(int i, double s);

}