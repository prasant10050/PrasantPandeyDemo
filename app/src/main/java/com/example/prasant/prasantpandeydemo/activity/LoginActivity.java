package com.example.prasant.prasantpandeydemo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.prasant.prasantpandeydemo.R;
import com.google.gson.Gson;

import com.example.prasant.prasantpandeydemo.model.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.prasant.prasantpandeydemo.task.GetUserTask;


public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.txtUsername)
    EditText txtUsername;
    @BindView(R.id.txtPassword)
    EditText txtPassword;
    @BindView(R.id.progressBarHolder)
    FrameLayout progressBarHolder;
    @BindView(R.id.btnSignIn)
    Button signInButton;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSignIn)
    public void btnSignIn(View view) {

        showProgressBar();

        String username = txtUsername.getText().toString().trim();
        String userPassword = txtPassword.getText().toString().trim();

        String basic = username + ":" + userPassword;
        String encoded = "Basic " + android.util.Base64.encodeToString(basic.getBytes(), android.util.Base64.NO_WRAP);

        new GetUserTask(LoginActivity.this, encoded, new GetUserTask.GetUserTaskListener() {
            @Override
            public void onSuccess(User user) {
                hideProgressBar();
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                Gson gson = new Gson();
                intent.putExtra("User", gson.toJson(user));
                startActivity(intent);
            }

            @Override
            public void onFailure(Exception e) {
                hideProgressBar();
                showDialog(e.getMessage());
            }

        }).execute((Void) null);
    }

    public void showDialog(String message) {
        // Create an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the Alert Dialog Message
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showProgressBar() {
        signInButton.setEnabled(false);
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);

    }

    public void hideProgressBar() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
        signInButton.setEnabled(true);
    }
}
