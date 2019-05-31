package com.tommypurkissdev.weather

//import com.tommypurkissdev.weather.MainActivity.tvTemperature
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.RadioButton
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }


    val TAG = "SettingsActivity"


/*

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }
*/

    override fun onBackPressed() {
        val intent = Intent()
        intent.action = Intent.ACTION_MAIN
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    fun onRadioButtonClick(view: View) {

        var checked = view as RadioButton
        if (rb_celsius == checked) {
            //message(rb_male.text.toString() + if (rb_male.isChecked) " Checked " else " UnChecked ")

        }
        if (rb_fahrenheit == checked) {
            //message(rb_female.text.toString() + if (rb_female.isChecked) " Checked " else " UnChecked ")
            //celsiusToFahrenheit()
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

/*    fun celsiusToFahrenheit() {

        *//*
        get number(double) from tvTemperature.setText(tempFormat) or just temperature;


         *//*

        tvTemperature.text

        var tempValue: Double = tvTemperature.text.toString().toDouble()

        Log.d(TAG, "celsiusToFahrenheit: " + tvTemperature)


        var fahrenheit: Double = tempValue * (9 / 5) + 32

        tvTemperature.text = fahrenheit.toString()

        Log.d(TAG, "celsiusToFahrenheit: " + fahrenheit)

    }*/
}
