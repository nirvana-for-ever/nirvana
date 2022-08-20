package com.nirvana.blog.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.nirvana.blog.bean.AppConfigBean;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;


public class ConfigDownloadUtils {

    public static void configDownload(String path,Context context,OnConfigDownloadCompleteListener listener){
        new ConfigDownloadTask(path,listener).execute();
    }

    public static class ConfigDownloadTask extends AsyncTask<Void,Void, AppConfigBean> {

        private final String fileServicePath;
        private final OnConfigDownloadCompleteListener listener;

        public ConfigDownloadTask(String path,OnConfigDownloadCompleteListener listener){
            this.fileServicePath = path;
            this.listener = listener;
        }

        @Override
        protected AppConfigBean doInBackground(Void... voids) {
            try {
                URL url = new URL(fileServicePath);
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                int lineType = 1;
                String lineValue;
                AppConfigBean config = new AppConfigBean();
                while ((lineValue = reader.readLine()) != null) {
                    String line = lineValue.trim();
                    if (line.matches("\\[.*\\]")) {
                        String section = line.replaceFirst("\\[(.*)\\]", "$1");
                        if(section.equals("version")){
                            lineType = 1;
                        }else if(section.equals("message")){
                            lineType = 2;
                            config.getMessage().clear();
                        }else if(section.equals("address")){
                            lineType = 3;
                        }
                    } else if (line.matches(".*=.*")) {
                        int i = line.indexOf('=');
                        switch (lineType){
                            case 1:
                                if(i<line.length()-1){
                                    String key = line.substring(0, i);
                                    if(key.equals("is_force")){
                                        config.setForce(line.substring(i + 1));
                                    }
                                    if(key.equals("version_name")){
                                        config.setVersionName(line.substring(i + 1));
                                    }
                                    if(key.equals("version_code")){
                                        config.setVersionCode(Integer.parseInt(line.substring(i + 1)));
                                    }
                                    if(key.equals("file_name")){
                                        config.setFileName(line.substring(i + 1));
                                    }
                                    if(key.equals("customer")){
                                        config.setCustomer(line.substring(i + 1));
                                    }
                                }
                                break;
                            case 2:
                                if(i<line.length()-1){
                                    config.getMessage().add(line.substring(i + 1));
                                }
                                break;
                            case 3:
                                if(i<line.length()-1){
                                    String key = line.substring(0, i);
                                    if(key.equals("service_address")){
                                        config.setServiceAddress(line.substring(i + 1));
                                    }
                                    if(key.equals("weight_address")){
                                        config.setWeightAddress(line.substring(i + 1));
                                    }
                                    if(key.equals("is_redundancy")){
                                        config.setRedundancy(line.substring(i + 1));
                                    }
                                    if(key.equals("weight_type")){
                                        config.setWeightType(line.substring(i + 1));
                                    }
                                    if(key.equals("redundancy_address")){
                                        config.setRedundancyAddress(line.substring(i + 1));
                                    }
                                    if(key.equals("serial_select")){
                                        config.setSerialSelect(line.substring(i + 1));
                                    }
                                }
                                break;
                        }
                    }
                }
                return config;
            }catch (Exception e){
                Log.e("exception------------>",e.getMessage() );
                return null;
            }
        }

        @Override
        protected void onPostExecute(AppConfigBean result) {
            listener.onConfigComplete(result);
        }
    }

    public interface OnConfigDownloadCompleteListener{
        void onConfigComplete(AppConfigBean result);
    }

}
