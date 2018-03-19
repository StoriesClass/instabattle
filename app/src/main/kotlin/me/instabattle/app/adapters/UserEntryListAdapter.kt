package me.instabattle.app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import me.instabattle.app.R
import me.instabattle.app.activities.BattleActivity
import me.instabattle.app.activities.MapActivity
import me.instabattle.app.activities.MyProfileActivity
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Battle
import me.instabattle.app.models.Entry
import me.instabattle.app.settings.State
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class UserEntryListAdapter(private val ctx: Context, private val entries: List<Entry>) : BaseAdapter(), AnkoLogger {
    private val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

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
        val res = convertView ?: inflater.inflate(R.layout.user_entry_list_item, parent, false)

        val entry = entries[position]

        PhotoManager.getPhotoInto(ctx, entry.imageName!!, res.findViewById<ImageView>(R.id.listEntryImage))

        res.findViewById<TextView>(R.id.listEntryUpvotes).text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(entry.createdOn)
        res.findViewById<TextView>(R.id.userEntryListItemUpvotes).text = entry.rating.toString() + " points"

        entry.getBattleAndDo(object : Callback<Battle> {
            override fun onResponse(call: Call<Battle>, response: Response<Battle>) {
                val battle = response.body()

                res.findViewById<TextView>(R.id.userEntryListItemTitle).text = battle!!.name

                res.findViewById<View>(R.id.userEntryListItemViewBtn).setOnClickListener { v ->
                    State.chosenBattle = battle
                    BattleActivity.gotHereFrom = MyProfileActivity::class.java
                    ctx.startActivity<BattleActivity>()
                }

                res.findViewById<View>(R.id.userEntryListItemViewOnMapBtn).setOnClickListener { v ->
                    MapActivity.viewPoint = battle.location
                    MapActivity.viewZoom = MapActivity.DEFAULT_ZOOM
                    ctx.startActivity<MapActivity>()
                }
            }

            override fun onFailure(call: Call<Battle>, t: Throwable) {
                error("can't get entries battle: $t")
            }
        })
        return res
    }
}
