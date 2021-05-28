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

package com.boxer.Iron;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.ViewPager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.boxer.Iron.tabs.ActionsTab;
import com.boxer.Iron.tabs.InterfaceTab;
import com.boxer.Iron.tabs.StatusBarTab;
import com.boxer.Iron.tabs.LockScreenTab;
import com.boxer.Iron.tabs.SystemMiscTab;

import com.boxer.Iron.navigation.BubbleNavigationConstraintView;
import com.boxer.Iron.navigation.BubbleNavigationChangeListener;

public class IronSettings extends SettingsPreferenceFragment {

    private static final String TAG = "IronSettings";

    Context mContext;
//    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();
        View view = inflater.inflate(R.layout.iron, container, false);

        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.iron_title);
        }

        BubbleNavigationConstraintView bubbleNavigationConstraintView =  (BubbleNavigationConstraintView) view.findViewById(R.id.bottom_navigation_view_constraint);
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getFragmentManager());
        viewPager.setAdapter(mPagerAdapter);

        bubbleNavigationConstraintView.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                if (view.getId() == R.id.tab_actions) {
                    viewPager.setCurrentItem(position, true);
                } else if (view.getId() == R.id.tab_interface) {
                    viewPager.setCurrentItem(position, true);
                } else if (view.getId() == R.id.tab_statusbar) {
                    viewPager.setCurrentItem(position, true);
                } else if (view.getId() == R.id.tab_lockscreen) {
                    viewPager.setCurrentItem(position, true);
                } else if (view.getId() == R.id.tab_system_misc) {
                    viewPager.setCurrentItem(position, true);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                bubbleNavigationConstraintView.setCurrentActiveItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        String titles[] = getTitles();
        private Fragment frags[] = new Fragment[titles.length];

        PagerAdapter(FragmentManager fm) {
            super(fm);
            frags[0] = new ActionsTab();
            frags[1] = new InterfaceTab();
            frags[2] = new StatusBarTab();
            frags[3] = new LockScreenTab();
            frags[4] = new SystemMiscTab();
        }

        @Override
        public Fragment getItem(int position) {
            return frags[position];
        }

        @Override
        public int getCount() {
            return frags.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    private String[] getTitles() {
        String titleString[];
        titleString = new String[]{
            getString(R.string.navigation_actions_title),
            getString(R.string.navigation_interface_title),
            getString(R.string.navigation_statusbar_title),
            getString(R.string.navigation_lockscreen_title),
            getString(R.string.navigation_system_title)};

        return titleString;
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.CUSTOM_SETTINGS;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0, 0, 0, R.string.ironos_about_title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            AboutIronOS newFragment = AboutIronOS .newInstance();
            newFragment.show(ft, "AboutIronOS");
            return true;
        }
        return false;
    }
}
