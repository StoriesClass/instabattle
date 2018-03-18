package me.instabattle.app.models

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.Date

import kotlinx.android.parcel.Parcelize
import me.instabattle.app.managers.BattleManager
import me.instabattle.app.managers.BitmapCallback
import me.instabattle.app.managers.PhotoManager
import retrofit2.Callback

@Parcelize
data class Entry(
        @field:SerializedName("battle_id") @field:Expose
        val battleId: Int,
        @field:SerializedName("user_id") @field:Expose
        val authorId: Int?,
        @field:SerializedName("latitude") @field:Expose
        private val lat: Double,
        @field:SerializedName("longitude") @field:Expose
        private val lng: Double,
        @field:SerializedName("id") @field:Expose
        var id: Int? = null,
        @field:SerializedName("rating") @field:Expose
        val rating: Double? = 25.0,
        @field:SerializedName("created_on") @field:Expose
        val createdOn: Date? = null,
        @field:SerializedName("image") @field:Expose
        val imageName: String? = null,
        @field:SerializedName("description") @field:Expose
        val description: String? = null) : Parcelable {

    fun getPhotoAndDo(callback: BitmapCallback) {
        PhotoManager.getPhotoAndDo(this.imageName!!, callback)
    }

    fun getAuthorAndDo(callback: Callback<User>) {
        //UserManager.getAndDo(authorId, callback);
    }

    fun getRating(): Int {
        return (rating!! * RATING_MULTIPLIER).toInt()
    }

    fun getBattleAndDo(callback: Callback<Battle>) {
        BattleManager.getAndDo(battleId, callback)
    }

    companion object {
        private const val RATING_MULTIPLIER = 60
    }
}
