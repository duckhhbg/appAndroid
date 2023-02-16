package com.duck.appandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
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

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading....");

        // check tài khoản đã được log in
        if (!MemoryData.getData(this).isEmpty()) {

            Intent intent = new Intent(dangKy.this,MainActivity.class);
            intent.putExtra("sdt",MemoryData.getData(this));
            intent.putExtra("name",MemoryData.getName(this));
            intent.putExtra("email","");
            startActivity(intent);
            finish();
        }

        dangkyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                final String nameTxt = name.getText().toString();
                final String sdtTxt = sdt.getText().toString();
                final String emailTxt = email.getText().toString();

                if (nameTxt.isEmpty() || sdtTxt.isEmpty() || emailTxt.isEmpty()) {
                    Toast.makeText(dangKy.this,"Chưa điền đủ thông tin!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else {
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            progressDialog.dismiss();

                            if (snapshot.child("users").hasChild(sdtTxt)) {
                                Toast.makeText(dangKy.this,"Số điện thoại đã tồn tại",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                databaseReference.child("users").child(sdtTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(sdtTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(sdtTxt).child("profile_pic").setValue("");

                                //Lưu số điện thoại vào bộ nhớ
                                MemoryData.saveData(sdtTxt, dangKy.this);
                                //Lưu số tên vào bộ nhớ
                                MemoryData.saveName(nameTxt,dangKy.this);

                                Toast.makeText(dangKy.this,"Đăng ký thành công",Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(dangKy.this,MainActivity.class);
                                intent.putExtra("sdt",sdtTxt);
                                intent.putExtra("name",nameTxt);
                                intent.putExtra("email",emailTxt);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });
    }
}