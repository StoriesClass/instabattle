package me.instabattle.app.adapters

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.instabattle.app.GlideApp
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

class UserEntryListAdapter(private val ctx: Context, private val entries: List<Entry>)
    : RecyclerView.Adapter<UserEntryListAdapter.ViewHolder>(), AnkoLogger {

    class ViewHolder(val cv: CardView) : RecyclerView.ViewHolder(cv)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.user_entry_list_item, parent, false) as CardView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        val l = holder.cv.findViewById<ConstraintLayout>(R.id.userEntryLayout)

        l.findViewById<TextView>(R.id.userEntryDate).text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(entry.createdOn)
        l.findViewById<TextView>(R.id.userEntryRating).text = entry.rating.toString() + " points"

        PhotoManager.getPhotoInto(ctx, entry.imageName!!, l.findViewById<ImageView>(R.id.userEntryImage))

        entry.getBattleAndDo(object : Callback<Battle> {
            override fun onResponse(call: Call<Battle>, response: Response<Battle>) {
                val battle = response.body()

                l.findViewById<TextView>(R.id.userEntryTitle).text = battle!!.name

                l.findViewById<View>(R.id.userEntryViewBtn).setOnClickListener {
                    State.chosenBattle = battle
                    BattleActivity.gotHereFrom = MyProfileActivity::class.java
                    ctx.startActivity<BattleActivity>()
                }

                l.findViewById<View>(R.id.userEntryMapBtn).setOnClickListener {
                    MapActivity.viewPoint = battle.location
                    ctx.startActivity<MapActivity>()
                }
            }

            override fun onFailure(call: Call<Battle>, t: Throwable) {
                error("can't get entries battle: $t")
            }
        })
    }


    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        GlideApp.with(ctx).clear(holder.cv.findViewById<ImageView>(R.id.userEntryImage))
    }

    override fun getItemCount() = entries.size
}
