package nick.weatherapp.mainscreen

/**
 * Created by Nick on 1/30/2018.
 */

interface WeatherDataMvpModel {

    fun setDataLoadingListener(listener: MainMvpPresenter)
    fun load5dayWeatherDataFromInternet()
}