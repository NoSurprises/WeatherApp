package nick.weatherapp.mainscreen

import data.OneDayWeather
import java.text.SimpleDateFormat
import java.util.*

class MainPresenter : MainMvpPresenter {

    private lateinit var view: MainMvpView
    private val model: WeatherDataMvpModel = WeatherDataModel()


    override fun onCreate(mvpView: MainMvpView) {
        view = mvpView

        view.startLoadingAnimation()
        model.setDataLoadingListener(this)
        model.load5dayWeatherDataFromInternet()
    }

    override fun onFailLoadingWeatherData() {
        view.cancelLoadingAnimation()
        view.showMessage("Failed to load weather")
    }

    override fun onWeatherDataLoaded(weather: Array<OneDayWeather>) {
        for (i in 0..weather.size - 1) {
            val item = weather[i]
            view.setChildDayWeather(i, item.dayWeather ?: "777")
            view.setChildNightWeather(i, item.nightWeather ?: "777")
            view.setChildDayDescription(i, item.dayDescription ?: "777")
            view.setChildNightDescription(i, item.nightDescription ?: "777")
            view.setChildDate(i, getDateDescription(item.date))
        }

        view.cancelLoadingAnimation()
    }

    private fun getDateDescription(date: Date): String {
        return SimpleDateFormat("EEE, dd MM").format(date)
    }

    override fun onAllWeatherRefresh() {
        view.startLoadingAnimation()
        model.load5dayWeatherDataFromInternet()
    }
}