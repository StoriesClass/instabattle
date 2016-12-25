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
import me.instabattle.app.models.Token;
import me.instabattle.app.settings.State;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {
    private static String TAG = "LoginActivity";
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
        final Intent menu = new Intent(this, MenuActivity.class);

        String email = login.getText().toString();
        String pass= password.getText().toString();

        Log.d(TAG, "email: " + email + "\t password: " + pass);

        UserManager.getTokenAndDo(email, pass, new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()) {
                            State.token = response.body().get();
                            Log.d(TAG, "Got token: " + State.token);
                            startActivity(menu);
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong email/password combination", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Log.e(TAG, "Cannot obtaining token");
                    }
                });


    }

    public void onSignupClick(View view) {
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
    }
}
