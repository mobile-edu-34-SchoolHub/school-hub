package com.mobileedu34.schoolhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.callbacks.OnClassroomSelectionListener;
import com.mobileedu34.schoolhub.models.Classroom;
import com.mobileedu34.schoolhub.models.User;
import java.util.ArrayList;
import java.util.List;
import static com.mobileedu34.schoolhub.helpers.Constants.USERS_COLLECTION;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder> {

    private final Context context;
    private List<Classroom> mStudentsList;
    private int currentPosition;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private Classroom currentClassroom;
    private User currentLecturer;
    private AlertDialog dialog;
    private ArrayList<User> lecturers = new ArrayList<>();
    private OnClassroomSelectionListener onClassroomSelectionListener;


    public ClassroomAdapter(List<Classroom> classroomsList, Fragment fragmentInstance) {
        this.mStudentsList = classroomsList == null ? new ArrayList<Classroom>() : classroomsList;
        this.context = fragmentInstance.requireContext();
        currentClassroom = new Classroom();
        currentLecturer = new User();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_classroom_layout, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClassroomAdapter.ViewHolder holder, int position) {
        final Classroom classroom = mStudentsList.get(position);

        holder.txvName.setText(classroom.getName());

        mDatabase.child(USERS_COLLECTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        if(snapshot.exists()) {
                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                User user = postSnapshot.getValue(User.class);
                                if(user != null && user.getUserRole() == 0) {
                                    if(user.getClassroomId().equals(classroom.getId())) {
                                        count += 1;
                                    }
                                }
                            }
                            holder.txvCount.setText(context.getString(R.string.classroom_students_count, count));
                        } else {
                            holder.txvCount.setText(context.getString(R.string.classroom_students_count, 0));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        holder.txvCount.setText(context.getString(R.string.classroom_students_count, 0));
                    }
                });

    }

    @Override
    public int getItemCount() {
        return mStudentsList.size();
    }

    public void clearDataSet() {
        mStudentsList.clear();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView txvName;
        private TextView txvCount;

        public ViewHolder(final View v, final Context contextHolder) {
            super(v);
            txvName = (TextView) v.findViewById(R.id.classroom_name);
            txvCount = (TextView) v.findViewById(R.id.classroom_members_count);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            currentPosition = getAdapterPosition();
            currentClassroom = mStudentsList.get(currentPosition);

            if (null != onClassroomSelectionListener) {
                onClassroomSelectionListener.onUpdate(currentClassroom);
            }
            //createDialogBuilder();
        }

        @Override
        public boolean onLongClick(final View v) {
            return true;
        }
    }

    public void setOnUserSelectionListener(OnClassroomSelectionListener onClassroomSelectionListener) {
        this.onClassroomSelectionListener = onClassroomSelectionListener;
    }

}

