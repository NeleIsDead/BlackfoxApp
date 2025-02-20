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

public class AddressListArrayAdapter extends RecyclerView.Adapter<AddressListArrayAdapter.ViewHolder> implements AddressChoiceInterface {
    Context ctx;
    @NonNull
    ArrayList<Place> addressList;
    private final AddressChoiceInterface addressChoiceInterface;

    public AddressListArrayAdapter(Context ctx, @NonNull ArrayList<Place> addressList, AddressChoiceInterface addressChoiceInterface) {
        this.ctx = ctx;
        this.addressList = addressList;
        this.addressChoiceInterface = addressChoiceInterface;
    }

    @NonNull
    @Override
    public AddressListArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.address_row, parent, false);
        return new AddressListArrayAdapter.ViewHolder(view, addressChoiceInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressListArrayAdapter.ViewHolder holder, int position) {
        holder.addressText.setText(addressList.get(position).getAddressString());
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    @Override
    public void onItemclick(int position) {
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressText;

        public ViewHolder(@NonNull View itemView, AddressChoiceInterface addressChoiceInterface) {
            super(itemView);
            addressText = itemView.findViewById(R.id.addressText);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (addressChoiceInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            addressChoiceInterface.onItemclick(pos);
                        }
                    }
                }
            });
        }
    }
}
