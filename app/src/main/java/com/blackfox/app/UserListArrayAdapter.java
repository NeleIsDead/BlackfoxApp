package com.blackfox.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserListArrayAdapter extends RecyclerView.Adapter<UserListArrayAdapter.ViewHolder> {
    Context ctx;
    @NonNull
    ArrayList<String> userNames;
    ArrayList<String> userPhones;
    ArrayList<String> userCodes;

    public UserListArrayAdapter(Context ctx, @NonNull ArrayList<String> userArrayList, ArrayList<String> userPhones, ArrayList<String> userCodes) {
        this.ctx = ctx;
        this.userNames = userArrayList;
        this.userPhones = userPhones;
        this.userCodes = userCodes;

    }

    @NonNull
    @Override
    public UserListArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.address_row, parent, false);
        return new UserListArrayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.phoneNumber.setText(userPhones.get(position));
        holder.userName.setText(userNames.get(position));
        holder.userCode.setText(userCodes.get(position));

    }



    public int getItemCount() {
        return userNames.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView phoneNumber, userName, userCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d("IFH", "IIIINEFIJWNIJFNIJNI");
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            userName = itemView.findViewById(R.id.userName);
            userCode = itemView.findViewById(R.id.userCode);


        }
    }
}

