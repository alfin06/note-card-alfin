package com.notecards.yohaniswarahartono.notecards;


import android.support.v4.app.Fragment;

public class SubjectListActivity extends SingleFragmentActivity
{
    @Override
    protected Fragment createFragment() {
        return new SubjectListFragment();
    }

}
