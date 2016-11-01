package com.teamawesome.client.probes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.teamawesome.client.windVane.windVaneLoop;

//import com.teamawesome.client.NotifyingDailyService;

/**
 * Created by mason on 10/2/16.
 */

public class massReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent arg1) {
        // TODO Auto-generated method stub
        Log.w("boot_broadcast_poc", "starting service locationFinder");
        context.startService(new Intent(context, locationFinder.class));
        context.startService(new Intent(context, AppUsage.class));
        context.startService(new Intent(context, networkUsage.class));

        // Services to start only on BOOT
        if(arg1.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            context.startService(new Intent(context, windVaneLoop.class));
        }
    }

}
