package com.example.prasant.prasantpandeydemo.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.prasant.prasantpandeydemo.R;
import com.google.gson.Gson;

import com.example.prasant.prasantpandeydemo.model.Repo;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepoFragment extends Fragment {

    Repo repo;
    private static final String REPO = "repo";

    public static RepoFragment newInstance(Repo repo) {
        RepoFragment frag = new RepoFragment();
        Bundle args=new Bundle();

        args.putString(REPO, new Gson().toJson(repo));
        frag.setArguments(args);

        return(frag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            repo = new Gson().fromJson(getArguments().getString(REPO), Repo.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_repo, container, false);
        TextView repoName = result.findViewById(R.id.repo_name);
        repoName.setText(repo.getName());

        TextView htmlUrl = result.findViewById(R.id.html_url);
        htmlUrl.setText(repo.getHtmlUrl());

        TextView description = result.findViewById(R.id.repo_description);
        description.setText(repo.getDescription());

        Log.i("RepoFragment", repo.getName());

        return(result);
    }

}
