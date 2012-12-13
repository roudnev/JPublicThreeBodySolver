package auxiliary;

import basicAbstractions.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author V.A.Roudnev
 * @version 2.0
 */

public class Mapper1D extends ACoefficientMapper
{

  public Mapper1D()
  {
  }

  public Mapper1D(int n)
  {
    this();
    coefArray=new double[n];
  }
  public ACoefficientMapper getCoefficientsCopy()
  {
    ACoefficientMapper res=new Mapper1D();
    res.coefArray=new double[coefArray.length];
    for (int i=0;i<coefArray.length;i++) res.coefArray[i]=coefArray[i];

    return res;
  }
}