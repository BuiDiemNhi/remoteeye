package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.Activity;
import android.graphics.Paint.Join;
import android.os.AsyncTask;

public class GetMoneyPaidAsynstask extends AsyncTask<String, Void, String> {
	Activity activity;
	public String money1="";

public GetMoneyPaidAsynstask(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		ArrayList<NameValuePair> arrNameValuePairs = new ArrayList<NameValuePair>();
		arrNameValuePairs.add(new BasicNameValuePair("user_id", params[0]));
		arrNameValuePairs.add(new BasicNameValuePair("type", params[1]));
		JSONParser jsonParser = new JSONParser();
		String json = jsonParser.makeHttpRequest(Config.API_GET_BUGET,
				arrNameValuePairs);
		JSONObject jsonObject;

		try {
			jsonObject = new JSONObject(json);
			money1= jsonObject.getString("data");
			Config.paid= money1;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return money1;
	}

}
