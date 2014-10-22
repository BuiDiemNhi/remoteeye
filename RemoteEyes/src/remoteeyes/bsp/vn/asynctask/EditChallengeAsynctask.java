package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class EditChallengeAsynctask extends AsyncTask<String,Void,String>{

	Context context;
	String id;
	public EditChallengeAsynctask(Context context) {
		this.context = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.getInt("status") == 200) {
				new LoadDetailChallengeAsynctask(context).execute(id);
			}
		}catch(Exception ex){}
	}
	
	@Override
	protected String doInBackground(String... params) {
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		id = params[0];
		arrNameValuePairs.add(new BasicNameValuePair("ui_challenge_id",
				params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("ui_user_id", params[1]));
		if (!params[2].equals("")) {
			arrNameValuePairs.add(new BasicNameValuePair("st_description",
					params[2]));
		}
		if (!params[3].equals("")) {
			arrNameValuePairs.add(new BasicNameValuePair("dt_start_date",
					params[3]));
		}
		if (!params[4].equals("")) {
			arrNameValuePairs.add(new BasicNameValuePair("ct_duration",
					params[4]));
		}
		if (!params[5].equals("")) {
			arrNameValuePairs.add(new BasicNameValuePair("ui_budget",
					params[5]));
		}
		if (!params[6].equals("")) {
			arrNameValuePairs.add(new BasicNameValuePair("ui_type_challenge",
					params[6]));
		}
		
		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(Config.API_EDIT_CHALLENGE,
				arrNameValuePairs);
		return json;
	}

}
