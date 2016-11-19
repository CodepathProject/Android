package com.codepath.project.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.codepath.project.android.fragments.CategoryDetailFragment;
import com.codepath.project.android.helpers.SmartFragmentStatePagerAdapter;

/**
 * Created by anmallya on 11/18/2016.
 */

public class CategoryDetailFragmentAdapter extends SmartFragmentStatePagerAdapter {
        final int PAGE_COUNT = 4;

        private Context context;

        public CategoryDetailFragmentAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }


        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return CategoryDetailFragment.newInstance(0);
                case 1:
                    return CategoryDetailFragment.newInstance(1);
                case 2:
                    return CategoryDetailFragment.newInstance(2);
                case 3:
                    return CategoryDetailFragment.newInstance(3);
                default:
                    return CategoryDetailFragment.newInstance(0);
            }
        }
    }

