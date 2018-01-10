package fr.vinetos.tpeapp

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Bluetooth
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter == null) {
            Toast.makeText(applicationContext, "Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show()
            return
        }

        // Start Bluetooth
        if(!bluetoothAdapter.isEnabled) {
            startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 0)
        }

        // Print and select bondedDevices
        val bondedDevices = bluetoothAdapter.bondedDevices
        if(bondedDevices.isEmpty()) {
            Toast.makeText(applicationContext, "Please pair the device first", Toast.LENGTH_SHORT).show()
            return
        }

        val device = bondedDevices.firstOrNull { device -> device.address == "cc" }

    }
}
