package com.blackfox.app;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

public class AddressListArrayAdapter extends RecyclerView.Adapter<AddressListArrayAdapter.ViewHolder> {

    Context ctx;
    @NonNull
    ArrayList<String> addressList;

    public AddressListArrayAdapter(Context ctx, @NonNull ArrayList<String> serverArrayList, AddressChoiceInterface addressChoiceInterface) {
        this.ctx = ctx;
        this.addressList = serverArrayList;
    }


    @NonNull
    @Override
    public AddressListArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.address_row, parent, false);

        return new AddressListArrayAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListArrayAdapter.ViewHolder holder, int position) {
        Log.d("Recycler", "setting text");
        holder.addressText.setText(addressList.get(position));
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView addressText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            addressText = itemView.findViewById(R.id.addressText);

        }


    }
}
