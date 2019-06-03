package com.tommypurkissdev.weather


import com.tommypurkissdev.weather.MainActivity.*
import java.text.NumberFormat


class Settings {


    var TAG = Settings::class


    fun celsiusToFahrenheit() {


        //get number(double) from tvTemperature.setText(tempFormat) or just temperature;


        // VARS

        var tempValue: Double = tvTemperature.text.toString().toDouble()
        var tempMinValue: Double = tvTempMin.text.toString().toDouble()
        var tempMaxValue: Double = tvTempMax.text.toString().toDouble()

        var tempForecastOne: Double = tvForecastOneTemp.text.toString().toDouble()
        var tempForecastTwo: Double = tvForecastTwoTemp.text.toString().toDouble()
        var tempForecastThree: Double = tvForecastThreeTemp.text.toString().toDouble()
        var tempForecastFour: Double = tvForecastFourTemp.text.toString().toDouble()
        var tempForecastFive: Double = tvForecastFiveTemp.text.toString().toDouble()
        var tempForecastSix: Double = tvForecastSixTemp.text.toString().toDouble()
        var tempForecastSeven: Double = tvForecastSevenTemp.text.toString().toDouble()
        var tempForecastEight: Double = tvForecastEightTemp.text.toString().toDouble()


        //var dailyForecastTempMinValue: Double = tvTempMinF.text.toString().toDouble()


        //Log.d(TAG, "celsiusToFahrenheit: " + MainActivity.tvTemperature)

        // MAIN TEMP
        var fahrenheitTemp: Double = tempValue * 9 / 5 + 32
        val nf = NumberFormat.getNumberInstance()
        nf.maximumFractionDigits = 0
        val rounded = nf.format(fahrenheitTemp)
        tvTemperature.text = rounded.toString()


        // MAIN TEMP MIN
        var fahrenheitTempMin: Double = tempMinValue * 9 / 5 + 32
        val nf1 = NumberFormat.getNumberInstance()
        nf1.maximumFractionDigits = 0
        val rounded1 = nf1.format(fahrenheitTempMin)
        tvTempMin.text = rounded1.toString()

        // MAIN TEMP MAX
        var fahrenheitTempMax: Double = tempMaxValue * 9 / 5 + 32
        val nf2 = NumberFormat.getNumberInstance()
        nf2.maximumFractionDigits = 0
        val rounded2 = nf2.format(fahrenheitTempMax)
        tvTempMax.text = rounded2.toString()


        //TODO FIX
/*        // DAY FORECAST TEMP MIN
        var fahrenheitDailyTempMin: Double = dailyForecastTempMinValue * 9 / 5 + 32
        val nf3 = NumberFormat.getNumberInstance()
        nf3.setMaximumFractionDigits(0)
        val rounded3 = nf3.format(fahrenheitDailyTempMin)
        tvTempMinF.text = rounded3.toString()*/

        // DAY FORECAST ONE
        var fahrenheitTempOne: Double = tempForecastOne * 9 / 5 + 32
        val nf4 = NumberFormat.getNumberInstance()
        nf4.maximumFractionDigits = 0
        val rounded4 = nf4.format(fahrenheitTempOne)
        tvForecastOneTemp.text = rounded4.toString()

        // DAY FORECAST TWO
        var fahrenheitTempTwo: Double = tempForecastTwo * 9 / 5 + 32
        val nf5 = NumberFormat.getNumberInstance()
        nf5.maximumFractionDigits = 0
        val rounded5 = nf5.format(fahrenheitTempTwo)
        tvForecastTwoTemp.text = rounded5.toString()

        // DAY FORECAST THREE
        var fahrenheitTempThree: Double = tempForecastThree * 9 / 5 + 32
        val nf6 = NumberFormat.getNumberInstance()
        nf6.maximumFractionDigits = 0
        val rounded6 = nf6.format(fahrenheitTempThree)
        tvForecastThreeTemp.text = rounded6.toString()

        // DAY FORECAST FOUR
        var fahrenheitTempFour: Double = tempForecastFour * 9 / 5 + 32
        val nf7 = NumberFormat.getNumberInstance()
        nf7.maximumFractionDigits = 0
        val rounded7 = nf7.format(fahrenheitTempFour)
        tvForecastFourTemp.text = rounded7.toString()

        // DAY FORECAST FIVE
        var fahrenheitTempFive: Double = tempForecastFive * 9 / 5 + 32
        val nf8 = NumberFormat.getNumberInstance()
        nf8.maximumFractionDigits = 0
        val rounded8 = nf8.format(fahrenheitTempFive)
        tvForecastFiveTemp.text = rounded8.toString()

        // DAY FORECAST SIX
        var fahrenheitTempSix: Double = tempForecastSix * 9 / 5 + 32
        val nf9 = NumberFormat.getNumberInstance()
        nf9.maximumFractionDigits = 0
        val rounded9 = nf9.format(fahrenheitTempSix)
        tvForecastSixTemp.text = rounded9.toString()

        // DAY FORECAST SEVEN
        var fahrenheitTempSeven: Double = tempForecastSeven * 9 / 5 + 32
        val nf10 = NumberFormat.getNumberInstance()
        nf10.maximumFractionDigits = 0
        val rounded10 = nf10.format(fahrenheitTempSeven)
        tvForecastSevenTemp.text = rounded10.toString()

        // DAY FORECAST EIGHT
        var fahrenheitTempEight: Double = tempForecastEight * 9 / 5 + 32
        val nf11 = NumberFormat.getNumberInstance()
        nf11.maximumFractionDigits = 0
        val rounded11 = nf11.format(fahrenheitTempEight)
        tvForecastEightTemp.text = rounded11.toString()


        //Log.d(TAG, "celsiusToFahrenheit: " + fahrenheit)

    }


