/*
 * Copyright (C) 2021 ArrowOS
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

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class ChargingCoolDownService extends Service {

    private static final boolean DEBUG = false;
    private static final String TAG = "ChargingCoolDownService";

    private static final String COOL_DOWN_PATH = "/sys/class/power_supply/battery/cool_down";
    private static final String TEMPERATURE_PATH = "/sys/class/power_supply/battery/temp";
    private static final double COOL_DOWN_START_THRESHOLD = 40.5; // degree C
    private static final double COOL_DOWN_STOP_THRESHOLD = 39.0; // degree C
    private static final int COOL_DOWN_ON_VAL = 2;
    private static final int COOL_DOWN_OFF_VAL = 0;

    private BroadcastReceiver mBatteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) == 0) {
                if (DEBUG) Log.i(TAG, "Device is not charging");
                return;
            }

            float temp = ((float) Integer.parseInt(Utils.readLine(TEMPERATURE_PATH))) / 10;
            int coolDown = Integer.parseInt(Utils.readLine(COOL_DOWN_PATH));

            if (DEBUG)
                Log.i(TAG, "Battery status received: temp=" + temp + " coolDown=" + coolDown);

            if (temp >= COOL_DOWN_START_THRESHOLD && coolDown == 0) {
                Utils.setValue(COOL_DOWN_PATH, COOL_DOWN_ON_VAL);
                if (DEBUG) Log.w(TAG, "Enabling cool_down");
            } else if (temp <= COOL_DOWN_STOP_THRESHOLD && coolDown != 0) {
                Utils.setValue(COOL_DOWN_PATH, COOL_DOWN_OFF_VAL);
                if (DEBUG) Log.w(TAG, "Disabling cool_down");
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBatteryInfoReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBatteryInfoReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
