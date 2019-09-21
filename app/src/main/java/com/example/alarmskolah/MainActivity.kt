package com.example.alarmskolah

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var am: AlarmManager
    lateinit var tp: TimePicker
    lateinit var display_text: TextView
    lateinit var con: Context
    lateinit var btnSet: ImageButton
    lateinit var btnStop: ImageButton
    var hour: Int  = 0
    var min: Int = 0
    lateinit var pi: PendingIntent

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val spinner: Spinner = findViewById(R.id.spinner)

         ArrayAdapter.createFromResource(
             this,
             R.array.jadwal,
             android.R.layout.simple_spinner_item
         ).also { adapter ->
             adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
             spinner.adapter = adapter
         }

        this.con = this
        am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        tp = findViewById(R.id.tp) as TimePicker
        display_text = findViewById(R.id.display_text) as TextView
        btnSet = findViewById(R.id.btnSet) as ImageButton
        btnStop = findViewById(R.id.btnStop) as ImageButton

        val calender: Calendar = Calendar.getInstance()
        val myInstant: Intent = Intent(this, AlarmReceive::class.java)
        btnSet.setOnClickListener(object : View.OnClickListener{
            @SuppressLint("NewApi")
            override fun onClick(p0: View?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    calender.set(Calendar.HOUR_OF_DAY, tp.hour)
                    calender.set(Calendar.MINUTE, tp.minute)
                    calender.set(Calendar.SECOND, 0)
                    calender.set(Calendar.MILLISECOND, 0)
                    hour = tp.hour
                    min = tp.minute

                }else{
                    calender.set(Calendar.HOUR_OF_DAY, tp.currentHour)
                    calender.set(Calendar.MINUTE, tp.currentMinute)
                    calender.set(Calendar.SECOND, 0)
                    calender.set(Calendar.MILLISECOND, 0)
                    hour = tp.currentHour
                    min = tp.currentMinute
                }
                var hr_str: String = hour.toString()
                var min_str: String = min.toString()
                if (hour > 12){
                    hr_str = (hour - 12).toString()
                }
                if (min < 10){
                    min_str = "0$min"
                }
                set_alarm_text("Alarm set to: $hr_str : $min_str")
                myInstant.putExtra("extra","on")
                pi = PendingIntent.getBroadcast(this@MainActivity,0,myInstant, PendingIntent.FLAG_UPDATE_CURRENT)
                am.setExact(AlarmManager.RTC_WAKEUP, calender.timeInMillis, pi)
            }
        })

        btnStop.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                set_alarm_text("Alarm off")
                pi = PendingIntent.getBroadcast(this@MainActivity,0,myInstant,PendingIntent.FLAG_UPDATE_CURRENT)
                am.cancel(pi)
                myInstant.putExtra("extra","off")
                sendBroadcast(myInstant)
            }
        })
    }

    private fun set_alarm_text(s: String) {
        display_text.setText(s)
    }
}
