package org.capstoneproject.logging;

import java.util.ArrayList;
import java.io.*;

public class Logger extends MasterLogger {

   private ArrayList<String> toWrite;
   private long timeStamp;
   
   public Logger() {
   
      toWrite = new ArrayList<String>();
   
   }
   
   public void log(String level, String subComponent, String entry) {      
   
      toWrite.add("Debug: " + level + " Subcomponent: " + subComponent + "Entry: " + entry);
         
   }
   
   public void close() {
   
     try{
         super.semaphore.acquire();//prevents race conditions on file write
      
         for(int x = 0; x < toWrite.size(); x++){   
         
            super.fileContentHolder.add(toWrite.get(x));
            
         }
         
         try{
            super.logMaster();
         }
         catch(IOException ex) {}
     }
     catch (InterruptedException ex) {}
     finally {
         super.semaphore.release();//allows next Logger in line to write to file
     }
   }

}

