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
            val stream = BufferedReader(InputStreamReader(inputStream))
            var dataLine = stream.readLine()
            while (dataLine != null) {
                data.append(dataLine)
                dataLine = stream.readLine()
            }
            stream.close()
            Log.d("daywint", "refceived from the internet ${data.toString()}")
            return data.toString()
        }
    }

    private fun deliverWeatherData(weather: String) {
        val json = JSONObject(weather)

        val forecast = LinkedHashMap<String, Pair<String, String>>()

        val jsonArray = json.getJSONArray("list")
        for (i in 0..(jsonArray.length() - 1)) {
            val item = jsonArray.getJSONObject(i)
            val date = item.getString("dt_txt")

            val day = date.split(" ")[0].split("-")[2]
            val month = date.split(" ")[0].split("-")[1]
            val hours = date.split(" ")[1].split(":")[0]

            if (forecast.contains(day)) continue;
            if (!forecast.contains(day) && hours.toInt() > 12) {
                val temp = item.getJSONObject("main").getString("temp")
                val description = item.getJSONArray("weather").getJSONObject(0).getString("main")
                forecast.put(day, temp to description)
            }
            if (hours != "12") continue;
        }


        weatherDataListener?.onWeatherDataLoaded(forecast)
    }

    override fun setDataLoadingListener(listener: MainMvpPresenter) {
        weatherDataListener = listener
    }
}