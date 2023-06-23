package com.example.rss_maariv_pplication

import android.os.AsyncTask
import android.os.Bundle
import android.util.Xml
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.myapplication.R
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.InputStream
import java.net.URL
import org.jsoup.Jsoup
import org.xmlpull.v1.XmlPullParserFactory
import java.util.Date

class MainActivity : AppCompatActivity() {
    var rssItemsArrayList = ArrayList<RssItem>()

    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    private lateinit var rssAdapter: RssAdapter
    lateinit var buttonGetRssItem: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGetRssItem=findViewById(R.id.button_get_rss_item)

        progressBar=findViewById(R.id.rss_progress_bar)
        progressBar.minWidth=1
        progressBar.maxWidth=100

        recyclerView = findViewById(R.id.rss_recycler_view)
        recyclerView.layoutManager=LinearLayoutManager(this)
        simulateProgress()
        FetchRssTask().execute("https://www.maariv.co.il/Rss/RssFeedsKalkalaBaArez")
        //rssAdapter = RssAdapter(rssItemsArrayList)
        //recyclerView.adapter = rssAdapter
        //simulateProgress()
        buttonGetRssItem.setOnClickListener {
            simulateProgress()
            Toast.makeText(this@MainActivity, "You clicked me.", Toast.LENGTH_SHORT).show()

        }

    }

     private fun simulateProgress() {
        Thread {
            for (i in 0..100) {
                // Delay for demonstration purposes
                Thread.sleep(50)

                // Update the progress bar on the UI thread
                runOnUiThread {
                    progressBar.progress = i
                    progressBar.minWidth
                }
            }
        }.start()
    }

    inner class FetchRssTask : AsyncTask<String, Void, List<RssItem>>() {

        override fun doInBackground(vararg urls: String): ArrayList<RssItem> {
            val url = URL(urls[0])
            val connection = url.openConnection()
            val inputStream = connection.getInputStream()

            return parseRssFeed(inputStream)
        }

        override fun onPostExecute(result: List<RssItem>) {
            rssAdapter = RssAdapter(result)
            recyclerView.adapter = rssAdapter
            progressBar.visibility= View.VISIBLE
        }

        private fun parseRssFeed(inputStream: InputStream): ArrayList<RssItem> {
            val rssItems = ArrayList<RssItem>()

            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)

            var eventType = parser.eventType
            var currentItem: RssItem? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if (parser.name == "item") {
                            currentItem = RssItem("", "","", Date())
                        } else if (parser.name == "link") /*&& currentItem != null)*/ {
                            if (currentItem != null) {
                                currentItem.link = parser.nextText()
                            }
                        } else if (parser.name == "title" && currentItem != null) {
                            currentItem.title = parser.nextText()
                        } else if (parser.name == "Author" && currentItem != null) {
                            currentItem.author = parser.nextText()
                        }else if (parser.name == "pubDate" && currentItem != null) {
                           // currentItem.pubDate = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (parser.name == "item" && currentItem != null) {
                            rssItems.add(currentItem)
                            currentItem = null
                        }
                    }
                }

                eventType = parser.next()
            }

            return rssItems
        }
    }

}




