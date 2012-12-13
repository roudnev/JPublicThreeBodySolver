package potentialCollection;
public class sapt1Potential extends saptPotential
{ // potential HFe-B(He), distance in \AA, Value in \AA^(-2)
  public String getDescription()
  { return "SAPT1 (He-He relativistic effects dumping v. 1), Aziz, Slaman, J. Chem. Phys. 107, 914 (1997) ";
  }

  protected double fsapt(double R)
  {       
    double RES=0.0;
    double P1, P2, P3, P4, P5,RR;
    if ((R>0.0)&(R<1.0e1)) 
    {
        P1=1.0;
        P2=-9.431089e-8;
        P3=-2.408169e-5;
        P4= 3.576804e-7;
        P5=-4.701832e-9;
	RES= P1
            +R*(P2 
            +R*(P3
            +R*(P4
            +R*P5)));
    }
    else if ((R>1.0e1)&(R<1.0e2)) 
    {
        P1=-1.62343e-3;
        P2= 2.22097e-3;
        P3=-1.17323e-3;
        P4= 3.00012e-4;
        P5=-1.05512e-5;
	RR=Math.sqrt(R);
	RES= 1.0e0-P1
            +RR*(-P2
            +RR*(-P3
            +RR*(-P4
            -RR*P5)));
    }
    else if ((R>1.0e2)&(R<2.0e2))
    {
        P1= 8.82506e-2;
        P2= 3.81846e-2;
        P3=-1.72421e-3;
        P4= 4.74897e-7;
        P5= 3.0445706e-3;
	RR=Math.sqrt(R);
	RES= (1+P1
               +RR*(P2
               +RR*(P3
               +R*P4)))
             /(1.2e0+0.8e0*R*P5);
    }
    else if  ((R>2.0e2)&(R<1.0e3)) 
    {
        P1= 1.488897e0;
        P2=-2.123572e0;
        P3= 1.043994e0;
        P4=-1.898459e-1;
        P5= 6.479867e-3;
	RES= Math.log(R)*(P1*Math.pow(R,0.4e0)
            +Math.pow(R,0.5e0)*P2
            +Math.pow(R,0.6e0)*P3
            +Math.pow(R,0.7e0)*P4
            +Math.pow(R,0.8e0)*P5);
     }
     else if ((R>1.0e3)&(R<1.0e4))
     {
        P1= 6.184108e-6;
        P2= 3.283043e2;
        P3= 1.367922e3;
        P4=-4.464489e7;
        P5= 1.365003e10;
	RES= P1
            +(P2
            +(P3
            +(P4
            +P5/R)/R)/R)/R;
     }
     else if ((R>1.0e4)&(R<10.0e5))
     {
        P1=-1.107002e-7;
        P2= 3.284717e2;
        P3=-9.819846e2;
        P4=-1.953816e7;
        P5=-1.079712e11;
	RES= P1
            +(P2
            +(P3
            +(P4
            +P5/R)/R)/R)/R;
     }
     return RES;
  }
}
