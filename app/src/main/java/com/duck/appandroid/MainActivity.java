package com.duck.appandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.duck.appandroid.messages.MessageAdapter;
import com.duck.appandroid.messages.MessageList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private final List<MessageList> messageLists = new ArrayList<>();
    private String sdt;
    private String email;
    private String name;
    private int unseenMessages = 0;
    private String lastMessage = "";
    private String chatKey = "";

    private boolean dataSet = false;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://btlandroid-47dc0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        messageRecyclerView = findViewById(R.id.messageRecyclerView);
        //get intent
        sdt = getIntent().getStringExtra("sdt");
        email = getIntent().getStringExtra("email");
        name = getIntent().getStringExtra("name");

        messageRecyclerView.setHasFixedSize(true);
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        messageAdapter = new MessageAdapter(messageLists, MainActivity.this);
        messageRecyclerView.setAdapter(messageAdapter);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        //Lấy ảnh từ filebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilePicUrl = snapshot.child("users").child(sdt).child("profile_pic").getValue(String.class);

                if (!profilePicUrl.isEmpty()) {
                    //set profile pic to circle image view
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                messageLists.clear();
                unseenMessages = 0;
                lastMessage = "";
                chatKey = "";

                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
                    final  String getSdt = dataSnapshot.getKey();

                    dataSet = false;

                    if (!getSdt.equals(sdt)) {
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profilePic").getValue(String.class);

                        databaseReference.child("chat").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                int getChatCounts = (int)snapshot.getChildrenCount();

                                if (getChatCounts > 0) {
                                    for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                        final String getKey = dataSnapshot1.getKey();
                                        chatKey = getKey;

                                        if ( dataSnapshot1.hasChild("user_1") &&  dataSnapshot1.hasChild("user_2") && dataSnapshot1.hasChild("messages")) {
                                            final String getUserOne = dataSnapshot1.child("user_1").getValue(String.class);
                                            final String getUserTwo = dataSnapshot1.child("user_2").getValue(String.class);

                                            if ((getUserOne.equals(getSdt) && getUserTwo.equals(sdt)) || (getUserOne.equals(sdt) && getUserTwo.equals(getSdt))) {

                                                for (DataSnapshot chatDataSnapshot : dataSnapshot1.child("messages").getChildren()) {

                                                    final long getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                                                    final long getLastSeenMessages = Long.parseLong(MemoryData.getLastMsgTS(MainActivity.this,getKey));

                                                    lastMessage = chatDataSnapshot.child("msg").getValue(String.class);

                                                    if (getMessageKey > getLastSeenMessages) {
                                                        unseenMessages++;
                                                    }
                                                }
                                            }
                                        }


                                    }
                                }

                                if (!dataSet) {
                                    dataSet = true;
                                    MessageList messageList = new MessageList(getName, getSdt,lastMessage,getProfilePic,unseenMessages,chatKey);
                                    messageLists.add(messageList);
                                    messageAdapter.updateDta(messageLists);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}