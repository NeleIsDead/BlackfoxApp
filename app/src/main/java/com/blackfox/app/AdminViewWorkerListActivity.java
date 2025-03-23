package com.blackfox.app;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminViewWorkerListActivity extends AppCompatActivity implements WorkerCopyCodeInterface{

    Button addUserButton;
    EditText userName, phoneNum;
    CheckBox isAdmin;
    RecyclerView userRecyclerView;

    ArrayList<CoolerUser> userArrayList;

    String LOG_TAG = "AdminViewWorkerListActivity";


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

        API api = RetrofitBuilder.api();

        userName = findViewById(R.id.userName);
        phoneNum = findViewById(R.id.phoneNumber);
        isAdmin = findViewById(R.id.isAdmin);
        userRecyclerView = findViewById(R.id.userListRecyclerView);

        Call<ArrayList<CoolerUser>> call = api.getAllUsers();
        call.enqueue(new Callback<ArrayList<CoolerUser>>() {
            @Override
            public void onResponse(Call<ArrayList<CoolerUser>> call, Response<ArrayList<CoolerUser>> response) {

                Log.d(LOG_TAG, "Recieved Users Arraylist from server:");
                Log.d(LOG_TAG, response.body().get(1).getCode());
                userArrayList = response.body();

                WorkerListArrayAdapter adapter = new WorkerListArrayAdapter(AdminViewWorkerListActivity.this, userArrayList, AdminViewWorkerListActivity.this);
                userRecyclerView.setAdapter(adapter);
                userRecyclerView.setLayoutManager(new LinearLayoutManager(AdminViewWorkerListActivity.this));
            }

            @Override
            public void onFailure(Call<ArrayList<CoolerUser>> call, Throwable t) {

            }
        });

        addUserButton = findViewById(R.id.addSlave);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(userName.getText().length() == 0) && !(phoneNum.getText().length() == 0)){
                    Call<String> call = api.addUser(new User(userName.getText().toString(),
                                                            phoneNum.getText().toString(),
                                                            isAdmin.isChecked()
                    ));
                    call.enqueue(
                            new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Log.d(LOG_TAG, "Added user :thumbs_up_emoji:");
                                    userName.setText("");
                                    phoneNum.setText("");
                                    isAdmin.setChecked(false);
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
    @Override
    public void onItemclick(int position) {
        Log.d(LOG_TAG, "Copied user Code");
        String userCode = userArrayList.get(position).getCode();
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(userCode, userCode);
        clipboard.setPrimaryClip(clip);
        Snackbar.make(userRecyclerView.getRootView(), "Код пользователя скопирован в буфер обмена", BaseTransientBottomBar.LENGTH_SHORT).show();
        Log.d(LOG_TAG, "Copied user Code " + userCode);
    }
}
