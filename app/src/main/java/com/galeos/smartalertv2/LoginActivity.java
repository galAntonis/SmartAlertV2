package com.galeos.smartalertv2;

import android.os.Bundle;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    RadioButton radioButtonUser,radioButtonEmployee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setReferences();
    }

    private void setReferences() {
        radioButtonUser = findViewById(R.id.radioButtonUser);
        radioButtonEmployee = findViewById(R.id.radioButtonEmployee);

        // Set Log In RadioButton as checked by default
        radioButtonUser.setChecked(true);

    }
}
