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

public class WorkerListArrayAdapter extends RecyclerView.Adapter<WorkerListArrayAdapter.ViewHolder> implements WorkerCopyCodeInterface {
    Context ctx;
    ArrayList<CoolerUser> userArrayList;
    WorkerCopyCodeInterface workerCopyCodeInterface;


    public WorkerListArrayAdapter(Context ctx, ArrayList<CoolerUser> userArrayList, WorkerCopyCodeInterface workerCopyCodeInterface) {
        this.ctx = ctx;
        this.userArrayList = userArrayList;
        this.workerCopyCodeInterface = workerCopyCodeInterface;
    }

    @NonNull
    @Override
    public WorkerListArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.worker_list_row, parent, false);
        return new WorkerListArrayAdapter.ViewHolder(view, workerCopyCodeInterface);
    }
    @Override
    public void onBindViewHolder(@NonNull WorkerListArrayAdapter.ViewHolder holder, int position) {
        holder.phoneNumber.setText(userArrayList.get(position).getPhone());
        holder.userName.setText(userArrayList.get(position).getFIO());
        holder.userCode.setText(userArrayList.get(position).getCode());
    }
    public int getItemCount() {
        return userArrayList.size();
    }
    @Override
    public void onItemclick(int position) {

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView phoneNumber, userName, userCode;
        public ViewHolder(@NonNull View itemView, WorkerCopyCodeInterface workerCopyCodeInterface) {
            super(itemView);

            Log.d("WorkerListArrayAdapter", "finding item ids");

            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            userName = itemView.findViewById(R.id.userName);
            userCode = itemView.findViewById(R.id.userCode);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (workerCopyCodeInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            workerCopyCodeInterface.onItemclick(pos);
                        }
                    }
                }
            });
        }
    }
}

