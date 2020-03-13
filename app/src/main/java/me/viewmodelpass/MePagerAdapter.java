package me.viewmodelpass;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MePagerAdapter extends FragmentPagerAdapter {

    private static final String[] PAGER_TITLES = {"Arya", "Allen", "Alita"};


    private static final int TAB_COUNT = 3;

    public MePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return PAGER_TITLES[position];
    }


    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return BlankFragment.newInstance(PAGER_TITLES[position], "content first");
        } else if (position == 1) {
            return BlankFragment.newInstance(PAGER_TITLES[position], "content second");

        } else if (position == 2) {
            return BlankFragment.newInstance(PAGER_TITLES[position], "content third");

        }
        return BlankFragment.newInstance("none", "content none");

    }

}