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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminMainActivity extends AppCompatActivity implements AddressChoiceInterface {

    public String chosenAddress;
    Button workerListButton;
    RecyclerView addressListRecycler;
    public final ArrayList<String> addressArrayOld = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));

    public ArrayList<Place> addressArray;

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

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_address))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);



        Call<Places> call = api.getPlaces();
        call.enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                addressArray = response.body().getPlaces();
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {

            }
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
        chosenAddress = addressArray.get(position).getAddressString();
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