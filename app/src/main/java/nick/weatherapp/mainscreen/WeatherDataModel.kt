package nick.weatherapp.mainscreen

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
import java.util.*

val TAG = "daywint"

class WeatherDataModel : WeatherDataMvpModel {
    private val urlString = "http://api.openweathermap.org/data/2.5/forecast?q=Moscow,ru&APPID=79790e8ed9ebca76ad012d5d4fd79045&units=metric"
    private var presenter: MainMvpPresenter? = null
    private val forecast: LinkedHashMap<String, OneDayWeather> = LinkedHashMap<String, OneDayWeather>()
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private var currentWeatherDTO: OneDayWeather? = null

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

    override fun setDataLoadingListener(listener: MainMvpPresenter) {
        presenter = listener
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
        val jsonArray = json.getJSONArray("list")
        val endForecastDay = getEndForecastDay()
        for (i in 0..(jsonArray.length() - 1)) {
            val item = jsonArray.getJSONObject(i)

            val dateRaw = shiftedNight(item.getString("dt_txt"))
            val hours = dateRaw.split(" ")[1].split(":")[0]
            val formattedShortDate = getFormattedDate(dateRaw)

            if (hours == "00" || hours == "12") {

                if (dateFormat.parse(dateRaw).after(endForecastDay))
                    break;

                val temperature = item.getJSONObject("main").getString("temp")
                val timeOfDay = if (hours == "00") "Night" else "Day"
                val description = item.getJSONArray("weather").getJSONObject(0).getString("main")

                currentWeatherDTO = getOneDayWeather(formattedShortDate, dateRaw)
                setWeatherData(timeOfDay, description, temperature)
                forecast.put(formattedShortDate, currentWeatherDTO!!)
            }
        }
        presenter?.onWeatherDataLoaded(forecast.values.toTypedArray())
    }

    private fun getEndForecastDay(): Date {
        val endForecastDay = Calendar.getInstance()
        endForecastDay.time = Date()
        endForecastDay.set(Calendar.HOUR, 23)
        endForecastDay.add(Calendar.DATE, 4)
        return endForecastDay.time
    }

    private fun shiftedNight(rawData: String): String {
        val hours = rawData.split(" ")[1].split(":")[0]
        if (hours == "00") {
            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(rawData);
            calendar.add(Calendar.DATE, -1)
            return dateFormat.format(calendar.time)
        }
        return rawData
    }

    private fun setWeatherData(timeOfDay: String, description: String?, temperature: String?) {
        if (timeOfDay == "Night") {
            currentWeatherDTO?.nightDescription = description;
            currentWeatherDTO?.nightWeather = temperature
        } else {
            currentWeatherDTO?.dayDescription = description
            currentWeatherDTO?.dayWeather = temperature
        }
    }

    private fun getOneDayWeather(formattedShortDate: String, dateRaw: String?): OneDayWeather? {
        if (forecast.containsKey(formattedShortDate))
            return forecast.get(formattedShortDate)

        return OneDayWeather(date = dateFormat.parse(dateRaw))
    }

    private fun getFormattedDate(dateRaw: String): String {
        val day = dateRaw.split(" ")[0].split("-")[2]
        val month = dateRaw.split(" ")[0].split("-")[1]
        val date = "$day-$month"
        return date
    }
}