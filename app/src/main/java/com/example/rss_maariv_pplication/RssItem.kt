package com.example.rss_maariv_pplication
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Date

class RssItem(var link: String, var title: String, var author: String, var pubDate: LocalDate) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")




    override fun toString(): String {
        return "MyClass(link='$link', title='$title', author='$author', date=${dateFormat.format(Date())})"
    }
}


