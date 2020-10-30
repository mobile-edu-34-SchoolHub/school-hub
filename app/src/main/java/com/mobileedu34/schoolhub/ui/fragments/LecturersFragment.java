package com.mobileedu34.schoolhub.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.mobileedu34.schoolhub.AddUserFragment;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.adapters.LecturerAdapter;
import com.mobileedu34.schoolhub.callbacks.lecturers.ActionContract;
import com.mobileedu34.schoolhub.callbacks.lecturers.LecturersActionController;
import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;
import java.util.List;

public class LecturersFragment extends Fragment implements ActionContract.View {


    private List<User> users;
    private DatabaseReference mDatabase;
    public String userId;
    private LecturerAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefresh;


    public LecturersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_lecturers, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        recyclerView = root.findViewById(R.id.recycler_view);
        mProgressBar = root.findViewById(R.id.loading_progressbar);
        mSwipeRefresh = root.findViewById(R.id.swipe_to_refresh_layout);
        FloatingActionButton fabAddList = root.findViewById(R.id.fab_add_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);

        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                LecturersActionController.getInstance().getLecturers();
            }
        });

        fabAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(AddUserFragment.ARG_TITLE, "Add a lecturer");
                //args.putBoolean("browsing", false);
                //args.putSerializable("type", ListType.PRIVATE);
                NavHostFragment
                        .findNavController(requireParentFragment())
                        .navigate(R.id.action_nav_lecturers_to_add_user_fragment, args);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LecturersActionController.getInstance().setListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        LecturersActionController.getInstance().getLecturers();
    }


    @Override
    public void onCreateLecturerSuccess() {

    }

    @Override
    public void onCreateLecturerFailure(String message) {

    }

    @Override
    public void onUpdateLecturerSuccess() {

    }

    @Override
    public void onUpdateLecturerFailure(String message) {

    }

    @Override
    public void onDeleteLecturerSuccess() {

    }

    @Override
    public void onDeleteLecturerFailure(String message) {

    }

    @Override
    public void onGetLecturersSuccess(ArrayList<User> lecturers) {
        adapter = new LecturerAdapter(lecturers, requireParentFragment());
        recyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetLecturersFailure(String message) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
        adapter = new LecturerAdapter(new ArrayList<User>(), requireParentFragment());
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}