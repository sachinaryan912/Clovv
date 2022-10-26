package com.company.clovv.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.company.clovv.fragment.WalkthroughOneFragment;
import com.company.clovv.fragment.WalkthroughThreeFragment;
import com.company.clovv.fragment.WalthroughTwoFragment;

public class AdapterWalkthrough extends FragmentStatePagerAdapter {


    public AdapterWalkthrough(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                WalkthroughOneFragment tab1 = new WalkthroughOneFragment();
                return tab1;

            case 1:
                WalthroughTwoFragment tab2 = new WalthroughTwoFragment();
                return tab2;

            case 2:
                WalkthroughThreeFragment tab3 = new WalkthroughThreeFragment();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
