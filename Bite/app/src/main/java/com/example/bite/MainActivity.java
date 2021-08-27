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

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignUp;
    TextView SignIn;
       FirebaseAuth bitefirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bitefirebase = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btnSignUp = findViewById(R.id.button);
        SignIn = findViewById(R.id.textView);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(MainActivity.this,"Field are Empty!!!",Toast.LENGTH_SHORT).show();
                }
                else if (!(email.isEmpty() && paswd.isEmpty()))
                {
                    bitefirebase.createUserWithEmailAndPassword(email,paswd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"LogIn Error, Please Try Again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this,"Error !!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();

            }
        });

    }
}