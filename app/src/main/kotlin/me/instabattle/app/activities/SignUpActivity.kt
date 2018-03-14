package me.instabattle.app.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

import me.instabattle.app.R
import me.instabattle.app.managers.ServiceGenerator
import me.instabattle.app.managers.UserManager
import me.instabattle.app.settings.KState
import me.instabattle.app.settings.State
import org.jetbrains.anko.getStackTraceString
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

class SignUpActivity : DefaultActivity() {
    private lateinit var login: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        login = findViewById(R.id.signUpLoginInput)
        email = findViewById(R.id.signUpEmailInput)
        password = findViewById(R.id.signUpPasswordInput)
    }

    fun onSignUpClick(view: View) {
        val loginText = login.text.toString()
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        UserManager.create(loginText, emailText, passwordText)
                .subscribeOn(Schedulers.io()) // “work” on io thread
                .observeOn(Schedulers.io())
                .flatMap { u ->
                        info("User was created successfully")
                        State.currentUser = u
                        UserManager.getToken(loginText, passwordText)
                }
                .observeOn(AndroidSchedulers.mainThread()) // “listen” on UIThread
                .subscribe({
                    KState.token = it.get()!!
                    info("Token was received successfully")
                    ServiceGenerator.initTokenServices()
                    startActivity<MenuActivity>()
                }, {
                    when (it) {
                        is HttpException -> {
                            val errorBody = it.response().errorBody()
                            if (errorBody == null) { // is it even possible?
                                error("Server returned unsuccessful response with empty error and code: %d".format(it.response().code()))
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
