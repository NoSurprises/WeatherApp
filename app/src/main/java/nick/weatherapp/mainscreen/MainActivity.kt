package nick.weatherapp.mainscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nick.weatherapp.R

class MainActivity : AppCompatActivity(), MainMvpView {

    private val presenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onCreate(this)
    }
}
