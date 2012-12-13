package basicAbstractions;
public abstract class AFunction implements Cloneable, java.io.Serializable
{ //
  public AFunction()
  { super();
  }
  public double[] coef;
  //
  abstract public double scal(AFunction u); //returns the scalar product (this,u)
  //
  abstract public void normalize(); // normalization of the coefficients
  //
  abstract public void setZero(); //sets all the function to constant eq zero
  //
  abstract public double norm2(); // calculates (this,this)
  //
  abstract public AFunction times(double x); // scales this by x
  //
  abstract public AFunction plus(AFunction u); // calculates this + u
  //
  abstract public AFunction add(AFunction u); // adds u to this
  //
  abstract public AFunction minus(AFunction u); // calculates this - u
  //
  public final void setRandom()
  {
    for (int i=0;i<coef.length;i++) coef[i]=Math.random();
  }
  //
  public final AFunction completeClone()
  { try
    { AFunction res=(AFunction) super.clone();
      res.coef=(double[]) coef.clone();
      //System.err.println("CompleteClone completed OK");
      return res;
    }
    catch (CloneNotSupportedException e)
    { System.err.println(e.toString());
      System.err.println("CompleteClone failed");
      throw new InternalError(e.toString());
    }
    finally
    { //System.err.println("CompleteClone finished");
    }

  }
  //
  public AFunction imprint(int n)
  { AFunction res=(AFunction) this.clone();
    res.coef= new double[n];
    return res;
  }
  //
    @Override
  public final Object clone()
  { try
    { AFunction res=(AFunction) super.clone();
      return res;
    }
    catch (CloneNotSupportedException e)
    { System.err.println(e.toString());
      throw new InternalError(e.toString());
    }
  }
  //
  public int getArrayLength()
  { return coef.length;
  }
  public void dump(String fname)
  {
  try
    {
      java.io.BufferedWriter
        dumpWriter=new java.io.BufferedWriter(
                        new java.io.FileWriter(fname)
                      );
      for (int i=0;i<coef.length;i++)
      { dumpWriter.write(i+" "+coef[i]);
        dumpWriter.newLine();
      }
      dumpWriter.close();
    }
    catch(Exception e)
    { System.err.println("Error initializing MultocomponentDiscretization3D:"+e.getMessage());
    }
  }
}

/*
interface AFunction
{
  //
  public double scal(AFunction u); //returns the scalar product (this,u)
  //
  public void normalize(); // normalization of the coefficients
  //
  public void setZero(); //sets all the function to constant eq zero
  //
  public double norm2(); // calculates (this,this)
  //
  public AFunction times(double x); // scales this by x
  //
  public AFunction plus(AFunction u); // calculates this + u
  //
  public AFunction minus(AFunction u); // calculates this - u
  //
  public int getArrayLength();
  //
  public AFunction completeClone();
}OC

*/
