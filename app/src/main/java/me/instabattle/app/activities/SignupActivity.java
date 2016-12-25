package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import me.instabattle.app.R;
import me.instabattle.app.managers.UserManager;
import me.instabattle.app.models.User;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";

    private EditText login;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        login = (EditText) findViewById(R.id.signUpLoginInput);
        email = (EditText) findViewById(R.id.signUpEmailInput);
        password = (EditText) findViewById(R.id.signUpPasswordInput);
    }

    public void onSignUpClick(View view) {
        final Intent menuActivity = new Intent(this, MenuActivity.class);

        final String _login = login.getText().toString();
        final String _email = email.getText().toString();
        final String _password = password.getText().toString();

        UserManager.createAndDo(_login, _email, _password, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "User created successfully");
                    State.currentUser = response.body();
                    UserManager.getTokenAndSet(_login, _password);
                    startActivity(menuActivity);
                } else {
                    Toast.makeText(SignupActivity.this, "Username or email is taken",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Cannot POST user");
            }
        });
    }
}
