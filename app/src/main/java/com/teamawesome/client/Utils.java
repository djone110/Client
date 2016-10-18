package com.teamawesome.client;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by matt on 10/18/16.
 */

public class Utils extends Activity {
    public void writeJsonToFile(JSONObject j) throws Exception {
        Context context = getApplicationContext();
        String filename = "out.json";
        String jsonString = j.toString(4);
        FileOutputStream outputStream;
        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(jsonString.getBytes());
        outputStream.close();
    }
}
