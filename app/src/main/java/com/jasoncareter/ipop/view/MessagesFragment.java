package com.jasoncareter.ipop.view;

import android.arch.persistence.room.Dao;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.model.AppDataBase;
import com.jasoncareter.ipop.model.MeInfoEntity;


public class MessagesFragment extends Fragment {

    private int killport =0;
    private String id = "";
    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_messages, container, false);
        final AppDataBase db = AppDataBase.getInstance(container.getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                MeInfoEntity entity = db.meInfoDao().getAll();
                id = entity.getMeIdentity();
                killport = entity.getMePort();
              }
        }).start();
        Button button = view.findViewById(R.id.clickme);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tag", "clickme: "+"Message Fragment , Identity : "+id+"  port : "+killport);
            }
        });
        return view;
    }

}
