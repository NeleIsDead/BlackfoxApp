package com.blackfox.app;

import android.os.Bundle;

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


    ArrayList<Slave> userArrayList;
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

        userRecyclerView = findViewById(R.id.userRecyclerView);
        userArrayList = new ArrayList<Slave>();

        UserListArrayAdapter adapter = new UserListArrayAdapter(this, getUserArrayList(userArrayList));
        userRecyclerView.setAdapter(adapter);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public ArrayList<Slave> getUserArrayList(ArrayList<Slave> userArrayList) {



        userArrayList.add(new Slave("HBFHB", "fiwehif", "fhweufhwehf", "weiufhiuhfiuh", false));

        return userArrayList;
    }
}
