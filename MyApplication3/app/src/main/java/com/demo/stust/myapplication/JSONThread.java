package com.demo.stust.myapplication;

import android.util.Log;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Handler;


/**
 * Created by STUST on 2015/2/16.
 */
public class JSONThread {
    private static String TAG = "myTAG",sss=null;
    private static HashMap<Integer,String> apiMap;
    private static String str = null, str0 = null, str1 = null, str2 = null, str3 = null;
    private static List<String> apiData;
    private static String ErrorStr = "The page cannot be displayed because an internal server error has occurred.";
    private static HttpClient httpClient;
    private static HttpGet httpGet;
    private static HttpResponse response;

    private static Double lat = 23.553118;
    private static Double lon = 121.0211024;

    Handler handler = new Handler();
    JSONOrder jsonorder0,jsonorder1,jsonorder2,jsonorder3;
    StringBuilder sb;

    public JSONThread() {
        apiMap = new HashMap<Integer, String>();
        apiMap.put(0, "http://housesafe3224.azurewebsites.net/api?datatype=raintime&loc=");
        apiMap.put(1, "http://housesafe3224.azurewebsites.net/api?datatype=airquality&loc=");
        apiMap.put(2, "http://housesafe3224.azurewebsites.net/api?datatype=UVI&loc=");
        apiMap.put(3, "http://housesafe3224.azurewebsites.net/api?datatype=mudslide&loc=");
//        apiMap.put(0, "http://120.117.130.27:8888/api?datatype=raintime&loc=");
//        apiMap.put(1, "http://120.117.130.27:8888/api?datatype=airquality&loc=");
//        apiMap.put(2, "http://120.117.130.27:8888/api?datatype=UVI&loc=");
//        apiMap.put(3, "http://120.117.130.27:8888/api?datatype=mudslide&loc=");

        ThreadAdr2LL a2l = new ThreadAdr2LL();
        a2l.start();
    }

    public static String getStr0() {
        if (str0 != null)
            return str0;
        else
            return null;
    }
    public static String getStr1() {
        if (str1 != null)
            return str1;
        else
            return null;
    }
    public static String getStr2() {
        if (str2 != null)
            return str2;
        else
            return null;
    }
    public static String getStr3() {
        if (str2 != null)
            return str2;
        else
            return null;
    }

    class ThreadAdr2LL extends Thread {
        public void run() {

            String inputADR = MainActivity.PlaceholderFragment.getStrADR();
            httpClient = new DefaultHttpClient();    // set HttpClient client
            httpGet = new HttpGet(  // require Http to get the values
                    "http://maps.google.com.tw/maps/api/geocode/json?address="
                            + inputADR + "&sensor=false");
            StringBuilder sb = new StringBuilder();
            try {
                response = httpClient.execute(httpGet);    // get the reply from http
                HttpEntity entity = response.getEntity();   // get the entity of reply from http
                InputStream stream = entity.getContent();   // get the contents from the entity of reply
                int count;  // count the length of stream of contents from reply
                while ((count = stream.read()) != -1) { // the loop which receiving the reply from http get
                    sb.append((char) count);
                }
//                Log.i(TAG, "ShowLatLonData : " + sb.toString());
                JSONObject jsonObject = new JSONObject(sb.toString());  // let jsonObject be receiving reply
                JSONObject location = jsonObject.getJSONArray("results")
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location"); // take the Latitude and Longitude of address by JSONObject
                lat = location.getDouble("lat");   // get the Latitude
                lon = location.getDouble("lng");    // get the Longitude
                str = lat + "," + lon;
                Log.i(TAG,"位址：" + inputADR +"  經緯：" + str +"\t 地址轉經緯結束");
                while (str != null)
                {
                    if (str != null){
                        ThreadBro t1 = new ThreadBro();
                        t1.start();
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class ThreadBro extends Thread{


        public void run(){
            httpClient = new DefaultHttpClient();    // set HttpClient client
            apiData = new ArrayList<String>();
            try {
                for (int i=0 ; i<4 ; i++){
                    httpGet = new HttpGet(apiMap.get(i)+str);    // require Http to get the values
                    response = httpClient.execute(httpGet);    // get the reply from http;
                    switch (i){
                        case 3: str0 = EntityUtils.toString(response .getEntity()); apiData.add(str0);  break;
                        case 0: str1 = EntityUtils.toString(response .getEntity()); apiData.add(str1);  break;
                        case 1: str2 = EntityUtils.toString(response .getEntity()); apiData.add(str2);  break;
                        case 2: str3 = EntityUtils.toString(response .getEntity()); apiData.add(str3);  break;
                    }
                }

                if (apiData != null) {
                    sb = new StringBuilder();
                    Log.i(TAG, "\n" + "API Data：" + "\n" + apiData.get(0) + "\n" + apiData.get(1) + "\n" + apiData.get(2) + "\n" + apiData.get(3));
                    //jsonorder3.getTitle() + "\n" + jsonorder3.getName()
                    jsonorder0 = new JSONOrder('0',str0);   sb.append("土石流機率：" + jsonorder0.getLevel() + "\n地標：" + jsonorder0.getLandmark());
                    jsonorder1 = new JSONOrder('1',str1);   sb.append("\n\n降雨量/小時："+ jsonorder1.getR1hr() +"\n降雨量/天：" + jsonorder1.getR24hr() + "\n危害層級(0~5)：" + jsonorder1.getLevel() + "\n危害程度：" + jsonorder1.getLeveltxt());
                    jsonorder2 = new JSONOrder('2',str2);   sb.append("\n\n空氣汙染指標："+jsonorder2.getPSI() + "\n汙染源：" + jsonorder2.getMajorPoutant() + "\n危害層級(0~4)：" + jsonorder2.getLevel() + "\n危害程度：" + jsonorder2.getLeveltxt());
                    jsonorder3 = new JSONOrder('3',str3);   sb.append("\n\n紫外線指標：" + jsonorder3.getUVI() + "\n危害層級(0~4)：" + jsonorder3.getLevel() + "\n危害程度：" + jsonorder3.getLeveltxt());
                    handler.post(ChangeText);
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.i(TAG, "\n" + e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "\n" + e);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable ChangeText = new Runnable() {
        @Override
        public void run() {
            MainActivity.PlaceholderFragment.tv.setText(sb);
        }
    };

}
