package com.blackfox.app;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class WorkerListActivity extends AppCompatActivity {

    Button addUserButton;
    EditText userName, phoneNum;
    CheckBox isAdmin;
    RecyclerView userRecyclerView;

    String LOG_TAG = "WorkerListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_worker_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_address))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);


        //Здесь запросить с сервера списки людей
        String[] userNames = {"Иван Иванов", "Павел Петров", "Мария Кузнецова"};
        String[] userPhones = {"+79002227724", "+79002227724", "+79002227724"};
        String[] userCodes = {"97987897989", "97987897989", "97987897989"};
        boolean[] areAdmins = {true, false, false};

        userName = findViewById(R.id.userName);
        phoneNum = findViewById(R.id.phoneNumber);
        isAdmin = findViewById(R.id.isAdmin);
        userRecyclerView = findViewById(R.id.userListRecyclerView);

        WorkerListArrayAdapter adapter = new WorkerListArrayAdapter(this, userNames, userPhones, userCodes, areAdmins);
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addUserButton = findViewById(R.id.addSlave);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(userName.getText().length() == 0) && !(phoneNum.getText().length() == 0)){

                    Call<String> call = api.addUser(new User(userName.getText().toString(),
                                                            phoneNum.getText().toString(),
                                                            isAdmin.isActivated()
                    ));
                    call.enqueue(
                            new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d(LOG_TAG, "Added user :thumbs_up_emoji:");
                                    userName.setText("");
                                    phoneNum.setText("");
                                    isAdmin.setActivated(false);
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Log.d(LOG_TAG, "User not added, fuck you");
                                    Snackbar.make(v, "Произошла ошибка, пользователь не добавлен", BaseTransientBottomBar.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            }
        });

    }


}
