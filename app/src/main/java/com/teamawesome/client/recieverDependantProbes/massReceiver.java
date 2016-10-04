package com.teamawesome.client.recieverDependantProbes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.teamawesome.client.NotifyingDailyService;

/**
 * Created by mason on 10/2/16.
 */
public class massReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent arg1) {
        // TODO Auto-generated method stub
        Log.w("boot_broadcast_poc", "starting service locationFinder");
        context.startService(new Intent(context, locationFinder.class));
    }
}
