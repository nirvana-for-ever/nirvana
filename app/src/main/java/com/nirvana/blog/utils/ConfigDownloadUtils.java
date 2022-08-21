package com.nirvana.blog.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nirvana.blog.bean.AppConfigBean;
import com.nirvana.blog.bean.AppUpdateBean;
import com.nirvana.blog.bean.AppUpdateListBean;
import com.nirvana.blog.bean.AppUpdateResultBean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class ConfigDownloadUtils {

    public static void configDownload(String path,Context context,OnConfigDownloadCompleteListener listener){
        new ConfigDownloadTask(path,listener).execute();
    }

    public static class ConfigDownloadTask extends AsyncTask<Void,Void, AppUpdateBean> {

        private final String fileServicePath;
        private final OnConfigDownloadCompleteListener listener;

        public ConfigDownloadTask(String path,OnConfigDownloadCompleteListener listener){
            this.fileServicePath = path;
            this.listener = listener;
        }

        @Override
        protected AppUpdateBean doInBackground(Void... voids) {
            try {
                AppUpdateListBean configList = new AppUpdateListBean("","","","","",false,"");
                AppUpdateBean config = new AppUpdateBean(false,0,"",configList);
                OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
                Request request = new Request.Builder()
                        .url(fileServicePath)//请求接口。如果需要传参拼接到接口后面。
                        .build();//创建Request 对象
                Response response = client.newCall(request).execute();//得到Response 对象
                if (response.isSuccessful()&&response.code()==200){
                    config = new Gson().fromJson(response.body().string(),new TypeToken<AppUpdateBean>(){}.getType());
                }

                return config;
            }catch (Exception e){
                Log.e("exception------------>",e.getMessage() );
                return null;
            }
        }

        @Override
        protected void onPostExecute(AppUpdateBean result) {
            listener.onConfigComplete(result);
        }
    }

    public interface OnConfigDownloadCompleteListener{
        void onConfigComplete(AppUpdateBean result);
    }

}
