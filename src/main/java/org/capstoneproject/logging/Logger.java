import java.util.ArrayList;
import java.io.*;
import java.util.Calendar;
import java.text.*;

/*Used to created instances of Logger class meant to serve thread based objects in accessing a log write to file*/

public class Logger extends MasterLogger {

   private ArrayList<String> toWrite;
   private String timeStamp;
   
   public Logger() {
   
      toWrite = new ArrayList<String>();
   
   }
   
   public void log(String level, String subComponent, String entry) {      
   
      timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
   
      if(level.equals("NORM") || level.equals("norm"))
            toWrite.add(timeStamp + " : " + subComponent + " : " + entry);
      else
            toWrite.add( timeStamp + " : " + subComponent + " : " + entry + " - [Debug Status - " + level + " ]");
         
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

