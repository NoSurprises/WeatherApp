package nick.weatherapp.mainscreen

interface MainMvpView {
    fun setForecast(data: Map<String, Pair<String, String>>)
}