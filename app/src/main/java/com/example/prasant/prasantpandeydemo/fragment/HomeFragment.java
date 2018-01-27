package com.example.prasant.prasantpandeydemo.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.prasant.prasantpandeydemo.GithubApplication;
import com.example.prasant.prasantpandeydemo.R;
import com.example.prasant.prasantpandeydemo.adapter.RepoAdapter;

import java.util.List;

import com.example.prasant.prasantpandeydemo.model.Repo;
import com.example.prasant.prasantpandeydemo.task.GetRepoTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String userLoginName;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param userLoginName Parameter 1.

     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String userLoginName) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, userLoginName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userLoginName = getArguments().getString(ARG_PARAM1);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Repositories");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_home, container, false);
        fetchRepositories(result);
        return result;
    }

    public void fetchRepositories(View view) {
        final ViewPager pager=(ViewPager)view.findViewById(R.id.vpPager);

        final ProgressBar progressBar = view.findViewById(R.id.progressbar);

        new GetRepoTask(GithubApplication.getInstance().getApplicationContext(), userLoginName, new GetRepoTask.GetRepoTaskListener() {
            @Override
            public void onSuccess(List<Repo> repoList) {
                pager.setAdapter(new RepoAdapter(getActivity(), getChildFragmentManager(), repoList));
                progressBar.setVisibility(View.INVISIBLE);
                pager.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Exception e) {
                showDialog(e.getMessage());
            }
        }).execute();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void showDialog(String message) {
        // Create an Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the Alert Dialog Message
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Retry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                fetchRepositories(getView());
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
