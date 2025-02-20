package com.blackfox.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    Button loginButton;
    EditText inputCode;
    TextView tempSavedCodeText;
    SharedPreferences sharedPreferences;
    private final String CODE_KEY_STRING = "savedUserDataString";
    private final String CODE_KEY_BOOLEAN = "savedUserDataBoolean";
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

        tempSavedCodeText = findViewById(R.id.tempSavedCode);

        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);

        String savedCode = sharedPreferences.getString(CODE_KEY_STRING, "");
        boolean isSavedUserAdmin = sharedPreferences.getBoolean(CODE_KEY_BOOLEAN, false);
        String temp = "Code:" + savedCode + " \n isAdmin: " + isSavedUserAdmin;
        tempSavedCodeText.setText(temp);

        if (!savedCode.equals("")) {
            if (isSavedUserAdmin){
                goToAdminScreen();
            } else {
                goToWorkerScreen();
            }
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Code inputtedCode = new Code(inputCode.getText().toString());

//                if (inputtedCode.equals("user")) {
//                    goToWorkerScreen();
//                }
//                else goToAdminScreen();


                /* Authenticating user with server */
                Call<User> call = api.activate(inputtedCode);
                call.enqueue(
                        new Callback<>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                User user = response.body();
                                Log.d(LOG_TAG, user.fio + " " + user.isAdmin());
                                if (!user.getFio().equals("")){
                                    /*Blocks entry if user is already authenticated on different device*/
                                    if (!user.getFio().equals("already")){

                                        if (user.isAdmin) {
                                            saveCode(inputtedCode.getCode(), true);
                                            goToAdminScreen();
                                        } else {
                                            saveCode(inputtedCode.getCode(), false);
                                            goToWorkerScreen();
                                        }
                                    }else {
                                        Snackbar.make(v, "Аккаунт пользователя уже используется кем-то другим", BaseTransientBottomBar.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Snackbar.make(v, "Произошла ошибка", BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Log.d("server Error", Objects.requireNonNull(t.getMessage()));
                                Snackbar.make(v, "Сервер не отвечает", BaseTransientBottomBar.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void saveCode(String code, boolean isAdmin) {
        /* Saving user stuff to sharedPreferences */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.putString(CODE_KEY_STRING, code);
        editor.putBoolean(CODE_KEY_BOOLEAN, isAdmin);
        editor.apply();
    }


    public void goToWorkerScreen() {
        Log.d("main_activity", "going to worker screen");
        invalidateMenu();
        Intent intent = new Intent(this, WorkerMainActivity.class);
        startActivity(intent);
    }

    public void goToAdminScreen() {
        Log.d("main_activity", "going to admin screen");
        invalidateMenu();
        Intent intent = new Intent(this, AdminMainActivity.class);
        startActivity(intent);
    }

}