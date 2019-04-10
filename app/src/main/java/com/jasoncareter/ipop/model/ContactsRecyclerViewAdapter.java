package com.jasoncareter.ipop.model;


import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jasoncareter.ipop.R;
import com.jasoncareter.ipop.controller.MainActivity;
import com.jasoncareter.ipop.controller.TalkActivity;

import java.util.ArrayList;


public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewHolder> {

    private static final String TAG = "mtag";
    private static boolean dataChanged = false ;
    private static final ArrayList<ContactItemObject> arrayList = new ArrayList<>();
    private AppDataBase db ;
    public ContactsRecyclerViewAdapter(final AppDataBase db){
        this.db = db ;
        //empty constructor
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: "+" list size : "+db.contactsDao().getAllContacts().size());
                for(ContactsEntity entity : db.contactsDao().getAllContacts()){
                    String name = entity.getUsername();
                    ContactItemObject contactsEntity = new ContactItemObject(name , null);
                    if ( !repeatable(name)) {
                        arrayList.add(new ContactItemObject(name, null));
                        Log.i(TAG, "run: " + arrayList.size());
                    }
                }
            }
        }).start();
    }
    private boolean repeatable(String name){
        int count = 0;
        for (ContactItemObject obj : arrayList){
            if (obj.getName() == name){
                count ++;
            }
            if(count > 1)
                return true;
        }
        return false;
    }

    @Override
    public ContactsRecyclerViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.home_contacts_item , viewGroup ,false);
        return new ContactsRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ContactsRecyclerViewHolder mViewHolder, final int i) {
        ContactItemObject currentobj = arrayList.get(i);
        if ( currentobj.getName() != null)
            mViewHolder.name.setText( currentobj.getName() );
        if (currentobj.getDrawable() != null )
            mViewHolder.header.setBackground( currentobj.getDrawable() );
        mViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = arrayList.get(i).getName();
                Intent i = new Intent(mViewHolder.view.getContext() , TalkActivity.class);
                Log.i(TAG, "onClick: "+name);
                i.putExtra("TargetClient",name);
                mViewHolder.view.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void deleteItem(int position){
        arrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void ArrayAddItem(ContactItemObject object){
        arrayList.add(object);
    }

    public void setDataChanged(boolean changed){
        this.dataChanged = changed;
    }
    public boolean getDataChanged(){
        return dataChanged;
    }
}
