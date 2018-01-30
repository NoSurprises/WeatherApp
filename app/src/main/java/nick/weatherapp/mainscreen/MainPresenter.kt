package nick.weatherapp.mainscreen

class MainPresenter : MainMvpPresenter {

    private lateinit var view: MainMvpView
    private val model: WeatherDataMvpModel = WeatherDataModel()


    override fun onCreate(mvpView: MainMvpView) {
        view = mvpView

        model.setDataLoadingListener(this)
        model.loadWeatherDataFromInternet()
    }

    override fun onWeatherDataLoaded(weather: HashMap<String, Pair<String, String>>) {

        val forecast = StringBuffer()
        for (i in weather) {
            forecast.append("day is ${i.key} weather is ${i.value.first}, description is ${i.value.second}\n")
        }
        view.setRawData(forecast.toString())
    }
}