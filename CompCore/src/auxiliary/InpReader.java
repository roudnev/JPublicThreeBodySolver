package auxiliary;
import java.io.*;
//import java.util.*;
public class InpReader
{
  public BufferedReader myInput;
  public InpReader(String InpFname)
   throws IOException
   {
     myInput=new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(InpFname))));
   }
  public InpReader()
  { myInput=new BufferedReader(new InputStreamReader(new DataInputStream(System.in)));
  }
//
   public double readDouble()
   throws IOException
   {
     String theLine=myInput.readLine();
     //System.out.println("parsing: "+theLine);
     StreamTokenizer st=new StreamTokenizer(new StringReader(theLine));
     st.eolIsSignificant(true);
     double res=13.13;
     {
       if (st.nextToken() == StreamTokenizer.TT_NUMBER)
       { res=st.nval;
       }
       else
       {
         System.err.println("error parsing double, exiting");
         System.exit(-1);
       }
     }
     return res;
   }
//
   public double parseDouble(String theLine)
   //throws IOException
   {
     //System.out.println("parsing: "+theLine);
     //StreamTokenizer st=new StreamTokenizer(new StringReader(theLine));
     //st.eolIsSignificant(true);
     //double res=13.13;
     //if (st.nextToken() == StreamTokenizer.TT_NUMBER)
     //  { res=st.nval;
     //  }
     //  else
     //  {
     //    System.err.println("error parsing double, exiting");
     // System.exit(-1);
     //  }
     return Double.parseDouble(theLine);
   }
//
   public String readLine()
   throws IOException
   {
     return myInput.readLine();
   }
//
   public int[] readIntArray()
   throws IOException
   {
     String theLine=myInput.readLine();
     StreamTokenizer st=new StreamTokenizer(new StringReader(theLine));
     st.eolIsSignificant(false);
     int count=0;
     double tmp[]=new double[100];
     while (st.nextToken()==StreamTokenizer.TT_NUMBER)
     {
       if (st.ttype == StreamTokenizer.TT_NUMBER)
       {  tmp[count]=st.nval;
          // System.err.println("reading:"+count+" "+tmp[count]);
          count++;
       }
       //System.err.println(" "+st.nval);
     }
     int result[]=new int[count];
     for (int i=0; i<result.length; i++)
       result[i]=(int)Math.round(tmp[i]);
     return result;
   }
//
   public double[] readDoubleArray()
   throws IOException
   {

     String theLine=myInput.readLine();
     StreamTokenizer st=new StreamTokenizer(new StringReader(theLine));
     st.eolIsSignificant(false);
     int count=0;
     double tmp[]=new double[100];
     while (st.nextToken()==StreamTokenizer.TT_NUMBER)
     {
       if (st.ttype == StreamTokenizer.TT_NUMBER)
       {  tmp[count]=st.nval;
          //System.err.println("reading:"+count+" "+tmp[count]);
          count++;
       }
     }
     return tmp;
   }
//

//
   public void close()
   {
   try
     {
       myInput.close();
     }
   catch(Exception e)
     {
       System.err.println("error reading in InpReader");
     }
   }
}
