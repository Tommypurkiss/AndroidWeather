package com.tommypurkissdev.weather

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*

class SettingsActivity : AppCompatActivity() {

    val TAG = "MainActivity"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

    fun onRadioButtonClick(view: View) {

        var checked = view as RadioButton
        if (rb_celsius == checked) {
            //message(rb_male.text.toString() + if (rb_male.isChecked) " Checked " else " UnChecked ")

        }
        if (rb_fahrenheit == checked) {
            //message(rb_female.text.toString() + if (rb_female.isChecked) " Checked " else " UnChecked ")
            celsiusToFahrenheit()
        }
    }


    fun tempUnits() {

        /* TODO
        pseudo

        if switch off = celsius

        else checked = fahrenheit

        if temp unit button? == c value {

        do the kelvin temp value - 273.15
        } if else? temp == f value {
        do the kelvin temp value * 9/(over or divided?)5 - 459.67

        } else {

        show kelvin
        }


         */
    }

    fun celsiusToFahrenheit() {

        /*
        get number(double) from tvTemperature.setText(tempFormat) or just temperature;


         */

        //tvTemperature.text

        //var tempValue: Double = tvTemperature.text.toString().toDouble()

        //Log.d(TAG, "celsiusToFahrenheit: " + tvTemperature)


        //var fahrenheit: BigDecimal = temp * (9/5) + 32

        //rb_celsius.text = fahrenheit.toString()
    }


}