    fun fahrenheitToCelsius() {


        // VARS
        var tempValue: Double = tvTemperature.text.toString().toDouble()
        var tempMinValue: Double = tvTempMin.text.toString().toDouble()
        var tempMaxValue: Double = tvTempMax.text.toString().toDouble()

        var tempForecastOne: Double = tvForecastOneTemp.text.toString().toDouble()
        var tempForecastTwo: Double = tvForecastTwoTemp.text.toString().toDouble()
        var tempForecastThree: Double = tvForecastThreeTemp.text.toString().toDouble()
        var tempForecastFour: Double = tvForecastFourTemp.text.toString().toDouble()
        var tempForecastFive: Double = tvForecastFiveTemp.text.toString().toDouble()
        var tempForecastSix: Double = tvForecastSixTemp.text.toString().toDouble()
        var tempForecastSeven: Double = tvForecastSevenTemp.text.toString().toDouble()
        var tempForecastEight: Double = tvForecastEightTemp.text.toString().toDouble()


        //var dailyForecastTempMaxValue: Double = tvTempMaxF.text.toString().toDouble()


        // MAIN TEMP
        var celsiusTemp: Double = (tempValue - 32) * 5 / 9
        val nf = NumberFormat.getNumberInstance()
        nf.maximumFractionDigits = 0
        val rounded = nf.format(celsiusTemp)
        tvTemperature.text = rounded.toString()

        // MAIN TEMP MIN
        var celsiusTempMin: Double = (tempMinValue - 32) * 5 / 9
        val nf1 = NumberFormat.getNumberInstance()
        nf1.maximumFractionDigits = 0
        val rounded1 = nf1.format(celsiusTempMin)
        tvTempMin.text = rounded1.toString()

        // MAIN TEMP MAX
        var celsiusTempMax: Double = (tempMaxValue - 32) * 5 / 9
        val nf2 = NumberFormat.getNumberInstance()
        nf2.maximumFractionDigits = 0
        val rounded2 = nf2.format(celsiusTempMax)
        tvTempMax.text = rounded2.toString()


        //TODO FIX
/*        // DAY FORECAST TEMP MAX
        var celsiusDailyTempMax: Double = (dailyForecastTempMaxValue - 32) * 5/9
        val nf3 = NumberFormat.getNumberInstance()
        nf3.setMaximumFractionDigits(0)
        val rounded3 = nf3.format(celsiusDailyTempMax)
        tvTempMaxF.text = rounded3.toString()*/


        // DAY FORECAST ONE
        var celsiusTempOne: Double = (tempForecastOne - 32) * 5 / 9
        val nf4 = NumberFormat.getNumberInstance()
        nf4.maximumFractionDigits = 0
        val rounded4 = nf4.format(celsiusTempOne)
        tvForecastOneTemp.text = rounded4.toString()

        // DAY FORECAST TWO
        var celsiusTempTwo: Double = (tempForecastTwo - 32) * 5 / 9
        val nf5 = NumberFormat.getNumberInstance()
        nf5.maximumFractionDigits = 0
        val rounded5 = nf5.format(celsiusTempTwo)
        tvForecastTwoTemp.text = rounded5.toString()

        // DAY FORECAST THREE
        var celsiusTempThree: Double = (tempForecastThree - 32) * 5 / 9
        val nf6 = NumberFormat.getNumberInstance()
        nf6.maximumFractionDigits = 0
        val rounded6 = nf6.format(celsiusTempThree)
        tvForecastThreeTemp.text = rounded6.toString()

        // DAY FORECAST FOUR
        var celsiusTempFour: Double = (tempForecastFour - 32) * 5 / 9
        val nf7 = NumberFormat.getNumberInstance()
        nf7.maximumFractionDigits = 0
        val rounded7 = nf7.format(celsiusTempFour)
        tvForecastFourTemp.text = rounded7.toString()

        // DAY FORECAST FIVE
        var celsiusTempFive: Double = (tempForecastFive - 32) * 5 / 9
        val nf8 = NumberFormat.getNumberInstance()
        nf8.maximumFractionDigits = 0
        val rounded8 = nf8.format(celsiusTempFive)
        tvForecastFiveTemp.text = rounded8.toString()

        // DAY FORECAST SIX
        var celsiusTempSix: Double = (tempForecastSix - 32) * 5 / 9
        val nf9 = NumberFormat.getNumberInstance()
        nf9.maximumFractionDigits = 0
        val rounded9 = nf9.format(celsiusTempSix)
        tvForecastSixTemp.text = rounded9.toString()

        // DAY FORECAST SEVEN
        var celsiusTempSeven: Double = (tempForecastSeven - 32) * 5 / 9
        val nf10 = NumberFormat.getNumberInstance()
        nf10.maximumFractionDigits = 0
        val rounded10 = nf10.format(celsiusTempSeven)
        tvForecastSevenTemp.text = rounded10.toString()

        // DAY FORECAST EIGHT
        var celsiusTempEight: Double = (tempForecastEight - 32) * 5 / 9
        val nf11 = NumberFormat.getNumberInstance()
        nf11.maximumFractionDigits = 0
        val rounded11 = nf11.format(celsiusTempEight)
        tvForecastEightTemp.text = rounded11.toString()


    }
}