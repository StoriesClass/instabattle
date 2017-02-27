package me.instabattle.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import me.instabattle.app.R;
import me.instabattle.app.managers.ServiceGenerator;
import me.instabattle.app.managers.UserManager;
import me.instabattle.app.models.Token;
import me.instabattle.app.models.User;
import me.instabattle.app.settings.State;
import me.instabattle.app.utils.Utils;
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

        final String username = login.getText().toString();
        final String pass= password.getText().toString();

        Log.d(TAG, "username: " + username + "\t password: " + pass);

        UserManager.getTokenAndDo(username, pass, new Callback<Token>() {
                    @Override
                    public void onResponse(Call<Token> call, Response<Token> response) {
                        if (response.isSuccessful()) {
                            UserManager.getAndDo(username, new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if (response.isSuccessful()) {
                                        State.currentUser = response.body();
                                    } else {
                                        Log.e(TAG, "Something strange");
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    //TODO
                                    Log.e(TAG, "Cannot GET user");
                                }
                            });
                            State.token = response.body().get();
                            Log.d(TAG, "Got token: " + State.token);
                            ServiceGenerator.initTokenServices();
                            startActivity(menu);
                        } else {
                            Utils.showToast(LoginActivity.this, "Wrong nickname/password combination");
                        }
                    }

                    @Override
                    public void onFailure(Call<Token> call, Throwable t) {
                        Utils.showToast(LoginActivity.this, "Failed to sign in, try again later.");
                        Log.e(TAG, "Cannot obtaining token");
                    }
                });
    }

    public void onSignupClick(View view) {
        Intent signup = new Intent(this, SignupActivity.class);
        startActivity(signup);
    }

    @Override
    public void onBackPressed() {
        // nothing
    }
}
