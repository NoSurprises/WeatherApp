package nick.weatherapp.mainscreen

interface MainMvpView {
    fun setChildDayWeather(i: Int, tmp: String)
    fun setChildNightWeather(i: Int, tmp: String)
    fun setChildDayDescription(i: Int, description: String)
    fun setChildNightDescription(i: Int, description: String)
    fun setChildDate(i: Int, date: String)
    fun cancelLoadingAnimation()
    fun startLoadingAnimation()
    fun showMessage(message: String)

}