package com.teamawesome.client.comm;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * Created by mason on 10/20/16.
 */
public class commManager {
    int portNumber = 24601;
    Socket socket;
    String host;

    commManager(){

        host = "111.1.1.111";
    }

    // Establishes a connection on a given port,
    // sends data as a stream. Closes the connection.
    public void send(){

        try{
            socket = new Socket(host, portNumber);

            File file = new File("files/keyboard_Window.json");
            long length = file.length();
            byte[] bytes = new byte[16*1024];
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = socket.getOutputStream();
            int count;

            // Actual Writing
            while((count = inputStream.read(bytes)) > 0){
                outputStream.write(bytes, 0, count);
            }
            outputStream.close();
            inputStream.close();
            socket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }




/*
    public int send(String path) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                path);

        return 0;
    }
*/


}
