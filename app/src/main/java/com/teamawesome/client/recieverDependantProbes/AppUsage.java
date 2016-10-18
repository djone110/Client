package com.teamawesome.client.recieverDependantProbes;

//Android OS stuff
import android.app.usage.UsageStats;
import android.util.Log;
import android.widget.Toast;

import com.teamawesome.client.R;

import java.util.Calendar;
import java.util.List;

//JSON stuff
//vanilla Java stuff

/**
 * App Usage Collector
 * Brandon Denton
 */

public class AppUsage {
/*
    String TAG = "getUsageStatistics";
    int counter = 0;
    int timeStart, timeEnd, wordSpeed;
    Calendar cal = Calendar.getInstance();
    List<UsageStats> queryUsageStats = mUsageStatsManager
            .queryUsageStats(intervalType, cal.getTimeInMillis(),
                    System.currentTimeMillis());

    @Override
    public List<UsageStats> getUsageStatistics(int period) {
        // Get the app statistics since one year ago from the current time.
        cal.add(Calendar.YEAR, -1);

        if (queryUsageStats.size() == 0) {
            Log.i(TAG, "The user may not allow the access to apps usage. ");
            Toast.makeText(getActivity(),
                    getString(R.string.explanation_access_to_appusage_is_not_enabled),
                    Toast.LENGTH_LONG).show();
            mOpenUsageSettingButton.setVisibility(View.VISIBLE);
            mOpenUsageSettingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
                }
            });
        }
        return queryUsageStats;
    }
*/
}
