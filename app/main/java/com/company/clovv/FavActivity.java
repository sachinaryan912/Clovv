package com.company.clovv;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.company.clovv.adapter.VpAdapter;
import com.company.clovv.fragment.FavProductFragment;
import com.company.clovv.fragment.FavShopFragment;
import com.google.android.material.tabs.TabLayout;

public class FavActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        tabLayout = findViewById(R.id.tab_lay_order);
        viewPager = findViewById(R.id.view_pager1);


        tabLayout.setupWithViewPager(viewPager);

        VpAdapter vpAdapter = new VpAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new FavProductFragment(),"PRODUCTS");
        vpAdapter.addFragment(new FavShopFragment(),"SHOPS");

        viewPager.setAdapter(vpAdapter);

    }
}