package com.duck.appandroid.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duck.appandroid.MemoryData;
import com.duck.appandroid.R;

import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHodel> {

    private List<chatList> chatLists;
    private final Context context;
    private String userSdt;

    public chatAdapter(List<chatList> chatLists, Context context) {
        this.chatLists = chatLists;
        this.context = context;
        this.userSdt = MemoryData.getData(context);
    }

    @NonNull
    @Override
    public chatAdapter.MyViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodel(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_adapter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull chatAdapter.MyViewHodel holder, int position) {

        chatList list2 = chatLists.get(position);

        if (list2.getSdt().equals(userSdt)) {
            holder.myLayout.setVisibility(View.VISIBLE);
            holder.oppoLayout.setVisibility(View.GONE);

            holder.myMessage.setText(list2.getMessage());
            holder.myMsgTime.setText(list2.getDate() + " " + list2.getTime());
        }
        else {
            holder.oppoLayout.setVisibility(View.VISIBLE);
            holder.myLayout.setVisibility(View.GONE);

            holder.oppoMessage.setText(list2.getMessage());
            holder.oppoMsgTime.setText(list2.getDate() + " " + list2.getTime());

        }

    }

    @Override
    public int getItemCount() {
        return chatLists.size();
    }

    public void updateChatList(List<chatList> chatLists) {

        this.chatLists = chatLists;
    }

    static class MyViewHodel extends RecyclerView.ViewHolder {

        private LinearLayout oppoLayout, myLayout;
        private TextView oppoMessage, myMessage;
        private TextView oppoMsgTime, myMsgTime;
        public MyViewHodel(@NonNull View itemView) {
            super(itemView);

            oppoLayout = itemView.findViewById(R.id.oppoLayout);
            myLayout = itemView.findViewById(R.id.myLayout);
            oppoMessage = itemView.findViewById(R.id.oppoMessage);
            myMessage = itemView.findViewById(R.id.myMessage);
            oppoMsgTime = itemView.findViewById(R.id.oppoMsgTime);
            myMsgTime = itemView.findViewById(R.id.myMsgTime);


        }
    }
}
