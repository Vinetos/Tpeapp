package fr.vinetos.tpeapp.bluetooth.request

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import fr.vinetos.tpeapp.bluetooth.IBluetoothEventListener
import java.io.IOException
import java.util.*

/*
 * ==============================================================================
 *            _    _______   __________________  _____
 *            | |  / /  _/ | / / ____/_  __/ __ \/ ___/
 *            | | / // //  |/ / __/   / / / / / /\__ \
 *            | |/ // // /|  / /___  / / / /_/ /___/ /
 *            |___/___/_/ |_/_____/ /_/  \____//____/
 *
 * ==============================================================================
 *
 * TpeApp game
 * Copyright (c) Vinetos Software 2018,
 * By Vinetos, janvier 2018
 * 
 * ==============================================================================
 * 
 * This file is part of TpeApp.
 * 
 * TpeApp is free software: you can redistribute it and/or modify
 * it under the terms of the MIT License.
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in 
 * the Software without restriction, including without limitation the rights to 
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies 
 * of the Software, and to permit persons to whom the Software is furnished to do so, 
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all 
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR 
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS 
 * FOR A PARTICULAR PURPOSE AND NONINFINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER 
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *==============================================================================
 */

private const val BASE_UUID = "00000000-0000-1000-8000-00805F9B34FB"

class ConnectionRequest(private val context : Context, private val eventListener: IBluetoothEventListener) : IBluetoothRequest {
    private var connectionThread : ConnectionThread? = null

    fun connect(device: BluetoothDevice) {
        eventListener.onConnecting()
        connectionThread = ConnectionThread(device)
        { isSuccess -> eventListener.onConnected(isSuccess)}
        connectionThread?.start()
    }

    fun stopConnect() {
        if (connectionThread != null)
            connectionThread?.cancel()
    }

    override fun cleanup() {
        stopConnect()
    }


    private class ConnectionThread(private val device : BluetoothDevice,
                                   private val onComplete: (isSuccess : Boolean) -> Unit) : Thread() {

        private var bluetoothAdapter : BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        private var bluetoothSocket : BluetoothSocket? = createSocket();

        private fun createSocket() : BluetoothSocket? {
            var socket : BluetoothSocket? = null;

            try {
                val uuid = if (device.uuids.isNotEmpty())
                    device.uuids[0].uuid
                else UUID.fromString(BASE_UUID);

                socket = device.createRfcommSocketToServiceRecord(uuid)
            }
            catch (e : IOException) {}

            return socket;
        }

        override fun run() {
            super.run()

            bluetoothAdapter.cancelDiscovery()
            var isSuccess = false

            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket?.connect()
                    isSuccess = true
                }

            }
            catch (e: Exception) { }

            onComplete(isSuccess)
        }

        fun cancel() {
            if (bluetoothSocket != null)
                bluetoothSocket?.close()
        }
    }


}