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
    ArrayList<Slave> userList;

    public UserListArrayAdapter(Context ctx, @NonNull ArrayList<Slave> userArrayList) {
        this.ctx = ctx;
        this.userList = userArrayList;

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

        holder.phoneNumber.setText(userList.get(position).getPhoneNumber());
        String userNameT = userList.get(position).getPhoneNumber() + " " + userList.get(position).getLastName();
        holder.userName.setText(userNameT);
        holder.userCode.setText(userList.get(position).getCode());

    }



    public int getItemCount() {
        return userList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView phoneNumber, userName, userCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            userName = itemView.findViewById(R.id.userName);
            userCode = itemView.findViewById(R.id.userCode);


        }
    }
}

