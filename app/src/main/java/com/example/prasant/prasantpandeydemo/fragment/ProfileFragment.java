package com.example.prasant.prasantpandeydemo.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.prasant.prasantpandeydemo.GithubApplication;
import com.example.prasant.prasantpandeydemo.R;
import com.example.prasant.prasantpandeydemo.adapter.RepoAdapter;
import com.example.prasant.prasantpandeydemo.model.Repo;
import com.example.prasant.prasantpandeydemo.task.GetRepoTask;
import com.example.prasant.prasantpandeydemo.task.GetUserTask;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import com.example.prasant.prasantpandeydemo.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private User user;
    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, new Gson().toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = new Gson().fromJson(getArguments().getString(ARG_PARAM1), User.class);
        }
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile");
    }

    public void loadUserProfile(final View view) {
        final RelativeLayout profileViewContainer = (RelativeLayout)view.findViewById(R.id.profile_view_container);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);

        if (loggedIn) {
            String authorizationKey = sharedPreferences.getString("authorizationKey", null);
            new GetUserTask(GithubApplication.getInstance().getApplicationContext(), authorizationKey , new GetUserTask.GetUserTaskListener() {

                @Override
                public void onSuccess(User user) {
                    progressBar.setVisibility(View.INVISIBLE);
                    profileViewContainer.setVisibility(View.VISIBLE);
                    updateProfileView(view, user);
                }

                @Override
                public void onFailure(Exception e) {
                    showDialog(e.getMessage());
                }
            }).execute();
        }
    }

    public void updateProfileView(View view, User user) {

        TextView txtUrl = view.findViewById(R.id.html_url_value);
        txtUrl.setText(user.getUrl() != null ? user.getHtmlUrl() : "");

        TextView txtName = view.findViewById(R.id.name_value);
        txtName.setText(user.getName() != null ? user.getName() : "");

        TextView txtLocation = view.findViewById(R.id.location);
        txtLocation.setText(user.getLocation() != null ? user.getLocation() : "");

        CircleImageView avatarImage = view.findViewById(R.id.avatar_iamge);
        ImageLoader.getInstance().displayImage(user.getAvatarUrl(), avatarImage);

        TextView txtLoginName = view.findViewById(R.id.profile_login_name);
        txtLoginName.setText(user.getLogin() != null ? user.getLogin() : "");

        TextView txtFollowers = view.findViewById(R.id.profile_followers);

        txtFollowers.setText(user.getFollowers() != null ? user.getFollowers().toString() : "0");

        TextView txtFollowing = view.findViewById(R.id.profile_following);
        txtFollowing.setText(user.getFollowing() != null ? user.getFollowing().toString() : "0");

        TextView public_repo = view.findViewById(R.id.public_repo_value);
        public_repo.setText(user.getPublicRepos() != null ? user.getPublicRepos().toString() : "0");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_view, container, false);
        loadUserProfile(view);
        return view;
    }

    public void setValue(User user) {

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
                                loadUserProfile(getView());
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
