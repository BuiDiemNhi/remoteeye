package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class DetailChallengeAsynctask extends
		AsyncTask<String, Void, Void> {

	Activity activity;
	ProgressDialog dialog;
	
	public DetailChallengeAsynctask(Activity activity){
		this.activity = activity;
		this.dialog = new ProgressDialog(activity);
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialog.setMessage("Loading....");
		dialog.show();
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
	}

	@Override
	protected Void doInBackground(String... params) {
		// ?lat=10.833137336282476&lng=106.63435757252205
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair("lat", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("lng", params[1]));
		arrNameValuePairs.add(new BasicNameValuePair("userID", params[2]));
		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(Config.GET_CURC,
				arrNameValuePairs);
		JSONObject jsonObject = parserJson(json);
		JSONArray jsonArray = null;

		try {
			jsonArray = jsonObject.getJSONArray("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Log.e("Test", jsonArray.toString());

		return null;
	}
	
	JSONObject parserJson(String json) {
		JSONObject job = null;
		try {
			job = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
		return job;
	}
}
