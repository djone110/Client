package com.teamawesome.client.recieverDependantProbes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mason on 10/2/16.
 */
public class massReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent arg1) {
        // TODO Auto-generated method stub
        Log.w("boot_broadcast_poc", "starting service locationFinder");
        Log.d(arg1.getAction() + " ", "onReceive: YOYOYO");
        context.startService(new Intent(context, locationFinder.class));
        context.startService(new Intent(context, AppUsage.class));


        // Start windVane on Boot only
        if (arg1.getAction() == "android.intent.action.BOOT_COMPLETED"){
            Log.d("Starting WV ", "onReceive: ");
            context.startService(new Intent(context, com.teamawesome.client.windVane.windVaneLoop.class));
        }
    }
}
