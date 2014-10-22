package remoteeyes.bsp.vn.asynctask;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import remoteeyes.bsp.vn.MyAccountActivity;
import remoteeyes.bsp.vn.common.Config;
import remoteeyes.bsp.vn.common.JSONParser;
import android.app.Activity;
import android.graphics.Paint.Join;
import android.os.AsyncTask;

public class GetBugetAsynstask extends AsyncTask<String, Void, String> {
	String debt="";
	String sending="";
	String recive="";
	@Override
	
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		JSONObject jsonObject;
		try {
			jsonObject= new JSONObject(result);
			JSONObject arr= jsonObject.getJSONObject("data");
			
		 debt= arr.getString("debt_budget");
		sending= arr.getString("paid_budget");
			 recive= arr.getString("received_budget");

				Config.Pending= debt;
				Config.paid= sending;
				Config.earn= recive;
((MyAccountActivity)activity).tvCountEarn.setText(recive+"");
((MyAccountActivity)activity).tvCountPending.setText(debt+"");
((MyAccountActivity)activity).tvcountPaid.setText(sending+"");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}


	Activity activity;
	public String money="";

	public GetBugetAsynstask(Activity activity) {
		super();
		this.activity = activity;
	}
	

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result = "";
		JSONParser jsonParser = new JSONParser();
		String url = String.format(Config.API_GET_BUGET, params[0]);
	 result = jsonParser.getStringFromUrl(url);
	 
		 return result;
	}

}
