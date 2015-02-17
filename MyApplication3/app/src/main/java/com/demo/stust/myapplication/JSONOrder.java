package com.demo.stust.myapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by STUST on 2015/2/17.
 */
public class JSONOrder {
    private static String title = null;
    private static String name = null;
    private static double lat = 0;
    private static double lon = 0;

    private String level = null;       //危害層級(0~4)
    private String leveltxt = null;    //危害層級中文解釋
    private String UpdateTime = null;
    private String R1hr = null;        //1小時降雨量
    private String R24hr = null;       //24小時降雨量
    private String PSI = null;         //空氣汙染指標
    private String UVI = null;         //紫外線指標
    private String MajorPoutant = null;//汙染源
    private String landmark = null;



//    JSONObject jsonObject = new JSONObject(sb.toString());  // let jsonObject be receiving reply
//    JSONObject location = jsonObject.getJSONArray("results")
//            .getJSONObject(0).getJSONObject("geometry")
//            .getJSONObject("location"); // take the Latitude and Longitude of address by JSONObject

    public JSONOrder(char order,String json) throws JSONException {
        json = json.replace("[","");    json = json.replace("]","");
        JSONObject jsonAPIData = new JSONObject(json);
        Log.i("myTag","json:"+json);
        title = jsonAPIData.getString("title");

        switch(order){
            case '0':   //土石流
                name = jsonAPIData.getString("name");
                level = jsonAPIData.getString("level");
                landmark = jsonAPIData.getString("landmark");
                lat = jsonAPIData.getJSONObject("loc").getDouble("lat");
                lon = jsonAPIData.getJSONObject("loc").getDouble("long");
                Log.i("myTag","name："+name +
                                "\t程度："+ level +
                                "\t地標："+ landmark +
                                "\t經緯："+ lat + "," + lon);
                break;
            case '1':   //降雨
                name = jsonAPIData.getString("county");
                R1hr = jsonAPIData.getString("R1hr");
                R24hr= jsonAPIData.getString("R24hr");
                level = jsonAPIData.getString("level");
                leveltxt = jsonAPIData.getString("leveltxt");
                UpdateTime = jsonAPIData.getString("UpdataTime");
                lat = jsonAPIData.getJSONObject("loc").getDouble("lat");
                lon = jsonAPIData.getJSONObject("loc").getDouble("long");
            break;
            case '2':   //空氣品質
                name = jsonAPIData.getString("name");
                PSI = jsonAPIData.getString("PSI");
                MajorPoutant= jsonAPIData.getString("MajorPoutant");
                level = jsonAPIData.getString("level");
                leveltxt = jsonAPIData.getString("leveltxt");
                UpdateTime = jsonAPIData.getString("UpdataTime");
                lat = jsonAPIData.getJSONObject("loc").getDouble("lat");
                lon = jsonAPIData.getJSONObject("loc").getDouble("long");
                break;
            case '3':   //紫外線
                name = jsonAPIData.getString("name");
                UVI= jsonAPIData.getString("UVI");
                level = jsonAPIData.getString("level");
                leveltxt = jsonAPIData.getString("leveltxt");
                UpdateTime = jsonAPIData.getString("UpdataTime");
                lat = jsonAPIData.getJSONObject("loc").getDouble("lat");
                lon = jsonAPIData.getJSONObject("loc").getDouble("long");
                break;
            default:
                Log.i("myTag","Nothinggggggggggggggggg");
                break;
        }
    }



    public String getTitle() {
        return title;
    }
    public String getName() {
        return name;
    }
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }
    public String getLevel() {
        return level;
    }
    public String getLeveltxt() {
        return leveltxt;
    }
    public String getUpdateTime() {
        return UpdateTime;
    }
    public String getR1hr() {
        return R1hr;
    }
    public String getR24hr() {
        return R24hr;
    }
    public String getPSI() {
        return PSI;
    }
    public String getUVI() {
        return UVI;
    }
    public String getMajorPoutant() {
        return MajorPoutant;
    }
    public String getLandmark() {
        return landmark;
    }
}
