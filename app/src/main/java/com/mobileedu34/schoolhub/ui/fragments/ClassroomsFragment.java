package com.mobileedu34.schoolhub.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.adapters.ClassroomAdapter;
import com.mobileedu34.schoolhub.adapters.ClassroomAdapter;
import com.mobileedu34.schoolhub.callbacks.OnClassroomSelectionListener;
import com.mobileedu34.schoolhub.callbacks.classrooms.ActionContract;
import com.mobileedu34.schoolhub.callbacks.classrooms.ClassroomsActionController;
import com.mobileedu34.schoolhub.callbacks.students.StudentsActionController;
import com.mobileedu34.schoolhub.helpers.UserAction;
import com.mobileedu34.schoolhub.models.Classroom;
import com.mobileedu34.schoolhub.models.User;

import java.util.ArrayList;

import static com.mobileedu34.schoolhub.helpers.Constants.CLASSROOMS_COLLECTION;
import static com.mobileedu34.schoolhub.helpers.Constants.USERS_COLLECTION;

public class ClassroomsFragment extends Fragment implements ActionContract.View, OnClassroomSelectionListener {

    private ClassroomAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefresh;
    private User currentLecturer = new User();
    private AlertDialog dialog;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



    public ClassroomsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_classrooms, container, false);
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

        mSwipeRefresh.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                ClassroomsActionController.getInstance().getClassrooms();
            }
        });

        fabAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialogBuilder(null);
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ClassroomsActionController.getInstance().setListener(this);
        mProgressBar.setVisibility(View.VISIBLE);
        ClassroomsActionController.getInstance().getClassrooms();
    }

    @Override
    public void onCreateClassroomSuccess() {
        adapter.clearDataSet();
        ClassroomsActionController.getInstance().getClassrooms();
    }

    @Override
    public void onCreateClassroomFailure(String message) {

    }

    @Override
    public void onUpdateClassroomSuccess() {
        adapter.clearDataSet();
        ClassroomsActionController.getInstance().getClassrooms();
    }

    @Override
    public void onUpdateClassroomFailure(String message) {

    }

    @Override
    public void onDeleteClassroomSuccess() {

    }

    @Override
    public void onDeleteClassroomFailure(String message) {

    }

    @Override
    public void onGetClassroomsSuccess(ArrayList<Classroom> classrooms) {
        adapter = new ClassroomAdapter(classrooms, requireParentFragment());
        recyclerView.setAdapter(adapter);
        adapter.setOnUserSelectionListener(this);
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onGetClassroomsFailure(String message) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefresh.setRefreshing(false);
        adapter = new ClassroomAdapter(new ArrayList<Classroom>(), requireParentFragment());
    }

    @Override
    public void onUpdate(Classroom classroom) {
        createDialogBuilder(classroom);
    }

    private void createDialogBuilder(Classroom classroom) {
        ClassroomCreateEditBuilder employeeBuilder = new ClassroomCreateEditBuilder(requireContext(), R.style.DialogTheme, classroom);
        ViewGroup viewGroup = ((Activity) requireContext()).findViewById(android.R.id.content);
        employeeBuilder.display(viewGroup);
    }


    private class ClassroomCreateEditBuilder extends AlertDialog.Builder {

        private MaterialButton btnCancel;
        private MaterialButton btnSave;
        private MaterialButton btnPickLecturer;
        private TextInputEditText edtName;
        private boolean updating;
        private MaterialTextView dialogTitle;
        private Classroom classroom;
        private ArrayList<User> lecturers = new ArrayList<>();

        public ClassroomCreateEditBuilder(@NonNull Context context, int themeResId, Classroom classroom) {
            super(context, themeResId);
            this.classroom = classroom;
        }

        public void display(ViewGroup viewGroup) {
            View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.create_classroom_layout, viewGroup, false);

            btnCancel = (MaterialButton) dialogView.findViewById(R.id.cancel);
            btnSave = (MaterialButton) dialogView.findViewById(R.id.save);
            dialogTitle = (MaterialTextView) dialogView.findViewById(R.id.title);
            btnPickLecturer = (MaterialButton) dialogView.findViewById(R.id.pick_lecturer);
            edtName = dialogView.findViewById(R.id.edt_classroom_name);

            if(classroom != null) {
                getClassroomManager(classroom.getHeadMaster());
                edtName.setText(classroom.getName());
                btnPickLecturer.setText(currentLecturer.getFullName());
                dialogTitle.setText(getResources().getString(R.string.update_classroom));
                btnCancel.setText(getResources().getString(R.string.close));
                btnSave.setText(getResources().getString(R.string.update));
            } else {
                dialogTitle.setText(getResources().getString(R.string.add_classroom));
            }

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(classroom == null) {
                        classroom = new Classroom();
                        classroom.setName(edtName.getText().toString());
                        classroom.setId(mDatabase.child(CLASSROOMS_COLLECTION).push().getKey());
                        if(currentLecturer.getUserId() != null) {
                            classroom.setHeadMaster(currentLecturer.getUserId());
                        }
                        ClassroomsActionController.getInstance().createClassroom(classroom);
                    } else {
                        classroom.setName(edtName.getText().toString());
                        if(currentLecturer.getUserId() != null) {
                            classroom.setHeadMaster(currentLecturer.getUserId());
                        }
                        ClassroomsActionController.getInstance().updateClassroom(classroom);
                    }
                }
            });

            btnPickLecturer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dialog.dismiss();
                    getLecturers();
                }
            });


            this.setView(dialogView);
            dialog = this.create();
            dialog.show();

        }

        private void getClassroomManager(String id) {
            mDatabase.child(USERS_COLLECTION)
                    .child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                currentLecturer = snapshot.getValue(User.class);
                                btnPickLecturer.setText(getString(R.string.classroom_manager, currentLecturer.getFullName()));
                            } else {
                                Toast.makeText(requireContext(), "There are no recorded lecturers", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "A network error occurred while trying to fetch lecturers", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void getLecturers() {
            lecturers.clear();
            mDatabase.child(USERS_COLLECTION)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                    User user = postSnapshot.getValue(User.class);
                                    if(user != null && user.getUserRole() == 1) {
                                        lecturers.add(user);
                                    }
                                }
                                showLecturers();
                            } else {
                                Toast.makeText(requireContext(), "There are no recorded lecturers", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(requireContext(), "A network error occurred while trying to fetch lecturers", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        private void showLecturers() {
            final String[] lecturersNames = new String[lecturers.size()];

            for(int i=0; i<lecturers.size(); i++) {
                lecturersNames[i] = lecturers.get(i).getFullName();
            }

            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getResources().getString(R.string.available_lecturers))
                    .setSingleChoiceItems(lecturersNames, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            currentLecturer = lecturers.get(which);
                            btnPickLecturer.setText(currentLecturer.getFullName());
                        }
                    })
                    .show();
        }

    }
}