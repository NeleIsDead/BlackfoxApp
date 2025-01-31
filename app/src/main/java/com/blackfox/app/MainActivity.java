package com.blackfox.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    EditText inputCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.loginButton);
        inputCode = findViewById(R.id.codeInputField);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtedCode = inputCode.getText().toString();

                if (isCodeValid(inputtedCode)) {
                    if (userIsAdmin(inputtedCode)) {
                        Log.d("main_activity", "going to admin screen");
                        goToAdminScreen();

                    } else {
                        Log.d("main_activity", "going to worker screen");
                        goToWorkerScreen();
                    }

                } else {
                    Snackbar.make(v, "Код неправильный или уже занят", BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean isCodeValid(String loginCode) {
        boolean isCodeValid = true;

        return isCodeValid;
    }

    private boolean userIsAdmin(String loginCode) {
        boolean userIsAdmin = false;

        return userIsAdmin;
    }

    public void goToWorkerScreen() {
        invalidateMenu();
        Intent intent = new Intent(this, WorkerMainActivity.class);
        startActivity(intent);
    }

    public void goToAdminScreen() {
        invalidateMenu();
        Intent intent = new Intent(this, AdminMainActivity.class);
        startActivity(intent);
    }

}