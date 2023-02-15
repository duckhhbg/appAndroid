package com.duck.appandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class dangKy extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://btlandroid-47dc0-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        final EditText name = findViewById(R.id.r_name);
        final EditText sdt = findViewById(R.id.r_sdt);
        final EditText email = findViewById(R.id.r_email);
        final AppCompatButton dangkyBtn = findViewById(R.id.r_dangkyBtn);

        dangkyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nameTxt = name.getText().toString();
                final String sdtTxt = sdt.getText().toString();
                final String emailTxt = email.getText().toString();

                if (nameTxt.isEmpty() || sdtTxt.isEmpty() || emailTxt.isEmpty()) {
                    Toast.makeText(dangKy.this,"Chưa điền đủ thông tin!",Toast.LENGTH_SHORT).show();
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.child("users").hasChild(sdtTxt)) {
                                Toast.makeText(dangKy.this,"Số điện thoại đã tồn tại",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("users").child(sdtTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(sdtTxt).child("name").setValue(nameTxt);

                                Toast.makeText(dangKy.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(dangKy.this,MainActivity.class);
                                intent.putExtra("sdt",sdtTxt);
                                intent.putExtra("name",nameTxt);
                                intent.putExtra("email",emailTxt);
                                startActivities(new Intent[]{intent});
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}