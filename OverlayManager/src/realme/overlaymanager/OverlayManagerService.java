package com.realme.overlaymanager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.IOverlayManager;
import android.os.RemoteException;
import android.os.BatteryManager;
import android.os.IBinder;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;

public class OverlayManagerService extends Service {

    private static final boolean Debug = false;
    private static final String TAG = "RMOverlayManager";
    private static final String OVERLAY = "com.realme.app.lawnchair.overlay";
    public static String path = "/sys/class/power_supply/battery/fastcharger";
    public static int voocchg;
    private IOverlayManager mOverlayService;
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
            boolean enable = ((String) String.valueOf(voocchg)).equals("1");
            try {
                mOverlayService.setEnabled(OVERLAY, enable, UserHandle.USER_CURRENT);
            } catch (RemoteException e) {
                Log.e(TAG, e.toString());
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mOverlayService = IOverlayManager.Stub
                .asInterface(ServiceManager.getService(Context.OVERLAY_SERVICE));
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
