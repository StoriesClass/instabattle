package me.instabattle.app;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
    private EditText _email;
    private EditText _password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        _email = (EditText) findViewById(R.id.email_input);
        _password = (EditText) findViewById(R.id.password_input);
    }

    private boolean login(String email, String password) {
        //TODO
        return true;
    }

    public void onSigninClick(View view) {
        String email = _email.toString();
        String password = _password.toString();

        if (!LoginTools.correctEmail(email)) {
            Toast.makeText(this, "Incorrect email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!LoginTools.correctPassword(password)) {
            Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (login(email, password)) {
            Intent menu = new Intent(this, MenuActivity.class);
            startActivity(menu);
        }
    }

    public void onSignupClick(View view) {
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
    }
}
