package me.instabattle.app.adapters

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import me.instabattle.app.GlideApp
import me.instabattle.app.R
import me.instabattle.app.activities.BattleActivity
import me.instabattle.app.activities.BattleListActivity
import me.instabattle.app.activities.MapActivity
import me.instabattle.app.managers.PhotoManager
import me.instabattle.app.models.Battle
import me.instabattle.app.models.Entry
import me.instabattle.app.models.User
import me.instabattle.app.settings.GlobalState
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BattleListAdapter(private val ctx: Context, private val battles: List<Battle>)
    : RecyclerView.Adapter<BattleListAdapter.ViewHolder>(), AnkoLogger {
    // FIXME unpack cv?
    class ViewHolder(val cv: CardView) : RecyclerView.ViewHolder(cv)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.battle_list_item, parent, false) as CardView)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val battle = battles[position]
        val l = holder.cv.findViewById<ConstraintLayout>(R.id.battleListItemLayout)
        l.findViewById<TextView>(R.id.battleListItemTitle).text = battle.name
        //l.findViewById<TextView>(R.id.battleListItemDate).text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(battle.createdOn)
        //l.findViewById<TextView>(R.id.battleListItemCount).text = battle.entriesCount.toString() + " photos"

        val battleListItemImage = l.findViewById<ImageView>(R.id.battleListItemImage)
        battle.getWinnerAndDo(object : Callback<List<Entry>> {
            override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                //FIXME
                if (response.isSuccessful) {
                    val top = response.body()
                    val winner = top!![0]

                    PhotoManager.getPhotoInto(ctx, winner.imageName!!, battleListItemImage)
                } else {
                    error("winner entry is null")
                }
            }

            override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                error("can't get entry: $t")
            }
        })

        battle.getAuthorAndDo(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                l.findViewById<TextView>(R.id.battleListItemAuthor).text = "by " + response.body()!!.username
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("can't get battle author")
            }
        })

        l.findViewById<View>(R.id.battleListItemViewBtn).setOnClickListener {
            GlobalState.chosenBattle = battle
            BattleActivity.gotHereFrom = BattleListActivity::class.java
            ctx.startActivity<BattleActivity>("battle" to battle)
        }

        l.findViewById<View>(R.id.battleListItemMapBtn).setOnClickListener {
            MapActivity.viewPoint = battle.location
            ctx.startActivity<MapActivity>()
        }
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        GlideApp.with(ctx).clear(holder.cv.findViewById<ImageView>(R.id.battleListItemImage))
    }

    override fun getItemCount() = battles.size
}
