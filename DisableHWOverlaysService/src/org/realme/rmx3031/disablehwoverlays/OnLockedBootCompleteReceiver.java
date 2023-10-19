package org.realme.rmx3031.disablehwoverlays;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

import android.app.Service;
import android.os.ServiceManager;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class OnLockedBootCompleteReceiver extends BroadcastReceiver {
    private static final String TAG = "DisableHWOverlays";
    private IBinder mSurfaceFlinger;

    @Override
    public void onReceive(final Context context, Intent intent) {
        Log.i(TAG, "onBoot");
        mSurfaceFlinger = ServiceManager.getService("SurfaceFlinger");
        disableHardwareOverlaysSetting();
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
