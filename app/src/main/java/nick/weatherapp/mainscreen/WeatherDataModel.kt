package nick.weatherapp.mainscreen

import android.util.Log
import data.OneDayWeather
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat

class WeatherDataModel : WeatherDataMvpModel {

    val urlString = "http://api.openweathermap.org/data/2.5/forecast?q=Moscow,ru&APPID=79790e8ed9ebca76ad012d5d4fd79045&units=metric"
    var presenter: MainMvpPresenter? = null

    override fun load5dayWeatherDataFromInternet() {
        val url = URL(urlString)
        Single.fromCallable { fetchHttp(url) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { data -> deliverWeatherData(data) },
                        { presenter?.onFailLoadingWeatherData() }
                )
    }

    private fun fetchHttp(url: URL): String {
        with(url.openConnection() as HttpURLConnection) {
            val data = StringBuffer()
            BufferedReader(InputStreamReader(inputStream)).use {
                var dataLine = it.readLine()
                while (dataLine != null) {
                    data.append(dataLine)
                    dataLine = it.readLine()
                }
            }
            return data.toString()
        }
    }

    private val linkedHashMap: LinkedHashMap<String, Pair<String, String>> = LinkedHashMap()


    private fun deliverWeatherData(weather: String) {
        val json = JSONObject(weather)
        val forecast = LinkedHashMap<String, OneDayWeather>()

        val jsonArray = json.getJSONArray("list")
        for (i in 0..(jsonArray.length() - 1)) {
            val item = jsonArray.getJSONObject(i)
            val dateRaw = item.getString("dt_txt")

            Log.d("daywint", dateRaw)
            val day = dateRaw.split(" ")[0].split("-")[2]
            val month = dateRaw.split(" ")[0].split("-")[1]
            val hours = dateRaw.split(" ")[1].split(":")[0]
            val date = "$day-$month"

            Log.d("daywint", "$hours   $date")

            // filter off non median values
            if (hours == "00" || hours == "12") {
                val temperature = item.getJSONObject("main").getString("temp")
                val timeOfDay = if (hours == "00") "Night" else "Day"
                val description = item.getJSONArray("weather").getJSONObject(0).getString("main")


                // get or create weather dto object
                var weatherDTO: OneDayWeather?

                if (!forecast.containsKey(date)) {
                    weatherDTO = OneDayWeather(date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateRaw))
                } else {
                    weatherDTO = forecast.get(date)
                }


                // set weather data
                if (timeOfDay == "Night") {
                    weatherDTO?.nightDescription = description;
                    weatherDTO?.nightWeather = temperature
                } else {
                    weatherDTO?.dayDescription = description
                    weatherDTO?.dayWeather = temperature
                }

                forecast.put(date, weatherDTO!!)
            }
        }

        presenter?.onWeatherDataLoaded(forecast.values.toTypedArray())
    }

    override fun setDataLoadingListener(listener: MainMvpPresenter) {
        presenter = listener
    }
}