package com.blackfox.app;

import static com.blackfox.app.MainActivity.RETROFIT_URL;

import android.os.Bundle;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.POST;

public class WorkerListActivity extends AppCompatActivity {

    Button addUserButton;
    EditText userName, phoneNum;
    CheckBox isAdmin;
    RecyclerView userRecyclerView;


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
                .baseUrl(RETROFIT_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);

        String[] userNames = {"Иван Иванов", "Павел Петров", "Мария Кузнецова"};
        String[] phoneNums = {"+79002227724", "+79002227724", "+79002227724"};
        boolean[] areAdmins = {true, false, false};


        userName = findViewById(R.id.userName);
        phoneNum = findViewById(R.id.phoneNumber);
        isAdmin = findViewById(R.id.isAdmin);

        addUserButton = findViewById(R.id.addSlave);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(userName.getText().isEmpty()) && !(phoneNum.getText().isEmpty())){
                    String userName1 = userName.getText().toString();
                    String phoneNum1 = phoneNum.getText().toString();
                    //Запрос серверу на добавление юзера

                    userName.setText("");
                    phoneNum.setText("");
                }






            }
        });

    }


}
