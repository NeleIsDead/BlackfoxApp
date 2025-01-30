package com.blackfox.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkerMainActivity extends AppCompatActivity implements AddressChoiceInterface{


    TextView nextWorkDate,nextWorkTime,nextWorkPlace;
    public final ArrayList<String> addressArray = new ArrayList<String>(Arrays.asList(new String[]{"address A", "address B", "address C"}));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EdgeToEdge.enable(this);

        nextWorkDate = findViewById(R.id.nextWorkDate);
        nextWorkPlace = findViewById(R.id.nextWorkPlace);
        nextWorkTime = findViewById(R.id.nextWorkTime);

        parseNextWorkTime(nextWorkDate,nextWorkTime,nextWorkPlace);

        RecyclerView addressListRecycler = findViewById(R.id.address_list_recycler);
        Log.d("WorkerMainActivity", addressArray.toString());


        AddressListArrayAdapter adapter = new AddressListArrayAdapter(this, addressArray,this);
        addressListRecycler.setAdapter(adapter);
        addressListRecycler.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    public void onItemclick(int position) {
        Intent intent = new Intent(this, WorkerPlaceViewActivity.class);
        invalidateMenu();
        startActivity(intent);
    }
    private void parseNextWorkTime(TextView nextWorkDate,TextView nextWorkTime,TextView nextWorkPlace){
        nextWorkPlace.setText("BLACK FOX (Остановка Октябрьская революция)");
        nextWorkDate.setText("31 февраля");
        nextWorkTime.setText("16:00 - 00:00");

    }
}
