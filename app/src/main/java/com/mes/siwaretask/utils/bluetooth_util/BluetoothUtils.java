package com.mes.siwaretask.utils;

import android.bluetooth.BluetoothAdapter;

public class BluetoothUtils {
    public static final String BLUETOOTH_OFF = "Bluetooth is OFF";
    public static final String BLUETOOTH_ON = "Bluetooth is ON";
    public static final String ENABLE_BLUETOOTH = "Enable Bluetooth";
    public static final String DISABLE_BLUETOOTH = "Disable Bluetooth";

    public static final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * toggle status of bluetooth
     */
    public static boolean toggleBluetoothState() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            return false;
        } else {
            mBluetoothAdapter.enable();
            return true;
        }
    }
}
