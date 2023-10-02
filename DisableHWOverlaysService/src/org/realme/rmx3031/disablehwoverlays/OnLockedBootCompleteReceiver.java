package org.realme.rmx3031.disablehwoverlays;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import android.util.Log;

public class OnLockedBootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DisableHWOverlays";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "onBoot");

        Intent sIntent = new Intent(context, DisableHWOverlaysService.class);
        context.startService(sIntent);
    }
}
