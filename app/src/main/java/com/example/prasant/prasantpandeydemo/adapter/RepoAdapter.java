package com.example.prasant.prasantpandeydemo.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.prasant.prasantpandeydemo.fragment.RepoFragment;

import java.util.List;

import com.example.prasant.prasantpandeydemo.model.Repo;


public class RepoAdapter extends FragmentPagerAdapter {

    Context ctxt;
    List<Repo> repoList;

    public RepoAdapter(Context ctxt, FragmentManager mgr, List<Repo> repos) {
        super(mgr);
        this.ctxt=ctxt;
        this.repoList = repos;
    }

    @Override
    public Fragment getItem(int position) {
        return (RepoFragment.newInstance(repoList.get(position)));
    }

    @Override
    public int getCount() {
        return repoList.size();
    }
}
