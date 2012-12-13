package hermitSplines;
import java.io.*;
class GridsDemo
{ public static PrintStream MyOutput;
  public static void main(String[] args)
   throws IOException
   { SimpleGrid x,y,z,u,v;
     FileInputStream gridConfig;
     FileOutputStream MyOutputFile;
     String InpFname;
     //
     if (args.length==0)
       InpFname="GRID.DAT";
     else
       InpFname=args[0];
     //
     gridConfig=new FileInputStream(InpFname);
     MyOutputFile=new FileOutputStream("MyFile.txt");
     MyOutput=new PrintStream(MyOutputFile,true);
     StreamTokenizer InpFReader=new StreamTokenizer(gridConfig);
     //
     x=MakeGrid(InpFReader);
     //
     y=MakeGrid(InpFReader);
     //
     z=new SimpleGrid();
     z.DisplayGrid(MyOutput);
     //
     gridConfig.close();
     MyOutput.print("OK");
     MyOutput.close();
   }
//
   public static double[] readDoubleArray(StreamTokenizer nums)
   throws IOException
   {
     nums.eolIsSignificant(true);
     int count=0;
     double tmp[]=new double[100];
     while (nums.nextToken()!=StreamTokenizer.TT_WORD)
     {
       if (nums.ttype == StreamTokenizer.TT_NUMBER)
       { tmp[count]=nums.nval;
         // System.out.println(count+" "+tmp[count]);
         count++;
       }
     }
     double result[]=new double[count];
     for (int i=0; i<result.length; i++)
       result[i]=tmp[i];
     return result;
   }
//
   public static int[] readIntArray(StreamTokenizer nums)
   throws IOException
   {
     nums.eolIsSignificant(true);
     int count=0;
     double tmp[]=new double[100];
     while (nums.nextToken()!=StreamTokenizer.TT_WORD)
     {
       if (nums.ttype == StreamTokenizer.TT_NUMBER)
       {  tmp[count]=nums.nval;
          // System.out.println(count+" "+tmp[count]);
          count++;
       }
     }
     int result[]=new int[count];
     for (int i=0; i<result.length; i++)
       result[i]=(int)Math.round(tmp[i]);
     return result;
   }
//
   public static SimpleGrid MakeGrid(StreamTokenizer InpFReader)
   throws IOException
   { SimpleGrid result;
     String gName="A grid";
     double D[]=readDoubleArray(InpFReader);
     int N[]=readIntArray(InpFReader);
     InpFReader.pushBack();
     if (InpFReader.nextToken()==StreamTokenizer.TT_WORD)
       gName=InpFReader.sval;
     result=new SimpleGrid(N,D,gName);
     result.DisplayGrid();
     result.DisplayGrid(MyOutput);
     return result;
   }
}
