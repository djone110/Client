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
import android.media.AudioManager;
import android.util.JsonWriter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Toast;

import com.teamawesome.client.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class SpeedoKey extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener, View.OnTouchListener{

    private KeyboardView kv;
    private Keyboard keyboard;
    private StringBuilder mComposing = new StringBuilder();
    Boolean PROCESS_HARD_KEYS = true;
    int counter = 0;
    int errorCount = 0;
    int timeStart, timeEnd, wordSpeed;
    JSONObject myJSON;
    OutputStreamWriter jsonWriter;

    String TAG = "SpeedoKey";


    private boolean caps = false;
    Calendar c = Calendar.getInstance();

    @Override
    public View onCreateInputView() {
        Log.d(TAG, "onCreateInputView: ");
        kv = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard, null);
        keyboard = new Keyboard(this, R.xml.qwerty);
        kv.setKeyboard(keyboard);
        kv.setOnKeyboardActionListener(this);
        kv.setOnTouchListener(this);
        kv.setPreviewEnabled(false);
        myJSON = new JSONObject();
        try{
           jsonWriter= new OutputStreamWriter(openFileOutput("app/storage/JSON/keyboard", MODE_APPEND));
        }catch (FileNotFoundException e){
            Log.d(TAG, "speedoKey: FILE ERR "+ e);

        }

        timeStart = 0;
        timeEnd = 0;
        wordSpeed = 0;

        Log.d(TAG, "ENDCREATE");
        return kv;
    }

    private void playClick(int keyCode){
        Log.d(TAG, "playClick: ");
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        switch (keyCode) {
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float pressure = event.getPressure();
        Log.d(TAG, "onTouch: Pressure: " + pressure);

        return false;
    }

    @Override
    public void onPress(int primaryCode) {

        Log.d(TAG, "onPress: ");


    }

    @Override
    public void onRelease(int primaryCode) {

        Log.d(TAG, "onRelease: ");
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        Log.d(TAG, "onKey: ");

        InputConnection ic = getCurrentInputConnection();
        //playClick(primaryCode);
        if (counter == 0){
            timeStart = c.get(Calendar.SECOND);
            Log.d(TAG, "onKey: " + timeStart);
            Toast.makeText(SpeedoKey.this, String.valueOf(timeStart) , Toast.LENGTH_SHORT).show();
        }
        counter++;
        switch (primaryCode){
            case Keyboard.KEYCODE_DELETE:
                errorCount++;
                handleBackspace();
                break;
            case Keyboard.KEYCODE_SHIFT:
                handleShift();
                break;
            case Keyboard.KEYCODE_DONE:
                timeEnd = c.get(Calendar.SECOND);
                wordSpeed = timeEnd - timeStart;
                counter--;
                try {
                    myJSON.put("time", c.getTimeInMillis());
                    myJSON.put("typeSpeed", wordSpeed);
                    myJSON.put("wordLength", counter);
                    myJSON.put("errors", errorCount);
                    myJSON.put("keystroke", wordSpeed/counter);
                    try {
                        jsonWriter.write(myJSON.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d(TAG, "onKey: length: " + counter );
                counter = 0;
                handleDone();
                break;
            case 32:
                timeEnd = c.get(Calendar.SECOND);
                wordSpeed = timeEnd - timeStart;
                counter--;
                Log.d(TAG, "onKey: length: " + counter );
                counter = 0;
                handleCharacter(primaryCode);

            default:
                handleCharacter(primaryCode);
                break;
        }

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: ");
        switch (keyCode) {

            case KeyEvent.KEYCODE_DEL:
                // Special handling of the delete key: if we currently are
                // composing text for the user, we want to modify that instead
                // of let the application to the delete itself.
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_ENTER:
                // Let the underlying text editor always handle these.
                return false;

            default:
                // For all other keys, if we want to do transformations on
                // text being entered with a hard keyboard, we need to process
                // it and do the appropriate action.
                if (PROCESS_HARD_KEYS) {
                    if (keyCode == KeyEvent.KEYCODE_SPACE
                            && (event.getMetaState() & KeyEvent.META_ALT_ON) != 0) {
                        // A silly example: in our input method, Alt+Space
                        // is a shortcut for 'android' in lower case.
                        InputConnection ic = getCurrentInputConnection();
                        if (ic != null) {
                            // First, tell the editor that it is no longer in the
                            // shift state, since we are consuming this.
                            ic.clearMetaKeyStates(KeyEvent.META_ALT_ON);
                            keyDownUp(KeyEvent.KEYCODE_A);
                            keyDownUp(KeyEvent.KEYCODE_N);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            keyDownUp(KeyEvent.KEYCODE_R);
                            keyDownUp(KeyEvent.KEYCODE_O);
                            keyDownUp(KeyEvent.KEYCODE_I);
                            keyDownUp(KeyEvent.KEYCODE_D);
                            // And we consume this event.
                            return true;
                        }
                    }
                }
        }

        return super.onKeyDown(keyCode, event);
    }



    private void keyDownUp(int keyEventCode) {
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_DOWN, keyEventCode));
        getCurrentInputConnection().sendKeyEvent(
                new KeyEvent(KeyEvent.ACTION_UP, keyEventCode));
    }


    private void handleCharacter(int primaryCode){
        getCurrentInputConnection().commitText(
                String.valueOf((char) primaryCode), 1);

    }
    private void handleBackspace(){
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
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyLongPress: YUP");
        return super.onKeyLongPress(keyCode, event);
    }


    @Override
    public void onText(CharSequence text) {

        Log.d(TAG, "onText: ");
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //counter = 0;
        if (timeStart != 0){
            timeEnd = c.get(Calendar.SECOND);
            wordSpeed = timeEnd - timeStart;
            counter--;

            // Packages data in JSON object, sends
            // to file
            try {
                myJSON.put("time", c.getTimeInMillis());
                myJSON.put("typeSpeed", wordSpeed);
                myJSON.put("wordLength", counter);
                myJSON.put("errors", errorCount);
                myJSON.put("keystroke", wordSpeed/counter);

                try {
                    jsonWriter.write(myJSON.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            Log.d(TAG, "onDestroy: Length: " + counter);
        }
        timeStart = 0;
        timeEnd = 0;
        wordSpeed = 0;
        counter = 0;
        errorCount = 0;
    }
}