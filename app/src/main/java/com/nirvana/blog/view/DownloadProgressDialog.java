package com.nirvana.blog.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nirvana.blog.R;

import java.text.DecimalFormat;


public class DownloadProgressDialog extends Dialog {

    private DownLoadProgressbar mProgressBar;
    private TextView tvProgress;
    private TextView tvTitle;

    public DownloadProgressDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_view);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public void initView(){
        mProgressBar = findViewById(R.id.dpb_download);
        tvProgress = findViewById(R.id.tv_download_progress);
        tvTitle = findViewById(R.id.tv_download_title);
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public void setCurrentValue(int total,int curProgress) {
        int value = (int) ((curProgress / (float) total) * 100);
        mProgressBar.setProgress(value);
        tvProgress.setText(setFileSize(curProgress) +"/"+setFileSize(total));
    }

    public static String setFileSize(int size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));
        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));
            return df.format(new Float(f2).doubleValue()) + "KB";
        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }
    }

}
