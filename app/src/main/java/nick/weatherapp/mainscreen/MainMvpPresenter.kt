package nick.weatherapp.mainscreen

import data.OneDayWeather

interface MainMvpPresenter {
    fun onCreate(mvpView: MainMvpView)
    fun onWeatherDataLoaded(weather: Array<OneDayWeather>)
    fun onAllWeatherRefresh()
    fun onFailLoadingWeatherData()
}