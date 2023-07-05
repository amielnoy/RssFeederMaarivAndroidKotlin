import com.example.rss_maariv_pplication.RssItem


import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R

class RssAdapter(private val rssItems: List<RssItem>) :
        RecyclerView.Adapter<RssAdapter.RssViewHolder>() {
        /* create references to the controls inside the recyceler view*/
        class RssViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                val textViewTitle: TextView = itemView.findViewById(R.id.tvDtls)
                val textViewLink: TextView = itemView.findViewById(R.id.tvKy)
                //val textViewAuthor: TextView = itemView.findViewById(R.id.text_view_author)
                //val textViewPubDate: TextView = itemView.findViewById(R.id.text_view_publication_date)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_rss, parent, false)
                return RssViewHolder(itemView)
        }
        /* populate the data from the rssItem to the controls on RecyclerView*/
        /*implement click event for the link text view control               */
        override fun onBindViewHolder(holder: RssViewHolder, position: Int) {
                val currentItem = rssItems[position]
                holder.textViewTitle.text = currentItem.toString()
                holder.textViewLink.text = "הקישור"+"\n"+currentItem.link

                holder.itemView.setOnClickListener {
                        val item = rssItems[position]
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(item.link)
                        holder.itemView.context.startActivity(intent)
                }
        }

        override fun getItemCount(): Int {
                return rssItems.size
        }
}
