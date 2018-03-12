package me.instabattle.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

import me.instabattle.app.R
import me.instabattle.app.managers.ServiceGenerator
import me.instabattle.app.managers.UserManager
import me.instabattle.app.models.Token
import me.instabattle.app.models.User
import me.instabattle.app.settings.State
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : Activity() {
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

        UserManager.createAndDo(loginText, emailText, passwordText, object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "User was created successfully")
                    State.currentUser = response.body() // FIXME
                    UserManager.getTokenAndDo(loginText, passwordText, object : Callback<Token> {
                        override fun onResponse(call: Call<Token>, response: Response<Token>) {
                            if (response.isSuccessful) {
                                State.token = response.body()!!.get()
                                Log.d(TAG, "Got token")
                                ServiceGenerator.initTokenServices()
                                startActivity<MenuActivity>()
                            } else {
                                Log.e(TAG, "Failed to obtain token")
                            }
                        }

                        override fun onFailure(call: Call<Token>, t: Throwable) {
                            // TODO
                            Log.e(TAG, "No response")
                        }
                    })
                } else {
                    toast("Username or email is taken.")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                toast("Failed to sign up, try again later.")
                Log.e(TAG, "Cannot POST user")
            }
        })
    }

    companion object {
        private val TAG = SignupActivity::class.java.simpleName
    }
}
