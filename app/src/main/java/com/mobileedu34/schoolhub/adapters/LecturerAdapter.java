package com.mobileedu34.schoolhub.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.ViewHolder> {


    private final Context context;
    private List<User> mLecturersList;
    private ArrayList<User> lecturersSet;
    private int currentItemPosition;
    private Fragment fragmentInstance;
    private int currentPosition;

    public LecturerAdapter(List<User> lecturersList, Fragment fragmentInstance) {
        this.mLecturersList = lecturersList == null ? new ArrayList<User>() : lecturersList;
        this.fragmentInstance = fragmentInstance;
        this.context = fragmentInstance.requireContext();
        lecturersSet = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_user_layout, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final LecturerAdapter.ViewHolder holder, int position) {
        final User lecturer = mLecturersList.get(position);

        holder.txvName.setText(lecturer.getFullName());
        holder.txvEmail.setText(lecturer.getEmailAddress());
        try {
            Picasso.get()
                    .load(lecturer.getPhotoUrl())
                    .placeholder(R.drawable.ic_settings_profile)
                    .error(R.drawable.ic_settings_profile)
                    .into(holder.mUserProfilePhoto);
        }catch (Exception e) {
            holder.mUserProfilePhoto.setImageResource(R.drawable.ic_settings_profile);
        }
    }

    @Override
    public int getItemCount() {
        return mLecturersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView txvName;
        private TextView txvEmail;
        private TextView txvAdminLabel;
        private CircularImageView mUserProfilePhoto;
        private MaterialCardView card;

        public ViewHolder(final View v, final Context contextHolder) {
            super(v);
            txvName = (TextView) v.findViewById(R.id.user_name);
            card = (MaterialCardView) v.findViewById(R.id.main_card);
            txvEmail = (TextView) v.findViewById(R.id.user_email_address);
            mUserProfilePhoto = (CircularImageView) v.findViewById(R.id.user_profile_photo);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            currentPosition = getAdapterPosition();
            User user = mLecturersList.get(currentPosition);
        }

        @Override
        public boolean onLongClick(final View v) {
            return true;
        }
    }


}
