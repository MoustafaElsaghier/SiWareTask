package com.mes.siwaretask.adapters;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mes.siwaretask.R;

import java.util.ArrayList;

public class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.MyAccountsViewHolder> {

    private final Context context;
    ArrayList<BluetoothDevice> bluetoothDevices;

    public BluetoothDevicesAdapter(Context context, ArrayList<BluetoothDevice> bluetoothDevices) {
        this.context = context;
        this.bluetoothDevices = bluetoothDevices;
    }

    @NonNull
    @Override
    public MyAccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAccountsViewHolder(LayoutInflater.from(context).inflate(R.layout.bluetooth_device_layout_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyAccountsViewHolder holder, final int position) {
        BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);
        holder.deviceName.setText(bluetoothDevice.getName());
        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return bluetoothDevices == null ? 0 : bluetoothDevices.size();
    }

    static class MyAccountsViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;

        public MyAccountsViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
        }
    }


}
