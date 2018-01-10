package com.example.suyog.locationtracker;

import android.support.v4.app.Fragment;

/**
 * Created by ADMIN-PC on 10-01-2018.
 */

public class MainActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return LoginFragment.newInstance();
    }




}
