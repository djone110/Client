package com.teamawesome.client.probes;

import android.annotation.TargetApi;
import android.app.Service;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.List;

public class networkUsage extends Service {
    public networkUsage() {
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public IBinder onBind(Intent intent) {
        Calendar now = Calendar.getInstance();
        long startTime = now.getTimeInMillis()-( 60 * 60 * 1000);
        long endTime = now.getTimeInMillis();
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) this.getSystemService(NETWORK_STATS_SERVICE);
        JSONObject jsonObject = new JSONObject();

        try {
            NetworkStats.Bucket bucket = networkStatsManager.querySummaryForDevice(1, "john", startTime, endTime);
            jsonObject.put(now.getTime().toString(), bucket.toString());
            File file = new File("network_Window.json");
            OutputStreamWriter jsonWriter = new OutputStreamWriter(openFileOutput("network_Window.json", MODE_APPEND));
            jsonWriter.write(jsonObject.toString(1));

        }catch (RemoteException e){

        }catch (JSONException e){

        }catch (IOException e){

        }

        return null;
    }
}
