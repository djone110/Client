/**
 *
 * Socket-driven client-server communicator.
 * Utilized only in the windVane module.
 *
 * When windVane module finds local comparison indicates a foreign user, calls commServer().
 * commServer initially sends a data packet to the server on a socket outputstream.
 * Server will then run topic modeling on packet and history, compare word ratios,
 * and make a decsion on wether the packet is typical behavior of our user,
 * or if not.
 *
 * Once the packet has been sent, client will begin reading from port 42069
 * to get the result from server.
 * The server should send a 0 indicating is typical behavior, or 1 indicating not.
 *
 * getRes reads from the file we wrote to locally indicating server response and returns the
 * boolean value,
 *
 *
 *
 */
package com.teamawesome.client.comm;

import android.content.Context;
import android.icu.util.Output;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.teamawesome.client.misc.KeyboardWindow;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.Key;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Call;

/**
 * Created by mason on 10/20/16.
 */
public class CommManager {
    // the Android devs have set it up so that requests made from the emulator to 10.0.2.2
    // go to your computer's localhost. so for dev on your local machine, use 10.0.2.2. in prod,
    // we will change this to the public IP address of the server we are using.
    
    i = 0;
    String ip = null;
    String fileName = "conConf.txt";    // or wherever this file is idk
    String line = null;
    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader = new FileReader(fileName);

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while((line = bufferedReader.readLine()) != null) {
            if(i == 0) {ip = line;}
            i++;
        } bufferedReader.close();         
    } catch(FileNotFoundException ex) {
        System.out.println("Unable to open file '" + fileName + "'");                
    }
    catch(IOException ex) {
        System.out.println("Error reading file '" + fileName + "'");                  
    }
        
    private static String url = "http://" + ip;
    private Context context;
    public CommManager(Context c) {
        context = c;
    }
    public void sendJSON() throws Exception {
        KeyboardWindow keyboardWindow = new KeyboardWindow(context);
        String json;
        try {
            json = keyboardWindow.getJSONString();
        } catch (FileNotFoundException e) {
            Toast.makeText(context, "JSON file not found. Type at least 3 words using our custom keyboard then try again.", Toast.LENGTH_LONG).show();
            return;
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        OkHttpClient client = new OkHttpClient();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Toast.makeText(context, "Failed to send data, check logs", Toast.LENGTH_SHORT);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Toast.makeText(context, "Success", Toast.LENGTH_SHORT);
                response.close();
            }
        });
    }
}
