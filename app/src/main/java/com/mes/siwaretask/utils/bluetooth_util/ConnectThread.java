package com.mes.siwaretask.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    Button transferData;

    public ConnectThread(BluetoothDevice device, Button transferData) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;
        this.transferData = transferData;
        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            UUID MY_UUID = new DeviceUuidFactory(transferData.getContext()).getDeviceUuid();
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            transferData.setVisibility(View.GONE);
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        BluetoothUtils.mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
//            if (mmSocket.isConnected())
//                transferData.setVisibility(View.VISIBLE);

        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                transferData.setVisibility(View.GONE);
                Log.e(TAG, "Could not close the client socket", closeException);
            }
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
//            manageMyConnectedSocket(mmSocket);
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            transferData.setVisibility(View.GONE);
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}