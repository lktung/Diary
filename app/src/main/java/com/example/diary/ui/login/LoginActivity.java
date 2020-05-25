package com.example.diary.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diary.MainActivity;
import com.example.diary.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private Button btn_login, btn_reset_pass,btn_signup;
    private TextView lbl_register;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        MainActivity.auth = FirebaseAuth.getInstance();
        if(MainActivity.auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        inputEmail = (EditText)findViewById(R.id.email_login);
        inputPassword = (EditText)findViewById(R.id.password_login);
        progressBar = (ProgressBar)findViewById(R.id.loadingProgressBar);
        btn_login = (Button)findViewById(R.id.btn_login);
        btn_reset_pass = (Button)findViewById(R.id.btn_reset_password);
        //lbl_register = (TextView)findViewById(R.id.lb_register);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        img_back = (ImageView)findViewById(R.id.img_back_login);

        //get Firebase aurh instance
        MainActivity.auth = FirebaseAuth.getInstance();

        //reset-password
        btn_reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        //register
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        /*lbl_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
*/
        //back Mainactivity
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();;
            }
        });

        //sigin-in
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String email_login_current = inputEmail.getText().toString();
                final String password_login_current = inputPassword.getText().toString();

                if(TextUtils.isEmpty( email_login_current )){
                    Toast.makeText(getApplicationContext(),"Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty( password_login_current )){
                    Toast.makeText(getApplicationContext(),"Enter password address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!haveNetwork()){
                    Toast.makeText(getApplicationContext(),"Network conection is not available",Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                MainActivity.auth.signInWithEmailAndPassword(email_login_current, password_login_current)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if(!task.isSuccessful()){
                                    if(password_login_current.length() < 6){
                                        inputPassword.setError("password must be at least 6 characters");
                                    }else{
                                        Toast.makeText(LoginActivity.this,"Incorrect email or password.", Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("email_current",email_login_current);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    public boolean haveNetwork(){
        boolean HAVE_WIFI = false;
        boolean HAVE_MOBILEDATA = false;

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for(NetworkInfo info:networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI")){
                if(info.isConnected())
                    HAVE_WIFI = true;
            }
            if(info.getTypeName().equalsIgnoreCase("BOBILE")){
                if(info.isConnected())
                    HAVE_MOBILEDATA = true;
            }
        }
        return HAVE_WIFI || HAVE_MOBILEDATA;
    }
}
