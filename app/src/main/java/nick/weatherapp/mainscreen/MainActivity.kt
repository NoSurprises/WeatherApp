package nick.weatherapp.mainscreen

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import nick.weatherapp.R

class MainActivity : AppCompatActivity(), MainMvpView {

    private val presenter = MainPresenter()
    private val itemsContainer: LinearLayout by lazy { findViewById<LinearLayout>(R.id.items_container) }
    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.forecast_refresh) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.onCreate(this)
        refreshLayout.setOnRefreshListener { presenter.onAllWeatherRefresh() }

    }

    override fun cancelLoadingAnimation() {
        refreshLayout.isRefreshing = false
    }

    override fun startLoadingAnimation() {
        refreshLayout.isRefreshing = true
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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
