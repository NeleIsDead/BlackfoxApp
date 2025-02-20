package com.blackfox.app;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    Button loginByCodeButton;
    EditText inputCode;
    ImageButton imageDebugButton;
    private int debugClickCounter;
    SharedPreferences sharedPreferences;
    private final String CODE_KEY_STRING = "savedUserDataString";
    private final String CODE_KEY_BOOLEAN1 = "savedUserDataBoolean";
    private final String CODE_KEY_BOOLEAN2 = "savedUserDataBoolean2";
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
        imageDebugButton = findViewById(R.id.debugButton);

        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);
        String savedCode = sharedPreferences.getString(CODE_KEY_STRING, "");
        boolean isSavedUserAdmin = sharedPreferences.getBoolean(CODE_KEY_BOOLEAN1, false);
        boolean isConfirmedByServer = sharedPreferences.getBoolean(CODE_KEY_BOOLEAN2, false);

        if (isConfirmedByServer && !savedCode.isEmpty()) {
            if (isSavedUserAdmin){
                goToAdminScreen();
            } else {
                goToWorkerScreen();
            }
        }
        String temp = "Code:" + savedCode + " \nisAdmin: " + isSavedUserAdmin + "\nisConfirmedByServer: " + isConfirmedByServer;
        TextView debugTextView = findViewById(R.id.tempSavedCode);
        Log.d(LOG_TAG, temp);
        debugTextView.setText(temp);
        debugTextView.setVisibility(INVISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Code inputtedCode = new Code(inputCode.getText().toString());
                if (!inputtedCode.getCode().isEmpty()) {


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
                                     if (!user.getFio().isEmpty()) {
                                         /*Blocks entry if user is already authenticated on different device*/
                                         if (!user.getFio().equals("already") && !user.getFio().equals("not exist")) {

                                             if (user.isAdmin) {
                                                 saveCode(inputtedCode.getCode(), true, true);
                                                 goToAdminScreen();
                                             } else {
                                                 saveCode(inputtedCode.getCode(), false, true);
                                                 goToWorkerScreen();
                                             }
                                         } else {
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
            }
        });
        /*The logo is a button that if spammed shows debug info*/
        imageDebugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (debugClickCounter >= 42){
                    if (debugTextView.getVisibility() == VISIBLE){
                        debugTextView.setVisibility(INVISIBLE);
                    }else {
                        debugTextView.setVisibility(VISIBLE);
                    }

                    debugClickCounter = 0;
                }else {
                    debugClickCounter+=1;
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void saveCode(String code, boolean isAdmin, boolean serverConfirmed) {
        /* Saving user stuff to sharedPreferences */
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.putString(CODE_KEY_STRING, code);
        editor.putBoolean(CODE_KEY_BOOLEAN1, isAdmin);
        editor.putBoolean(CODE_KEY_BOOLEAN2, serverConfirmed);
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

    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA



}