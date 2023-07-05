package com.example.rss_maariv_pplication

import RssAdapter
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.URL
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
class MainActivity : AppCompatActivity() {
    var rssItemsArrayList = ArrayList<RssItem>()

    lateinit var progressBar: ProgressBar
    var progressBarValue: Int = 0

    lateinit var recyclerView: RecyclerView
    private lateinit var rssAdapter: RssAdapter
    lateinit var buttonGetRssItem: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGetRssItem = findViewById(R.id.button_get_rss_item)

        progressBar = findViewById(R.id.rss_progress_bar)
        progressBar.minWidth = 1
        progressBar.scaleY = 4f
        progressBar.maxWidth = 100

        recyclerView = findViewById(R.id.rss_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        simulateProgress()
        FetchRssTask().execute("https://www.maariv.co.il/Rss/RssFeedsKalkalaBaArez")

        buttonGetRssItem.setOnClickListener {
            simulateProgress()
            //Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()
            rssItemsArrayList.clear()
            FetchRssTask().execute("https://www.maariv.co.il/Rss/RssFeedsKalkalaBaArez")
            rssAdapter = RssAdapter(rssItemsArrayList)
            recyclerView.adapter = rssAdapter
        }
    }

    /*Advance the progress bar according to the rss feed advancment*/
    private fun simulateProgress() {
        CoroutineScope(IO).launch {
            val totalItems: Int = 20
            for (i in 0..totalItems) {
                delay(500) // Delay for 500 milliseconds

                // Calculate the progress value
                val progress = (i.toFloat() / totalItems.toFloat() * 100).toInt()

                // Update the progress bar on the UI thread
                runOnUiThread {
                    progressBar.progress = progress
                }
            }
        }
    }


    inner class FetchRssTask : AsyncTask<String, Void, List<RssItem>>() {

        /*create input stream object for parseRssFeed method*/
        /*Handle Internet connection errors                */
        override fun doInBackground(vararg urls: String): List<RssItem> {
                var inputStream: InputStream? = null
                try {
                    val url = URL(urls[0])
                    val connection = url.openConnection()
                    inputStream = connection.getInputStream()
                } catch (e: IOException) {
                    // Handle internet connection error
                    Toast.makeText(this@MainActivity, "INPUT OUTPUT ERROR.", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    // Handle other exceptions
                    Toast.makeText(this@MainActivity, "INTERNET CONNECTION ERROR.", Toast.LENGTH_SHORT)
                        .show()
                }

                inputStream?.let {
                    return parseRssFeed(it)
                }

                return emptyList()
            }




       /*Update recyclerView adapter      */
        override  fun onPostExecute(result: List<RssItem>) {
            rssAdapter = RssAdapter(result)
            recyclerView.adapter = rssAdapter
        }
        /*Parse all the maariv feeds by the input url
        and return a list of rssItems for the RecyclerView adapter to use
        Handale feed syntax errors*/
        private fun parseRssFeed(inputStream: InputStream): ArrayList<RssItem> {
            rssItemsArrayList = ArrayList<RssItem>()
            progressBarValue = 0
            var totalItems: Int = 20
            try {
                val factory = XmlPullParserFactory.newInstance()
                val parser = factory.newPullParser()
                parser.setInput(inputStream, null)

                var eventType = parser.eventType
                var currentItem: RssItem? = null

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    try {
                        when (eventType) {
                            XmlPullParser.START_TAG -> {
                                if (parser.name == "item") {
                                    currentItem = RssItem("", "", "", LocalDate.now())
                                } else if (parser.name == "link") /*&& currentItem != null)*/ {
                                    if (currentItem != null) {
                                        currentItem.link = parser.nextText()
                                    }
                                } else if (parser.name == "title" && currentItem != null) {
                                    currentItem.title = parser.nextText()
                                } else if (parser.name == "Author" && currentItem != null) {
                                    currentItem.author = parser.nextText()
                                } else if (parser.name == "pubDate" && currentItem != null) {
                                    val dateText = parser.nextText()
                                    val pattern = DateTimeFormatter.ofPattern(
                                        "EEE, dd MMM yyyy HH:mm:ss z",
                                        Locale.ENGLISH
                                    )

                                    try {
                                        val zonedDateTime = ZonedDateTime.parse(dateText, pattern)
                                        val localDateTime = LocalDateTime.from(zonedDateTime)
                                        currentItem.pubDate = localDateTime.toLocalDate()
                                    } catch (e: DateTimeParseException) {
                                        // Handle parsing error
                                        currentItem.pubDate = LocalDate.now()
                                    }
                                }
                            }

                            XmlPullParser.END_TAG -> {
                                if (parser.name == "item" && currentItem != null) {
                                    rssItemsArrayList.add(currentItem)
                                    progressBarValue =
                                        (rssItemsArrayList.size.toFloat() / totalItems.toFloat() * 100).toInt()

                                    runOnUiThread {
                                        progressBar.progress = progressBarValue
                                    }
                                    currentItem = null
                                }
                            }
                        }

                        eventType = parser.next()
                    } catch (e: IOException) {
                        Toast.makeText(this@MainActivity, "FEED Parsing ERROR.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "INTERNET CONNECTION ERROR", Toast.LENGTH_SHORT)
                    .show()
                e.printStackTrace()
            }
            return rssItemsArrayList
        }
    }
}




