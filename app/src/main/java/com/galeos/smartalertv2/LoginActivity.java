package com.galeos.smartalertv2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    RadioButton radioButtonUser,radioButtonEmployee;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String isUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();


    }

    private void setReferences() {
        radioButtonUser = findViewById(R.id.radioButtonUser);
        radioButtonEmployee = findViewById(R.id.radioButtonEmployee);
        //isUser = intent.getStringExtra("isUser");

        // Set Log In RadioButton as checked by default
        radioButtonUser.setChecked(true);

        //TODO: Add onbuttonclick -> loginAccountInFirebase

    }

    void loginAccountInFirebase(String email, String password){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    if(firebaseUser.isEmailVerified()){
                        firestore = FirebaseFirestore.getInstance();
                        checkUserAccessLevel(firebaseUser);
                    }else{
                        Toast.makeText(LoginActivity.this, getString(R.string.email_verify), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //TODO: Create UserMainActivity and EmployeeMainAcivity.
    void checkUserAccessLevel(FirebaseUser firebaseuser){
        DocumentReference df = firestore.collection("users").document(firebaseuser.getUid());

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isUser").equals("1") && isUser.equals("1")){
                    //startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                    finish();
                }else if (documentSnapshot.getString("isUser").equals("0") && isUser.equals("0")){
                    //startActivity(new Intent(LoginActivity.this, EmployeeMainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,getString(R.string.Account_doesnt_exist), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
