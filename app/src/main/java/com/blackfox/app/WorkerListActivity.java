package com.blackfox.app;

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

import java.util.ArrayList;
import java.util.Arrays;

public class WorkerListActivity extends AppCompatActivity {

    Button addUserButton;
    EditText userName, phoneNum;
    CheckBox isAdmin;
    RecyclerView userRecyclerView;

    ArrayList<String> userNames;
    ArrayList<String> userPhones;
    ArrayList<String> userCodes;
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

        userName = findViewById(R.id.userName);
        phoneNum = findViewById(R.id.phoneNumber);
        isAdmin = findViewById(R.id.isAdmin);

        addUserButton = findViewById(R.id.addSlave);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName1 = userName.getText().toString();
                String phoneNum1 = phoneNum.getText().toString();
                userName.setText("");
                phoneNum.setText("");
                //Запрос серверу на добавление юзера
                userNames.add(userName1);
                userCodes.add("a");
                userPhones.add(phoneNum1);
            }
        });

        //userRecyclerView = findViewById(R.id.userRecyclerView);
        //userNames = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));
        //userPhones = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));
        //userCodes = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));

        //UserListArrayAdapter adapter = new UserListArrayAdapter(this, userNames,userCodes,userPhones);
        //userRecyclerView.setAdapter(adapter);
        //userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


}
