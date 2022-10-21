package com.app.mealer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginForm extends AppCompatActivity {
    private FirebaseAuth mauth;
    private FirebaseFirestore fstore;
    private EditText mail, password;
    private Button login_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        mauth = FirebaseAuth.getInstance();
        fstore=FirebaseFirestore.getInstance();
        mail=findViewById(R.id.login_mail);
        password=findViewById(R.id.login_password);
        login_btn=findViewById(R.id.login_btn);
        
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });


        
        

    }

    private void login() {
        String user=mail.getText().toString().trim();
        String pass=password.getText().toString().trim();
        if(user.isEmpty()){
            mail.setError("Email can not be empty..");
        }
        if(pass.isEmpty()){
            password.setError("Password can not be empty");
        }
        else{
//            mauth.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                @Override
//                public void onComplete(@NonNull Task<AuthResult> task) {
//                    if(task.isSuccessful()){
//                        Toast.makeText(LoginForm.this,"Logoin Successfully", Toast.LENGTH_SHORT).show();
//                        checkIfCook
//                        startActivity(new Intent(LoginForm.this, WelcomeActivity.class));
//                    }
//                    else{
//                        Toast.makeText(LoginForm.this,"Login Failed"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
            mauth.signInWithEmailAndPassword(user,pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(LoginForm.this,"Logoin Successfully", Toast.LENGTH_SHORT).show();
                    checkuseraccesslevel(authResult.getUser().getUid());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginForm.this,"Logoin Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkuseraccesslevel(String uid) {
        DocumentReference df=fstore.collection("Users").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                Log.d("dataonsuccess",documentSnapshot.getData().toString());
                if(documentSnapshot.getString("iscook")!=null){
                    startActivity(new Intent(getApplicationContext(),CookWelcome.class));

                }
                else{
                    startActivity(new Intent(getApplicationContext(),WelcomeActivity.class));
                }

            }
        });


    }
}