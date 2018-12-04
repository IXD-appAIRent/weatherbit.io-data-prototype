package org.codingixd.appairent.data

fun main(args: Array<String>) {
    val api = Api("")
    val weather = api.get_history("daily", city = "berlin", state = "berlin", country = "germany", start_date = "2018-12-03",
        end_date = "2018-12-04")

    println(weather.result)
}