package com.codepath.project.android.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.codepath.project.android.R;
import com.codepath.project.android.adapter.CategoryDetailFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.codepath.project.android.data.TestData.tabNameList;

public class CategoryDetailActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        ButterKnife.bind(this);
        setupTabs();
    }

    private void setupTabs(){
        viewPager.setAdapter(new CategoryDetailFragmentAdapter(getSupportFragmentManager(),
                this));
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setText(tabNameList[i]);
        }
    }
}
