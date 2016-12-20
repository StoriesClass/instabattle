package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import me.instabattle.app.R;

public class LoginActivity extends Activity {
    private EditText login;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (EditText) findViewById(R.id.loginInput);
        password = (EditText) findViewById(R.id.passwordInput);
    }

    public void onSigninClick(View view) {
        Intent menu = new Intent(this, MenuActivity.class);
        startActivity(menu);
    }

    public void onSignupClick(View view) {
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
    }
}
