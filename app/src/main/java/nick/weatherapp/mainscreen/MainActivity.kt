package nick.weatherapp.mainscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import nick.weatherapp.R

class MainActivity : AppCompatActivity(), MainMvpView {

    private val presenter = MainPresenter()
    private val itemsContainer: LinearLayout by lazy { findViewById<LinearLayout>(R.id.items_container) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onCreate(this)
    }

    override fun setChildDayDescription(i: Int, description: String) {
        itemsContainer.getChildAt(i).findViewById<TextView>(R.id.day_description).setText(description)
    }

    override fun setChildDayWeather(i: Int, tmp: String) {
        itemsContainer.getChildAt(i).findViewById<TextView>(R.id.weather_day_value).setText(tmp)
    }

    override fun setChildNightDescription(i: Int, description: String) {
        itemsContainer.getChildAt(i).findViewById<TextView>(R.id.night_description).setText(description)
    }

    override fun setChildNightWeather(i: Int, tmp: String) {
        itemsContainer.getChildAt(i).findViewById<TextView>(R.id.night_value).setText(tmp)

    }
}
