package org.capstoneproject.logging;

import java.util.concurrent.*;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public  class MasterLogger {

   protected static Semaphore semaphore;
   
   private static java.io.File outFile;
   private static java.io.PrintWriter outStream;
   private static Scanner inStream;
   private static String fileName;
   protected static ArrayList<String> fileContentHolder;
   
   
   public MasterLogger() {
   
   }
   
   public MasterLogger(String passedFileName) {
   
      semaphore = new Semaphore(1);
      
      try{
         fileName = passedFileName;
         
         outFile = new java.io.File(fileName);
         outStream = new java.io.PrintWriter(outFile);
         outStream.println("");//ensures the file has content
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
   
      //will eventually sort file
   
   }
   
}