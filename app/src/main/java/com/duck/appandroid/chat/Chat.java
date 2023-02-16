package com.duck.appandroid.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private String chatKey;
    String getUserSdt = "";
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://btlandroid-47dc0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView backBtn = findViewById(R.id.backBtn);
        final TextView nameTV = findViewById(R.id.name);
        final EditText messageEditTxt = findViewById(R.id.messageEditTxt);
        final ImageView sentBtn = findViewById(R.id.sendBtn);
        final CircleImageView profilePic = findViewById(R.id.profilePic);


        //lấy dữ liệu từ message adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getSdt = getIntent().getStringExtra("sdt");

        //Lấy sdt user từ bộ nhớ
        getUserSdt = MemoryData.getData(Chat.this);


        nameTV.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);

        if (chatKey.isEmpty()) {

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //Tạo chat key. Mặc đinh chatkey = 1
                    chatKey = "1";
                    if (snapshot.hasChild("chat")) {
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String getTxtMessages = messageEditTxt.getText().toString();

                //Lấy thời gian gửi
                final String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

                MemoryData.saveLastMsgTS(currentTimestamp, chatKey,Chat.this);


                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserSdt);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getSdt);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("msg").setValue(getTxtMessages);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTimestamp).child("sdt").setValue(getUserSdt);



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