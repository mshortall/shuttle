import java.util.concurrent.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

/*MasterLogger is a shell that lets a program using the logger initialize a filewriter.  MasterLogger also contains static objects to protect the file writing process*/

public  class MasterLogger {

   protected static Semaphore semaphore;//protects file writing process
   
   private static java.io.File outFile;
   private static java.io.PrintWriter outStream;
   private static Scanner inStream;
   private static String fileName;
   protected static ArrayList<String> fileContentHolder;//contains contents of log during file sort process
   
   public static long testTime;
   
   public MasterLogger() {//not used
   
   }
   
   public MasterLogger(String passedFileName) {//used in user program's main function to initialize fileWriter
   
      semaphore = new Semaphore(1);
      
      try{
         fileName = passedFileName;
         
         outFile = new java.io.File(fileName);
         outStream = new java.io.PrintWriter(outFile);
         outStream.println("");//ensures the file has content to enable sort
         inStream = new Scanner(fileName);
      }
      catch(IOException ex) {}
      
      fileContentHolder = new ArrayList<String>();
         
   }
   
   protected void logMaster() throws IOException {
      
      inStream.close();
      inStream = new Scanner(outFile);
      
      while(inStream.hasNext()){//assumes some content in file, adds that to log content from Logger objects in fileContentHolder
      
         fileContentHolder.add(inStream.nextLine());
      
      }
      
      sortFile();//sorts fileContentHolder
      
      writeToFile();//writes fileContentHolder to a file
   
   }
   
   private void writeToFile() throws IOException {
      
      outStream = new PrintWriter(outFile);//clears file
            
      for(int x = 0; x < fileContentHolder.size(); x++){
      
         outStream.println(fileContentHolder.get(x));
      
      }
      
      outStream.close();//shuts down current PrintWriter
      fileContentHolder.clear();//resets the content holder so that the data has to be pulled from file
   }

   private void sortFile() {
   
      Collections.sort(fileContentHolder);//using library sort method is simplest implementation, and fulfills functional requirements
   
   }
   
 }