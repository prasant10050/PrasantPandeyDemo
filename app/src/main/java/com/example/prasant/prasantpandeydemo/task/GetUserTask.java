package com.example.prasant.prasantpandeydemo.task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.prasant.prasantpandeydemo.exception.MaxRetryException;
import com.example.prasant.prasantpandeydemo.exception.NoConnectivityException;
import com.example.prasant.prasantpandeydemo.exception.UnAuthorizedException;

import java.io.IOException;

import com.example.prasant.prasantpandeydemo.common.GitHubApi;
import com.example.prasant.prasantpandeydemo.model.User;
import com.example.prasant.prasantpandeydemo.service.GithubServices;
import com.example.prasant.prasantpandeydemo.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class GetUserTask  extends AsyncTask<Void, Void, User>{
    String authorizationKey;
    Context context;
    GithubServices githubServices;
    Exception exception;
    GetUserTaskListener listener;

    public GetUserTask(Context context , String authorizationKey, GetUserTaskListener listener) {
        this.context = context;
        this.authorizationKey = authorizationKey;
        this.listener = listener;
    }

    @Override
    protected User doInBackground(Void... voids) {
        githubServices = RetrofitClient.getClient(GitHubApi.base_url).create(GithubServices.class);
        Call<User> userApiCall = githubServices.getUser(authorizationKey);
        User user = null;
        try {
            Response<User> response = userApiCall.execute();
            if (response.isSuccessful()) {
                user =  response.body();
                SharedPreferences sharedPreferences = context.getSharedPreferences("Login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("loggedIn", true);
                editor.putString("authorizationKey", authorizationKey);
                editor.commit();
            } else  {
                if(response.code() == 401) {
                    exception = new UnAuthorizedException("Bad credentials");
                } else if(response.code() == 403) {
                    exception = new MaxRetryException("Maximum number of login attempts exceeded. Please try again later.");
                }
            }
        } catch (IOException e) {
            if(e instanceof NoConnectivityException == false) {
                exception = new Exception("Unknown connection error");
            } else {
                exception = e;
            }
        }
        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        if (user != null) {
            listener.onSuccess(user);
        }
        else {
            listener.onFailure(exception);
            Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCancelled() {

    }

    public interface GetUserTaskListener {

        public void onSuccess(User user);

        public void onFailure(Exception e);

    }
}
