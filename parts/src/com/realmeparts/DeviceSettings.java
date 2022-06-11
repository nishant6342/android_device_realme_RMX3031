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

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;

import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.TwoStatePreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

public class DeviceSettings extends PreferenceFragment
        implements Preference.OnPreferenceChangeListener {

    public static final String KEY_SRGB_SWITCH = "srgb";
    public static final String KEY_HBM_SWITCH = "hbm";
    public static final String KEY_DC_SWITCH = "dc";
    public static final String KEY_OTG_SWITCH = "otg";
    public static final String KEY_PERF_PROFILE = "perf_profile";
	public static final String KEY_VIBRATION_STRENGTH = "vibration_strength";
	public static final String VIB_STRENGTH_SYSTEM_PROPERTY = "persist.vib_strength";
    public static final String PERF_PROFILE_SYSTEM_PROPERTY = "persist.perf_profile";
    public static final String KEY_GAME_SWITCH = "game";
    public static final String KEY_CHARGING_SWITCH = "smart_charging";
    public static final String KEY_RESET_STATS = "reset_stats";
	public static final String KEY_DT2W_SWITCH = "dt2w";
    public static final String KEY_DND_SWITCH = "dnd";
    public static final String KEY_CABC = "cabc";
    public static final String CABC_SYSTEM_PROPERTY = "persist.cabc_profile";
    public static final String KEY_SETTINGS_PREFIX = "device_setting_";
    public static final String TP_DIRECTION = "/proc/touchpanel/oplus_tp_direction";
    private static final String ProductName = Utils.ProductName();
    private static final String KEY_CATEGORY_CHARGING = "charging";
    private static final String KEY_CATEGORY_GRAPHICS = "graphics";
    private static final String KEY_CATEGORY_REFRESH_RATE = "refresh_rate";
    public static TwoStatePreference mResetStats;
    public static TwoStatePreference mRefreshRate120Forced;
	private static TwoStatePreference mDT2WModeSwitch;
    public static SeekBarPreference mSeekBarPreference;
    public static DisplayManager mDisplayManager;
    private static NotificationManager mNotificationManager;
    public TwoStatePreference mDNDSwitch;
    public PreferenceCategory mPreferenceCategory;
	private Vibrator mVibrator;
	private SecureSettingListPreference mVibStrength;
    private TwoStatePreference mDCModeSwitch;
    private TwoStatePreference mSRGBModeSwitch;
    private TwoStatePreference mHBMModeSwitch;
    private TwoStatePreference mOTGModeSwitch;
    private TwoStatePreference mGameModeSwitch;
    private TwoStatePreference mSmartChargingSwitch;
    private boolean CABC_DeviceMatched;
    private boolean DC_DeviceMatched;
    private boolean HBM_DeviceMatched;
    private boolean sRGB_DeviceMatched;
    private SecureSettingListPreference mCABC;
	private SecureSettingListPreference mPerfProfile;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
        prefs.edit().putString("ProductName", ProductName).apply();

        addPreferencesFromResource(R.xml.main);

        mDCModeSwitch = findPreference(KEY_DC_SWITCH);
        mDCModeSwitch.setEnabled(DCModeSwitch.isSupported());
        mDCModeSwitch.setChecked(DCModeSwitch.isCurrentlyEnabled(this.getContext()));
        mDCModeSwitch.setOnPreferenceChangeListener(new DCModeSwitch());

        mSRGBModeSwitch = findPreference(KEY_SRGB_SWITCH);
        mSRGBModeSwitch.setEnabled(SRGBModeSwitch.isSupported());
        mSRGBModeSwitch.setChecked(SRGBModeSwitch.isCurrentlyEnabled(this.getContext()));
        mSRGBModeSwitch.setOnPreferenceChangeListener(new SRGBModeSwitch());

        mHBMModeSwitch = (TwoStatePreference) findPreference(KEY_HBM_SWITCH);
        mHBMModeSwitch.setEnabled(HBMModeSwitch.isSupported());
        mHBMModeSwitch.setChecked(HBMModeSwitch.isCurrentlyEnabled(this.getContext()));
        mHBMModeSwitch.setOnPreferenceChangeListener(new HBMModeSwitch(getContext()));

        mOTGModeSwitch = (TwoStatePreference) findPreference(KEY_OTG_SWITCH);
        mOTGModeSwitch.setEnabled(OTGModeSwitch.isSupported());
        mOTGModeSwitch.setChecked(OTGModeSwitch.isCurrentlyEnabled(this.getContext()));
        mOTGModeSwitch.setOnPreferenceChangeListener(new OTGModeSwitch());

        mGameModeSwitch = findPreference(KEY_GAME_SWITCH);
        mGameModeSwitch.setEnabled(GameModeSwitch.isSupported());
        mGameModeSwitch.setChecked(GameModeSwitch.isCurrentlyEnabled(this.getContext()));
        mGameModeSwitch.setOnPreferenceChangeListener(new GameModeSwitch(getContext()));

        mDNDSwitch = findPreference(KEY_DND_SWITCH);
        mDNDSwitch.setChecked(prefs.getBoolean(KEY_DND_SWITCH, false));
        mDNDSwitch.setOnPreferenceChangeListener(this);

        mSmartChargingSwitch = findPreference(KEY_CHARGING_SWITCH);
        mSmartChargingSwitch.setChecked(prefs.getBoolean(KEY_CHARGING_SWITCH, false));
        mSmartChargingSwitch.setOnPreferenceChangeListener(new SmartChargingSwitch(getContext()));

        mResetStats = findPreference(KEY_RESET_STATS);
        mResetStats.setChecked(prefs.getBoolean(KEY_RESET_STATS, false));
        mResetStats.setEnabled(mSmartChargingSwitch.isChecked());
        mResetStats.setOnPreferenceChangeListener(this);

        mSeekBarPreference = findPreference("seek_bar");
        mSeekBarPreference.setEnabled(mSmartChargingSwitch.isChecked());
        SeekBarPreference.mProgress = prefs.getInt("seek_bar", 95);

        mRefreshRate120Forced = findPreference("refresh_rate_120Forced");
        mRefreshRate120Forced.setChecked(prefs.getBoolean("refresh_rate_120Forced", false));
        mRefreshRate120Forced.setOnPreferenceChangeListener(new RefreshRateSwitch(getContext()));

        mCABC = (SecureSettingListPreference) findPreference(KEY_CABC);
        mCABC.setValue(Utils.getStringProp(CABC_SYSTEM_PROPERTY, "0"));
        mCABC.setSummary(mCABC.getEntry());
        mCABC.setOnPreferenceChangeListener(this);
		
		mPerfProfile = (SecureSettingListPreference) findPreference(KEY_PERF_PROFILE);
        mPerfProfile.setValue(Utils.getStringProp(PERF_PROFILE_SYSTEM_PROPERTY, "0"));
        mPerfProfile.setSummary(mPerfProfile.getEntry());
        mPerfProfile.setOnPreferenceChangeListener(this);
		
		mDT2WModeSwitch = (TwoStatePreference) findPreference(KEY_DT2W_SWITCH);
        mDT2WModeSwitch.setEnabled(DT2WModeSwitch.isSupported());
        mDT2WModeSwitch.setChecked(DT2WModeSwitch.isCurrentlyEnabled(this.getContext()));
        mDT2WModeSwitch.setOnPreferenceChangeListener(new DT2WModeSwitch());

        mVibStrength = (SecureSettingListPreference) findPreference(KEY_VIBRATION_STRENGTH);
        mVibStrength.setValue(Utils.getStringProp(VIB_STRENGTH_SYSTEM_PROPERTY, "2500"));
        mVibStrength.setSummary(mVibStrength.getEntry());
        mVibStrength.setOnPreferenceChangeListener(this);

        mVibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);

        isSmartChgAvailable();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference == mCABC) {
            mCABC.setValue((String) newValue);
            mCABC.setSummary(mCABC.getEntry());
            Utils.setStringProp(CABC_SYSTEM_PROPERTY, (String) newValue);
        }
		if (preference == mPerfProfile) {
            mPerfProfile.setValue((String) newValue);
            mPerfProfile.setSummary(mPerfProfile.getEntry());
            Utils.setStringProp(PERF_PROFILE_SYSTEM_PROPERTY, (String) newValue);
        }
		if (preference == mVibStrength) {
            mVibStrength.setValue((String) newValue);
            mVibStrength.setSummary(mVibStrength.getEntry());
            Utils.setStringProp(VIB_STRENGTH_SYSTEM_PROPERTY, (String) newValue);
            mVibrator.vibrate(VibrationEffect.createOneShot(85, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        return true;
    }

    // Remove Smart Charging preference if cool_down node is unavailable
    private void isSmartChgAvailable() {
        mPreferenceCategory = (PreferenceCategory) findPreference(KEY_CATEGORY_CHARGING);

        if (Utils.fileWritable(SmartChargingService.mmi_charging_enable)) {
        } else {
            getPreferenceScreen().removePreference(mPreferenceCategory);
        }
    }
}
