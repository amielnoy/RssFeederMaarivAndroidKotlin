package com.example.rss_maariv_pplication;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RssAdapter(private val rssItems: List<RssItem>) :
        RecyclerView.Adapter<RssAdapter.RssViewHolder>() {

        class RssViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
                val textViewDescription: TextView = itemView.findViewById(R.id.text_view_link)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_rss, parent, false)
                return RssViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
                val currentItem = rssItems[position]
                holder.textViewTitle.text = currentItem.title
                holder.textViewDescription.text = currentItem.link
        }

        override fun getItemCount(): Int {
                return rssItems.size
        }
}