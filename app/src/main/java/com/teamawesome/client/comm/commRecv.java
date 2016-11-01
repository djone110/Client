package com.teamawesome.client.comm;

import java.io.*;
import java.net.*;  // sockets
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class commRecv extends BroadcastReceiver {
    public commRecv() {
        // listen on port 42069 for training data from server
        int portNumber = 42069;
    }

    @Override
    // I have to change some stuff with this because right now it's
    // pretty mUDAMUDAMUDAMUDAMUDAMUDA!!! - Brandon
    public void onReceive(Context context, Intent intent) {
        String inputLine, outputLine;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Initiate conversation with client
            commRecvProtocol crp = new commRecvProtocol();
            outputLine = crp.processInput(null);
            out.println(outputLine);

            while ((inputLine = in.readLine()) != null) {
                outputLine = crp.processInput(inputLine);
                out.println(outputLine);
                if (outputLine.equals("some terminating stuff idk"))
                    break;
            }
        } catch (IOException e) {
              System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
              System.out.println(e.getMessage());
        }
    }

}
