//black fox руквод.контроль

package com.blackfox.app;

import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences sharedPreferences;
    private static final String CODE_KEY = "saved_code"; // Ключ для сохранения кода и вывода
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Инициализация любимого SharedPreferences
        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);

        loginButton = findViewById(R.id.loginButton);
        inputCode = findViewById(R.id.codeInputField);

        // Автоматический ввод сохраненного кода (если он есть)
        String savedCode = sharedPreferences.getString(CODE_KEY, "");
        if (!savedCode.isEmpty()) {
            inputCode.setText(savedCode); // Вставляем сохраненный код в поле ввода
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtedCode = inputCode.getText().toString();

                if (isCodeValid(inputtedCode)) {
                    // Сохраняем код в SharedPreferences
                    saveCode(inputtedCode);

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

    // Метод для сохранения кода в SharedPreferences
    private void saveCode(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CODE_KEY, code); // Сохраняем код по ключу
        editor.apply();
    }

    private boolean isCodeValid(String loginCode) {
        boolean isCodeValid = true;
        // Здесь можно добавить логику проверки кода (например, проверка длины или формата)
        return isCodeValid;
    }

    private boolean userIsAdmin(String loginCode) {
        boolean userIsAdmin = false;
        // Здесь можно добавить логику проверки, является ли код админским но Глеб это уже вроде сделал
        // Например, если код равен "1234", то это админ
        if (loginCode.equals("1234")) {
            userIsAdmin = true;
        }
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
