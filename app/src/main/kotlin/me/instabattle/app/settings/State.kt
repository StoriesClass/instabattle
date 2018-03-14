package me.instabattle.app.settings

import com.chibatching.kotpref.KotprefModel
import me.instabattle.app.models.Battle
import me.instabattle.app.models.User

// FIXME
object State {
    var chosenBattle: Battle? = null
    lateinit var currentUser: User
}

object KState: KotprefModel() {
    var token by stringPref()
    var creatingBattle by booleanPref()
}

/*object UserInfo : KotprefModel() {
    var gameLevel by enumValuePref(GameLevel.NORMAL)
    var code by nullableStringPref()
    var age by intPref(default = 14)
    val prizes by stringSetPref {
        val set = TreeSet<String>()
        set.add("Beginner")
        return@stringSetPref set
    }
}

enum class GameLevel {
    EASY,
    NORMAL,
    HARD
}*/
