package com.duck.appandroid.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.duck.appandroid.MemoryData;
import com.duck.appandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://btlandroid-47dc0-default-rtdb.firebaseio.com/");

    private final List<chatList> chatLists = new ArrayList<>();

    private String chatKey;
    String getUserSdt = "";
    private RecyclerView chattingRecyclerView;
    private chatAdapter chatAdapter;
    private boolean loadingFirstTime = true ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditTxt = findViewById(R.id.messageEditTxt);
        final ImageView sendBtn = findViewById(R.id.sendBtn);
        final CircleImageView profilePic = findViewById(R.id.profilePic);


        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);
        //lấy dữ liệu từ message adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getSdt = getIntent().getStringExtra("sdt");

        //Lấy sdt user từ bộ nhớ
        getUserSdt = MemoryData.getData(Chat.this);


        nameTV.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);

        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(Chat.this));

        chatAdapter = new chatAdapter(chatLists, Chat.this);
        chattingRecyclerView.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (chatKey.isEmpty()) {
                    //Tạo chat key. Mặc đinh chatkey = 1
                    chatKey = "1";
                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                if (snapshot.hasChild("chat")) {

                    if (snapshot.child("chat").child(chatKey).hasChild("messages")) {

                        chatLists.clear();

                        for (DataSnapshot messagesSnapshot : snapshot.child("chat").child(chatKey).child("messages").getChildren()) {

                            if(messagesSnapshot.hasChild("msg") && messagesSnapshot.hasChild("sdt")) {
                                final String messageTimestamps = messagesSnapshot.getKey();
                                final String getSdt = messagesSnapshot.child("sdt").getValue(String.class);
                                final String getMsg = messagesSnapshot.child("msg").getValue(String.class);

                                Timestamp timestamp = new Timestamp(Long.parseLong(messageTimestamps));
                                Date date = new Date(timestamp.getTime());
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yy", Locale.getDefault());
                                SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                chatList chatList = new chatList(getSdt,getName,getMsg, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                                chatLists.add(chatList);

                                if (loadingFirstTime || Long.parseLong(messageTimestamps) > Long.parseLong(MemoryData.getLastMsgTS(Chat.this, chatKey))) {

                                    loadingFirstTime = false;
                                    MemoryData.saveLastMsgTS(messageTimestamps, chatKey, Chat.this);
                                    chatAdapter.updateChatList(chatLists);

                                    chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
                                }

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getTxtMessages = messageEditTxt.getText().toString();

                //Lấy thời gian gửi
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserSdt);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getSdt);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessages);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("sdt").setValue(getUserSdt);

                //clear edittext khi tin nhắn được gửi đi
                messageEditTxt.setText("");

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}