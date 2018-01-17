package fr.vinetos.tpeapp.bluetooth

import android.bluetooth.BluetoothDevice
import android.content.Context
import fr.vinetos.tpeapp.bluetooth.request.*

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
class BluetoothConnectionService(val context: Context) {

    private var eventListener : IBluetoothEventListener = EmptyBluetoothEventListener()
    private val enableRequest = EnableRequest(context, eventListener)
    private val discoverRequest = DiscoverRequest(context, eventListener)
    private val pairRequest = PairRequest(context, eventListener)
    private val connectionRequest = ConnectionRequest(context, eventListener)
    private val audioConnectionRequest = AudioConnectionRequest(context, eventListener)

    fun setBluetoothEventListener(listener: IBluetoothEventListener) {
        eventListener = listener
    }

    fun enableBluetoothAdapter() {
        enableRequest.enableluetooth()
    }

    fun disableBluetoothAdapter() {
        enableRequest.disableBluetooth()
    }

    fun discoverDevices() {
        discoverRequest.discover()
    }

    fun pairDevice(device : BluetoothDevice) {
        pairRequest.pair(device)
    }

    fun connectDevice(device: BluetoothDevice) {
        connectionRequest.connect(device)
    }

    fun stopConnectDevice() {
        connectionRequest.stopConnect()
    }

    fun connectAudioDevice(device : BluetoothDevice) {
        audioConnectionRequest.connect(device)
    }

    fun cleanUp() {
        enableRequest.cleanup()
        discoverRequest.cleanup()
        pairRequest.cleanup()
    }
}