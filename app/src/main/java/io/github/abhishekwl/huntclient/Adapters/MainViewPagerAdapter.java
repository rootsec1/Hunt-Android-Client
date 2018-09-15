package io.github.abhishekwl.huntclient.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.abhishekwl.huntclient.Fragments.DepartmentFragment;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private String[] departmentsArrayList;

    public MainViewPagerAdapter(FragmentManager fm, String[] departmentsArrayList) {
        super(fm);
        this.departmentsArrayList = departmentsArrayList;
    }

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DepartmentFragment departmentFragment = new DepartmentFragment();
        departmentFragment.setDepartmentName(departmentsArrayList[position]);
        return departmentFragment;
    }

    @Override
    public int getCount() {
        return departmentsArrayList.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return departmentsArrayList[position];
    }
}
