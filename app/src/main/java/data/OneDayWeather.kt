package data

import java.util.*

data class OneDayWeather(var dayWeather: String? = null,
                         var dayDescription: String? = null,
                         var nightWeather: String? = null,
                         var nightDescription: String? = null,
                         val date: Date);