package remoteeyes.bsp.vn.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Get_LatLong_FromAddress {
	
	public  double latitude;
	public  double longitute;
	static JSONObject jsonObject;
	public  JSONObject getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

       // address = address.replaceAll(" ","%20");    //address "97 vo van tan" ==>sensor=false
        //HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
        	
        //address postal/zip code ==>sensor=true
        try{
        	address = URLEncoder.encode(address, "UTF-8");
        } catch (Exception ex){}
        String url = "http://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=true";
        
        HttpGet httppost = new HttpGet(url.trim());
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        stringBuilder = new StringBuilder();
        response = client.execute(httppost);
        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        

        //JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            
            Log.d("111000000",jsonObject.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return jsonObject;
    }
	
	public  boolean getLatLong() {

        try {

            longitute = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

            latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

        } catch (JSONException e) {
            return false;

        }

        return true;
    }

}
