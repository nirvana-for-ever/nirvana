package com.nirvana.blog.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.nirvana.blog.BlogApplication;
import com.nirvana.blog.view.DownloadProgressDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class APKRefreshDownload {

    private DownloadProgressDialog dialog;
    private boolean isStop;

    public APKRefreshDownload(Context mContext) {
        this.dialog = new DownloadProgressDialog(mContext);
        this.dialog.setCanceledOnTouchOutside(false);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isStop = true;
            }
        });
    }

    public void startDownload(String url,OnDownLoadCompleteListener listener){
        MyDownloadAsyncTask asyncTask = new MyDownloadAsyncTask();
        asyncTask.setOnDownLoadCompleteListener(listener);
        asyncTask.execute(url);
    }

    public static String getSaveFilePath(){
        String path = BlogApplication.instance.getCacheDir() + File.separator + "Download";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        String newFilePath = path +File.separator + "nirvana.apk";
        return newFilePath;
    }

    private class MyDownloadAsyncTask extends AsyncTask<String, Integer, Boolean> {

        public OnDownLoadCompleteListener listener;

        public void setOnDownLoadCompleteListener(OnDownLoadCompleteListener listener){
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            InputStream inputStream = null;
            FileOutputStream outputStream = null;
            boolean isComplete = true;
            try {
                outputStream = new FileOutputStream(getSaveFilePath());
                URL url = new URL(params[0]);
                URLConnection conn = url.openConnection();
                // 建立链接
                conn.connect();
                // 获取输入流
                inputStream = conn.getInputStream();
                // 获取文件流大小，用于更新进度
                int file_length = conn.getContentLength();
                //dialog.setMaxValue(file_length);
                int len;
                int total_length = 0;
                byte[] data = new byte[1024];
                while ((len = inputStream.read(data)) != -1) {
                    if(isStop){
                        isComplete = false;
                        break;
                    }
                    total_length += len;
                    // 调用update函数，更新进度
                    publishProgress(file_length,total_length);
                    outputStream.write(data, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isComplete = false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return isComplete;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialog.setCurrentValue(values[0],values[1]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialog.dismiss();
            listener.onComplete(result);
        }

    }

    public interface OnDownLoadCompleteListener{
        void onComplete(boolean isComplete);
    }


}
