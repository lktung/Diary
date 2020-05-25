package com.example.diary.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edt_reset;
    private Button btn_reset;
    private ProgressBar loadingProgress;
    private ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        MainActivity.auth = FirebaseAuth.getInstance();

        edt_reset = (EditText)findViewById(R.id.edt_email_reset);
        loadingProgress = (ProgressBar)findViewById(R.id.progressbar_reset);
        btn_reset = (Button)findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_reset.getText().toString().trim();

                if(TextUtils.isEmpty( email )){
                    Toast.makeText(ResetPasswordActivity.this,"Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                }

                loadingProgress.setVisibility(View.VISIBLE);

                //sent
                MainActivity.auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                loadingProgress.setVisibility(View.GONE);
                            }
                        });
            }
        });

        img_back = (ImageView)findViewById(R.id.img_back_reset);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
