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
    int sendPort = 24601;
    int recvPort = 42069;
    int count;
    Socket socket;
    String host;

    commManager(){

        host = "111.1.1.111";
    }

    // Establishes a connection on a given port,
    // sends data as a stream. Closes the connection.
    public boolean commServer(){

        try{
            socket = new Socket(host, sendPort);

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
            receive();
            return getRes();
        }catch (IOException e){
            e.printStackTrace();
        }

        return false;
    }

    // After a data packet has been sent,
    // We will need to get servers opinion on
    // The data we gave it. Just listens on a specified port
    // for the information.
    private void receive(){
        try {
            socket = new Socket(host, recvPort);
            File file = new File("servResponse");
            byte[] bytes = new byte[16*1024];

            InputStream inputStream = socket.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(file);
            while ((count = inputStream.read()) > 0){
                outputStream.write(bytes, 0, count);
            }
            outputStream.close();
            inputStream.close();
            socket.close();

        }catch (IOException e){
            e.printStackTrace();

        }
    }




    // Reads the response from the server. If it is 1
    // then the server thinks the user is bad. If it is 0
    // server thinks user is good.
    private boolean getRes(){
        File file = new File("servResponse");
        int res;
        try {
            FileInputStream inputStream = new FileInputStream(file);
            res = inputStream.read();
            if (res == 1){
                return false;
            }else return true;
        }catch (IOException e){

        }

        return true;

    }


}
