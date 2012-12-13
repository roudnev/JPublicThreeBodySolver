import basicAbstractions.*;
import potentialCollection.*;
//import linearEquationSolvers.*;
//import twoBodyOperators.*;
//import java.io.*;
public class Main
{ public static void main(String[] args)
  throws Exception
  { PotentialsRegister register=new PotentialsRegister();
    register.check();
    AMain theMain;
    double e2=Double.parseDouble(args[0]);
   // System.out.println("Check: e2="+e2);
    if (e2<0.0)
    {
      theMain=new Main3BBound();
      System.out.println("The main class is "+theMain);
    }
    else
    {
      theMain=new Main3BScat();
      System.out.println("The main class is "+theMain);
    }
    if (args.length==3)
    {
      theMain=new Main3BKernelAnalysis();
    }

//    BufferedReader configReader=new BufferedReader(new FileReader("./Main.conf"));
//    String cname=configReader.readLine();
//    configReader.close();
//    AMain theMain= (AMain)Class.forName(cname).newInstance();
    theMain.main(args);
    System.out.println();
    System.out.println("Finished.");
  }
}
