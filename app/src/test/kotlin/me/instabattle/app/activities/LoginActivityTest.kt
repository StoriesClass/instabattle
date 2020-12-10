package me.instabattle.app.activities

import android.os.Build

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

import me.instabattle.app.BuildConfig

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue

@Config(constants = BuildConfig::class, sdk = [(Build.VERSION_CODES.LOLLIPOP)])
@RunWith(RobolectricTestRunner::class)
class LoginActivityTest {
    private lateinit var activity: LoginActivity

    @Before
    fun setup() {
        // Convenience method to run MainActivity through the Activity Lifecycle methods:
        // onCreate(...) => onStart() => onPostCreate(...) => onResume()
        activity = Robolectric.setupActivity(LoginActivity::class.java)
    }

    @Test
    fun validateLoginInputHint() {
        val loginInput = activity.loginInput
        assertNotNull("Login input could not be found", loginInput)
        assertTrue("Login input contains incorrect hint",
                "Username" == loginInput.hint)
    }

    @Test
    fun validatePasswordInputHint() {
        val passwordInput = activity.passwordInput
        assertNotNull("Password input could not be found", passwordInput)
        assertTrue("Password input contatins incorrect hint",
                "Password" == passwordInput.hint)
    }
}
