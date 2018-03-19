package me.instabattle.app.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.instabattle.app.GlideApp
import me.instabattle.app.R
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Entry
import me.instabattle.app.models.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntryListAdapter(private val ctx: Context, private val entries: List<Entry>)
    : RecyclerView.Adapter<EntryListAdapter.ViewHolder>(), AnkoLogger {

    class ViewHolder(val cv: CardView) : RecyclerView.ViewHolder(cv)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false) as CardView)


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        val l = holder.cv.findViewById<ConstraintLayout>(R.id.listEntryLayout)

        l.findViewById<TextView>(R.id.listEntryRating).text = "${entry.rating} points"

        PhotoManager.getPhotoInto(ctx, entry.imageName!!, l.findViewById<ImageView>(R.id.listEntryImage))

        entry.getAuthorAndDo(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                info("got author")
                l.findViewById<TextView>(R.id.listEntryAuthor).text = response.body()!!.username
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("can't get entries author: $t")
            }
        })
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        GlideApp.with(ctx).clear(holder.cv.findViewById<ImageView>(R.id.listEntryImage))
    }

    override fun getItemCount() = entries.size
}
