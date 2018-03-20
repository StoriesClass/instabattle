package me.instabattle.app.activities

import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

import me.instabattle.app.R
import me.instabattle.app.managers.ServiceGenerator
import me.instabattle.app.managers.UserManager
import me.instabattle.app.settings.GlobalState
import org.jetbrains.anko.debug
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class LoginActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onSignInClick(view: View) {
        val username = loginInput.text.toString()
        val pass = passwordInput.text.toString()

        debug("username: $username\t password: $pass")

        UserManager.getToken(username, pass)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap {
                    GlobalState.token = it.get()!!
                    debug("Got token: %s".format(GlobalState.token))
                    ServiceGenerator.initTokenServices()
                    UserManager.get(username)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    GlobalState.currentUser = it
                    startActivity<MenuActivity>()
                }, {
                    when (it) {
                        is HttpException -> {
                            val errorBody = it.response().errorBody()
                            if (errorBody == null) { // is it even possible?
                                error("Server returned unsuccessful response with empty error and code: %d".format(it.response().code()))
                            } else {
                                //val errorMessage = JSONObject(errorBody.string()).getString("error")
                                toast("Couldn't login")
                                errorBody.close()
                            }
                        }
                        is SocketTimeoutException -> {
                            toast("Request has timed out")
                        }
                        is IOException -> {
                            toast("Unknown network or conversation error")
                            error(it.getStackTraceString())
                        }
                    }
                })
    }

    fun onSignUpClick(view: View) = startActivity<SignUpActivity>()

    override fun onBackPressed() { }
}
