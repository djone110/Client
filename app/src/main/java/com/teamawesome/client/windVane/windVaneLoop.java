package com.teamawesome.client.windVane;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.teamawesome.client.comm.CommManager;
import com.teamawesome.client.misc.KeyboardDevi;
import com.teamawesome.client.probes.AppUsage;
import com.teamawesome.client.probes.locationFinder;
import com.teamawesome.client.probes.networkUsage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// Basically a wrapper for the loop.
// Called by massReceiver when ON_BOOT is recieved,
// Calls onBind() immediately and starts a looping WV thread of form:
// sleep(5min)->wake->compare Window & Storage->delete Window.
public class windVaneLoop extends Service {

    Boolean initialLearning;
    String TAG = "WindVane";

    public windVaneLoop() {
        initialLearning = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.d(TAG, "onBind: CALLED");
        return null;

    }
    @Override
    public void onCreate(){
        Log.d(TAG, "onCreate: CALLED");
        beginLoop();
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

        int interval;
        CommManager myComm;

        looper(){
            interval = 50000;
        }


        @Override
        public void run() {
            try {
               Log.d(TAG, "running: now sleeping");
              while(true) {
                  sleep(interval, 0);
                  compareWindow();
                  startSample();
                  deleteWindow();
              }
            } catch (InterruptedException e) {
                  e.printStackTrace();
            }
        }


        // Where the magic happens.
        // Read in from the window and local storage.
        // Look for the keywords associated with each keyboard field;
        private void compareWindow(){
            int flags = 0;

            // Vague idea of how the comparison will work.
            // Run the three tests, and count the number of flags
            // each sets. If it is above a certain threshold,
            // we need to increase the sampling rates;
            flags += keyboardComp();
            flags += appComp() * .1;
            flags += networkComp();


            // When the comparison functions return
            if (flags > 3) {
                interval -= 10000;
            }



            // If the interval has gotten sufficiently small (3min in this case), consult the server

            if (interval < 30000) {
                // If getRes returns true, the server indicates
                // the data we received does not suggest
                // a foreign user. If it returns false, then lock the user out until a password
                // is provided.
                // Alt, just reset interval. We're not locking out.
              interval = 50000;

            }

        }

        // Compares window file to storarage file
        // There are four unique fields in a keyboard obj.
        // Compare each of these fields in a window against local storage
        // return the number of fields that were inconsistant.
        private int keyboardComp(){


            FileInputStream winIn, storIn;
            InputStreamReader winISR, storISR;
            BufferedReader winBR, storBR;
            String line;
            String words[];
            File Storage;
            KeyboardDevi window, storage;
            double errDiff, pressDiff, speedDiff;
            double errMOE, pressMOE, speedMOE, netMOE;


            errMOE = 0;
            pressMOE = 0;
            speedMOE = 0;
            // Try and open the storage and window files.
            // Read each line by line. Look for specific keywords that
            // indicate a value field will be next. Collect that value,
            // apply it in a meaningful against-server comparison.
            try {

                Log.d(TAG, "keyboardComp: TRY ");
                Storage = new File(getFilesDir(),"keyboard_Storage.json");
                if (Storage.length() < 200){
                   initialLearning = true;
                }

                winISR = new InputStreamReader(openFileInput("keyboard_Window.json"));
                storISR = new InputStreamReader(openFileInput("keyboard_Storage.json"));

                Log.d(TAG, "keyboardComp: PAST OPEN");


                winBR = new BufferedReader(winISR);
                storBR = new BufferedReader(storISR);


                if (!initialLearning) {


                    window = new KeyboardDevi("keyboard_Window.json");
                    storage = new KeyboardDevi("keyboard_Storage.json");

                    window.analyze(getBaseContext(), true);
                    storage.analyze(getBaseContext(), true);
                    errDiff = Math.abs(window.errAvg - storage.errAvg);
                    speedDiff = Math.abs(window.speedAvg - storage.speedAvg);
                    pressDiff = Math.abs(window.pressAvg - storage.pressAvg);


                    Log.d(TAG, "keyboardComp: ERR AVG W: " + window.errAvg + " S: " + storage.errAvg);
                    Log.d(TAG, "keyboardComp: SPEED AVG W: " + window.speedAvg + " S: " + storage.speedAvg );
                    Log.d(TAG, "keyboardComp: PRESS AVG W: " + window.pressAvg + " S: " + storage.pressAvg);
                    //Log.d(TAG, "keyboardComp: ERR DEVI: " + storage.errDev);





                    Log.d(TAG, "keyboardComp: ERR DIFF: " + errDiff);
                    Log.d(TAG, "keyboardComp: SPEED DIFF: " + speedDiff);
                    Log.d(TAG, "keyboardComp: PRESS DIFF: " + pressDiff);
                    Log.d(TAG, "keyboardComp: ERR DEVI: " + storage.errDev);
                    Log.d(TAG, "keyboardComp: SPEED DEVI: " + storage.speedDev);
                    Log.d(TAG, "keyboardComp: PRESS DEVI " + storage.pressDev);


                    if (errDiff > storage.errDev) {
                        errMOE = errDiff/storage.errDev;
                        Log.d(TAG, "compareWindow: BAD DELETION COUNT : MARGIN: " + errMOE);

                    }
                    if(speedDiff > storage.speedDev){
                        speedMOE = speedDiff/storage.speedDev;
                        Log.d(TAG, "keyboardComp: SPEED WINDOW OUT OF DEVIATION : MARGIN : " + speedMOE);
                    }
                    if(pressDiff > storage.pressDev){
                        pressMOE = pressDiff/storage.pressDev;
                        Log.d(TAG, "keyboardComp: PRESSURE WINDOW OF DEVIATION : MARGIN : " + pressMOE);

                    }
                    // IF the net margin of error is less than 400% (testing) for the three fields
                    //
                    netMOE = errMOE + speedMOE + pressMOE;
                    if (netMOE < 4){
                        OutputStreamWriter storeOSW= new OutputStreamWriter(openFileOutput("keyboard_Storage.json", MODE_PRIVATE));
                        while((line = winBR.readLine()) != null){
                            Log.d(TAG, "keyboardComp: ATTEMPTING TO WRITE TO STORAGE");
                            storeOSW.write(line);
                        }
                        storeOSW.close();
                    }
                    OutputStreamWriter keyConfOSW = new OutputStreamWriter(openFileOutput("keyboardConfidence.txt", MODE_PRIVATE));

                    keyConfOSW.write(Double.toString(netMOE));
                    keyConfOSW.close();
                    winISR.close();
                    storBR.close();
                    winBR.close();
                    storBR.close();
                }else{

                    // We're building the initial window
                    Log.d(TAG, "keyboardComp: baseline writing");
                    OutputStreamWriter storageWriter = new OutputStreamWriter(openFileOutput("keyboard_Storage.json", Context.MODE_APPEND));
                    InputStreamReader windowReader = new InputStreamReader(openFileInput("keyboard_Window.json"));
                    winBR = new BufferedReader(windowReader);
                    while ((line = winBR.readLine()) != null) {
                        Log.d(TAG, "WRITING: " + line );
                        storageWriter.write(line + "\n");
                    }
                    storageWriter.close();
                    windowReader.close();

                }

            }catch (FileNotFoundException e){
                Log.d(TAG, "keyboardComp: FNF");
                //e.printStackTrace();
            } catch (IOException e) {
                Log.d(TAG, "keyboardComp: IOEXCEPTION");
                //e.printStackTrace();
            }
            return 1;
        }


        // Compares window file to storage file.
        // There are many potential package fields.
        // Compare them against local storage and count inconsistancies.
        // Return the number.
        private int appComp(){
            return 1;
        }

        // Compares window file to storage file.
        // There is currently only one network field.
        private int networkComp(){
            return 1;
        }


        // This starts sampling select data fields
        private void startSample(){

            startService(new Intent(windVaneLoop.this, locationFinder.class));
            startService(new Intent(windVaneLoop.this, AppUsage.class));
            startService(new Intent(windVaneLoop.this, networkUsage.class));

        }


        // Deletes the last window gotten,
        // making room for the new 5 minute window.
        private void deleteWindow(){

            File dir = getFilesDir();
            File fd = new File(dir, "keyboard_Window.json");

            if (fd.delete()){ //fd.delete()
                Log.d(TAG, "deleteWindow: SUCCESS");
            }else{
                Log.d(TAG, "deleteWindow: FAILURE");
            }

            fd = new File(dir, "keyboard_Storage.json");
            // Resets the long term storage occasionally.
            if(fd.length() > 100000){
                fd.delete();
            }

        }
    }

}
