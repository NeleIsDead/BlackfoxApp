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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorkerMainActivity extends AppCompatActivity implements AddressChoiceInterface {

    public String chosenAddress;
    TextView nextWorkDate, nextWorkTime, nextWorkPlace;
    public final ArrayList<String> addressArrayOld = new ArrayList<String>(Arrays.asList(new String[]{"Ленинский район", "Проспект Гагарина", "Ул.Крупской 42", "Большая Краснофлотская улица", "Промышленный район", "Улица Рыленкова, 18", "Багратиона 16", "Улица Октябрьской Революции, 24", "Проспект Гагарина, 1/3", "Улица Ленина, 4", "Коммунистическая улица, 6", "Улица 25 Сентября, 35А"}));
    public final String LOG_TAG = "WorkerMainActivity";
    public ArrayList<Place> addressArray;
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

        parseNextWorkTime(nextWorkDate, nextWorkTime, nextWorkPlace);

        RecyclerView addressListRecycler = findViewById(R.id.address_list_recycler);

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
                Log.d(LOG_TAG, "Failed to retrieve places array");
            }
        });


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

    private void parseNextWorkTime(TextView nextWorkDate, TextView nextWorkTime, TextView nextWorkPlace) {
        //Сделать запрос к серверу чтоб получить дату и чет пошаманить с ней
        nextWorkPlace.setText("BLACK FOX (Остановка Октябрьская революция)");
        nextWorkDate.setText("31 февраля");
        nextWorkTime.setText("16:00 - 00:00");

    }

}
