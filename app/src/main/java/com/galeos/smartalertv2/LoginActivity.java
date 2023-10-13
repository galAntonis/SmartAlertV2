package com.galeos.smartalertv2;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    RadioButton radioButtonUser,radioButtonEmployee;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();

        //TODO: Add onbuttonclick -> loginAccountInFirebase
    }

    private void setReferences() {
        radioButtonUser = findViewById(R.id.radioButtonUser);
        radioButtonEmployee = findViewById(R.id.radioButtonEmployee);

        // Set Log In RadioButton as checked by default
        radioButtonUser.setChecked(true);

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

    //TODO: Create checkUserAccessLevel function. First need to fix some stuff in Firestore
    void checkUserAccessLevel(FirebaseUser firebaseuser){

    }
}
