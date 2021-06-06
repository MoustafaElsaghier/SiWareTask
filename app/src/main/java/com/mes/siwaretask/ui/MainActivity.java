package com.mes.siwaretask.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mes.siwaretask.R;
import com.mes.siwaretask.adapters.BluetoothDevicesAdapter;
import com.mes.siwaretask.utils.BluetoothUtils;
import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;

import static com.mes.siwaretask.utils.BluetoothUtils.BLUETOOTH_OFF;
import static com.mes.siwaretask.utils.BluetoothUtils.BLUETOOTH_ON;
import static com.mes.siwaretask.utils.BluetoothUtils.DISABLE_BLUETOOTH;
import static com.mes.siwaretask.utils.BluetoothUtils.ENABLE_BLUETOOTH;

public class MainActivity extends AppCompatActivity {

    TextView bluetoothTextView;
    Button bluetoothOnOffButton, bluetoothDiscoverDevices;
    BluetoothAdapter mBluetoothAdapter;
    private RippleBackground rippleBg;
    RecyclerView bluetoothDevicesRecycler;
    BluetoothDevicesAdapter devicesAdapter;
    ArrayList<BluetoothDevice> bluetoothDevicesListArrayList;

    /**
     * BroadcastReceiver for getting & discover opened devices
     */
    private final BroadcastReceiver bluetoothDevicesBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    //discovery starts, we can show progress dialog or perform other tasks
                    rippleBg.startRippleAnimation();
                    rippleBg.setVisibility(View.VISIBLE);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    //discovery finishes, dismiss progress dialog
                    rippleBg.stopRippleAnimation();
                    rippleBg.setVisibility(View.GONE);
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    //bluetooth device found
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    updateBluetoothDevicesList(device);
                    break;
            }
        }
    };

    /**
     * append to list every new device
     */
    private void updateBluetoothDevicesList(BluetoothDevice device) {
        bluetoothDevicesListArrayList.add(device);
        devicesAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        rippleBg = findViewById(R.id.ripple_bg);
        bluetoothDiscoverDevices = findViewById(R.id.bluetoothDiscoverDevices);
        bluetoothDevicesRecycler = findViewById(R.id.bluetoothDevicesRecycler);
        bluetoothTextView = findViewById(R.id.bluetoothTextView);
        bluetoothOnOffButton = findViewById(R.id.bluetoothOnOffButton);

        bluetoothDevicesListArrayList = new ArrayList<>();
        devicesAdapter = new BluetoothDevicesAdapter(this, bluetoothDevicesListArrayList);

        bluetoothDevicesRecycler.setLayoutManager(new LinearLayoutManager(this));
        bluetoothDevicesRecycler.setAdapter(devicesAdapter);

        bluetoothOnOffButton.setOnClickListener(v -> switchCase());
        bluetoothDiscoverDevices.setOnClickListener(v -> devicesDiscoverAndList());

    }

    private void devicesDiscoverAndList() {
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(bluetoothDevicesBroadcastReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter.isEnabled()) {
            updateBluetoothText(BLUETOOTH_OFF, ENABLE_BLUETOOTH);
            bluetoothDiscoverDevices.setVisibility(View.VISIBLE);

            // get list of available devices if the bluetooth is on
            devicesDiscoverAndList();
        } else {
            updateBluetoothText(BLUETOOTH_ON, DISABLE_BLUETOOTH);
            bluetoothDiscoverDevices.setVisibility(View.GONE);
        }
    }

    private void switchCase() {
        if (BluetoothUtils.toggleBluetoothState()) {
            updateBluetoothText(BLUETOOTH_OFF, ENABLE_BLUETOOTH);
        } else {
            updateBluetoothText(BLUETOOTH_ON, DISABLE_BLUETOOTH);
        }
    }

    /**
     * update text on text view & button
     */
    private void updateBluetoothText(String textViewText, String buttonText) {
        bluetoothOnOffButton.setText(buttonText);
        bluetoothTextView.setText(textViewText);
    }

    /**
     * unregister receiver to avoid memory leak due to open receivers
     */
    @Override
    public void onDestroy() {
        unregisterReceiver(bluetoothDevicesBroadcastReceiver);
        super.onDestroy();
    }
}