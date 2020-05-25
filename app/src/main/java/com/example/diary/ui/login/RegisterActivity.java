package com.example.diary.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.diary.MainActivity;
import com.example.diary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText edt_email_register, edt_password_register, edt_confirmPassword_register;
    private Button btn_signUp;
    private ImageView img_back;
    private ProgressBar progressBar;
    private String user;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        MainActivity.auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        edt_email_register = (EditText)findViewById(R.id.edt_email_sinup);
        edt_password_register = (EditText)findViewById(R.id.edt_password_signup);
        edt_confirmPassword_register = (EditText)findViewById(R.id.edt_confirmPassword_signup);
        btn_signUp = (Button)findViewById(R.id.btn_signup);
        img_back = (ImageView)findViewById(R.id.img_back_signup);
        progressBar = (ProgressBar)findViewById(R.id.progressBar_regis);

        //back activity
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //sigin up
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = edt_email_register.getText().toString().trim();
                final String password = edt_password_register.getText().toString().trim();
                final String confirmPassword = edt_confirmPassword_register.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),"Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(getApplicationContext(),"Enter confirm password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                MainActivity.auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(!task.isSuccessful()){
                                    if(password.length() < 6){
                                        edt_password_register.setError("password must be at least 6 characters");
                                    }else {
                                        if(!password.equals(confirmPassword)){
                                            edt_confirmPassword_register.setError("Those password didn't match. Try agian!");
                                        }else{
                                            Toast.makeText(RegisterActivity.this,"Not successful", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(RegisterActivity.this,"You registered successfully", Toast.LENGTH_LONG).show();
                                    mDatabase.child(MainActivity.auth.getCurrentUser().getUid()).child("user").setValue(email);
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    intent.putExtra("email_current",email);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }
    public void createUserNode(final String userId, final String nickname) {
        final DatabaseReference mDbRoot = FirebaseDatabase.getInstance().getReference();
        mDbRoot.child(userId)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    mDbRoot.child(userId).child("userInfo").child("posts").setValue(nickname)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                    } else {
                                                        Toast.makeText(RegisterActivity.this, "Wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        }
                );
    }
}
