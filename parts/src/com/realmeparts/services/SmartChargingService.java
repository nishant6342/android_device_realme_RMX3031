/*
 * Copyright (C) 2020 The LineageOS Project
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

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class SmartChargingService extends Service {

    private static final int Charging_Notification_Channel_ID = 0x110110;
    private static final boolean Debug = false;
    private static final boolean resetBatteryStats = false;
    public static String current = "/sys/class/power_supply/battery/current_now";
    public static String mmi_charging_enable = "/sys/class/power_supply/battery/mmi_charging_enable";
    public static String battery_capacity = "/sys/class/power_supply/battery/capacity";
    public static String battery_temperature = "/sys/class/power_supply/battery/temp";
    private static Notification notification;
    private boolean mconnectionInfoReceiver;
    private SharedPreferences sharedPreferences;
    public BroadcastReceiver mBatteryInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            float battTemp = ((float) Integer.parseInt(Utils.readLine(battery_temperature))) / 10;
            int battCap = Integer.parseInt(Utils.readLine(battery_capacity));
            int currentmA = -(Integer.parseInt(Utils.readLine(current)));
            int chargingLimit = Integer.parseInt(Utils.readLine(mmi_charging_enable));
            int userSelectedChargingLimit = sharedPreferences.getInt("seek_bar", 95);

            if (Debug) Log.d("DeviceSettings", "Battery Temperature: " + battTemp + ", Battery Capacity: " + battCap + "%, " + "\n" + "Charging Current: " + currentmA + " mA,");

            // Charging limit based on user selected battery percentage
            if (((userSelectedChargingLimit == battCap) || (userSelectedChargingLimit < battCap)) && chargingLimit != 0) {
                Utils.writeValue(mmi_charging_enable, "0");
                if (Debug) Log.d("DeviceSettings", "Battery Temperature: " + battTemp + ", Battery Capacity: " + battCap + "%, " + "User selected charging limit: " + userSelectedChargingLimit + "%. Stopped charging");
            } else if (userSelectedChargingLimit > battCap && chargingLimit != 1) {
                Utils.writeValue(mmi_charging_enable, "1");
                if (Debug) Log.d("DeviceSettings", "Charging...");
            }
        }
    };
    public BroadcastReceiver mconnectionInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int battCap = Integer.parseInt(Utils.readLine(battery_capacity));
            if (intent.getAction() == Intent.ACTION_POWER_CONNECTED) {
                if (!mconnectionInfoReceiver) {
                    IntentFilter batteryInfo = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                    context.getApplicationContext().registerReceiver(mBatteryInfo, batteryInfo);
                    mconnectionInfoReceiver = true;
                }
                if (Debug) Log.d("DeviceSettings", "Charger/USB Connected");
            } else if (intent.getAction() == Intent.ACTION_POWER_DISCONNECTED) {
                if (sharedPreferences.getBoolean("reset_stats", false) && sharedPreferences.getInt("seek_bar", 95) == battCap)
                    resetStats();
                if (mconnectionInfoReceiver) {
                    context.getApplicationContext().unregisterReceiver(mBatteryInfo);
                    mconnectionInfoReceiver = false;
                }
                if (Debug) Log.d("DeviceSettings", "Charger/USB Disconnected");
            }
        }
    };

    public static void resetStats() {
        try {
            Runtime.getRuntime().exec("dumpsys batterystats --reset");
            Thread.sleep(1000);
        } catch (Exception e) {
            Log.e("DeviceSettings", "SmartChargingService: " + e.toString());
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        IntentFilter connectionInfo = new IntentFilter();
        connectionInfo.addAction(Intent.ACTION_POWER_CONNECTED);
        connectionInfo.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(mconnectionInfo, connectionInfo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mconnectionInfo);
        if (mconnectionInfoReceiver) getApplicationContext().unregisterReceiver(mBatteryInfo);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
