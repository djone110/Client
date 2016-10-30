/*
This module creates specifies the functionality of the keyboard specifed
in res/xml/qwerty.xml and res/layout/keyboard.xml.
It monitors typespeed, errors over some miliseconds, and avg keystroke time, putting
them into a JSON object.

When closed it writes this information to the JSON file res/json/keyboard.json
with it's current timestamp.

 */


package com.teamawesome.client.main;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.teamawesome.client.R;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.util.Vector;

public class SpeedoKey extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener, View.OnTouchListener {

    private KeyboardView kv;
    private Keyboard keyboard;
    private int numKeyPresses = 0;
    private int numDeletes = 0;
    private long timeOfWordStart = 0;
    // pressure of each keypress, from 0 to 1
    // TODO: test pressure with real hardware. the emulator always returns the same value
    private Vector<Float> pressure = new Vector<>();
    // time it takes the user to type each word (in milliseconds)
    private Vector<Long> wordSpeed = new Vector<>();
    private String TAG = "SpeedoKey";
    private boolean caps = false;
    private boolean writeJsonOnNextWord = false;
    // the number of keypresses that it will take for us to write to the JSON file
    private int sampleSize = 10;

    @Override
    public View onCreateInputView() {
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setOnTouchListener(this);
        kv.setPreviewEnabled(false);
        return kv;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressure.add(event.getPressure());
        }
        return false;
    }

    @Override
    public void onPress(int primaryCode) {
    }

    @Override
    public void onRelease(int primaryCode) {
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        // every time we type sampleSize number of characters,
        // write to JSON and reset all arrays and counters we're using
        if (numKeyPresses == sampleSize) {
            writeJsonOnNextWord = true;
        }

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                numDeletes++;
                handleBackspace();
                break;
            case Keyboard.KEYCODE_SHIFT:
                handleShift();
                break;
            case Keyboard.KEYCODE_DONE:
                endWord();
                if (writeJsonOnNextWord) {
                    writeJson();
                    resetValues();
                }
                handleDone();
                break;
            // space key
            case 32:
                // after the user presses the space key, they've completed a word
                endWord();
                if (writeJsonOnNextWord) {
                    writeJson();
                    resetValues();
                }
                handleCharacter(32);
                break;
            default:
                numKeyPresses++;
                if (timeOfWordStart == 0) {
                    timeOfWordStart = System.currentTimeMillis();
                }
                handleCharacter(primaryCode);
                break;
        }
    }

    private void writeJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("wordSpeed", wordSpeed);
            json.put("numWords", wordSpeed.size());
            json.put("numDeletes", numDeletes);
            json.put("avgPressure", calculateAveragePressure());
            // this is located at: /data/data/com.teamawesome.client/files/
            OutputStreamWriter jsonWriter = new OutputStreamWriter(openFileOutput("keyboard_Window.json", MODE_APPEND));
            jsonWriter.write(json.toString(4));
            Toast.makeText(this, "wrote json", Toast.LENGTH_SHORT).show();
            jsonWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void resetValues() {
        wordSpeed = new Vector<>();
        numDeletes = 0;
        pressure = new Vector<>();
        numKeyPresses = 0;
        writeJsonOnNextWord = false;
    }

    private void endWord() {
        if (timeOfWordStart == 0) {
            return;
        }

        long timeOfWordEnd = System.currentTimeMillis();
        wordSpeed.add(timeOfWordEnd - timeOfWordStart);
        timeOfWordStart = 0;
    }

    private float calculateAveragePressure() {
        float sum = 0;
        for (float f : pressure) {
            sum += f;
        }
        return sum / pressure.size();
    }

    private void handleCharacter(int primaryCode){
        getCurrentInputConnection().commitText(
                String.valueOf((char) primaryCode), 1);

    }

    private void handleBackspace() {
        getCurrentInputConnection().deleteSurroundingText(1, 0);
    }

    private void handleDone(){
        getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
    }

    private void handleShift(){
        caps = !caps;
        keyboard.setShifted(caps);
        kv.invalidateAllKeys();
    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeUp() {
    }
}