package com.mobileedu34.schoolhub.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import com.mobileedu34.schoolhub.R;

/**
 * DialogLoader will be used to show and hide Dialog with ProgressBar
 **/

public class DialogLoader {

    private Context context;
    private ProgressDialog progressDialog;
    private TextView loaderStatus;

    public DialogLoader(Context context) {
        this.context = context;
        initDialog();
    }

    private void initDialog() {

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void showProgressDialog(String customMessage) {
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        loaderStatus = progressDialog.findViewById(R.id.message);
        loaderStatus.setText(customMessage);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.setCancelable(true);
                loaderStatus.setText(context.getResources().getString(R.string.taking_longer_than_expected));
            }
        }, 8000);
    }
    public void dismissProgressDialog() {
        progressDialog.dismiss();
    }
}

