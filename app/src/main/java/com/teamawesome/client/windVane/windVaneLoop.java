package com.teamawesome.client.windVane;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Basically a wrapper for the loop.
// Called by massReceiver when ON_BOOT is recieved,
// Calls onBind() immediately and starts a looping WV thread of form:
// sleep(5min)->wake->compare Window & Storage->delete Window.
public class windVaneLoop extends Service {

    String TAG = "WindVane";

    public windVaneLoop() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onBind: CALLED");
        beginLoop();
        return null;

    }
    private void beginLoop(){
        looper l = new looper();
        l.start();
    }

    // Looper is doing 90% of the work.
    // It is  a thread that sleeps in the background for 5 minutes
    // wakes up, and runs a comparison operation on the recent data
    // collected. If the comparison finds that the data is inconsistant
    // with local storage, we consult the server.
    // If the server finds it bad, we lock the user out.
    private class looper extends Thread{
        @Override
        public void run() {
            try {
               // Log.d(TAG, "running: now sleeping");
                sleep(50000, 0);
                compileWindow();
                compareWindow();
                deleteWindow();

            } catch (InterruptedException e) {
                  e.printStackTrace();
            }
        }

        // Agglomerate recent files into a single window file
        // then wipe them.
        private void compileWindow(){


        }

        // Where the magic happens.
        // Read in from the window and local storage.
        // Look for the keywords associated with each keyboard field.
        //
        private void compareWindow(){

            FileInputStream winIn, storIn;
            InputStreamReader winISR, storISR;
            BufferedReader winBR, storBR;
            String line;
            String words[];
            int i, numberObjects;
            double windowDeletions, storageDeletions;

            // Counter for all number of deletions across objects.
            // At the end of counting, made into average deletions per 10char
            // by dividing by numberObjects.
            windowDeletions =0;
            storageDeletions = 0;
            //Counter for number of Objects in a file.
            numberObjects = 0;


            // Try and open the storage and window files.
            // Read each line by line. Look for specific keywords that
            // indicate a value field will be next. Collect that value,
            // apply it in a meaningful against-server comparison.
            try {
                winIn = new FileInputStream("files/keyboard_Window.json");
                storIn = new FileInputStream("files/keyboard_Storage.json");

                winISR = new InputStreamReader(winIn);
                storISR = new InputStreamReader(storIn);

                winBR = new BufferedReader(winISR);
                storBR = new BufferedReader(storISR);


                // Read-in the window info.
                while((line = winBR.readLine()) != null){
                    words = line.split(" ");

                    for (i =0; i < words.length; i++){
                        if (words[i] == "numDeletes"){
                            numberObjects++;
                            windowDeletions += Integer.parseInt(words[i+1]);
                        }
                    }
                }
                windowDeletions /= numberObjects;
                numberObjects = 0;
                while((line = storBR.readLine()) != null){
                    words = line.split(" ");

                    for(i = 0; i < words.length; i++){
                        if (words[i] == "numDeletes"){
                            numberObjects++;
                            storageDeletions += Integer.parseInt(words[i+1]);
                        }
                    }
                }
                storageDeletions /= numberObjects;


                // Now we have the average number of deletions across
                // stored objects and window objects, compare the two to see if close.
                // Say storage AVG del = 10 and window AVG del = 5.
                // avgDiff is then 5. If this is above our static threshhold, comp
                // another keyboard field. If THATS above threshold, repeat new comparison
                // or comm server.
                double avgDiff = storageDeletions-windowDeletions;
                if (avgDiff < 0) {}
                else if (avgDiff > 50){
                    // New comparison/ do stuff.
                    Log.d(TAG, "compareWindow: BAD DELETION COUNT ");
                }else{
                    // If we find that the window and
                    // storage are similar enough, append the
                    // window to the storage file.
                }

            }catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        // Deletes the last window gotten,
        // making room for the new 5 minute window.
        private void deleteWindow(){

            File fd = new File("files/keyboard_Window.json");

            if (fd.delete()){
                Log.d(TAG, "deleteWindow: SUCCESS");
            }else{
                Log.d(TAG, "deleteWindow: FAILURE");
            }


        }
    }


}
