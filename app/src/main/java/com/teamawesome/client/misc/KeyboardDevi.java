package com.teamawesome.client.misc;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by mason on 11/26/16.
 */
public class KeyboardDevi implements JSONInterpreter {
    public double errDev, strokeDev, speedDev, pressDev;
    public double errAvg, strokeAvg, speedAvg, pressAvg;
    private String file;
    private Vector<Double> errArr, strokeArr, speedArr, pressArr;
    String TAG = "Deviation";


    public KeyboardDevi(String f){
        errDev = 0;
        strokeDev = 0;
        speedDev = 0;
        pressDev = 0;
        errAvg = 0;
        strokeAvg = 0;
        speedAvg = 0;
        pressAvg = 0;
        speedArr = new Vector<Double>();
        errArr = new Vector<Double>();
        strokeArr = new Vector<Double>();
        pressArr = new Vector<Double>();
        file = f;

    }

    public void analyze(Context context, boolean getDevi){
        InputStreamReader ISR;
        BufferedReader  BR;
        int i, numberObjects;
        double value;
        String line;
        String words[];

        numberObjects = 0;

        try{
            ISR = new InputStreamReader(context.openFileInput(file));
            //storeISR = new InputStreamReader(context.openFileInput("keyboard_Storage.json"));
            BR = new BufferedReader(ISR);
            //storeBR = new BufferedReader(storeISR);

            while((line = BR.readLine()) != null){
                words = line.split(" ");

                for (i = 0; i < words.length; i++) {
                    switch (words[i]) {
                        case "\"numWords\":":
                            numberObjects++;

                        case "\"numDeletes\":":
                            value = Double.parseDouble((words[i + 1]).substring(0, words[i+1].length() - 1));
                            errArr.add(value);
                            errAvg += value;
                            break;
                        case "\"avgPressure\":":
                            value = Double.parseDouble((words[i + 1]).substring(0, words[i+1].length() - 1));
                            pressArr.add(value);
                            pressAvg += value;
                            break;
                        case  "\"avgWordSpeed\":":
                            value = Double.parseDouble((words[i + 1]).substring(0, words[i+1].length() - 1));
                            speedArr.add(value);
                            speedAvg += value;
                            break;
                    }

                }


            }
            if (numberObjects > 0) {
                pressAvg /= numberObjects;
                speedAvg /= numberObjects;
                errAvg /= numberObjects;
                strokeAvg /= numberObjects;

                pressDev = getDevi(pressArr, pressAvg);
                speedDev = getDevi(speedArr, speedAvg);
                errDev = getDevi(errArr, errAvg);

            }

            ISR.close();
            BR.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private double getDevi(Vector<Double> vector, double avg){
        double ret = 0;
        double mean = 0;
        double count = 0;
        double value;

        for(double d : vector){
            count++;
            value = d - avg;
            value = Math.sqrt(Math.abs(value));
            mean += value;
        }

        mean = mean/count;
        ret = Math.sqrt(mean);

        return ret;

    }


}
