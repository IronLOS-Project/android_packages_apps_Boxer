/*
 * Copyright (C) 2020 IronOS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boxer.Iron.fragments.lockscreen;

import android.content.Context;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.SearchIndexableResource;
import android.os.UserHandle;
import android.provider.Settings;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.SwitchPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import android.text.TextUtils;

import com.android.internal.util.iron.FodUtils;

import com.android.settings.R;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.Indexable;
import com.android.settingslib.search.SearchIndexable;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import androidx.preference.*;

import com.android.internal.logging.nano.MetricsProto;

import com.iron.support.preferences.CustomSeekBarPreference;
import com.iron.support.preferences.SystemSettingListPreference;
import com.iron.support.preferences.SystemSettingMasterSwitchPreference;
import com.iron.support.preferences.SystemSettingSwitchPreference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SearchIndexable
public class LockscreenGeneral extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {

    private static final String KEY_PULSE_BRIGHTNESS = "ambient_pulse_brightness";
    private static final String KEY_DOZE_BRIGHTNESS = "ambient_doze_brightness";
    private static final String POCKET_JUDGE = "pocket_judge";
    private static final String KEY_EDGE_LIGHTNING = "pulse_ambient_light";
    private static final String LOCK_DATE_FONTS = "lock_date_fonts";
    private static final String LOCK_CLOCK_FONT_STYLE = "lock_clock_font_style";

    private CustomSeekBarPreference mPulseBrightness;
    private CustomSeekBarPreference mDozeBrightness;
    private Context mContext;
    private Preference mPocketJudge;
    private SystemSettingMasterSwitchPreference mEdgeLightning;
    private ListPreference mLockDateFonts;
    private ListPreference mLockClockFonts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContentResolver resolver = getActivity().getContentResolver();
        addPreferencesFromResource(R.xml.lockscreen_general);

        mContext = getActivity();

        final Resources res = getResources();
        final PreferenceScreen prefScreen = getPreferenceScreen();

        int defaultDoze = getResources().getInteger(
                com.android.internal.R.integer.config_screenBrightnessDoze);
        int defaultPulse = getResources().getInteger(
                com.android.internal.R.integer.config_screenBrightnessPulse);
        if (defaultPulse == -1) {
            defaultPulse = defaultDoze;
        }

        mPulseBrightness = (CustomSeekBarPreference) findPreference(KEY_PULSE_BRIGHTNESS);
        int value = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_PULSE_BRIGHTNESS, defaultPulse);
        mPulseBrightness.setValue(value);
        mPulseBrightness.setOnPreferenceChangeListener(this);

        mDozeBrightness = (CustomSeekBarPreference) findPreference(KEY_DOZE_BRIGHTNESS);
        value = Settings.System.getInt(getContentResolver(),
                Settings.System.OMNI_DOZE_BRIGHTNESS, defaultDoze);
        mDozeBrightness.setValue(value);
        mDozeBrightness.setOnPreferenceChangeListener(this);

        // Lockscren Date Fonts
        mLockDateFonts = (ListPreference) findPreference(LOCK_DATE_FONTS);
        mLockDateFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_DATE_FONTS, 28)));
        mLockDateFonts.setSummary(mLockDateFonts.getEntry());
        mLockDateFonts.setOnPreferenceChangeListener(this);

        mLockClockFonts = (ListPreference) findPreference(LOCK_CLOCK_FONT_STYLE);
        mLockClockFonts.setValue(String.valueOf(Settings.System.getInt(
                getContentResolver(), Settings.System.LOCK_CLOCK_FONT_STYLE, 0)));
        mLockClockFonts.setSummary(mLockClockFonts.getEntry());
        mLockClockFonts.setOnPreferenceChangeListener(this);

        mPocketJudge = (Preference) prefScreen.findPreference(POCKET_JUDGE);
        boolean mPocketJudgeSupported = res.getBoolean(
                com.android.internal.R.bool.config_pocketModeSupported);
        if (!mPocketJudgeSupported)
            prefScreen.removePreference(mPocketJudge);
        mEdgeLightning = (SystemSettingMasterSwitchPreference)
                findPreference(KEY_EDGE_LIGHTNING);
        boolean enabled = Settings.System.getIntForUser(resolver,
                KEY_EDGE_LIGHTNING, 0, UserHandle.USER_CURRENT) == 1;
        mEdgeLightning.setChecked(enabled);
        mEdgeLightning.setOnPreferenceChangeListener(this);

        SystemSettingSwitchPreference mFodAnim = (SystemSettingSwitchPreference) findPreference("fod_recognizing_animation");
        Preference mFodAnimList = (Preference) findPreference("fod_recognizing_animation_list");

        boolean mFodAnimPkgInstalled = com.android.internal.util.iron.Utils.isPackageInstalled(getContext(),getResources()
                .getString(com.android.internal.R.string.config_fodAnimationPackage));
        PreferenceCategory fod = (PreferenceCategory) prefScreen.findPreference("fod_category");
        if (!mFodAnimPkgInstalled) {
            fod.removePreference(mFodAnim);
            fod.removePreference(mFodAnimList);
        }
        if (!FodUtils.hasFodSupport(mContext)) {
            prefScreen.removePreference(fod);
        }
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
        if (preference == mPulseBrightness) {
            int value = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.OMNI_PULSE_BRIGHTNESS, value);
            return true;
         } else if (preference == mDozeBrightness) {
            int value = (Integer) newValue;
            Settings.System.putInt(getContentResolver(),
                    Settings.System.OMNI_DOZE_BRIGHTNESS, value);
            return true;
         } else if (preference == mLockDateFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_DATE_FONTS,
                    Integer.valueOf((String) newValue));
            mLockDateFonts.setValue(String.valueOf(newValue));
            mLockDateFonts.setSummary(mLockDateFonts.getEntry());
            return true;
         } else if (preference == mLockClockFonts) {
            Settings.System.putInt(getContentResolver(), Settings.System.LOCK_CLOCK_FONT_STYLE,
                    Integer.valueOf((String) newValue));
            mLockClockFonts.setValue(String.valueOf(newValue));
            mLockClockFonts.setSummary(mLockClockFonts.getEntry());
            return true;
         } else if (preference == mEdgeLightning) {
            boolean value = (Boolean) newValue;
            Settings.System.putIntForUser(resolver, KEY_EDGE_LIGHTNING,
                    value ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM_SETTINGS;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
            @Override
            public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                    boolean enabled) {
                final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                final SearchIndexableResource sir = new SearchIndexableResource(context);
                sir.xmlResId = R.xml.lockscreen_general;
                result.add(sir);
                return result;
            }

            @Override
            public List<String> getNonIndexableKeys(Context context) {
                final List<String> keys = super.getNonIndexableKeys(context);
                return keys;
            }
    };
}
