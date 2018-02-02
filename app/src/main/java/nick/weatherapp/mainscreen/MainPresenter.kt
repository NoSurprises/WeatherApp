package nick.weatherapp.mainscreen

import data.OneDayWeather

class MainPresenter : MainMvpPresenter {

    private lateinit var view: MainMvpView
    private val model: WeatherDataMvpModel = WeatherDataModel()


    override fun onCreate(mvpView: MainMvpView) {
        view = mvpView

        model.setDataLoadingListener(this)
        model.loadWeatherDataFromInternet()
    }

    override fun onWeatherDataLoaded(weather: Array<OneDayWeather>) {
        for (i in 0..weather.size - 1) {
            val item = weather[i]
            view.setChildDayWeather(i, item.dayWeather ?: "777")
            view.setChildNightWeather(i, item.nightWeather ?: "777")
            view.setChildDayDescription(i, item.dayDescription ?: "777")
            view.setChildNightDescription(i, item.nightDescription ?: "777")
        }
    }
}