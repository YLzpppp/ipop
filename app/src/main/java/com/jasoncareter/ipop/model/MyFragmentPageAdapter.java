package com.jasoncareter.ipop.model;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.jasoncareter.ipop.controller.MainActivity;
import com.jasoncareter.ipop.view.HomeFragment;
import com.jasoncareter.ipop.view.MessagesFragment;

public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    private int pagecount = 2;
    private HomeFragment homeFragment = null;
    private MessagesFragment messagesFragment = null;

    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
        homeFragment = new HomeFragment();
        messagesFragment = new MessagesFragment();
    }


    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fa = null;
        switch ( i ){
            case MainActivity.homeFragmentPage :
                fa = homeFragment;
                break;
            case MainActivity.messageFragmentPage :
                fa = messagesFragment;
                break;
        }
        return fa;
    }

    @Override
    public int getCount() {
        return pagecount;
    }

    @Override
    public void destroyItem( ViewGroup container, int position,  Object object) {
        super.destroyItem(container, position, object);
    }
}
