package com.mobileedu34.schoolhub.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.adapters.StudentAdapter;
import com.mobileedu34.schoolhub.callbacks.students.ActionContract;
import com.mobileedu34.schoolhub.callbacks.students.StudentsActionController;
import com.mobileedu34.schoolhub.helpers.UserAction;
import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;


public class StudentsFragment extends Fragment implements ActionContract.View {

    private StudentAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefresh;

    public StudentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_students, container, false);
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
                StudentsActionController.getInstance().getStudents();
            }
        });

        fabAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString(AddUserFragment.ARG_TITLE, "Add a student");
                args.putSerializable(AddUserFragment.ARG_USER_ACTION_TYPE, UserAction.ADD_STUDENT);
                NavHostFragment
                        .findNavController(requireParentFragment())
                        .navigate(R.id.action_nav_students_to_add_user_fragment, args);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StudentsActionController.getInstance().setListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        StudentsActionController.getInstance().getStudents();
    }

    @Override
    public void onCreateStudentSuccess() {

    }

    @Override
    public void onCreateStudentFailure(String message) {

    }

    @Override
    public void onUpdateStudentSuccess() {

    }

    @Override
    public void onUpdateStudentFailure(String message) {

    }

    @Override
    public void onDeleteStudentSuccess() {

    }

    @Override
    public void onDeleteStudentFailure(String message) {

    }

    @Override
    public void onGetStudentsSuccess(ArrayList<User> students) {
        adapter = new StudentAdapter(students, requireParentFragment());
        recyclerView.setAdapter(adapter);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetStudentsFailure(String message) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
        adapter = new StudentAdapter(new ArrayList<User>(), requireParentFragment());
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}