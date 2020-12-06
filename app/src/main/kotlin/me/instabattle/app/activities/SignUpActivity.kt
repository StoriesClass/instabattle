package me.instabattle.app.activities

import android.os.Bundle
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_signup.*

import me.instabattle.app.R
import me.instabattle.app.managers.ServiceGenerator
import me.instabattle.app.managers.UserManager
import me.instabattle.app.settings.GlobalState
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SignUpActivity : DefaultActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }

    fun onSignUpClick(view: View) {
        val loginText = signUpLoginInput.text.toString()
        val emailText = signUpEmailInput.text.toString()
        val passwordText = signUpPasswordInput.text.toString()

        UserManager.create(loginText, emailText, passwordText)
                .subscribeOn(Schedulers.io()) // “work” on io thread
                .observeOn(Schedulers.io())
                .flatMap { u ->
                        info("User was created successfully")
                    GlobalState.currentUser = u
                        UserManager.getToken(loginText, passwordText)
                }
                .observeOn(AndroidSchedulers.mainThread()) // “listen” on UIThread
                .subscribe({
                    GlobalState.token = it.get()!!
                    info("Token was received successfully")
                    ServiceGenerator.initTokenServices()
                    startActivity<MenuActivity>()
                }, {
                    when (it) {
                        is HttpException -> {
                            val errorBody = it.response()?.errorBody()
                            if (errorBody == null) { // is it even possible?
                                error("Server returned unsuccessful response with empty error and code: %d".format(it.response()?.code()))
                            } else {
                                //val errorMessage = JSONObject(errorBody.string()).getString("error")
                                toast("Couldn't create user")
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
                }
            )
    }
}
