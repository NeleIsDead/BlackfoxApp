package com.blackfox.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMainActivity extends AppCompatActivity implements AddressChoiceInterface {

    public String chosenAddress;
    Button workerListButton;
    RecyclerView addressListRecycler;
    public final ArrayList<String> addressArrayOld = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));

    public final String LOG_TAG  = "AdminMainActivity";
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

        API api = RetrofitBuilder.api();

        workerListButton = findViewById(R.id.workerListButton);

        workerListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSlaveList();
            }
        });

        Log.d(LOG_TAG, "Initializing recyclerview");

        addressListRecycler = findViewById(R.id.address_list_recycler);
        Call<ArrayList<Place>> call = api.getPlaces();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ArrayList<Place>> call, Response<ArrayList<Place>> response) {

                Log.d(LOG_TAG, "Recieved Places Arraylist from server:");
                Log.d(LOG_TAG, response.body().toString());
                addressArray = response.body();

                AddressListArrayAdapter adapter = new AddressListArrayAdapter(AdminMainActivity.this, addressArray, AdminMainActivity.this);
                addressListRecycler.setAdapter(adapter);
                addressListRecycler.setLayoutManager(new LinearLayoutManager(AdminMainActivity.this));
            }

            @Override
            public void onFailure(Call<ArrayList<Place>> call, Throwable t) {
                Log.d(LOG_TAG, "Failed to retrieve places array");
                Log.d(LOG_TAG, Objects.requireNonNull(t.getMessage()));
            }
        });

    }

    @Override
    public void onItemclick(int position) {
        Intent intent = new Intent(this, AdminPlaceViewActivity.class);
        chosenAddress = addressArray.get(position).getAddressString();
        Bundle bundle = new Bundle();
        bundle.putString("address", chosenAddress);
        intent.putExtras(bundle);
        invalidateMenu();
        startActivity(intent);
    }
    public void goToSlaveList(){
        invalidateMenu();
        Intent intent = new Intent(this, AdminViewWorkerListActivity.class);
        startActivity(intent);
    }

}