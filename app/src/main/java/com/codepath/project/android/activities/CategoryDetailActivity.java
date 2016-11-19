package com.codepath.project.android.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.CategoryDetailFragmentAdapter;

public class CategoryDetailActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        setupTabs();
    }

    String[] list = {"Samsung","Apple","Sony","LG"};

    private void setupTabs(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new CategoryDetailFragmentAdapter(getSupportFragmentManager(),
                this));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        System.out.println("############## list "+tabLayout.getTabCount());
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            System.out.println("############## list "+list[i]);
            tabLayout.getTabAt(i).setText(list[i]);
        }
    }
}
