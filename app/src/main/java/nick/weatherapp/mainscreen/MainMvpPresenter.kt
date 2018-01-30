package nick.weatherapp.mainscreen

interface MainMvpPresenter {
    fun onCreate(mvpView: MainMvpView)
    fun onWeatherDataLoaded(weather: HashMap<String, Pair<String, String>>)
}