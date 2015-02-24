package com.demo.stust.myapplication;

import android.os.Handler;
import android.util.Log;

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


/**
 * Created by STUST on 2015/2/16.
 */
public class JSONThread {
    private static String TAG = "myTAG";
    private static HashMap<Integer,String> apiMap;
    private static String LatLon = null, str_Rain = null, str_Air = null, str_UVI = null, str_Mudflows = null;
    private static List<String> apiData;
    private static String ErrorStr = "The page cannot be displayed because an internal server error has occurred.";
    private static HttpClient httpClient;
    private static HttpGet httpGet;
    private static HttpResponse response;

    private static Double lat = 23.553118;
    private static Double lon = 121.0211024;

    Handler handler = new Handler();
    JSONOrder JO_Rain,JO_Air,JO_UVI,JO_Mudflows;  //Json整理=JsonOrder=JO
    StringBuilder sbAPIText;

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

    class ThreadAdr2LL extends Thread { //用於地址轉經緯 之執行緒
        public void run() {

            String inputADR = MainActivity.PlaceholderFragment.getStrADR();
            httpClient = new DefaultHttpClient();    // set HttpClient client
            httpGet = new HttpGet(  // require Http to get the values
                    "http://maps.google.com.tw/maps/api/geocode/json?address="
                            + inputADR + "&sensor=false");  //於Google地址轉經緯中加入讀取至EditText之內容
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
                LatLon = lat + "," + lon;
                Log.i(TAG,"位址：" + inputADR +"  經緯：" + LatLon +"\t 地址轉經緯結束");
                while (LatLon != null)
                {
                    if (LatLon != null){
                        ThreadBro t1 = new ThreadBro();
                        t1.start(); break;
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
                    httpGet = new HttpGet(apiMap.get(i)+LatLon);    // require Http to get the values   //取得四個API其中一個後+入地址轉換過後的經緯
                    response = httpClient.execute(httpGet);    // get the reply from http;
                    switch (i){
                        case 0: str_Rain = EntityUtils.toString(response .getEntity()); apiData.add(str_Rain);  Log.i(TAG,"str_Rain:"+str_Rain);break;  //降雨
                        case 1: str_Air = EntityUtils.toString(response .getEntity()); apiData.add(str_Air);  Log.i(TAG,"str_Air:"+str_Air);break;  //空氣
                        case 2: str_UVI = EntityUtils.toString(response .getEntity()); apiData.add(str_UVI);  Log.i(TAG,"str_UVI:"+str_UVI);break;  //紫外線
                        case 3: str_Mudflows = EntityUtils.toString(response .getEntity()); apiData.add(str_Mudflows);  Log.i(TAG,"str_Mudflows:"+str_Mudflows);break;  //土石流
                    }
                }

                if (apiData != null) {
                    sbAPIText = new StringBuilder();
                    Log.i(TAG, "\n" + "API Data：" + "\n" + apiData.get(0) + "\n" + apiData.get(1) + "\n" + apiData.get(2) + "\n" + apiData.get(3));

                    JO_Rain = new JSONOrder('0',str_Rain);   Log.i(TAG,"j0time");
                    sbAPIText.append("降雨量/小時："+ JO_Rain.getR1hr() +"\n降雨量/天：" + JO_Rain.getR24hr() + "\n危害層級(0~5)：" + JO_Rain.getLevel() + "\n危害程度：" + JO_Rain.getLeveltxt());

                    JO_Air = new JSONOrder('1',str_Air);   Log.i(TAG,"j1time");
                    sbAPIText.append("\n\n空氣汙染指標："+JO_Air.getPSI() + "\n汙染源：" + JO_Air.getMajorPoutant() + "\n危害層級(0~4)：" + JO_Air.getLevel() + "\n危害程度：" + JO_Air.getLeveltxt());

                    JO_UVI = new JSONOrder('2',str_UVI);   Log.i(TAG,"j2time");
                    sbAPIText.append("\n\n紫外線指標：" + JO_UVI.getUVI() + "\n危害層級(0~4)：" + JO_UVI.getLevel() + "\n危害程度：" + JO_UVI.getLeveltxt());

                    JO_Mudflows = new JSONOrder('3',str_Mudflows);   Log.i(TAG,"j3time");
                    sbAPIText.append("\n\n土石流機率：" + JO_Mudflows.getLevel() + "\n地標：" + JO_Mudflows.getLandmark());

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
            MainActivity.PlaceholderFragment.tv.setText(sbAPIText);
            MainActivity.PlaceholderFragment.btn.setEnabled(true);
        }
    };

}
