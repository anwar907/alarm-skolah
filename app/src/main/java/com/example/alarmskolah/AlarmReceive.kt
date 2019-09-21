package com.example.alarmskolah

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceive : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var alaramResult: String = intent!!.getStringExtra("extra")

        var service_intent: Intent = Intent(context, RingtoneService::class.java)
        service_intent.putExtra("extra",alaramResult)
        context!!.startService(service_intent)
    }
}