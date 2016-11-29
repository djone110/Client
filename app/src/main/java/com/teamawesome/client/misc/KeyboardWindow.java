package com.teamawesome.client.misc;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.util.JsonReader;
import android.util.Log;
import android.util.StringBuilderPrinter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 11/1/2016.
 */

public class KeyboardWindow {
    private Context context;
    private static final String KEYBOARD_WINDOW_FILENAME = "keyboard_Window.json";
    private static final String KEYBOARD_STORAGE_FILENAME = "keyboard_Storage.json";
    private static final String TOP_LEVEL_ARRAY_NAME = "datapoints";
    private static final String AVERAGE_PRESSURE_NAME = "avgPressure";
    private static final String AVERAGE_WORD_SPEED_NAME = "avgWordSpeed";
    private static final String NUMBER_OF_DELETES_NAME = "numDeletes";
    private static final String TIMESTAMP_NAME = "timeStamp";
    private static final String NUMBER_OF_WORDS_NAME = "numWords";
    private static final int NUMBER_OF_SPACES_FOR_JSON_INDENTATION = 4;

    public KeyboardWindow(Context c) {
        this.context = c;
    }

    public ArrayList<KeyboardDatapoint> getDataPoints() throws JSONException, IOException {
        ArrayList <KeyboardDatapoint> ret = new ArrayList<>();
        JSONObject jsonObject = parseOrCreate();
        JSONArray elements = jsonObject.getJSONArray(TOP_LEVEL_ARRAY_NAME);
        for (int i = 0; i < elements.length(); i++) {
            JSONObject element = elements.getJSONObject(i);
            KeyboardDatapoint keyboardDatapoint = new KeyboardDatapoint();
            keyboardDatapoint.avgPressure = element.getDouble(AVERAGE_PRESSURE_NAME);
            keyboardDatapoint.avgWordSpeed = element.getInt(AVERAGE_WORD_SPEED_NAME);
            keyboardDatapoint.numDeletes = element.getInt(NUMBER_OF_DELETES_NAME);
            keyboardDatapoint.timeStamp = element.getLong(TIMESTAMP_NAME);
            keyboardDatapoint.numWords = element.getInt(NUMBER_OF_WORDS_NAME);
            ret.add(keyboardDatapoint);
        }
        return ret;
    }

    public void addDatapoint(KeyboardDatapoint datapoint) throws JSONException, IOException {
        JSONObject newElement = new JSONObject();
        newElement.put(AVERAGE_WORD_SPEED_NAME, datapoint.avgWordSpeed);
        newElement.put(NUMBER_OF_WORDS_NAME, datapoint.numWords);
        newElement.put(NUMBER_OF_DELETES_NAME, datapoint.numDeletes);
        newElement.put(AVERAGE_PRESSURE_NAME, datapoint.avgPressure);
        newElement.put(TIMESTAMP_NAME, datapoint.timeStamp);
        JSONObject existingKeyboardWindow = parseOrCreate();
        JSONArray elements = existingKeyboardWindow.getJSONArray(TOP_LEVEL_ARRAY_NAME);
        elements.put(newElement);
        JSONObject newKeyboardWindow = new JSONObject();
        newKeyboardWindow.put(TOP_LEVEL_ARRAY_NAME, elements);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(KEYBOARD_WINDOW_FILENAME, Context.MODE_PRIVATE));

        File Storage = new File( "data/data/com.teamawesome.client/files/", KEYBOARD_STORAGE_FILENAME);
        if (Storage.length() < 1500){

            Log.d("WINDOW", "addDatapoint: Store Length: " + Storage.length());
            OutputStreamWriter storageWriter = new OutputStreamWriter(context.openFileOutput(KEYBOARD_STORAGE_FILENAME, Context.MODE_PRIVATE));
            storageWriter.write(newKeyboardWindow.toString(NUMBER_OF_SPACES_FOR_JSON_INDENTATION));
            storageWriter.close();
        }
        outputStreamWriter.write(newKeyboardWindow.toString(NUMBER_OF_SPACES_FOR_JSON_INDENTATION));
        outputStreamWriter.close();

    }

    public String getJSONString() throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput(KEYBOARD_WINDOW_FILENAME)));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public void clearData() {
        context.deleteFile(KEYBOARD_WINDOW_FILENAME);
    }

    private JSONObject parseOrCreate() throws JSONException, IOException {
        JSONArray elements = new JSONArray();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(context.openFileInput(KEYBOARD_WINDOW_FILENAME));
            JsonReader jsonReader = new JsonReader(inputStreamReader);
            jsonReader.beginObject();
            String name = jsonReader.nextName();
            if (name.equals(TOP_LEVEL_ARRAY_NAME)) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginObject();
                    JSONObject element = new JSONObject();
                    while (jsonReader.hasNext()) {
                        name = jsonReader.nextName();
                        if (name.equals(AVERAGE_WORD_SPEED_NAME)) {
                            element.put(AVERAGE_WORD_SPEED_NAME, jsonReader.nextInt());
                        } else if (name.equals(NUMBER_OF_WORDS_NAME)) {
                            element.put(NUMBER_OF_WORDS_NAME, jsonReader.nextInt());
                        } else if (name.equals(NUMBER_OF_DELETES_NAME)) {
                            element.put(NUMBER_OF_DELETES_NAME, jsonReader.nextInt());
                        } else if (name.equals(AVERAGE_PRESSURE_NAME)) {
                            element.put(AVERAGE_PRESSURE_NAME, jsonReader.nextDouble());
                        } else if (name.equals(TIMESTAMP_NAME)) {
                            element.put(TIMESTAMP_NAME, jsonReader.nextLong());
                        } else {
                            throw new JSONException("weird name found: " + name);
                        }
                    }
                    jsonReader.endObject();
                    elements.put(element);
                }
                jsonReader.endArray();
                jsonReader.endObject();
                JSONObject ret = new JSONObject();
                ret.put(TOP_LEVEL_ARRAY_NAME, elements);
                return ret;
            } else {
                throw new JSONException("could not find the top level \"" + TOP_LEVEL_ARRAY_NAME + "\" array");
            }
        } catch (FileNotFoundException e) {
            // if keyboard_Window.json does not exist, create one with an empty array and return that.
            JSONObject ret = new JSONObject();
            ret.put(TOP_LEVEL_ARRAY_NAME, new JSONArray());
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(KEYBOARD_WINDOW_FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(ret.toString(NUMBER_OF_SPACES_FOR_JSON_INDENTATION));
            outputStreamWriter.close();
            return ret;
        }
    }
}
