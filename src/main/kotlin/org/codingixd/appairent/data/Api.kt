package org.codingixd.appairent.data

import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

class Api(val key: String, var forecast_granularity: String = "daily", val history_granularity: String = "daily", val https: Boolean = true) {
    val version = "v2.0"
    val api_domain = "api.weatherbit.io"

    private fun _get_base_url(): String {
        val baseUrl: String = if (https) {
            "https://"
        } else {
            "http://"
        }
        return "$baseUrl$api_domain/$version/"
    }

    private fun _get_forecast_url(granularity: String): String {
        return _get_base_url() + "forecast/" + granularity + "?key=" + key
    }

    private fun _get_current_url(): String {
        return _get_base_url() + "current/" + "?key=" + key
    }

    private fun _get_history_url(granularity: String): String {
        return _get_base_url() + "history/" + granularity + "?key=" + key
    }

    private fun get_forecast_url(granularity: String, lat: String? = null, lon: String? = null, city: String? = null,
                                 city_id: String? = null, state: String? = null, country: String? = null,
                                 days: String? = null, units: String? = null): String {
        val base_url = _get_forecast_url(granularity)

        var arg_url_str: String = ""
        // Build root geo-lookup.
        if (lat != null && lon != null) {
            arg_url_str = "&lat=$lat&lon=$lon"
        } else if (city != null) {
            arg_url_str = "&city=$city"
        } else if (city_id != null) {
            arg_url_str = "&city_id=$city_id"
        }

        // Add on additional parameters.
        if (state != null)
            arg_url_str += "&state=$state"
        if (country != null)
            arg_url_str += "&country=$country"
        if (days != null)
            arg_url_str += "&days=$days"
        if (units != null)
            arg_url_str += "&units=$units"

        return base_url + (arg_url_str)
    }

    private fun get_current_url(lat: String? = null, lon: String? = null, city: String? = null,
                                city_id: String? = null, state: String? = null, country: String? = null,
                                units: String? = null): String {
        val base_url = _get_current_url()

        var arg_url_str: String = ""
        // Build root geo-lookup.
        if (lat != null && lon != null) {
            arg_url_str = "&lat=$lat&lon=$lon"
        } else if (city != null) {
            arg_url_str = "&city=$city"
        } else if (city_id != null) {
            arg_url_str = "&city_id=$city_id"
        }

        // Add on additional parameters.
        if (state != null)
            arg_url_str += "&state=$state"
        if (country != null)
            arg_url_str += "&country=$country"
        if (units != null)
            arg_url_str += "&units=$units"

        return base_url + (arg_url_str)
    }

    private fun get_history_url(granularity: String, lat: String? = null, lon: String? = null, city: String? = null,
                        city_id: String? = null, station: String? = null, state: String? = null, country: String? = null,
                        units: String? = null, start_date: String, end_date: String): String {
        val base_url = _get_history_url(granularity)

        var arg_url_str: String = ""
        // Build root geo-lookup.
        if (lat != null && lon != null) {
            arg_url_str = "&lat=$lat&lon=$lon"
        } else if (city != null) {
            arg_url_str = "&city=$city"
        } else if (city_id != null) {
            arg_url_str = "&city_id=$city_id"
        } else if (station != null) {
            arg_url_str = "&station=$station"
        }

        // Add on additional parameters.
        arg_url_str += "&start_date=$start_date"
        arg_url_str += "&end_date=$end_date"

        if (state != null)
            arg_url_str += "&state=$state"
        if (country != null)
            arg_url_str += "&country=$country"
        if (units != null)
            arg_url_str += "&units=$units"

        return base_url + (arg_url_str)
    }

    fun get_forecast(granularity: String, lat: String? = null, lon: String? = null, city: String? = null,
                     city_id: String? = null, state: String? = null, country: String? = null,
                     days: String? = null, units: String? = null): Weather {

        val url = get_forecast_url(granularity, lat, lon, city, city_id, state, country, days, units)

        val (request, response, result) = url.httpGet().responseString()

        when (result) {
            is Result.Failure -> { error(response) }
            is Result.Success -> {
                return Weather(request, response, result.value)
            }
        }
    }

    fun get_current(lat: String? = null, lon: String? = null, city: String? = null,
                    city_id: String? = null, state: String? = null, country: String? = null,
                    units: String? = null): Weather {

        val url = get_current_url(lat, lon, city, city_id, state, country, units)

        val (request, response, result) = url.httpGet().responseString()

        when (result) {
            is Result.Failure -> { error(response) }
            is Result.Success -> {
                return Weather(request, response, result.value)
            }
        }
    }


    /**
     *
     * Assumes all time is in UTC.
     * time format: '%YYYY-%mm-%dd
     *
     */
    fun get_history(granularity: String, lat: String? = null, lon: String? = null, city: String? = null,
                    city_id: String? = null, station: String? = null, state: String? = null, country: String? = null,
                    units: String? = null, start_date: String, end_date: String): Weather {

        val url = get_history_url(granularity, lat, lon, city, city_id, station, state, country, units, start_date, end_date)

        val (request, response, result) = url.httpGet().responseString()

        when (result) {
            is Result.Failure -> { error(response) }
            is Result.Success -> {
                return Weather(request, response, result.value)
            }
        }
    }
}

data class Weather(val request: Request, val response: Response, val result: String)

