package com.mobileedu34.schoolhub.ui.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mobileedu34.schoolhub.R;
import com.mobileedu34.schoolhub.callbacks.lecturers.ActionContract;
import com.mobileedu34.schoolhub.callbacks.lecturers.LecturersActionController;
import com.mobileedu34.schoolhub.callbacks.students.StudentsActionController;
import com.mobileedu34.schoolhub.helpers.Constants;
import com.mobileedu34.schoolhub.helpers.UserAction;
import com.mobileedu34.schoolhub.models.Classroom;
import com.mobileedu34.schoolhub.models.User;
import com.mobileedu34.schoolhub.preferences.AppPreferences;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_OK;
import static com.mobileedu34.schoolhub.helpers.UserAction.ADD_LECTURER;
import static com.mobileedu34.schoolhub.helpers.UserAction.ADD_STUDENT;
import static com.mobileedu34.schoolhub.helpers.UserAction.VIEW_LECTURER;

public class AddUserFragment extends Fragment implements ActionContract.View, com.mobileedu34.schoolhub.callbacks.students.ActionContract.View {

    public static final String ARG_TITLE = "title";
    public static final String ARG_USER = "user";
    public static final String ARG_USER_ACTION_TYPE = "user_action_type";
    private TextView txvFullName;
    private TextView txvGender;
    private TextView txvPhoneNumber;
    private TextView txvEmailAddress;
    private CircularImageView profilePhoto;
    private FrameLayout btnLaunchCamera;
    private MaterialCardView btnEditName;
    private MaterialCardView btnEditPhoneNumber;
    private MaterialCardView btnEditEmail;
    private MaterialCardView btnEditGender;
    private MaterialCardView btnEditClassrooms;
    private Uri filePath;
    private MaterialButton btnDeleteUser;
    private static final int REQUEST_PICTURE_CAPTURE = 9001;
    private static final int PICK_IMAGE_REQUEST = 844;
    private DatabaseReference mDatabase;
    private String userId;
    private BottomSheetDialog chooseImage;
    private ProgressBar progressBar;
    private Bitmap bmp;
    private int PERMISSIONS_REQUEST_CAMERA = 2000;
    private String pictureFilePath;
    private ProgressDialog progressDialog;
    private User mUser;
    private String dateFormat = "yyyy-MM-dd";
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.FRENCH);
    private UserAction mAction;
    private int currentSettings;
    private boolean creating;
    private String generatedPassword;
    private ArrayList<Classroom> classrooms;

    public AddUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(requireContext());
        if (getArguments() != null) {
            String mTitle = getArguments().getString(ARG_TITLE);
            mUser = getArguments().getParcelable(ARG_USER);
            mAction = (UserAction) getArguments().getSerializable(ARG_USER_ACTION_TYPE);

            ((AppCompatActivity)requireActivity()).getSupportActionBar().setTitle(mTitle);
        }

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if(creating) {
                    promptConfirmDialog();
                } else {

                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_user, container, false);
        init(root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        switch (mAction) {
            case ADD_STUDENT:
            case VIEW_STUDENT:
                StudentsActionController.getInstance().setListener(this);
                break;
            case ADD_LECTURER:
            case VIEW_LECTURER:
                LecturersActionController.getInstance().setListener(this);
                break;
        }

        creating = mAction == ADD_STUDENT || mAction == ADD_LECTURER;

        if(creating) {
            mUser = new User();
        } else {
            userId = mUser.getUserId();
            populateViews();
        }

        if(! creating) {
            btnDeleteUser.setVisibility(View.VISIBLE);
        } else {
            btnDeleteUser.setVisibility(View.GONE);
        }

    }

    private void populateViews() {
        try {
            Picasso.get()
                    .load(mUser.getPhotoUrl())
                    .placeholder(R.drawable.ic_settings_profile)
                    .error(R.drawable.ic_settings_profile)
                    .into(profilePhoto);
        }catch (Exception e) {
            profilePhoto.setImageResource(R.drawable.ic_settings_profile);
        }

        txvFullName.setText(mUser.getFullName() == null ? "N/A" : mUser.getFullName());
        txvPhoneNumber.setText(mUser.getPhoneNumber() == null ? "N/A" : mUser.getPhoneNumber());
        txvEmailAddress.setText(mUser.getEmailAddress() == null ? "N/A" : mUser.getEmailAddress());
        txvGender.setText(mUser.getGender() == null ? "N/A" : mUser.getGender());
    }

    private void init(View root) {
        txvFullName = root.findViewById(R.id.user_name);
        txvGender = root.findViewById(R.id.user_gender);
        txvPhoneNumber = root.findViewById(R.id.user_phone_number);
        txvEmailAddress = root.findViewById(R.id.user_email_address);
        profilePhoto = root.findViewById(R.id.user_profile_photo);
        btnLaunchCamera = root.findViewById(R.id.launch_camera);
        btnEditName = root.findViewById(R.id.card_name_section);
        btnEditPhoneNumber = root.findViewById(R.id.card_phone_section);
        btnEditEmail = root.findViewById(R.id.card_email_section);
        btnEditGender = root.findViewById(R.id.card_gender_section);
        progressBar = root.findViewById(R.id.upload_progress);
        btnEditClassrooms = root.findViewById(R.id.card_classroom_section);
        btnDeleteUser = root.findViewById(R.id.btn_delete_user);

        if(mAction == ADD_LECTURER || mAction == VIEW_LECTURER) {
            btnEditClassrooms.setVisibility(View.VISIBLE);
        } else {
            btnEditClassrooms.setVisibility(View.GONE);
        }

        setupListeners();
    }

    private void setupListeners() {

        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getResources().getString(R.string.confirm_deletion))
                        .setMessage(getResources().getString(R.string.warn_delete))
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                progressDialog.setMessage(getResources().getString(R.string.deleting_user));
                                progressDialog.show();
                                if(mAction == VIEW_LECTURER) {
                                    LecturersActionController.getInstance().deleteLecturer(userId);
                                } else {
                                    StudentsActionController.getInstance().deleteStudent(userId);
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnEditClassrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClassRooms();
            }
        });

        btnEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings = 0;
                showEditorDialog(currentSettings);
            }
        });

        btnEditPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings = 1;
                showEditorDialog(currentSettings);
            }
        });

        btnEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAction == UserAction.VIEW_LECTURER || mAction == UserAction.VIEW_STUDENT)
                    return;
                currentSettings = 3;
                showEditorDialog(currentSettings);
            }
        });

        btnEditGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSettings = 2;
                showEditorDialog(currentSettings);
            }
        });
        
    }

    private void getClassRooms() {
        classrooms = new ArrayList<>();
        mDatabase.child(Constants.CLASSROOMS_COLLECTION)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                                Classroom classroom = postSnapshot.getValue(Classroom.class);
                                classrooms.add(classroom);
                            }
                            showClassrooms();
                        } else {
                            Toast.makeText(requireContext(), "There are no recorded classrooms", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "A network error occurred while trying to fetch classrooms", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showClassrooms() {
        final String[] classRoomsNames = new String[classrooms.size()];

        for(int i=0; i<classrooms.size(); i++) {
            classRoomsNames[i] = classrooms.get(i).getName();
        }

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getResources().getString(R.string.school_classrooms))
                .setMultiChoiceItems(classRoomsNames, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) {
                            Toast.makeText(requireContext(), classRoomsNames[which], Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setPositiveButton(getResources().getString(R.string.apply), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
                profilePhoto.setImageBitmap(bitmap);
                chooseImage.dismiss();
                if(! creating)
                    uploadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == REQUEST_PICTURE_CAPTURE && resultCode == RESULT_OK) {
            File imgFile = new  File(pictureFilePath);
            if(imgFile.exists())            {
                profilePhoto.setImageURI(Uri.fromFile(imgFile));
                filePath = (Uri.fromFile(imgFile));
                if(! creating)
                    uploadFile();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImagePicker();
            } else {
                showMessage("Camera access permission denied.");
            }
        }

    }

    private void showMessage(String message) {
        View parentLayout = getActivity().findViewById(android.R.id.content);

        Snackbar mSnackBar = Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(R.color.sh_yellow500));
        TextView tv = (TextView) (mSnackBar.getView()).findViewById(com.google.android.material.R.id.snackbar_text);
        Typeface font = Typeface.createFromAsset(requireContext().getAssets(), "font/productsansregular.ttf");
        tv.setTypeface(font);
        mSnackBar.show();
    }

    private void deleteProfilePhoto(){

        final AlertDialog.Builder dialogPhotoDeletion = new AlertDialog.Builder(requireContext());
        dialogPhotoDeletion.setTitle(getResources().getString(R.string.delete_profile_photo));
        dialogPhotoDeletion.setMessage(getResources().getString(R.string.confirm_profile_deletion));
        dialogPhotoDeletion.setPositiveButton(getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage(getResources().getString(R.string.please_wait));
                progressDialog.show();
                StorageReference profilePhotoRef = FirebaseStorage.getInstance().getReference().child("users").child("pictures").child(userId);
                profilePhotoRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mUser.setPhotoUrl(null);
                                mDatabase.child("users").child(userId).setValue(mUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        progressDialog.dismiss();
                                        profilePhoto.setImageResource(R.drawable.ic_settings_profile);
                                        showMessage("Profile photo successfully deleted.");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        showMessage("Failed to delete your profile photo.");
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                showMessage("Failed to delete your profile photo.");
                            }
                        });
            }
        });
        dialogPhotoDeletion.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogPhotoDeletion.show();

    }

    private void uploadFile() {
        if (filePath != null) {

            progressBar.setVisibility(View.VISIBLE);
            final StorageReference riversRef = FirebaseStorage.getInstance().getReference().child("users").child("pictures").child(userId);
            try {
                bmp = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), filePath);
            }catch (Exception e){
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] data = baos.toByteArray();

            riversRef.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.getDownloadUrl();

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    mUser.setPhotoUrl(uri.toString());
                                    mDatabase.child("users").child(userId).setValue(mUser);

                                    if(creating) {
                                        //switchBack();
                                        promptPasswordDialog();
                                    }

                                }});

                            progressBar.setVisibility(View.INVISIBLE);
                            String message = !creating ? "Successfully updated profile photo." : "Successfully created profile photo.";
                            showMessage(message);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressBar.setVisibility(View.INVISIBLE);
                            showMessage("Failed to upload profile photo.");
                            if(creating) {
                                //switchBack();
                                promptPasswordDialog();
                            }
                        }
                    });
        }
        else {
            showMessage("Operation failed");
        }
    }

    private void switchBack() {
        if(mAction == ADD_LECTURER) {
            NavHostFragment
                    .findNavController(requireParentFragment())
                    .navigate(R.id.action_add_user_fragment_to_nav_lecturers);
        } else if(mAction == ADD_STUDENT){
            NavHostFragment
                    .findNavController(requireParentFragment())
                    .navigate(R.id.action_add_user_fragment_to_nav_students);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chosir une image"), PICK_IMAGE_REQUEST);
    }

    private void showImagePicker(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && requireActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {

            String timeStamp = simpleDateFormat.format(new Date());
            String pictureFileName = "sh_" + timeStamp;
            File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;

            try {
                image = File.createTempFile(pictureFileName,  ".jpg", storageDir);
            }catch (Exception e){
                Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            pictureFilePath = image.getAbsolutePath();

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (cameraIntent.resolveActivity(requireContext().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);

                File pictureFile;
                pictureFile = image;
                Uri photoURI = FileProvider.getUriForFile(requireContext(),
                        "com.mobileedu34.schoolhub",
                        pictureFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_PICTURE_CAPTURE);
            }
        }
    }

    private void pickImage(){
        chooseImage = new BottomSheetDialog(requireContext(), R.style.Theme_SHMaterial_Light_BottomSheetDialog);
        chooseImage.setContentView(R.layout.image_chooser);
        LinearLayout gallery = chooseImage.findViewById(R.id.gallery);
        LinearLayout camera = chooseImage.findViewById(R.id.camera);
        LinearLayout deleteProfilePhoto = chooseImage.findViewById(R.id.delete_image);

        if(creating) {
            deleteProfilePhoto.setVisibility(View.GONE);
        } else {
            if(mUser.getPhotoUrl() == null || mUser.getPhotoUrl().equals("")) {
                deleteProfilePhoto.setVisibility(View.GONE);
            }
        }

        deleteProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage.dismiss();
                deleteProfilePhoto();
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage.dismiss();
                showFileChooser();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage.dismiss();
                showImagePicker();
            }
        });
        chooseImage.show();
    }

    private void showEditorDialog(final int id) {
        final BottomSheetDialog dialog = new BottomSheetDialog(requireContext(), R.style.Theme_SHMaterial_Light_BottomSheetDialog);

        final View bottomSheetView = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_editor_popup,
                (LinearLayout)requireActivity().findViewById(R.id.bottomSheetContainer));

        final TextInputLayout nameSection = bottomSheetView.findViewById(R.id.name_section);
        final TextInputLayout phoneSection = bottomSheetView.findViewById(R.id.phone_section);
        final TextInputLayout emailSection = bottomSheetView.findViewById(R.id.email_section);
        final TextInputEditText edtName = bottomSheetView.findViewById(R.id.edt_name);
        final TextInputEditText edtPhone = bottomSheetView.findViewById(R.id.edt_phone);
        final TextInputEditText edtMail = bottomSheetView.findViewById(R.id.edt_email);
        final RadioGroup genderGroup = bottomSheetView.findViewById(R.id.radio_gender_group);
        final RadioButton radioMaleGender = bottomSheetView.findViewById(R.id.radio_male_gender);
        final RadioButton radioFemaleGender = bottomSheetView.findViewById(R.id.radio_gender_female);
        final MaterialButton btnSave = bottomSheetView.findViewById(R.id.save);
        final MaterialButton btnCancel = bottomSheetView.findViewById(R.id.cancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id == 0) {
                    mUser.setFullName(edtName.getText().toString().trim());
                    txvFullName.setText(mUser.getFullName());
                } else if(id == 1) {
                    mUser.setPhoneNumber(edtPhone.getText().toString().trim());
                    txvPhoneNumber.setText(mUser.getPhoneNumber());
                } else if(id == 2) {
                    if(radioMaleGender.isChecked()) {
                        mUser.setGender(getResources().getString(R.string.gender_male));
                    } else {
                        mUser.setGender(getResources().getString(R.string.gender_female));
                    }
                    txvGender.setText(mUser.getGender());
                } else if(id == 3) {
                    mUser.setEmailAddress(edtMail.getText().toString().trim());
                    txvEmailAddress.setText(mUser.getEmailAddress());
                }

                if(! creating) {
                    progressDialog.setMessage(getResources().getString(R.string.updating_user_account));
                    progressDialog.show();

                    if(mAction == UserAction.VIEW_STUDENT) {
                        StudentsActionController.getInstance().updateStudent(mUser);
                    } else if(mAction == UserAction.VIEW_LECTURER) {
                        LecturersActionController.getInstance().updateLecturer(mUser);
                    }
                }
                dialog.dismiss();
            }
        });

        if(id == 0) {
            if(! creating) {
                if(mUser.getFullName() != null) {
                    edtName.setText(mUser.getFullName());
                }
            }
            nameSection.setVisibility(View.VISIBLE);
            phoneSection.setVisibility(View.GONE);
            genderGroup.setVisibility(View.GONE);
            emailSection.setVisibility(View.GONE);
        } else if(id == 1) {
            if(! creating) {
                if(mUser.getPhoneNumber() != null) {
                    edtPhone.setText(mUser.getPhoneNumber());
                }
            }
            phoneSection.setVisibility(View.VISIBLE);
            nameSection.setVisibility(View.GONE);
            genderGroup.setVisibility(View.GONE);
            emailSection.setVisibility(View.GONE);
        } else if(id == 2) {
            if(! creating) {
                if(mUser.getGender() != null) {
                    if(mUser.getGender().equals(getResources().getString(R.string.gender_female))) {
                        radioFemaleGender.setChecked(true);
                    } else {
                        radioMaleGender.setChecked(true);
                    }
                }
            }

            phoneSection.setVisibility(View.GONE);
            nameSection.setVisibility(View.GONE);
            genderGroup.setVisibility(View.VISIBLE);
            emailSection.setVisibility(View.GONE);
        } else if(id == 3) {
            if(! creating) {
                if(mUser.getEmailAddress() != null) {
                    edtMail.setText(mUser.getEmailAddress());
                }
            }
            phoneSection.setVisibility(View.GONE);
            nameSection.setVisibility(View.GONE);
            genderGroup.setVisibility(View.GONE);
            emailSection.setVisibility(View.VISIBLE);
        }

        dialog.setContentView(bottomSheetView);
        dialog.show();
    }

    @Override
    public void onCreateLecturerSuccess() {
        progressDialog.dismiss();
        if(filePath != null) {
            uploadFile();
        } else {
            promptPasswordDialog();
        }
    }

    @Override
    public void onCreateLecturerFailure(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.unsuccessful_creation));
    }

    @Override
    public void onUpdateLecturerSuccess() {
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.user_info_successful_update));
    }

    @Override
    public void onUpdateLecturerFailure(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.unsuccessful_update));
    }

    @Override
    public void onDeleteLecturerSuccess() {
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.user_successful_deletion));
        requireActivity().onBackPressed();
    }

    @Override
    public void onDeleteLecturerFailure(String message) {
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.user_unsuccessful_deletion));
    }

    @Override
    public void onGetLecturersSuccess(ArrayList<User> lecturers) {

    }

    @Override
    public void onGetLecturersFailure(String message) {

    }

    @Override
    public void onCreateStudentSuccess() {
        progressDialog.dismiss();
        if(filePath != null) {
            uploadFile();
        } else {
            promptPasswordDialog();
        }
    }

    @Override
    public void onCreateStudentFailure(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.unsuccessful_creation));
    }

    @Override
    public void onUpdateStudentSuccess() {
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.user_info_successful_update));
    }

    @Override
    public void onUpdateStudentFailure(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.unsuccessful_update));
    }

    @Override
    public void onDeleteStudentSuccess() {
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.user_successful_deletion));
        requireActivity().onBackPressed();
    }

    @Override
    public void onDeleteStudentFailure(String message) {
        progressDialog.dismiss();
        showMessage(getResources().getString(R.string.user_unsuccessful_deletion));
    }

    @Override
    public void onGetStudentsSuccess(ArrayList<User> students) {

    }

    @Override
    public void onGetStudentsFailure(String message) {

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            if(creating) {
                promptConfirmDialog();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void promptPasswordDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getResources().getString(R.string.copy_password))
                .setMessage(getString(R.string.password_copy_instructions, generatedPassword))
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switchBack();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void promptConfirmDialog() {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(getResources().getString(R.string.abort_operation))
                .setMessage(getResources().getString(R.string.confirm_abort))
                .setPositiveButton(getResources().getString(R.string.save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        progressDialog.setMessage(getResources().getString(R.string.creating_user_account));
                        progressDialog.show();

                        if(creating) {
                            userId = mDatabase.push().getKey();
                            mUser.setUserId(userId);
                            mUser.setRandomPassword(getRandomString());
                            if(mAction == ADD_LECTURER) {
                                mUser.setUserRole(1);
                                LecturersActionController.getInstance().createLecturer(mUser);
                            } else {
                                mUser.setUserRole(0);
                                StudentsActionController.getInstance().createStudent(mUser);
                            }
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.abort), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        switchBack();
                    }
                })
                .show();
    }

    private String getRandomString(){
        String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
        final Random random = new Random();
        final StringBuilder sb=new StringBuilder(8);

        for(int i=0; i<8; i++){
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        }
        generatedPassword = sb.toString();
        return sb.toString();
    }
}