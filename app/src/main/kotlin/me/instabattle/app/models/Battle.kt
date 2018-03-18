package me.instabattle.app.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

import java.util.Date

import me.instabattle.app.managers.EntryManager
import me.instabattle.app.settings.State
import retrofit2.Callback

@Parcelize
data class Battle(
        @field:Expose @field:SerializedName("user_id")
        private val userId: Int,
        @field:Expose @field:SerializedName("name")
        val name: String,
        @field:Expose @field:SerializedName("description")
        val description: String,
        @field:Expose @field:SerializedName("latitude")
        private val latitude: Double?,
        @field:Expose @field:SerializedName("longitude")
        private val longitude: Double?,
        @field:Expose  @field:SerializedName("radius")
        val radius: Int?,
        @field:Expose @field:SerializedName("entry_count")
        var entriesCount: Int = 0,
        @field:Expose @field:SerializedName("created_on")
        var createdOn: Date? = null,
        @field:Expose @field:SerializedName("id")
        val id: Int? = null,
        @field:Expose @field:SerializedName("creator_id")
        private var creatorId: Int? = null ): Parcelable {

    val location: LatLng
        get() = LatLng(latitude!!, longitude!!)

    init {
        this.creatorId = userId

        this.creatorId = State.currentUser.id
        this.entriesCount = 1
        this.createdOn = Date()
    }//FIXME

    fun getRadius(): Int {
        return radius!!
    }

    fun getEntriesAndDo(callback: Callback<List<Entry>>) {
        EntryManager.getByBattleAndDo(id!!, callback)
    }

    fun getWinnerAndDo(callback: Callback<List<Entry>>) {
        EntryManager.getTopByBattleAndDo(id!!, 1, callback)
    }

    fun getVoteAndDo(callback: Callback<List<Entry>>) {
        EntryManager.getVoteAndDo(id, callback)
    }

    fun createEntryAndDo(callback: Callback<Entry>) {
        EntryManager.createAndDo(id, State.currentUser.id, callback)
    }

    fun getAuthorAndDo(callback: Callback<User>) {
        //UserManager.getAndDo(creatorId, callback);
    }
}