package nick.weatherapp.mainscreen

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
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

    override fun setForecast(data: Map<String, Pair<String, String>>) {
        itemsContainer.removeAllViews()
        for (i in data) {
            addNewWeatherItem(i)
        }
    }

    private fun addNewWeatherItem(i: Map.Entry<String, Pair<String, String>>) {
        val weatherItem = layoutInflater.inflate(R.layout.weather_item, itemsContainer, false)
        setDataToView(weatherItem, i)

        itemsContainer.addView(weatherItem)
    }

    private fun setDataToView(weatherItem: View, i: Map.Entry<String, Pair<String, String>>) {
        weatherItem.findViewById<TextView>(R.id.weather_day_value).setText(i.value.first)
        weatherItem.findViewById<TextView>(R.id.day_description).setText(i.value.second)
        weatherItem.findViewById<TextView>(R.id.date).setText(i.key)
    }
}
