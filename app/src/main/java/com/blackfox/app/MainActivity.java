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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button loginButton;
    EditText inputCode;
    SharedPreferences sharedPreferences;
    private final String CODE_KEY = "saved_code";
    String LOG_TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        Retrofit retrofit = new Builder()
                .baseUrl(getString(R.string.server_address))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);

        loginButton = findViewById(R.id.loginButton);
        inputCode = findViewById(R.id.codeInputField);

        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);
        String savedCode = sharedPreferences.getString(CODE_KEY, "");
        if (!savedCode.isEmpty()) {
            inputCode.setText(savedCode);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputtedCode = inputCode.getText().toString();

                if (inputtedCode.equals("user")) {
                    goToWorkerScreen();
                }
                else goToAdminScreen();

//                ActivateRequest activateRequest = new ActivateRequest(inputtedCode);
//
//                Call<String> call = api.activate(activateRequest);
//                call.enqueue(
//                        new Callback<>() {
//                            @Override
//                            public void onResponse(Call<String> call, Response<String> response) {
//                                String answer = response.body();
//                                Log.d(LOG_TAG, response.body());
//                                Log.d(LOG_TAG, response.body().toString());
//                                if (answer.equals("user")) {
//                                    Log.d("main_activity", "going to worker screen");
//                                    saveCode(inputtedCode);
//                                    goToWorkerScreen();
//
//                                } else if (answer.equals("admin")) {
//                                    Log.d("main_activity", "going to admin screen");
//                                    saveCode(inputtedCode);
//                                    goToAdminScreen();
//                                } else {
//                                    Log.d("ERROR", "ERROR");
//                                    Snackbar.make(v, "Код неправильный или уже занят", BaseTransientBottomBar.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<String> call, Throwable t) {
//                                Log.d("server Error", call.toString());
//                                Snackbar.make(v, "Сервер не отвечает", BaseTransientBottomBar.LENGTH_SHORT).show();
//                            }
//                        }
//                );

            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveCode(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CODE_KEY, code); // Сохраняем код по ключу
        editor.apply();
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