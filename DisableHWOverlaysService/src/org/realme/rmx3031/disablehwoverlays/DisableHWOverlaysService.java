package org.realme.rmx3031.disablehwoverlays;

import android.content.Intent;
import android.app.Service;
import android.os.ServiceManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class DisableHWOverlaysService extends Service {
    public static final String TAG = "DisableHWOverlaysService";
    private IBinder mSurfaceFlinger;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.i(TAG, "Service is starting...");
        mSurfaceFlinger = ServiceManager.getService("SurfaceFlinger");
        disableHardwareOverlaysSetting();
        return START_STICKY;
    }

    void disableHardwareOverlaysSetting() {
        if (mSurfaceFlinger == null) {
            Log.e(TAG, "mSurfaceFlinger is null! Aborting");
            return;
        }
        try {
            final Parcel data = Parcel.obtain();
            data.writeInterfaceToken("android.ui.ISurfaceComposer");
            data.writeInt(1);
            mSurfaceFlinger.transact(1008, data,
                    null /* reply */, 0 /* flags */);
            data.recycle();
        } catch (RemoteException ex) {
            Log.e(TAG, "Failed to disable HW overlays\n" + ex.toString());
        }
    }
}
