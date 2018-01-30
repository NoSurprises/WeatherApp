package nick.weatherapp.mainscreen

import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class WeatherDataModel : WeatherDataMvpModel {

    val urlString = "http://api.openweathermap.org/data/2.5/forecast?q=Moscow,ru&APPID=79790e8ed9ebca76ad012d5d4fd79045&units=metric"
    var weatherDataListener: MainMvpPresenter? = null

    override fun loadWeatherDataFromInternet() {
        val url = URL(urlString)
        Single.fromCallable { fetchHttp(url) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { data -> deliverWeatherData(data) }
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

    private fun deliverWeatherData(weather: String) {
        val json = JSONObject(weather)

        val forecast = LinkedHashMap<String, Pair<String, String>>()

        val jsonArray = json.getJSONArray("list")
        for (i in 0..(jsonArray.length() - 1)) {
            val item = jsonArray.getJSONObject(i)
            val dateRaw = item.getString("dt_txt")

            val day = dateRaw.split(" ")[0].split("-")[2]
            val month = dateRaw.split(" ")[0].split("-")[1]
            val hours = dateRaw.split(" ")[1].split(":")[0]
            val date = "$day-$month-$hours"

            Log.d("daywint", "$hours   $date")
            if (hours == "00" || hours == "12") {
                val temperature = item.getJSONObject("main").getString("temp")
                val timeOfDay = if (hours == "00") "Night" else "Day"
                val description = "${item.getJSONArray("weather").getJSONObject(0).getString("main")}:$timeOfDay"

                forecast.put(date, temperature to description)
            }
        }


        weatherDataListener?.onWeatherDataLoaded(forecast)
    }

    override fun setDataLoadingListener(listener: MainMvpPresenter) {
        weatherDataListener = listener
    }
}