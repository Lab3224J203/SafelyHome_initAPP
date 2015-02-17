package com.demo.stust.myapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by STUST on 2015/2/16.
 */
public class JSONAsyncTask extends AsyncTask<String, Void, String>{

    private static String TAG = "http";
    private static String apidata = "";
    private static HashMap<Integer,String> apiMap;
    public AsyncResponse delegate=null;

    @Override
    protected String doInBackground(String... params) {
        apiMap = new HashMap<Integer, String>();
        apiMap.put(0, "http://housesafe3224.azurewebsites.net/api?datatype=raintime&loc=");
        apiMap.put(1, "http://housesafe3224.azurewebsites.net/api?datatype=airquality&loc=");
        apiMap.put(2, "http://housesafe3224.azurewebsites.net/api?datatype=UVI&loc=");
        apiMap.put(3, "http://housesafe3224.azurewebsites.net/api?datatype=mudslide&loc=");
        //http://housesafe3224.azurewebsites.net/api?datatype=mudslide&loc=23.0393,121.2929

        for (int i=0 ; i<4 ; i++) {
            try {
                HttpClient httpClient = new DefaultHttpClient();    // set HttpClient client
                HttpGet httpGet = new HttpGet(apiMap.get(i) + params[0]);    // require Http to get the values
                HttpResponse response = httpClient.execute(httpGet);    // get the reply from http;
                apidata = EntityUtils.toString(response.getEntity());
                Log.i(TAG, "doInBackground Over :" + apidata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apidata;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }
}
