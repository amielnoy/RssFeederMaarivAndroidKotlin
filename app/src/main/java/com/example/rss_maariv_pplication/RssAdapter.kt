package com.example.rss_maariv_pplication;

import android.R.attr.data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import java.util.Date


class RssAdapter(private val rssItems: List<RssItem>) :
        RecyclerView.Adapter<RssAdapter.RssViewHolder>() {
        var rssFeedItems: ArrayList<RssItem> = ArrayList<RssItem>()

        private fun <T> List(): List<RssItem> {
                TODO("Not yet implemented")
        }

        class RssViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
                val textViewLink: TextView = itemView.findViewById(R.id.text_view_link)
                val textViewAuthor: TextView=itemView.findViewById(R.id.text_view_author)
                val textViewPubDate: TextView=itemView.findViewById(R.id.text_view_publication_date)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_rss, parent, false)
                return RssViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
                val currentItem = rssItems[position]
                holder.textViewTitle.text = currentItem.title
                holder.textViewLink.text = currentItem.link
                holder.textViewAuthor.text=currentItem.author
                holder.textViewPubDate.text=currentItem.pubDate.toString()
        }

        override fun getItemCount(): Int {
                return rssItems.size
        }

        fun update(FeedItemsList: ArrayList<RssItem>) {
                rssFeedItems.clear()
                rssFeedItems.addAll(FeedItemsList)
                notifyDataSetChanged()
        }
}