/* Read input from STDIN. Print your output to STDOUT*/

import java.util.*;
public class a {


   @SuppressWarnings("empty-statement")
   public static void main(String args[] ) throws NullPointerException {

      String arr[][]= new String[500][500];
      String temp[][]= new String[500][500];
      Scanner s= new Scanner(System.in);
   
      
      int n=s.nextInt();

      for(int i=0;i<n-1;i++)
      {
         for(int j=0;j<n-1;j++)
         {
            arr[i][j]=s.nextLine();
         }
      }

      for(int i=0;i<n;i++)
      {
         for(int j=0;j<n;j++)
         {
            if("0".equals(arr[i][j]))
            {
               temp[i][j]="0";
            }

         }
      }
      int count=1;

    for(int i=0;i<n;i++)
      {
         for(int j=0;j<n;j++)
         {
            if(temp[i][j].equals(temp[i][j+1]) && temp[i][j].equals(temp[i+1][j]) && temp[i][j].equals(temp[i+1][j+1]))
            {
               count=count+1;
            }
            else
            {
                System.out.print(-1);;
            }
          }
         }
         System.out.print(count);
   }

}
