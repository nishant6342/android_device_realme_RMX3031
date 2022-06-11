package com.realme.overlaymanager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class OverlayManagerService extends Service {

    private static final boolean Debug = false;
    private static final String TAG = "RMOverlayManager";
    public static String path = "/sys/class/power_supply/battery/fastcharger";
    public static int voocchg;
    public BroadcastReceiver mChgInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == 0) {
                if (Debug) Log.d(TAG, "Device is not charging");
                return;
            }
            try {
                if (Debug) Log.d(TAG, "SuperDart Charging Node: " + path );
                // wait for 5 seconds for status to be written in node
                Thread.sleep(5000);
                voocchg = Integer.parseInt(Utils.readLine(path));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (Debug) Log.d(TAG, "SuperDart Charging Status: " + voocchg );

            if (voocchg == 1) {
                if (Debug) Log.d(TAG, "Overlaying Lawnchair with SuperDart Charging Text ");
                try {
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("cmd overlay enable --user current com.realme.app.lawnchair.overlay");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else if (voocchg == 0) {
                if (Debug) Log.d(TAG, "Disabling SuperDart overlay for Lawnchair");
                try {
                    Runtime runtime = Runtime.getRuntime();
                    runtime.exec("cmd overlay disable --user current com.realme.app.lawnchair.overlay");
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mChgInfo, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mChgInfo);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
