package com.duck.appandroid.messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duck.appandroid.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHodel> {
    private final List<MessageList> messageLists;
    private final Context context;

    public MessageAdapter(List<MessageList> messageLists, Context context) {
        this.messageLists = messageLists;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.MyViewHodel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHodel(LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_adpter_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MyViewHodel holder, int position) {

    }

    @Override
    public int getItemCount() {
        return messageLists.size();
    }

    static class MyViewHodel extends RecyclerView.ViewHolder {

        private CircleImageView profilePic;
        private TextView name;
        private TextView lastMessage;
        private TextView unseenMessages;

        public MyViewHodel(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);

        }
    }
}
