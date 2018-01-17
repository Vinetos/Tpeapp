package fr.vinetos.tpeapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fr.vinetos.tpeapp.bluetooth.BluetoothConnectionService


class MainActivity : AppCompatActivity() {

    private val bluetoothConnectionService : BluetoothConnectionService = BluetoothConnectionService(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
