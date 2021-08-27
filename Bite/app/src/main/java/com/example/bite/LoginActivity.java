package com.example.bite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignIn;
    TextView SignUp;
    FirebaseAuth bitefirebase;
    private FirebaseAuth.AuthStateListener biteAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bitefirebase = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.button);
        SignUp = findViewById(R.id.textView);

        biteAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser bitefirebaseUser = bitefirebase.getCurrentUser();
                if(bitefirebaseUser != null){
                    Toast.makeText(LoginActivity.this, "Logged In Sucessfully",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(LoginActivity.this, "Try To Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String paswd = password.getText().toString();
                if(email.isEmpty()) {
                    emailId.setError("Please Entetr User EmailId");
                    emailId.requestFocus();
                }
                else if (paswd.isEmpty())
                {
                    password.setError("Password is Empty");
                    password.requestFocus();
                }
                else if(email.isEmpty() && paswd.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Field are Empty!!!",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && paswd.isEmpty()))
                {
                    bitefirebase.signInWithEmailAndPassword(email, paswd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"LogIn Error, please Try Again ",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Intent inToHome = new Intent(LoginActivity.this,HomeActivity.class);
                                startActivity(inToHome);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(LoginActivity.this,"Error !!!!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent IntSignup = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(IntSignup);
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bitefirebase.addAuthStateListener(biteAuthStateListener);
    }
}