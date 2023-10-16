package com.galeos.smartalertv2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

// TODO: Use Google, Facebook or Twitter as a Login option
// TODO: Fix the xml
public class LoginActivity extends AppCompatActivity {
    RadioButton radioButtonUser,radioButtonEmployee;
    Button signInBtn;
    EditText passwordEditText,emailEditText;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    Boolean isUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();


    }

    private void setReferences() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        radioButtonUser = findViewById(R.id.radioButtonUser);
        radioButtonEmployee = findViewById(R.id.radioButtonEmployee);
        signInBtn = findViewById(R.id.signInBtn);
        //isUser = intent.getStringExtra("isUser");

        // Set Log In RadioButton as checked by default
        radioButtonUser.setChecked(true);
        isUser = true;

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });



    }


    private void loginUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        boolean isValidated = validateData(email,password);
        if(!isValidated){
            return;
        }
        if(radioButtonUser.isChecked())
            isUser = true;
        else
            isUser = false;
        loginAccountInFirebase(email,password);
    }

    private void loginAccountInFirebase(String email, String password){
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    //TODO: Create EmployeeMainAcivity.
    private void checkUserAccessLevel(FirebaseUser firebaseuser){
        DocumentReference df = firestore.collection("users").document(firebaseuser.getUid());

        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getBoolean("isUser").equals(true) && isUser.equals(true)){
                    startActivity(new Intent(LoginActivity.this, UserMainActivity.class));
                    finish();
                }else if (documentSnapshot.getBoolean("isUser").equals(false) && isUser.equals(false)){
                    //startActivity(new Intent(LoginActivity.this, EmployeeMainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,getString(R.string.Account_doesnt_exist), Toast.LENGTH_SHORT).show();
                }
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    //TODO: Do some proper validation maybe
    boolean validateData(String email, String password) {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.email_invalid));
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError(getString(R.string.password_invalid));
            return false;
        }
        return true;
    }
}
