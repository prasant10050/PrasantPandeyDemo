package com.example.prasant.prasantpandeydemo.service;

import java.util.List;

import com.example.prasant.prasantpandeydemo.model.Repo;
import com.example.prasant.prasantpandeydemo.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;


public interface GithubServices {
    @GET("/user")
    Call<User> getUser(@Header("Authorization") String authHeader);

    @GET("/users/{login_name}/repos")
    Call<List<Repo>> getRepos(@Path("login_name") String login_name);

}
