package com.jasoncareter.ipop.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.controller.AddActivity;
import com.jasoncareter.ipop.controller.MainActivity;
import com.jasoncareter.ipop.model.AppDataBase;
import com.jasoncareter.ipop.model.ContactItemObject;
import com.jasoncareter.ipop.model.ContactsRecyclerViewAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private RecyclerView mrecyclerview;
    private ContactsRecyclerViewAdapter madapter;
    private AppDataBase db = null ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mrecyclerview = view.findViewById(R.id.home_recycler);

        db = AppDataBase.getInstance(view.getContext());
        madapter = new ContactsRecyclerViewAdapter(db);

        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setAdapter(madapter);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(view.getContext()));

        mrecyclerview.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener(){
            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                super.onTouchEvent(rv, e);

            }
        });
        return view ;
    }

    @Override
    public void onResume() {
        Log.i("tag", "onResume: "+"resume function been called ");
//        madapter.UpdateList();
        if (madapter.getDataChanged())
            Log.i("tag", "onResume: "+"datachanged is True ");
            madapter.notifyDataSetChanged();
            madapter.setDataChanged(false);
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu,menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if(menu != null){
            if ( menu.getClass().getSimpleName().equals("MenuBuilder")){
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu ,true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add :
                Intent i = new Intent(getActivity() , AddActivity.class);
                startActivity(i);
                return true;
            case R.id.scan:

                Toast.makeText(getContext(),"not finished yet" , Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
