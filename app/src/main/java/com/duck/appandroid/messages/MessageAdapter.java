package com.duck.appandroid.messages;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duck.appandroid.R;
import com.duck.appandroid.chat.Chat;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHodel> {
    private List<MessageList> messageLists;
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

        MessageList list2 = messageLists.get(position);

        if (list2.getProfilePic() != null) {
            Picasso.get().load(list2.getProfilePic()).into(holder.profilePic);
        }

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLastMessage());

        if (list2.getUnseenMessages() == 0) {
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        }
        else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUnseenMessages()+"");
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));
        }

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("sdt",list2.getSdt());
                intent.putExtra("name",list2.getName());
                intent.putExtra("profile_pic",list2.getProfilePic());
                intent.putExtra("chat_key",list2.getChatKey());

                context.startActivity(intent);
            }
        });
    }

    public void updateDta(List<MessageList> messageLists) {

        this.messageLists = messageLists;
        notifyDataSetChanged();
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
        private LinearLayout rootLayout;

        public MyViewHodel(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.rootLayout);

        }
    }
}
