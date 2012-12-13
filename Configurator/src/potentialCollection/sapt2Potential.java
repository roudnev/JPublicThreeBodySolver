package potentialCollection;
public class sapt2Potential extends sapt1Potential
{ // potential 
  public String getDescription()
  { return "SAPT2 (He-He relativistic effects dumping v. 2), Aziz, Slaman, J. Chem. Phys. 107, 914 (1997) ";
  }

  protected double fsapt(double R)
  {       
    double RES=0.0;
    double P1, P2, P3, P4;
    if ((R>0.0)&(R<5.7)) 
    {
	RES= 1.0;;
    }
    else if ((R>5.7)&(R<1.0e1)) 
    {
        P1= 9.860029e-1;
        P2= 5.942027e-3;
        P3=-7.924833e-4;
        P4= 3.172548e-5;
	RES= P1
            +R*(P2
            +R*(P3
            +R*P4));
    }    
    else
    {
       RES=super.fsapt(R);
    }
    return RES;
  }
}
