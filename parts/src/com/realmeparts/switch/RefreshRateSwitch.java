/*
 * Copyright (C) 2016 The OmniROM Project
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.realmeparts;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;

public class RefreshRateSwitch implements OnPreferenceChangeListener {

    private static final IBinder SF = ServiceManager.getService("SurfaceFlinger");
    public static int setRefreshRate;
    private final Context mContext;

    public RefreshRateSwitch(Context context) {
        mContext = context;
    }

    public static void setForcedRefreshRate(int value) {
        Parcel Info = Parcel.obtain();
        Info.writeInterfaceToken("android.ui.ISurfaceComposer");
        Info.writeInt(value);
        try {
            SF.transact(1035, Info, null, 0);
        } catch (RemoteException e) {
            Log.e("DeviceSettings", e.toString());
        } finally {
            Info.recycle();
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Boolean enabled = (Boolean) newValue;

        if (preference == DeviceSettings.mRefreshRate120Forced && enabled) {
            setForcedRefreshRate(1);
        } else if (preference == DeviceSettings.mRefreshRate120Forced && !enabled) {
            setForcedRefreshRate(0);
        }
        return true;
    }
}