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

package com.boxer.Iron.tabs;

import android.os.Bundle;
import android.preference.Preference.OnPreferenceChangeListener;

import androidx.preference.Preference;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.widget.CardPreference;

public class LockScreenTab extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {

    private static final String LOCKSCREEN_AOSP_CATEGORY = "lockscreen_aosp_category";
    private static final String LOCKSCREEN_GENERAL_CATEGORY = "lockscreen_general_category";
//    private static final String LOCKSCREEN_TUNER_CATEGORY = "lockscreen_tuner_category";
    private static final String LOCKSCREEN_WEATHER_CATEGORY = "lockscreen_weather";

    private CardPreference mLockscreenAosp;
    private CardPreference mLockscreenGeneral;
//    private CardPreference mLockscreenTuner;
    private CardPreference mLockscreenWeather;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.tab_lock_screen);

        CardPreference mLockscreenAosp = findPreference("lockscreen_aosp_category");
        if (!getResources().getBoolean(R.bool.lockscreen_aosp_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenAosp);
        } else {
            mLockscreenAosp = (CardPreference) findPreference(LOCKSCREEN_AOSP_CATEGORY);
        }

        CardPreference mLockscreenGeneral = findPreference("lockscreen_general_category");
        if (!getResources().getBoolean(R.bool.lockscreen_general_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenGeneral);
        } else {
            mLockscreenGeneral = (CardPreference) findPreference(LOCKSCREEN_GENERAL_CATEGORY);
        }
/** Uncomment once Tuner is added
        CardPreference mLockscreenTuner = findPreference("lockscreen_tuner_category");
        if (!getResources().getBoolean(R.bool.lockscreen_tuner_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenTuner);
        } else {
            mLockscreenTuner = (CardPreference) findPreference(LOCKSCREEN_TUNER_CATEGORY);
        }
*/
        CardPreference mLockscreenWeather = findPreference("lockscreen_weather");
        if (!getResources().getBoolean(R.bool.lockscreen_weather_category_isVisible)) {
            getPreferenceScreen().removePreference(mLockscreenWeather);
        } else {
            mLockscreenWeather = (CardPreference) findPreference(LOCKSCREEN_WEATHER_CATEGORY);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceChange(Preference preference, Object objValue) {
        final String key = preference.getKey();
        return false;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM_SETTINGS;
    }
}
