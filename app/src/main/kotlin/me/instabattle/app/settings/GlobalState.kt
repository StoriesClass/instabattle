package me.instabattle.app.settings

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.gsonpref.gsonNullablePref
import com.chibatching.kotpref.gsonpref.gsonPref
import me.instabattle.app.models.Battle
import me.instabattle.app.models.User

object GlobalState : KotprefModel() {
    var chosenBattle by gsonNullablePref<Battle>()
    var currentUser by gsonPref<User>(default = User("EMPTY_USER"))
    var token by stringPref()
    var creatingBattle by booleanPref()
}
