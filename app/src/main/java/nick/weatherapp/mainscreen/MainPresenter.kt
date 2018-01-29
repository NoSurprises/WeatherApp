package nick.weatherapp.mainscreen

class MainPresenter : MainMvpPresenter {

    private lateinit var view: MainMvpView


    override fun onCreate(mvpView: MainMvpView) {
        view = mvpView
    }


}