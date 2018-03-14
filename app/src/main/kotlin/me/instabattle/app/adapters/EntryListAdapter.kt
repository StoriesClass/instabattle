package me.instabattle.app.adapters

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import me.instabattle.app.managers.BitmapCallback
import me.instabattle.app.models.Entry
import me.instabattle.app.R
import me.instabattle.app.models.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EntryListAdapter(private val context: Context, private val entries: List<Entry>) : BaseAdapter(), AnkoLogger {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return entries.size
    }

    override fun getItem(position: Int): Any {
        return entries[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val res = convertView ?: inflater.inflate(R.layout.entry_list_item, parent, false)

        val listEntryImage = res.findViewById<ImageView>(R.id.listEntryImage)
        val entry = entries[position]

        if (listEntryImage.drawable == null) {
            entry.getPhotoAndDo(object : BitmapCallback {
                override fun onResponse(photo: Bitmap) {
                    (context as Activity).runOnUiThread { listEntryImage.setImageBitmap(photo) }
                }

                override fun onFailure(e: Exception) {
                    error("Can't get entry photo")
                }
            })
        }

        res.findViewById<TextView>(R.id.listEntryUpvotes).text = entry.rating.toString() + " points"
        entry.getAuthorAndDo(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                info("got author")
                res.findViewById<TextView>(R.id.listEntryAuthor).text = response.body()!!.username
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("can't get entries author: $t")
            }
        })
        return res
    }
}
