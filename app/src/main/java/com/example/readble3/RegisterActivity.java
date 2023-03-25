package com.example.readble3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    // creating database reference
    private DatabaseReference databaseReference;

    // progress bar
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // initializing xml fields
        final EditText name = findViewById(R.id.r_name);
        final EditText mobile = findViewById(R.id.r_mobile);
        final EditText email = findViewById(R.id.r_email);
        final EditText password = findViewById(R.id.r_password);
        final AppCompatButton registerBtn = findViewById(R.id.r_registerBtn);
        final TextView loginNowBtn = findViewById(R.id.r_loginNowBtn);

        // getting database reference
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl(getString(R.string.database_url));

        // Create a progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        //check if user already exists
        if(!MemoryData.getMobile(this).isEmpty()){
            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
            intent.putExtra("mobile",MemoryData.getMobile(this));
            intent.putExtra("name",MemoryData.getName(this));
            intent.putExtra("email","");
            startActivity(intent);
            finish();
        }

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // getting user details from EditText fields
                final String nameTxt = name.getText().toString();
                final String mobileTxt = mobile.getText().toString();
                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                // checking whether user has entered all the fields on not
                if (nameTxt.isEmpty() || mobileTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "All Fields Required!!!", Toast.LENGTH_SHORT).show();

                } else {

                    // show progress bar
                    progressDialog.show();

                    // sending data to firebase
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            // hide progress bar
                            progressDialog.dismiss();

                            // checking if user's mobile number already exists
                            if (snapshot.child("users").hasChild(mobileTxt)) {
                                Toast.makeText(RegisterActivity.this, "Mobile already exists", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(RegisterActivity.this, "RegisterActivityed successfully", Toast.LENGTH_SHORT).show();

                                databaseReference.child("users").child(mobileTxt).child("email").setValue(emailTxt);
                                databaseReference.child("users").child(mobileTxt).child("name").setValue(nameTxt);
                                databaseReference.child("users").child(mobileTxt).child("password").setValue(passwordTxt);

                                // saving user's mobile number to memory so when the next time user will open the application then he will not have to login again
                                MemoryData.saveMobile(mobileTxt, RegisterActivity.this);

                                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                intent.putExtra("mobile",mobileTxt);
                                intent.putExtra("name",nameTxt);
                                intent.putExtra("email",emailTxt);
                                startActivity(intent);
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        loginNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // open login activity
                finish(); // finish this(register) activity
            }
        });
    }
}