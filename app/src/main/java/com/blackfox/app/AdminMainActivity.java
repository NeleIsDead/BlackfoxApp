package com.blackfox.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminMainActivity extends AppCompatActivity implements AddressChoiceInterface {

    public String chosenAddress;
    Button workerListButton;
    RecyclerView addressListRecycler;
    public final ArrayList<String> addressArray = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        setContentView(R.layout.activity_admin_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        workerListButton = findViewById(R.id.workerListButton);
        workerListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSlaveList();
            }
        });



        addressListRecycler = findViewById(R.id.address_list_recycler);

        AddressListArrayAdapter adapter = new AddressListArrayAdapter(this, addressArray, this);
        addressListRecycler.setAdapter(adapter);
        addressListRecycler.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    public void onItemclick(int position) {
        Intent intent = new Intent(this, WorkerPlaceViewActivity.class);
        chosenAddress = addressArray.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("address", chosenAddress);
        intent.putExtras(bundle);
        invalidateMenu();
        startActivity(intent);
    }
    public void goToSlaveList(){
        invalidateMenu();
        Intent intent = new Intent(this, WorkerListActivity.class);
        startActivity(intent);
    }

}