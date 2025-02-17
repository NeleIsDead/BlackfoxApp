package com.blackfox.app;

import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences sharedPreferences;
    private final String CODE_KEY_STRING = "savedUserDataString";
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


        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.server_address))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        API api = retrofit.create(API.class);

        sharedPreferences = getSharedPreferences("СodePreferences", MODE_PRIVATE);
        Call<Time> call = api.nearestShift(new Code(sharedPreferences.getString(CODE_KEY_STRING, "")));
        call.enqueue(new Callback<Time>() {
            @Override
            public void onResponse(Call<Time> call, Response<Time> response) {

                if (response.body() != null){
                    parseNextWorkTime(response.body());
                } else {
                    nextWorkTime.setText("Нет смен");
                    nextWorkDate.setText("Добби свободный эльф");
                    nextWorkPlace.setText("");
                }


            }

            @Override
            public void onFailure(Call<Time> call, Throwable t) {

            }
        });



        RecyclerView addressListRecycler = findViewById(R.id.address_list_recycler);

        Call<ArrayList<Place>> call1 = api.getPlaces();
        call1.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ArrayList<Place>> call, Response<ArrayList<Place>> response) {
                Log.d(LOG_TAG, "AAAAA");
                Log.d(LOG_TAG, response.body().toString());
                addressArray = response.body();

                AddressListArrayAdapter adapter = new AddressListArrayAdapter(WorkerMainActivity.this, addressArray, WorkerMainActivity.this);
                addressListRecycler.setAdapter(adapter);
                addressListRecycler.setLayoutManager(new LinearLayoutManager(WorkerMainActivity.this));
            }

            @Override
            public void onFailure(Call<ArrayList<Place>> call, Throwable t) {
                Log.d(LOG_TAG, "Failed to retrieve places array");
                Log.d(LOG_TAG, t.getMessage());
            }
        });



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

    private void parseNextWorkTime(Time time) {
        nextWorkPlace.setText(time.getAddress());
        String nextDate = new java.text.SimpleDateFormat("MM/dd/yyyy").format(time.getDate());
        String nextTime = new java.text.SimpleDateFormat("HH:mm:ss").format(time.getDate());
        nextWorkTime.setText(nextDate);
        nextWorkDate.setText(nextTime);
    }

}
