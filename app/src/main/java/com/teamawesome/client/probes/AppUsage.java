package com.teamawesome.client.probes;

//Android OS stuff
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.app.usage.*;
import android.util.Log;

//JSON stuff

//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;

//vanilla Java stuff
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * App Usage Collector
 */

public class AppUsage extends Service {

    String TAG = "getUsageStatistics";
    int counter = 0;
    int timeStart, timeEnd, wordSpeed;
    Calendar cal = Calendar.getInstance();
    UsageStatsManager myUsageManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: Binding Usage");
        myUsageManager = (UsageStatsManager)this.getSystemService(USAGE_STATS_SERVICE);
        Log.d(TAG, "onBind: Finished Usage");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: Usage Created");
        super.onCreate();
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        Map<String, UsageStats> usageByPackage;
        myUsageManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        //Get app usage from 30 minutes ago.
        long startTime = now.getTimeInMillis()-( 30 * 60 * 1000);
        long endTime = now.getTimeInMillis();
        int today = now.get(Calendar.DAY_OF_YEAR);
        JSONObject jsonObject = new JSONObject();



        start.set(Calendar.DATE, 1);
        start.set(Calendar.MONTH, 1);

        List<UsageStats> queryUsageStats = myUsageManager.queryUsageStats(UsageStatsManager.INTERVAL_YEARLY, startTime,now.getTimeInMillis() );

        usageByPackage = myUsageManager.queryAndAggregateUsageStats(startTime, endTime);
        Log.d(TAG, "Usage: Start:" + (today-1) + " End:" + today + " Size:" + queryUsageStats.size() );

        // Iterates over every package use and prints time used.
        // Print info to the dev log for now.

        try {
            for (Map.Entry<String, UsageStats> entry : usageByPackage.entrySet()) {
                //Log.d(TAG, "Package: " + entry.getKey() + " Time used: " + entry.getValue().getTotalTimeInForeground());
                jsonObject.put(entry.getKey(), entry.getValue().getTotalTimeInForeground());
                Log.d(TAG, "JSONOBJ: " + jsonObject.toString());
            }

            // Writes to the Usage Window file.
            OutputStreamWriter storageWriter = new OutputStreamWriter(openFileOutput("usage_Window.json", Context.MODE_APPEND));
            storageWriter.write(jsonObject.toString(4));
            storageWriter.close();

        }catch (JSONException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

}
