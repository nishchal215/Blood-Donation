package com.example.blooddonation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String[] tabTitles = new String[]{"Request", "Feed", "Chat"};
    FragmentManager fragmentManager;


    public PageAdapter(@NonNull FragmentManager fm, int numofTabs) {
        super(fm, numofTabs);
        fragmentManager = fm;
        this.numOfTabs=numofTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;

        switch (position) {
            case 0:
                return new RequestFragment();
            case 1:
                return new FeedFragment();
            case 2:
                return new ChatFragment();
        }

        return null;

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.frameLayout, fragment);
//        fragmentTransaction.commit();
//        return fragment;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
