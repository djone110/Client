package com.teamawesome.client.comm;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by mason on 10/20/16.
 */
public class serverManager {
    String url = "www.google.com";
    
    public int send(String path) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                path);

        return 0;
    }



}
