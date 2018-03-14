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

import java.text.SimpleDateFormat
import java.util.Locale

import me.instabattle.app.managers.BitmapCallback
import me.instabattle.app.models.Battle
import me.instabattle.app.R
import me.instabattle.app.models.User
import me.instabattle.app.settings.State
import me.instabattle.app.activities.BattleActivity
import me.instabattle.app.activities.BattleListActivity
import me.instabattle.app.activities.MapActivity
import me.instabattle.app.models.Entry
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BattleListAdapter(private val ctx: Context, private val battles: List<Battle>) : BaseAdapter(), AnkoLogger {
    private val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return battles.size
    }

    override fun getItem(position: Int): Any {
        return battles[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val res = convertView ?: inflater.inflate(R.layout.battle_list_item, parent, false)

        val battleListItemImage = res.findViewById<ImageView>(R.id.battleListItemImage)
        val battle = battles[position]

        if (battleListItemImage.drawable == null) {
            battle.getWinnerAndDo(object : Callback<List<Entry>> {
                override fun onResponse(call: Call<List<Entry>>, response: Response<List<Entry>>) {
                    //FIXME pls
                    if (response.isSuccessful) {
                        val top = response.body()
                        val winner = top!![0]
                        winner.getPhotoAndDo(object : BitmapCallback {
                            override fun onResponse(photo: Bitmap) {
                                (ctx as Activity).runOnUiThread { battleListItemImage.setImageBitmap(photo) }
                            }

                            override fun onFailure(e: Exception) {
                                error("Can't get winner's photo")
                            }
                        })

                    } else {
                        error("winner entry is null")
                    }
                }

                override fun onFailure(call: Call<List<Entry>>, t: Throwable) {
                    error("can't get entry: $t")
                }
            })
        }

        res.findViewById<TextView>(R.id.battleListItemTitle).text = battle.name
        res.findViewById<TextView>(R.id.battleListItemDate).text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(battle.createdOn)
        res.findViewById<TextView>(R.id.battleListItemCount).text = battle.entriesCount.toString() + " photos"

        res.findViewById<View>(R.id.battleListItemViewBtn).setOnClickListener { v ->
            State.chosenBattle = battle
            BattleActivity.gotHereFrom = BattleListActivity::class.java
            ctx.startActivity<BattleActivity>()
        }
        res.findViewById<View>(R.id.battleListItemViewOnMapBtn).setOnClickListener { v ->
            MapActivity.viewPoint = battle.location
            MapActivity.viewZoom = MapActivity.DEFAULT_ZOOM
            MapActivity.gotHereFrom = BattleListActivity::class.java
            ctx.startActivity<MapActivity>()
        }

        battle.getAuthorAndDo(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                res.findViewById<TextView>(R.id.battleListItemAuthor).text = "by " + response.body()!!.username
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                error("can't get battle author")
            }
        })
        return res
    }
}
