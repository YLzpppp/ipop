package com.jasoncareter.ipop.controller;


import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;


import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.model.MyFragmentPageAdapter;
import com.jasoncareter.ipop.view.mViewPager;

import java.lang.reflect.Method;


public class MainActivity extends AppCompatActivity {

    public static final int homeFragmentPage = 0;
    public static final int messageFragmentPage = 1;



    private mViewPager viewPager;
    private MyFragmentPageAdapter madapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected( MenuItem item) {
            switch ( item.getItemId() ) {

                case R.id.navigation_home:
                    viewPager.setCurrentItem(homeFragmentPage);
                    return true;

                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(messageFragmentPage);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         *  set status translucent
         */
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        /*
         *  set navigationview Listener
         */
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager = findViewById(R.id.view_pager);
        madapter = new MyFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(madapter);
        viewPager.setCurrentItem(homeFragmentPage);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
