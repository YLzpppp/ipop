package com.jasoncareter.ipop.model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jasoncareter.ipop.R;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final ArrayList<Message> mMessageList = new ArrayList<>();

    /**
     *  Part of ViewHolder
     */
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMessage;
        TextView rightMessage;
        public ViewHolder(View view){
            super(view);
            leftLayout=(LinearLayout)view.findViewById(R.id.left_layout);
            rightLayout=(LinearLayout)view.findViewById(R.id.right_layout);
            leftMessage=(TextView)view.findViewById(R.id.left_msg);
            rightMessage=(TextView)view.findViewById(R.id.right_msg);
        }
    }

    /*
     * Part of Adapter
     *
     */
    public MessageAdapter(){

    }
//    public MessageAdapter(ArrayList<Message> MessageList){
//        mMessageList=MessageList;
//    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder Holder,int position){
        Message Message=mMessageList.get(position);
        if(Message.getType()==Message.TYPE_RECEIVED){
            Holder.leftLayout.setVisibility(View.VISIBLE);
            Holder.rightLayout.setVisibility(View.GONE);
            Holder.leftMessage.setText(Message.getContent());
        }else if(Message.getType()==Message.TYPE_SENT) {
            Holder.rightLayout.setVisibility(View.VISIBLE);
            Holder.leftLayout.setVisibility(View.GONE);
            Holder.rightMessage.setText(Message.getContent());
        }
    }
    @Override
    public int getItemCount(){
        return mMessageList.size();
    }

    public void addMessage(Message message,int position){
        mMessageList.add(message);
        position = getItemCount() - 1;
        notifyItemInserted(position);
    }

}
