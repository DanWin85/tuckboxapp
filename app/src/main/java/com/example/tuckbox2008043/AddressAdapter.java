package com.example.tuckbox2008043;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tuckbox2008043.DataModel.DeliveryAddress;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<DeliveryAddress> addresses = new ArrayList<>();
    private static final String TAG = "AddressAdapter";
    private Context context;
    private AddressClickListener listener;
    private long selectedAddressId = -1;

    public interface AddressClickListener {
        void onDeleteClick(DeliveryAddress address);
        void onEditClick(DeliveryAddress address);
        void onAddressSelected(DeliveryAddress address);
    }

    public AddressAdapter(Context context, AddressClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        DeliveryAddress address = addresses.get(position);
        holder.tvAddress.setText(address.getAddress());

        // Set the selected state
        holder.itemView.setSelected(address.getAddressId() == selectedAddressId);

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(address);
            }
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(address);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                selectedAddressId = address.getAddressId();
                listener.onAddressSelected(address);
                notifyDataSetChanged(); // Refresh the list to update selection state
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public void setAddresses(List<DeliveryAddress> newAddresses) {
        Log.d(TAG, "Setting new addresses. Count: " + newAddresses.size());
        this.addresses = newAddresses;
        notifyDataSetChanged();
    }

    static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        ImageButton btnEdit;
        ImageButton btnDelete;

        AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}