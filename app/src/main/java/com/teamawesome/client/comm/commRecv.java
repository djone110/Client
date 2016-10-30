package com.teamawesome.client.comm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;  // sockets
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class commRecv extends BroadcastReceiver {

    public commRecv() {


        String TAG = "commRecv";
        // listen on port 42069 for training data from server
        int portNumber = 42069;

        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out =
                    new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            //
            String inputLine, outputLine;

            /*
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
        System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
        System.out.println(e.getMessage());
    }


    @Override
    public int onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    */
        } catch (IOException e) {

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {

    }
}