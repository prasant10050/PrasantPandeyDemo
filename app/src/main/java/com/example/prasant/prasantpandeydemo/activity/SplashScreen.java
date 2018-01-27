package com.example.prasant.prasantpandeydemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.prasant.prasantpandeydemo.GithubApplication;
import com.example.prasant.prasantpandeydemo.R;
import com.google.gson.Gson;

import com.example.prasant.prasantpandeydemo.model.User;
import com.example.prasant.prasantpandeydemo.utils.NetworkUtil;
import com.example.prasant.prasantpandeydemo.task.GetUserTask;

public class SplashScreen extends AppCompatActivity {

    private static final  int SPLASH_TIME_OUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        kickOff();
    }

    public void kickOff() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!NetworkUtil.isOnline(GithubApplication.getInstance().getApplicationContext())) {
                    showDialog("No Internet Connection ");
                } else {
                    final SharedPreferences sharedPreferences = getSharedPreferences("Login", MODE_PRIVATE);
                    boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);

                    if (loggedIn) {
                        Log.e("SplashScreen", "LoggedIn" + loggedIn);
                        String authorizationKey = sharedPreferences.getString("authorizationKey", null);
                        GetUserTask mAuthTask = new GetUserTask(SplashScreen.this, authorizationKey, new GetUserTask.GetUserTaskListener() {
                            @Override
                            public void onSuccess(User user) {
                                Toast.makeText(SplashScreen.this, "Login Successful", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SplashScreen.this, HomeActivity.class);
                                Gson gson = new Gson();
                                intent.putExtra("User", gson.toJson(user));
                                startActivity(intent);
                                // close this activity
                                finish();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                showDialog(e.getMessage());
                            }

                        });
                        mAuthTask.execute((Void) null);
                    } else {
                        // This method will be executed once the timer is over
                        // Start your app main activity
                        startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                        // close this activity
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);

    }


    public void showDialog(String message) {
        // Create an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the Alert Dialog Message
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                kickOff();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
