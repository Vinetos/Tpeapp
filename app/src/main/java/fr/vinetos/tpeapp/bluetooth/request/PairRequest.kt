package fr.vinetos.tpeapp.bluetooth.request

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import fr.vinetos.tpeapp.bluetooth.IBluetoothEventListener

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

class PairRequest(private val context : Context, private val eventListener: IBluetoothEventListener) : IBluetoothRequest {

    private var isPairing = false
    private lateinit var currentBluetoothDevice : BluetoothDevice

    init {
        registerReceiver()
    }

    fun pair(device : BluetoothDevice) {
        if (isPairing)
            return

        isPairing = true
        currentBluetoothDevice = device
        val method = device.javaClass.getMethod("createBond", *(null as Array<Class<Any>>))
        method.invoke(device, *(null as Array<Any>))
        eventListener.onPairing()
    }

    private fun registerReceiver() {
        context.registerReceiver(pairReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
    }


    private val pairReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val state = intent?.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR)
            val prevState = intent?.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR)

            if (prevState == BluetoothDevice.BOND_BONDING && state == BluetoothDevice.BOND_BONDED) {
                isPairing = false
                eventListener.onPaired()
            }

        }
    }

    override fun cleanup() {
        context.unregisterReceiver(pairReceiver)
    }

}