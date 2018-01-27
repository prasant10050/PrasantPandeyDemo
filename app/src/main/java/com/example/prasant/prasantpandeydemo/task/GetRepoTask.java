package com.example.prasant.prasantpandeydemo.task;

import android.content.Context;
import android.os.AsyncTask;

import com.example.prasant.prasantpandeydemo.exception.NoConnectivityException;

import java.io.IOException;
import java.util.List;

import com.example.prasant.prasantpandeydemo.common.GitHubApi;
import com.example.prasant.prasantpandeydemo.model.Repo;
import com.example.prasant.prasantpandeydemo.service.GithubServices;
import com.example.prasant.prasantpandeydemo.utils.RetrofitClient;
import retrofit2.Call;
import retrofit2.Response;


public class GetRepoTask  extends AsyncTask<Void, Void, List<Repo>> {

    String userName;
    Context context;
    GithubServices githubServices;
    private GetRepoTaskListener listener;
    Exception exception;

    public GetRepoTask(Context context, String userName, GetRepoTaskListener listener) {
        this.context = context;
        this.userName = userName;
        this.listener = listener;
    }

    @Override
    protected List<Repo> doInBackground(Void... voids) {

        githubServices = RetrofitClient.getClient(GitHubApi.base_url).create(GithubServices.class);
        Call<List<Repo>> userApiCall = githubServices.getRepos(userName);

        List<Repo> repos = null;

        try {
            Response<List<Repo>> response = userApiCall.execute();

            if (response.isSuccessful()) {
                repos =  response.body();
            }
        } catch (IOException e) {
            if(e instanceof NoConnectivityException == false) {
                exception = new Exception("Unknown connection error");
            } else {
                exception = e;
            }
        }

        return repos;
    }

    @Override
    protected void onPostExecute(List<Repo> repos) {
        if(listener != null) {
            if(repos != null) {
                listener.onSuccess(repos);
            } else {
                listener.onFailure(exception);
            }
        }
    }

    @Override
    protected void onCancelled() {

    }

    public interface GetRepoTaskListener {

        public void onSuccess(List<Repo> repoList);

        public void onFailure(Exception e);

    }
}
